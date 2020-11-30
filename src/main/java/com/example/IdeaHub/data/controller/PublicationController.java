package com.example.IdeaHub.data.controller;

import com.example.IdeaHub.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.service.interfaces.FileStorageService;
import com.example.IdeaHub.data.service.interfaces.PublicationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.IOException;
import java.util.*;

@ApiOperation(value = "/api/publications",tags = "Publication Controller")
@RestController
@RequestMapping("/api/publications")
@CrossOrigin(origins = "http://localhost:8081/",allowedHeaders = "*")
public class PublicationController {

    private final FileStorageService fileStorageService;

    private final PublicationService publicationService;

    @Autowired
    public PublicationController(FileStorageService fileStorageService, PublicationService publicationService) {
        this.fileStorageService = fileStorageService;
        this.publicationService = publicationService;
    }

    @RequestMapping(
            path = "/",
            method = RequestMethod.POST,
            consumes ={ MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
            )
    @ApiOperation(value="Uploads publication ")
    public ResponseEntity<ResponseMessage> uploadPublication(@RequestPart("file") MultipartFile file,
                                                             @RequestPart("title") String title,
                                                             @RequestPart("abst") String abst,
                                                             @RequestPart("detail") String detail,
                                                             @RequestPart("publicationHouse") String publicationHouse,
                                                             @RequestPart("publicationType") String publicationType
                                                      ) {
        String message ;
        try{

            String fileId = publicationService.storeFile(file);
            Publication publication = new Publication(
                    title,abst,detail,publicationHouse,fileId,publicationType
            );

            //saving publication
            return publicationService.uploadPublication(publication);

        } catch (IOException e) {
            e.printStackTrace();
            message = "Could not upload the file: " ;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    //getting publication with id
    @ApiOperation(value="Getting publications with id")
    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublication(@ApiParam(required = true,example = "298fda03-b259-48c5-bf22-9a5cd538d206") @PathVariable String id){
        return publicationService.getPublication(id);
    }


    //getting publication by Author id
    @ApiOperation(value = "returns publication by author id",response = Iterable.class)
    @GetMapping ("/author")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<List<Publication>>  getAuthorPublications(){
        return publicationService.getAuthorPublications();
    }

    //getting publication by publicationHouse id and submitted to it
    @ApiOperation(value = "returns publication by publication house",response = Iterable.class)
    @GetMapping ("/publicationHouse")
    @PreAuthorize("hasRole('ROLE_PUBLICATION_HOUSE')")
    public ResponseEntity<List<Publication>>  getPublicationHousePublications(){
        return publicationService.getPublicationHousePublications();
    }


    //getting all the approved publications
    @GetMapping
    @ApiOperation(value = "returns all approved publications",response = Iterable.class)
    public ResponseEntity<List<Publication>> getApprovedPublications(){
        return publicationService.getApprovedPublications();
    }

    //deleting publication with id
    @DeleteMapping ("/{id}")
    @ApiOperation(value = "deletes publication by publication id",response = ResponseMessage.class)
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<ResponseMessage> deletePublication(@PathVariable String id){
        return publicationService.deletePublication(id);
    }

    //here the id is id of publication house
    //the id should not contain ; as it cause error while executing sql command
    //getting all the publications to be reviewed for this publicationHouse
    @GetMapping("/review/publicationHouse")
    @ApiOperation(value = "returns publications by publication house",response = Iterable.class)
    @PreAuthorize("hasRole('ROLE_PUBLICATION_HOUSE')")
    public ResponseEntity<List<Publication>> getPublicationToReview(){
        return publicationService.getPublicationToReview();
    }

    //publication to review
    //returns all the publications to review to author with his/her id in it
    @GetMapping("/review/author")
    @ApiOperation(value = "returns publication to review by author id",response = Iterable.class)
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<List<Publication>> getPublicationToReviewByAuthor(){
        return publicationService.getPublicationsToReviewByAuthor();
    }


    //updating review score
    //id here is publication id
    @PutMapping("/reviewScore/{id}/{isApproved}")
    @ApiOperation(value = "increases review score of publication by 1",response = ResponseMessage.class)
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<ResponseMessage> updateReview(@PathVariable String id,@PathVariable boolean isApproved){
        return publicationService.updateReviewScore(id,isApproved);
    }

    //assigning reviewers to a publication
    //this is only authorized to publication house
    //publication house can only assign reviewers to publications submitted to them

    //this is a post request as we will be creating new data in the reviewers table
    @PutMapping("/reviewer/{id}")
    @ApiOperation(value = "assigns reviewers to publication by id",response = Iterable.class)
    @PreAuthorize("hasRole('ROLE_PUBLICATION_HOUSE')")
    public ResponseEntity<ResponseMessage> assignReviewers(@PathVariable String id ,@RequestBody List<String>reviewers){
        return publicationService.assignReviewers(id,reviewers);
    }
}   
