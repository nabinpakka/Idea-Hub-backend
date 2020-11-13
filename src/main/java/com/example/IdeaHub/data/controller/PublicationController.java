package com.example.IdeaHub.data.controller;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.service.FileStorageService;
import com.example.IdeaHub.data.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/publication")
public class PublicationController {

    private FileStorageService fileStorageService;

    private PublicationService publicationService;

    @Autowired
    public PublicationController(FileStorageService fileStorageService, PublicationService publicationService) {
        this.fileStorageService = fileStorageService;
        this.publicationService = publicationService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("title") String title,
                                                      @RequestParam("abstract") String abs,
                                                      @RequestParam("reviewScore") Integer reviewScore,
                                                      @RequestParam("detail") String detail,
                                                      @RequestParam("publicationHouse") String publicationHouse,
                                                      @RequestParam("authorId") String authorId,
                                                      @RequestParam("reviewers") List<String> reviewers
                                                      ) {
        String message = "";
        try{

            String fileId = publicationService.storeFile(file);

            Publication publication = new Publication(
                    authorId,title,abs,detail,reviewScore,publicationHouse,reviewers,fileId
            );

            //saving publication
            publicationService.upload(publication);
            message = "Uploaded Successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
            message = "Could not upload the file: " ;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    //here the id is id of publication house
    //the id should not contain ; as it cause error while executing sql command
    //getting all the publications to be reviewed for this publicationHouse
    @GetMapping("/publicationToReview/{id}")
    public ResponseEntity<List<Publication>> getPublicationToReview(@PathVariable String id){
            return publicationService.getPublicationToReview(id);
    }

    //getting publication with id
    @GetMapping("{id}")
    public ResponseEntity<Optional<Publication>> getPublication(@PathVariable String id){
        return Optional
                .of( publicationService.getPublication(id))
                .map(value -> ResponseEntity.ok()
                .body(value))
                        .orElseGet(() ->  ResponseEntity.notFound().build());

    }

    //deleting publication with id
    @DeleteMapping ("{id}")
    public ResponseEntity<ResponseMessage> deletePublication(@PathVariable String id){
        return publicationService.deletePublication(id);
    }

    //updating review score
    @PostMapping("/updateReview/{id}")
    public ResponseEntity<ResponseMessage> updateReview(@PathVariable String id){
        return publicationService.updateReviewScore(id);

    }

}
