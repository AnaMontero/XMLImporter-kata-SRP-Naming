import org.junit.jupiter.api.*;
import xmlmodels.Company;
import xmlmodels.Salary;
import xmlmodels.Staff;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseHandlerShould {
    @Test
    @Order(1)
    void saveCompany_Ok() throws SQLException {
        DatabaseHandler.clearCompanyTable();
        Company testCompany = mock(Company.class);
        var testCompanyName = "Croqueta S.L";
        testCompany.name = testCompanyName;
        DatabaseHandler.saveCompany(testCompany);

        var companies = DatabaseHandler.getAllCompanies();
        assertThat(companies).hasSize(1);
        assertThat(companies.get(0).name.equals(testCompanyName)).isTrue();
    }

    @Test
    @Order(2)
    void findCompanyById_Ok() throws SQLException {
        var testCompanyName = "Croqueta S.L";
        var testCompanyId = DatabaseHandler.findCompanyById(testCompanyName);
        assertThat(testCompanyId).isNotEqualTo(-1);
    }

    @Test
    @Order(3)
    void findCompanyById_NOk() throws SQLException {
        var testCompanyName = "No existe";
        var testCompanyId = DatabaseHandler.findCompanyById(testCompanyName);
        assertThat(testCompanyId).isEqualTo(-1);
    }

    @Test
    @Order(4)
    void saveStaff_Ok() throws SQLException {
        DatabaseHandler.clearSalaryTable();
        DatabaseHandler.clearStaffTable();
        Staff testStaff = mock(Staff.class);
        var testCompanyName = "Croqueta S.L";
        var testCompanyId = DatabaseHandler.findCompanyById(testCompanyName);
        testStaff.id = 10;
        testStaff.firstname = "Ada";
        testStaff.lastname = "Lovelace";
        testStaff.nickname = "Condesa de Lovelace";

        if (testCompanyId != -1)
            DatabaseHandler.saveStaff(testStaff, testCompanyId);

        var staff = DatabaseHandler.getAllStaff();
        assertThat(staff).hasSize(1);
        assertThat(staff.get(0).id.intValue()).isEqualTo(10);
    }

    @Test
    @Order(5)
    void saveSalary_Ok() throws SQLException {
        DatabaseHandler.clearSalaryTable();
        Salary testSalary = mock(Salary.class);
        Staff testStaff = mock(Staff.class);
        testStaff.id = 10;
        testStaff.salary = testSalary;
        testStaff.salary.currency = "EUR";
        testStaff.salary.value = 100;
        DatabaseHandler.saveSalary(testStaff);

        var salary = DatabaseHandler.findStaffSalaryById(10);
        assertThat(salary).isEqualTo(100);
    }
}