package main.service;

import main.dao.DocumentRepository;
import main.model.Document;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

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

    public Iterable<Document> getByDocLocation(String location)
    {
        return documentRepository.search(matchQuery("doclocation",location));
    }

    public List<Document> findBydoclocation(String location) {

        return documentRepository.findBydoclocation(location);
    }

}
