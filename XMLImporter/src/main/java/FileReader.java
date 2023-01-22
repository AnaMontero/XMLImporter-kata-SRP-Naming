import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import xmlmodels.Company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;

public class FileReader {
    public static List<Path> readFiles(Path folderPath, String fileExtension) throws IOException {
            List<Path> paths;
            try (Stream<Path> pathStream = walk(folderPath)
                    .filter(Files::isRegularFile)
                    .filter(filePath ->
                            filePath.toString()
                                    .endsWith(fileExtension))) {
                paths = pathStream
                        .collect(Collectors.toList());
            }

            return paths;
        }

    public static ArrayList<Company> companyParser(List<Path> paths) throws JAXBException {
        ArrayList<Company> companies = new ArrayList<>();
        for (Path path : paths) {
            File file = new File(path.toString());
            JAXBContext jaxbContext = JAXBContext.newInstance(Company.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Company company = (Company) jaxbUnmarshaller.unmarshal(file);
            companies.add(company);
        }
        return companies;
    }
}