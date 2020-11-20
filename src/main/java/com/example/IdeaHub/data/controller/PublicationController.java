package com.example.IdeaHub.data.controller;

import com.example.IdeaHub.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.service.interfaces.FileStorageService;
import com.example.IdeaHub.data.service.interfaces.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ResponseMessage> uploadPublication(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("title") String title,
                                                      @RequestParam("abstract") String abs,
                                                      @RequestParam("detail") String detail,
                                                      @RequestParam("publicationHouse") String publicationHouse
                                                      ) {
        String message ;
        try{

            String fileId = publicationService.storeFile(file);
            Publication publication = new Publication(
                    title,abs,detail,publicationHouse,fileId
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

    //getting publication with id
    @GetMapping("{id}")
    public ResponseEntity<Optional<Publication>> getPublication(@PathVariable String id){
        return Optional
                .of( publicationService.getPublication(id))
                .map(value -> ResponseEntity.ok()
                .body(value))
                        .orElseGet(() ->  ResponseEntity.notFound().build());

    }


    //getting publication by Author id
    @GetMapping ("/userPublications")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<List<Publication>>  getMyPublications(){
        return publicationService.getMyPublications();
    }


    //getting all the approved publications
    @GetMapping
    public ResponseEntity<List<Publication>> getApprovedPublications(){
        return publicationService.getApprovedPublications();
    }

    //deleting publication with id
    @DeleteMapping ("{id}")
    public ResponseEntity<ResponseMessage> deletePublication(@PathVariable String id){
        return publicationService.deletePublication(id);
    }

    //here the id is id of publication house
    //the id should not contain ; as it cause error while executing sql command
    //getting all the publications to be reviewed for this publicationHouse
    @GetMapping("/publicationsToReview")
    @PreAuthorize("hasAuthority('list:submitted_publication')")
    public ResponseEntity<List<Publication>> getPublicationToReview(){
        return publicationService.getPublicationToReview();
    }

    //publication to review
    //returns all the publications to review to author with his/her id in it
    @GetMapping("/publicationsToReviewByAuthor")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<List<Publication>> getPublicationToReviewByAuthor(){
        return publicationService.getPublicationsToReviewByAuthor();
    }


    //updating review score
    //id here is publication id
    @PostMapping("/updateReview/{id}")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<ResponseMessage> updateReview(@PathVariable String id){
        return publicationService.updateReviewScore(id);

    }

    //assigning reviewers to a publication
    //this is only authorized to publication house
    //publication house can only assign reviewers to publications submitted to them
    @PostMapping("/assignReviewers/{id}")
    @PreAuthorize("hasRole('ROLE_PUBLICATION_HOUSE')")
    public ResponseEntity<ResponseMessage> assignReviewers(@PathVariable String id ,@RequestParam("reviewers") List<String>reviewers){
        return publicationService.assignReviewers(id,reviewers);
    }
}   
