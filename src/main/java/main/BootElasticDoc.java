package main;

import main.model.Document;
import main.service.EntityExtractionService;
import main.service.ParseContent;
import main.service.ParseService;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.xml.sax.SAXException;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Jatin_Saxena on 4/4/2017.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource("application.properties")
public class BootElasticDoc implements CommandLineRunner {

    @Autowired
    private ParseService parseService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment env;

    @Autowired
    private ParseContent parseContent;

    private Resource[] resources ;
    private Map<String,Long> fileDetailsMap = new HashMap<String, Long>();


    private static final Logger logger = LoggerFactory.getLogger(BootElasticDoc.class);

    private void addDocuments() throws IOException, TikaException, org.xml.sax.SAXException
    {
        List<File> pstFileList = new ArrayList<File>();
        try {
            resources = applicationContext.getResources(env.getProperty("spring.file.location"));

            for (Resource resource : resources) {
              //  List<Document> docNameQuery = parseService.getByName(resource.getFilename());
                //~$rdDocument.docx  getByDocLocation
                String filename = resource.getFilename();
               // List<Document> docNameQuery = parseService.getByName("\""+filename+"\"");

                Iterable<Document> iterable = parseService.getByDocLocation("\""+filename+"\"");
                Iterator<Document> it = iterable.iterator();
                while (it.hasNext())
                {
                    Document doc = it.next();
                    parseService.deleteDocument(doc);
                }

                String ext = FilenameUtils.getExtension(resource.getFile().getAbsolutePath());
                if ("pst".equalsIgnoreCase(ext))
                    pstFileList.add(resource.getFile());
                else {
                    Document document = new Document();
                    document.setName(resource.getFilename());
                   // document.setDoclocation(resource.getURI().toString());
                    document.setDoclocation(resource.getFile().getAbsolutePath());
                   // String content = new ParseContent().parseBodyContent(resource.getFile());
                    String content = parseContent.parseBodyContent(resource.getFile());

                    document.setContent(content);
                    document.setEntityName(EntityExtractionService.extractPersonName(content));
                    document.setEntityLocation(EntityExtractionService.extractLocationName(content));
                    parseService.addDocument(document);
                }
            }
            if(pstFileList.size() >0)
            {
                processPSTDoc(pstFileList);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void processPSTDoc(List<File> pstFileList) throws TikaException, IOException, SAXException {
        List<Document> documentList = new ArrayList<Document>();

        for(File file : pstFileList )
        {
            //documentList = new ParseContent().parsePSTfile(file);
            documentList = parseContent.parsePSTfile(file);
        }

        for (Document document : documentList)
        {
            parseService.addDocument(document);
        }
    }


    public void run(String... args) throws Exception {
        addDocuments();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootElasticDoc.class, args);
    }

}
