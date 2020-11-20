package com.example.IdeaHub.data.model;

import javax.persistence.*;

@Entity(name = "reviewers")
public class Reviewers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId",unique = true)
    private Long id;

    private String publicationId;

    private String authorId;

    public Reviewers(String publicationId, String authorId) {
        this.publicationId = publicationId;
        this.authorId = authorId;
    }

    public Reviewers() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
