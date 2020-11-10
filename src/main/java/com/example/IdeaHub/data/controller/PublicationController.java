package com.example.IdeaHub.data.controller;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.service.FileStorageService;
import com.example.IdeaHub.data.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/publication")
public class PublicationController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PublicationService publicationService;


    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("title") String title,
                                                      @RequestParam("abstract") String abs,
                                                      @RequestParam("reviewScore") Integer reviewScore,
                                                      @RequestParam("detail") String detail,
                                                      @RequestParam("publicationHouse") String publicationHouse,
                                                      @RequestParam("authorId") List<String> authorId){
        String message = "";
        try{
            FileDB fileDB = fileStorageService.store(file);
            String fileId = fileDB.getId();
            System.out.println(fileId);

            //creating new publication object
            Publication publication = new Publication(
                    authorId,title,abs,detail,reviewScore,publicationHouse,fileId,false
            );
            //saving publication
            publicationService.upload(publication);

            System.out.println(fileDB);
            message = "Uploaded Successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
            message = "Could not upload the file: " ;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


    //the id should not contain ; as it cause error while executing sql command

    //getting all the publications to be reviewed for this publicationHouse
    @GetMapping("/publicationToReview/{id}")
    public ResponseEntity<List<Publication>> getPublicationToReview(@PathVariable String id){

            return Optional
                    .ofNullable( publicationService.getPublicationToReview(id) )
                    .map( publications -> ResponseEntity.ok().body(publications) )          //200 OK
                    .orElseGet( () -> ResponseEntity.notFound().build() );

    }


    @GetMapping("{id}")
    public Optional<Publication> getPublication(@PathVariable String id){
        return publicationService.getPublication(id);

    }

    //updating review score
    @PostMapping("/updateReview/{id}")
    public ResponseEntity<ResponseMessage> updateReview(@PathVariable String id, @RequestParam("reviewScore") Integer reviewScore){
        String message="";
        int status =publicationService.updateReviewScore(id,reviewScore);

        if (status == 1){
            message = "Uploaded Successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
        else{
            message = "Could not find publication with id: "+ id ;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }

}
