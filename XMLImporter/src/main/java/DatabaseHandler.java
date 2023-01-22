import xmlmodels.Company;
import xmlmodels.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "postgres";
    public static int companyId;

    public static void saveCompany(Company company) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

             PreparedStatement insertCompanyQuery = conn.prepareStatement(
                     "INSERT INTO company(name) VALUES (?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            insertCompanyQuery.setString(1, company.name);
            insertCompanyQuery.executeUpdate();
            getSavedCompanyId(insertCompanyQuery);
        }
    }

    private static void getSavedCompanyId(PreparedStatement insertCompanyQuery) throws SQLException {
        try (ResultSet insertCompanyResult = insertCompanyQuery.getGeneratedKeys()) {
            if (insertCompanyResult.next()) {
                companyId = (int) insertCompanyResult.getLong(1);
            } else throw new SQLException("No ID obtained.");
        }
    }

    public static void saveStaff(Staff staff, int companyId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement insertStaffQuery = conn.prepareStatement(
                     "INSERT INTO staff(id, company_id, first_name, last_name, nick_name) VALUES (?,?,?,?,?)")) {

            insertStaffQuery.setInt(1, staff.id);
            insertStaffQuery.setInt(2, companyId);
            insertStaffQuery.setString(3, staff.firstname);
            insertStaffQuery.setString(4, staff.lastname);
            insertStaffQuery.setString(5, staff.nickname);
            insertStaffQuery.executeUpdate();
        }
    }

    public static void saveSalary(Staff staff) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement insertSalaryQuery = conn.prepareStatement(
                     "INSERT INTO salary(staff_id, currency, value) VALUES (?,?,?)")) {

            insertSalaryQuery.setInt(1, staff.id);
            insertSalaryQuery.setString(2, staff.salary.currency);
            insertSalaryQuery.setInt(3, staff.salary.value);
            insertSalaryQuery.executeUpdate();
        }
    }

    public static List<Company> getAllCompanies() throws SQLException {
        ArrayList<Company> companies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            getCompanies(companies, conn);
        }
        return companies;
    }

    private static void getCompanies(ArrayList<Company> companies, Connection conn) throws SQLException {
        var companiesFound = conn.createStatement().executeQuery("SELECT * FROM company");
        while (companiesFound.next()) {
            var company = new Company();
            company.id = companiesFound.getInt("id");
            company.name = companiesFound.getString("name");
            companies.add(company);
        }
    }

    public static List<Staff> getAllStaff() throws SQLException {
        ArrayList<Staff> staff = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            getStaff(staff, conn);
        }
        return staff;
    }

    private static void getStaff(ArrayList<Staff> stuff, Connection conn) throws SQLException {
        var staffFound = conn.createStatement().executeQuery("SELECT * FROM staff");
        while (staffFound.next()) {
            var staff = new Staff();
            staff.id = staffFound.getInt("id");
            staff.firstname = staffFound.getString("first_name");
            staff.lastname = staffFound.getString("last_name");
            staff.nickname = staffFound.getString("nick_name");
            stuff.add(staff);
        }
    }

    public static int findCompanyById(String companyName) throws SQLException {
        int companyId = -1;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement findCompanyQuery = conn.prepareStatement("SELECT * FROM company WHERE name = ?")) {
             findCompanyQuery.setString(1,companyName);
             ResultSet companyFound = findCompanyQuery.executeQuery();
             if (companyFound.next()){
                 companyId = companyFound.getInt("id");
             }
        }
        return companyId;
    }

    public static int findStaffSalaryById(int staffId) throws SQLException {
        int salaryValue = -1;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement findSalaryQuery = conn.prepareStatement("SELECT * FROM salary WHERE staff_id = ?")) {
            findSalaryQuery.setInt(1,staffId);
            ResultSet salaryFound = findSalaryQuery.executeQuery();
            if (salaryFound.next()){
                salaryValue = salaryFound.getInt("value");
            }
        }
        return salaryValue;
    }

    public static void clearCompanyTable() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement deleteTableQuery = conn.prepareStatement(
                    "TRUNCATE company RESTART IDENTITY CASCADE;")) {
                deleteTableQuery.executeUpdate();
        }
    }

    public static void clearStaffTable() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement deleteTableQuery = conn.prepareStatement(
                     "DELETE FROM staff;")) {
            deleteTableQuery.executeUpdate();
        }
    }

    public static void clearSalaryTable() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement deleteTableQuery = conn.prepareStatement(
                     "DELETE FROM salary;")) {
            deleteTableQuery.executeUpdate();
        }
    }
}
