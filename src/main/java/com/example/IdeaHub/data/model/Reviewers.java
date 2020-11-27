package com.example.IdeaHub.data.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "reviewers")
public class Reviewers {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Column (name="uuid", unique = true)
    private String uuid;

    private String publicationId;

    private String authorId;

    public Reviewers(String publicationId, String authorId) {
        this.publicationId = publicationId;
        this.authorId = authorId;
    }

    public Reviewers() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
