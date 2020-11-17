package com.example.IdeaHub.data.service;

import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.auth.service.ApplicationUserDetails;
import com.example.IdeaHub.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.repo.PublicationRepo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PublicationServiceImpl implements PublicationService{

    private FileStorageService fileStorageService;

    private final PublicationRepo publicationRepo;

    @Autowired
    public PublicationServiceImpl(FileStorageService fileStorageService, PublicationRepo publicationRepo) {
        this.fileStorageService = fileStorageService;
        this.publicationRepo = publicationRepo;
    }

    public ResponseEntity<ResponseMessage> upload(Publication publication){

        try{
            String message= "Upload Successful";
            String authorId="";
            //getting current user
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof UserDetails){
                authorId = ((ApplicationUserDetails) principal).getUserId();
                publication.setAuthorId(authorId);
                publicationRepo.save(publication);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            String message= "Upload Failed";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }

    }


    public Optional<Publication> getPublication(String id){
        return publicationRepo.findById(id);
    }

    @Override
    public ResponseEntity<ResponseMessage> updateReviewScore(String id) throws NoSuchElementException {
        int approvalThreshold = 3;


        try{
            Optional<Publication> publication = publicationRepo.findById(id);

            int reviewScore = publication.get().getReviewScore() +1;
            //increasing approval score by one
            publication.get().setReviewScore( reviewScore);
            publicationRepo.save(publication.get());

            if(reviewScore >= approvalThreshold ){
                int status  = updateApprovedStatus(publication.get());
                if(status <= 0){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Update Approval failed"));
                }
            }

            String message = "Review Score updated successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        } catch (NoSuchElementException e) {
            e.printStackTrace();
            String message = "Publication with id: "+ id +" was not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }catch (Exception e){
            e.printStackTrace();
            String message = e.toString();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }


    @Override
    public ResponseEntity<List<Publication>> getPublicationToReview(String publicationHouse) {
        List<Publication> publications = new ArrayList<>();

        try{
            //we want only the publication that are yet to be approved
            publications = publicationRepo.findAllByApprovedAndPublicationHouse(false,publicationHouse);

            //checking if publications is null
            if(publications.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(publications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(publications);
        }

    }

    @Override
    public ResponseEntity<List<Publication>> getMyPublications(String authorId) {
        List<Publication> publications = new ArrayList<>();
        try{
            publications = publicationRepo.findAllByAuthorId(authorId);
            return ResponseEntity.status(HttpStatus.OK).body(publications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    //get public publications
    //these publications are approved by publication house
    @Override
    public ResponseEntity<List<Publication>> getApprovedPublications() {
        List<Publication> publications = new ArrayList<>();
        try{
            publicationRepo.findAllByApproved(true);
            return ResponseEntity.status(HttpStatus.OK).body(publications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<List<Publication>> getPublicationsToReviewByAuthor() {
        List<Publication> publications = new ArrayList<>();
        try{

            //take author id from security context
            String authorId="";
            //getting current user
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof UserDetails){
                authorId = ((ApplicationUserDetails) principal).getUserId();
                publications = publicationRepo.findAllByReviewersEquals(authorId);
                return ResponseEntity.status(HttpStatus.OK).body(publications);
            }


            return ResponseEntity.status(HttpStatus.OK).body(publications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> assignReviewers(String id, List<String> reviewers) {
        //checking if 5 reviewers are present
        if(reviewers.size() < 5){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("5 reviewers are mandatory"));
        }
        try{
            //first get the pulblication with that id
            Optional<Publication> publication = publicationRepo.findById(id);
            if (publication.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Publication with id "+ id+" was not found."));

            }
            Publication publication1 = publication.get();
            publication1.setReviewers(reviewers);

            //now save the publication
            publicationRepo.save(publication1);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Reviewers assigned successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(e.getMessage()));
        }

    }

    @SneakyThrows
    @Override
    public String storeFile(MultipartFile file)  {
        return fileStorageService.store(file).getId();
    }

    @Override
    public ResponseEntity<ResponseMessage> deletePublication(String id) {
        try{
            Optional<Publication> publication = getPublication(id);
            String fileId = publication.get().getFileId();
            //deleting file associated with publication
            int status = fileStorageService.deleteFile(fileId);
            String message = "";
            if(status <= 0){
                message = "File associated with publication was not found";
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
            }
            publicationRepo.deleteById(id);
            message = "Publication deleted successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }


    //you should add this in utilities later
    //updating approve status after analysing reviewScore
    private int updateApprovedStatus(Publication publication) {

        try{
            publication.setApproved(true);
            publicationRepo.save(publication);
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
