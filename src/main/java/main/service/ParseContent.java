package main.service;

import main.model.Document;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.RecursiveParserWrapper;
import org.apache.tika.parser.html.BoilerpipeContentHandler;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.mbox.OutlookPSTParser;
import org.apache.tika.sax.BasicContentHandlerFactory;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jatin_Saxena on 4/4/2017.
 */
public class ParseContent {
    String content;
    String location;


    public String parseBodyContent(File file) throws IOException, TikaException, org.xml.sax.SAXException {
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        InputStream stream = TikaInputStream.get(file);
        ParseContext context = new ParseContext();

        parser.parse(stream, handler, metadata, context);
        return handler.toString();
    }

    public List<Document> parsePSTfile(File file) throws IOException, TikaException, org.xml.sax.SAXException {
        String result="";
        List<Document> resultList = new ArrayList<Document>();
        Parser p = new AutoDetectParser();
       // ContentHandler handler = new ToHTMLContentHandler();
        RecursiveParserWrapper wrapper = new RecursiveParserWrapper(p,
                new BasicContentHandlerFactory(
                        BasicContentHandlerFactory.HANDLER_TYPE.XML, -1));
        InputStream stream = TikaInputStream.get(file);
        ParseContext context = new ParseContext();

        wrapper.parse(stream, new DefaultHandler(), new Metadata(), context);

        int i = 0;
        int j = 0;
        for (Metadata metadata : wrapper.getMetadata()) {

            if(j!=0) {
                Document document = new Document();
              //  document.setId(Integer.toString(i));
                document.setDoclocation(file.getAbsolutePath());
                for (String name : metadata.names()) {

                    for (String value : metadata.getValues(name)) {
                       // System.out.println(i + " " + name + ": " + value);
                        if (name.equalsIgnoreCase("Message-From"))
                            document.setName(value);
                        if ("X-TIKA:content".equalsIgnoreCase(name)) {
                            AutoDetectParser parser = new AutoDetectParser();
                            ContentHandler textHandler = new BodyContentHandler();
                            Metadata xmetadata = new Metadata();
                            InputStream inputStream = new ByteArrayInputStream(value.getBytes("UTF-8"));
                            //parser.parse(inputStream, new BoilerpipeContentHandler(textHandler), xmetadata);
                            new HtmlParser().parse(inputStream,textHandler,xmetadata,new ParseContext());
                            document.setContent(textHandler.toString());
                           // document.setContent(value);
                            result = result + " " + textHandler.toString();
                        }
                    }

                }
                i++;
                resultList.add(document);
            }
            j++;

        }
    return resultList;
    }

    /*public static void main(String args[]) throws TikaException, IOException, SAXException {
        ParseContent parseContent = new ParseContent();
        File file = new File("C:\\EPAM_Projects\\Files\\Temp.pst");
        parseContent.parsePSTfile(file);
    }
*/
}
