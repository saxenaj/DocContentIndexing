package main.service;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;
import java.io.*;

/**
 * Created by Jatin_Saxena on 4/10/2017.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class EntityExtractionService {

    private final static String PERSON_FINDER_MODEL = "C:\\EPAM_Files\\en-ner-person.bin";
    private final static String LOCATION_FINDER_MODEL = "C:\\EPAM_Files\\en-ner-location.bin";
    private final static String SENTENCE_MODEL = "C:\\EPAM_Files\\en-sent.bin";
    private final static String TOKEN_MODEL = "C:\\EPAM_Files\\en-token.bin";


    public static String extractPersonName(String input) throws IOException {

        String tokens[] = tokenize(input);

        InputStream is = new FileInputStream(PERSON_FINDER_MODEL);
        TokenNameFinderModel model = new TokenNameFinderModel(is);
        is.close();

        NameFinderME nameFinder = new NameFinderME(model);
        Span nameSpans[] = nameFinder.find(tokens);

        String a[] = Span.spansToStrings(nameSpans, tokens);
        StringBuilder fd = new StringBuilder();
        int l = a.length;

        for (int j = 0; j < l; j++) {
            fd = fd.append(a[j] + "\n");
        }
        return fd.toString();
    }


    public static String extractLocationName(String input) throws IOException {

        String tokens[] = tokenize(input);
        InputStream is = new FileInputStream(LOCATION_FINDER_MODEL);
        TokenNameFinderModel model = new TokenNameFinderModel(is);
        is.close();

        NameFinderME nameFinder = new NameFinderME(model);
        Span nameSpans[] = nameFinder.find(tokens);


        String a[] = Span.spansToStrings(nameSpans, tokens);
        StringBuilder fd = new StringBuilder();
        int l = a.length;

        for (int j = 0; j < l; j++) {
            fd = fd.append(a[j] + "\n");
        }
        return fd.toString();
    }


    public static String[] tokenize(String input) throws IOException {
        InputStream tokenstream = new FileInputStream(TOKEN_MODEL);
        TokenizerModel tokenizerModel = new TokenizerModel(tokenstream);
        Tokenizer tokenizer = new TokenizerME(tokenizerModel);
        return tokenizer.tokenize(input);
    }

    /*public static void main(String args[]) throws IOException {
        EntityExtractionService entityExtractionService = new EntityExtractionService();
        String result = entityExtractionService.extractLocationName("My name is John Saxena.I know Mike Smith who is my friend.I live in Chicago USA." +
                "Before i used to live in Los Angeles California.");
        System.out.println("Result-->>" + result);
    }*/
}
