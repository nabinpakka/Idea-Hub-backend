package com.example.IdeaHub.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="publication")
public class Publication {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Column (name="uuid", unique = true)
    private String uuid;

    @Column(name="authorId", columnDefinition = "VARCHAR(128)")
    private String authorId;

    @Column(name ="title",columnDefinition = "VARCHAR(128)",unique = true)
    private String title;

    //abstract is keywords hence abs
    @Lob
    @Column(name = "abstract")
    private String  abs;

    @Lob
    @Column(name = "detail")
    private String detail;


    @Column(name = "reviewScore" )
    private Integer reviewScore;

    @Column(name = "publicationHouse",columnDefinition = "VARCHAR(128)")
    private String publicationHouse;

    @Column(name = "fileId")
    private String fileId;

    //first the variable name was isApproved
    //this cause error while filtering publication using this boolean
    @Column(name="approved",columnDefinition = "TINYINT")
    private Boolean approved;

    @ElementCollection
    @Column (name = "reviewers")
    private List<String> reviewers= new ArrayList<>();

    public Publication(@JsonProperty("authorId") String authorId,
                       @JsonProperty("title") String title,
                       @JsonProperty("abstract") String abs,
                       @JsonProperty("detail") String detail,
                       @JsonProperty("reviewScore") Integer reviewScore,
                       @JsonProperty("publicationHouse") String publicationHouse,
                       @JsonProperty("reviewers") List<String> reviewers,
                       String fileId) {
        this.authorId = authorId;
        this.title = title;
        this.abs = abs;
        this.detail = detail;
        this.reviewScore = reviewScore;
        this.publicationHouse = publicationHouse;
        this.fileId = fileId;
        this.reviewers = reviewers;
        this.approved = false;
    }


    public Publication() {

    }

    public List<String> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<String> reviewers) {
        this.reviewers = reviewers;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getPublicationHouse() {
        return publicationHouse;
    }

    public void setPublicationHouse(String publicationHouse) {
        this.publicationHouse = publicationHouse;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(Integer reviewScore) {
        this.reviewScore = reviewScore;
    }

    public String getPublishHouse() {
        return publicationHouse;
    }

    public void setPublishHouse(String publishHouse) {
        this.publicationHouse = publishHouse;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "title='" + title + '\'' +
                ", abs='" + abs + '\'' +
                ", detail='" + detail + '\'' +
                ", reviewScore=" + reviewScore +
                ", publishHouse='" + publicationHouse + '\'' +
                ", isApproved=" + approved +
                '}';
    }
}
