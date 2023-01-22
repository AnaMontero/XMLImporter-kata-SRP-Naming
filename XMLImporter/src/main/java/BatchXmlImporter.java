import jakarta.xml.bind.JAXBException;
import xmlmodels.Company;
import xmlmodels.Staff;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;

public class BatchXmlImporter {

    public void importFiles(Path folderPath) throws IOException, JAXBException, SQLException {
        final String fileExtension = ".xml";
        var paths = FileReader.readFiles(folderPath, fileExtension);

        ArrayList<Company> companies = FileReader.companyParser(paths);
        for (Company company : companies) {

            final int companyId = DatabaseHandler.insertCompany(company);

            for (Staff staff : company.staff) {
                DatabaseHandler.insertStaff(staff, companyId);

                DatabaseHandler.insertSalary(staff);
            }
        }
    }
}