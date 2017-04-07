package main.dao;

import main.model.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by Jatin_Saxena on 4/4/2017.
 */
public interface DocumentRepository extends ElasticsearchRepository<Document, Long> {

    public List<Document> findByName(String docname);
}
