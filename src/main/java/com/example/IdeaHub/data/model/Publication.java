package com.example.IdeaHub.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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
    @JsonProperty("uuid")
    @ApiModelProperty(
            notes = "Uuid of the publication",
            name = "uuid",
            required = true,
            example=    "298fda03-b259-48c5-bf22-9a5cd538d206"
    )
    private String uuid;

    @Column(name="authorId", columnDefinition = "VARCHAR(128)")
    @ApiModelProperty(
            notes = "Uuid of author of the publication",
            name = "authorId",
            required = true,
            example="298fda03-b259-48c5-bf22-9a5cd538d206"
    )
    @JsonProperty("authorId")
    private String authorId;

    @Column(name ="title",columnDefinition = "VARCHAR(128)",unique = true)
    @ApiModelProperty(
            notes = "Title of the publication",
            name = "title",
            required = true,
            example="Intrusion Detection System for IoT Networks using Machine Learning"
    )
    @JsonProperty("title")
    private String title;

    @Column(name ="publicationType",columnDefinition = "VARCHAR(128)")
    @ApiModelProperty(
            notes = "Title of the publication",
            name = "publicationType",
            required = true,
            example="Science/Art"
    )
    @JsonProperty("publicationType")
    private String publicationType;

    //abstract is keywords hence abs
    @Lob
    @Column(name = "abstract",columnDefinition = "TEXT(500)")
    @ApiModelProperty(
            notes = "Abstract of the publication",
            name = "abst",
            required = true,
            example=" Abstract of publication"
    )
    @JsonProperty("abst")
    private String  abst;


    @Lob
    @Column(name = "detail",columnDefinition = "TEXT(500)")
    @ApiModelProperty(
            notes = "Detail of the publication",
            name = "detail",
            required = true,
            example=" Detail of the publication"
    )
    @JsonProperty("detail")
    private String detail;

    @Column(name = "reviewScore" )
    @ApiModelProperty(
            notes = "No of approval of the publication",
            name = "reviewScore",
            required = true,
            dataType = "java.lang.Integer",
            value= "0",
            example = "0"
    )
    @JsonProperty("reviewScore")
    private Integer reviewScore;

    @Column(name = "publicationHouse",columnDefinition = "VARCHAR(128)")
    @ApiModelProperty(
            notes = "Name of publication house where the pulblication has been submitted",
            name = "publicationHouse",
            required = true,
            value= "0",
            example= "Nima"
    )
    @JsonProperty("publicationHouse")
    private String publicationHouse;

    @Column(name = "fileId")
    @ApiModelProperty(
            notes = "Uuid of the file associated with the publication",
            name = "fileId",
            required = true,
            value= "c",
            example = "98fda03-b259-48c5-bf22-9a5cd538d206"
    )
    @JsonProperty("fileId")
    private String fileId;

    //first the variable name was isApproved
    //this cause error while filtering publication using this boolean
    @Column(name="approved",columnDefinition = "TINYINT")
    @ApiModelProperty(
            notes = "Uuid of the file associated with the publication",
            name = "fileId",
            required = true,
            dataType = "java.lang.Boolean",
            example = "true"
    )
    @JsonProperty("approved")
    private Boolean approved;

    public Publication(
                       String title,
                       String abst,
                       String detail,
                       String publicationHouse,
                       String fileId,
                       String publicationType
                       ) {
        this.title = title;
        this.publicationType = publicationType;
        this.abst = abst;
        this.detail = detail;
        this.reviewScore = 0;
        this.publicationHouse = publicationHouse;
        this.fileId = fileId;
        this.approved = false;
    }


    public Publication() {

    }


    public String getPublicationType() {
        return publicationType;
    }

    public void setPublicationType(String publicationType) {
        this.publicationType = publicationType;
    }

    public String getAbst() {
        return abst;
    }

    public void setAbst(String abst) {
        this.abst = abst;
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
                ", abst='" + abst + '\'' +
                ", detail='" + detail + '\'' +
                ", reviewScore=" + reviewScore +
                ", publishHouse='" + publicationHouse + '\'' +
                ", isApproved=" + approved +
                '}';
    }
}
