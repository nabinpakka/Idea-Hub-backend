package com.example.IdeaHub.data.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "reviewers")
public class Reviewers {

    @Id
    private String publicationId;

    private String authorId;

    public Reviewers(String publicationId, String authorId) {
        this.publicationId = publicationId;
        this.authorId = authorId;
    }

    public Reviewers() {

    }

    public String getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
