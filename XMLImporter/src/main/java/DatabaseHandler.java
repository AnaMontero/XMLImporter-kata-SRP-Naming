import xmlmodels.Company;
import xmlmodels.Staff;

import java.sql.*;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "postgres";

    public static int insertCompany(Company company) throws SQLException {
        final int companyId;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

             PreparedStatement insertCompanyQuery = conn.prepareStatement("INSERT INTO company(name) VALUES (?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            insertCompanyQuery.setString(1, company.name);
            insertCompanyQuery.executeUpdate();

            try (ResultSet insertCompanyResult = insertCompanyQuery.getGeneratedKeys()) {
                if (insertCompanyResult.next()) {
                    companyId = (int) insertCompanyResult.getLong(1);
                } else throw new SQLException("No ID obtained.");
            }
        }
        return companyId;
    }

    public static void insertStaff(Staff staff, int companyId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement insertStaffQuery = conn.prepareStatement("INSERT INTO staff(id, company_id, first_name, last_name, nick_name) VALUES (?,?,?,?,?)")) {

            insertStaffQuery.setInt(1, staff.id);
            insertStaffQuery.setInt(2, companyId);
            insertStaffQuery.setString(3, staff.firstname);
            insertStaffQuery.setString(4, staff.lastname);
            insertStaffQuery.setString(5, staff.nickname);
            insertStaffQuery.executeUpdate();
        }
    }

    public static void insertSalary(Staff staff) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement insertSalaryQuery = conn.prepareStatement("INSERT INTO salary(staff_id, currency, value) VALUES (?,?,?)")) {

            insertSalaryQuery.setInt(1, staff.id);
            insertSalaryQuery.setString(2, staff.salary.currency);
            insertSalaryQuery.setInt(3, staff.salary.value);
            insertSalaryQuery.executeUpdate();
        }
    }
}