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
    @Column(name = "abstract",columnDefinition = "TEXT(500)")
    private String  abs;

    @Lob
    @Column(name = "detail",columnDefinition = "TEXT(500)")
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

    public Publication(
                       @JsonProperty("title") String title,
                       @JsonProperty("abstract") String abs,
                       @JsonProperty("detail") String detail,
                       @JsonProperty("publicationHouse") String publicationHouse,
                       String fileId) {
        this.title = title;
        this.abs = abs;
        this.detail = detail;
        this.reviewScore = 0;
        this.publicationHouse = publicationHouse;
        this.fileId = fileId;
        this.approved = false;
    }


    public Publication() {

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
