package com.example.IdeaHub.data.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Publication {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    private String uuid;

    @ElementCollection
    private List<String> authorId= new ArrayList<>();

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
    @Column(name="approved")
    private Boolean approved;

    public Publication(List<String> authorId,
                       String title,
                       String abs,
                       String detail,
                       Integer reviewScore,
                       String publishHouse,
                       String fileId,
                       Boolean approved) {
        this.authorId = authorId;
        this.title = title;
        this.abs = abs;
        this.detail = detail;
        this.reviewScore = reviewScore;
        this.publicationHouse = publishHouse;
        this.fileId = fileId;
        this.approved = approved;
    }


    public Publication() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getAuthorId() {
        return authorId;
    }

    public void setAuthorId(List<String> authorId) {
        this.authorId = authorId;
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
        approved = approved;
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
