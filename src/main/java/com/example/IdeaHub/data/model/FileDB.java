package com.example.IdeaHub.data.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@Entity
@Table(name="files")
public class FileDB {

    //the ApiModelProperty is not needed as this class is used only inside serverside
    //no api will return this or upload to this class
    //uploading of file is done by publicationServiceImpl
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
//
//    @ApiModelProperty(
//            notes = "Uuid of file",
//            name = "uuid",
//            required = true,
//            value= "0",
//            example= "298fda03-b259-48c5-bf22-9a5cd538d20"
//    )
    private String uuid;

//    @ApiModelProperty(
//            notes = "Name of file",
//            name = "name",
//            required = true,
//            value= "0",
//            example= "CommonReportViewer.pdf"
//    )
    private String name;

    private String type;

    @Lob
    private byte[] data;

    public FileDB(){

    }

    public FileDB(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public String getId() {
        return uuid;
    }

    public void setId(String id) {
        this.uuid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FileDB{" +
                "id='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
