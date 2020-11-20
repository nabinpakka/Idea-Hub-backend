package com.example.IdeaHub.data.service.implementations;

import com.example.IdeaHub.auth.service.ApplicationUserDetails;
import com.example.IdeaHub.data.model.Reviewers;
import com.example.IdeaHub.data.repo.ReviewersRepo;
import com.example.IdeaHub.data.service.interfaces.FileStorageService;
import com.example.IdeaHub.data.service.interfaces.PublicationService;
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
public class PublicationServiceImpl implements PublicationService {

    private final FileStorageService fileStorageService;

    private final ReviewersRepo reviewersRepo;

    private final PublicationRepo publicationRepo;

    @Autowired
    public PublicationServiceImpl(FileStorageService fileStorageService, ReviewersRepo reviewersRepo, PublicationRepo publicationRepo) {
        this.fileStorageService = fileStorageService;
        this.reviewersRepo = reviewersRepo;
        this.publicationRepo = publicationRepo;
    }

    public ResponseEntity<ResponseMessage> upload(Publication publication){

        try{
            String message= "Upload Successful";
            String authorId;
            //getting current user
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println(principal);
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

            if(publication.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else{
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
            }

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
        List<Publication> publications;
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
        List<Publication> publications;
        try{
            publications =publicationRepo.findAllByApproved(true);
            return ResponseEntity.status(HttpStatus.OK).body(publications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




    //this queries publications by authorId and returns list of publication ids
    //which are assigned to the author to review
    public List<String> getPublicationIdsToReview(String authorId) {
        List<Reviewers> reviewers = reviewersRepo.findAllByAuthorId(authorId);

        List<String> publicationIds = new ArrayList<>();
        //getting list of publication ids
        for (Reviewers reviewers1: reviewers){
            publicationIds.add(reviewers1.getPublicationId());
        }
        return publicationIds;
    }

    @Override
    public ResponseEntity<List<Publication>> getPublicationsToReviewByAuthor() {

        //get it from filesToReview in applicationTable table
        List<Publication> publications = new ArrayList<>();
        List<String> publicationIds ;
        try{

            //take author id from security context
            String authorId;
            //getting current user
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof UserDetails){
                authorId = ((ApplicationUserDetails) principal).getUserId();

                //getting publication ids
                publicationIds = getPublicationIdsToReview(authorId);

                for (String publicationId:publicationIds){
                    publications.add(publicationRepo.findByUuid(publicationId));
                }
                return ResponseEntity.status(HttpStatus.OK).body(publications);
            }


            return ResponseEntity.status(HttpStatus.OK).body(publications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> assignReviewers(String publicationId, List<String> reviewers) {
        //checking if 5 reviewers are present
        if(reviewers.size() < 5){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("5 reviewers are mandatory"));
        }
        try{
            //first get the publication with that id
            Optional<Publication> publication = publicationRepo.findById(publicationId);
            if (publication.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Publication with id "+ publicationId+" was not found."));

            }
            Publication publication1 = publication.get();

            //storing publication id and author ids to reviewers table
            for (String reviewer: reviewers){
                Reviewers reviewers1 = new Reviewers(publication1.getUuid(),reviewer);
                reviewersRepo.save(reviewers1);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Reviewers assigned successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(e.getMessage()));
        }

    }

    @SneakyThrows
    @Override
    public String storeFile(MultipartFile file)  {
        return fileStorageService.storeFile(file).getId();
    }

    @Override
    public ResponseEntity<ResponseMessage> deletePublication(String id) {
        try{
            Optional<Publication> publication = this.getPublication(id);

            if(publication.isPresent()){
                String fileId = publication.get().getFileId();
                //deleting file associated with publication
                int status = fileStorageService.deleteFile(fileId);
                String message;
                if(status <= 0){
                    message = "File associated with publication was not found";
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
                }
                publicationRepo.deleteById(id);
                message = "Publication deleted successfully";
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
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
