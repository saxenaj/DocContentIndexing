package main;

import main.model.Document;
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

    private Resource[] resources ;
    private Map<String,Long> fileDetailsMap = new HashMap<String, Long>();


    private static final Logger logger = LoggerFactory.getLogger(BootElasticDoc.class);

    private void addDocuments() throws IOException, TikaException, org.xml.sax.SAXException
    {
        List<File> pstFileList = new ArrayList<File>();
        try {
            resources = applicationContext.getResources(env.getProperty("spring.file.location"));

            for (Resource resource : resources) {
                List<Document> docNameQuery = parseService.getByName(resource.getFilename());
                if (!(docNameQuery.isEmpty()))
                {
                    Iterator<Document> iterator = docNameQuery.iterator();
                    while (iterator.hasNext())
                        parseService.deleteDocument(iterator.next());
                }

                String ext = FilenameUtils.getExtension(resource.getFile().getAbsolutePath());
                if ("pst".equalsIgnoreCase(ext))
                    pstFileList.add(resource.getFile());
                else {
                    Document document = new Document();
                    document.setName(resource.getFilename());
                    document.setDoclocation(resource.getURI().toString());
                    document.setContent(new ParseContent().parseBodyContent(resource.getFile()));
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
            documentList = new ParseContent().parsePSTfile(file);
            System.out.println("Document List size-->>" + documentList.size());
        }

        for (Document document : documentList)
        {
            parseService.addDocument(document);
        }
    }


    public void run(String... args) throws Exception {

        addDocuments();

     /*  List<Document> docNameQuery = parseService.getByName("Jatin.txt");
         Iterator<Document> iterator = docNameQuery.iterator();
        while(iterator.hasNext())
        {
            Document doc = iterator.next();
            fileDetailsMap.put(doc.getName(),doc.getFileModified());
        }*/
        //fileTobeAdded();
       //logger.info("Content of test.docx name query is {}", docNameQuery);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BootElasticDoc.class, args);
    }

}
