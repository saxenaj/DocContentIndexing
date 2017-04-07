package main.service;

import main.dao.DocumentRepository;
import main.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jatin_Saxena on 4/4/2017.
 */
@Service
public class ParseService {

    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> getByName(String name) {
       return documentRepository.findByName(name);
    }

    public void addDocument(Document document) {
        documentRepository.save(document);
    }
    public void deleteDocument(Document document)
    {
        documentRepository.delete(document);
    }
}
