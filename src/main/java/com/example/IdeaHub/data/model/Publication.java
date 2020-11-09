package com.example.IdeaHub.data.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Publication {

    @Id
    @Column(unique = true)
    private String authorId;

    @Column(name ="title",columnDefinition = "VARCHAR(128)")
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
    private String publishHouse;

    @Column(name = "fileId")
    private String fileId;

    public Publication(String authorId,
                       String title,
                       String abs,
                       String detail,
                       Integer reviewScore,
                       String publishHouse,
                       String fileId) {
        this.authorId = authorId;
        this.title = title;
        this.abs = abs;
        this.detail = detail;
        this.reviewScore = reviewScore;
        this.publishHouse = publishHouse;
        this.fileId = fileId;
    }


    public Publication() {

    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
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
        return publishHouse;
    }

    public void setPublishHouse(String publishHouse) {
        this.publishHouse = publishHouse;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "title='" + title + '\'' +
                ", abs='" + abs + '\'' +
                ", detail='" + detail + '\'' +
                ", reviewScore=" + reviewScore +
                ", publishHouse='" + publishHouse + '\'' +
                '}';
    }
}
