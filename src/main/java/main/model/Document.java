package main.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * Created by Jatin_Saxena on 4/4/2017.
 */
@org.springframework.data.elasticsearch.annotations.Document(indexName = "elastic_doc", type = "doc")

public class Document {

    @Id
    private String id;

    private String name;
    private String doclocation;
    private String content;
    //private Long fileModified;

    public String getDoclocation() {
        return doclocation;
    }

    public void setDoclocation(String doclocation) {
        this.doclocation = doclocation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public Long getFileModified() {
        return fileModified;
    }

    public void setFileModified(Long fileModified) {
        this.fileModified = fileModified;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", docname='" + name + '\'' +
                ", doclocation=" + doclocation +
                ", content=" + content +
                '}';
    }
}
