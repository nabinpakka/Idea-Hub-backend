package com.example.IdeaHub.data.service.implementations;

import com.example.IdeaHub.auth.service.ApplicationUserDetails;
import com.example.IdeaHub.data.model.Reviewers;
import com.example.IdeaHub.data.model.repo.ReviewersRepo;
import com.example.IdeaHub.data.service.interfaces.FileStorageService;
import com.example.IdeaHub.data.service.interfaces.PublicationService;
import com.example.IdeaHub.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.model.repo.PublicationRepo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PublicationServiceImpl implements PublicationService {

    private final FileStorageService fileStorageService;

    private final ReviewersRepo reviewersRepo;

    private final PublicationRepo publicationRepo;

    private UserDetailsService userDetailsService;

    @Autowired
    public PublicationServiceImpl(FileStorageService fileStorageService, ReviewersRepo reviewersRepo, PublicationRepo publicationRepo) {
        this.fileStorageService = fileStorageService;
        this.reviewersRepo = reviewersRepo;
        this.publicationRepo = publicationRepo;
    }

    public String getCurrentApplicationUserId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            return ((ApplicationUserDetails) principal).getUserId();
        }else{
            return null;
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> uploadPublication(Publication publication){

        try{

            //check if publication with same title exists
            Publication publicationExistsCheck= publicationRepo.findByTitle(publication.getTitle());

            if(publicationExistsCheck != null){
                System.out.println("null");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Publication with title already exists!"));
            }
           else{
                String message= "Upload Successful";
                String currentUserId = this.getCurrentApplicationUserId();

                if (currentUserId == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
                }
                publication.setAuthorId(currentUserId);

                publicationRepo.save(publication);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

            }
        }
        catch (Exception e) {
            e.printStackTrace();
            String message= "Upload Failed";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }

    }


    public ResponseEntity<Publication> getPublication(String id){
        try{
            Optional<Publication> oPublication =  publicationRepo.findById(id);
            if(oPublication.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(oPublication.get());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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


    @Override
    public ResponseEntity<List<Publication>> getPublicationToReview() {
        List<Publication> publications = new ArrayList<>();

        try{
            //we want only the publication that are yet to be approved
            //getting publication house id
            String publicationHouse= "";
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof  UserDetails){
                publicationHouse=((UserDetails) principal).getUsername();
            }
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
    public ResponseEntity<List<Publication>> getAuthorPublications() {
        List<Publication> publications;
        try{

            //take author id from security context
            String authorId = this.getCurrentApplicationUserId();

            if (authorId == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            publications = publicationRepo.findAllByAuthorId(authorId);
            return ResponseEntity.status(HttpStatus.OK).body(publications);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<List<Publication>> getPublicationHousePublications() {
        List<Publication> publications = new ArrayList<>();
        List<Publication> submittedPublications  = new ArrayList<>();
        try{
            String publicationHouseId="";
            String publicationHouse="";
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof UserDetails) {
                publicationHouseId =((ApplicationUserDetails) principal).getUserId();
                publicationHouse =((ApplicationUserDetails) principal).getUsername();
            }

            if (publicationHouseId == null || publicationHouse==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            publications = publicationRepo.findAllByAuthorId(publicationHouseId);
            //also getting all the publications submitted to the publication house
            submittedPublications = publicationRepo.findAllByPublicationHouse(publicationHouse);
            publications.addAll(submittedPublications);
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
            String authorId = this.getCurrentApplicationUserId();
            if(authorId ==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            //getting publication ids
            publicationIds = getPublicationIdsToReview(authorId);

            for (String publicationId:publicationIds){
                publications.add(publicationRepo.findByUuid(publicationId));
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
        if(reviewers.size() != 5){
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

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> updateReviewScore(String publicationId,boolean isApproved) throws NoSuchElementException {
        int approvalThreshold = 3;

        try{
            //we do not have to check if current user is reviewer to the publication
            //as only those publication will be accessed by the user in which he/she has been assigned as
            //reviewer
            //also after one successful review frontend page will call the api which lists the to-review publications
            //which will not contain recently reviewed publication
            Optional<Publication> publication = publicationRepo.findById(publicationId);

            if(publication.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else{
                //review score is increased only if it has been approved
                //else the publication has been rejected
                //hence just removing the author from reviewer list

                if(isApproved){
                    int reviewScore = publication.get().getReviewScore() +1;
                    //increasing approval score by one
                    publication.get().setReviewScore( reviewScore);
                    publicationRepo.save(publication.get());

                    //checking reviewScore for approval of the publication
                    if(reviewScore >= approvalThreshold ){
                        int status  = updateApprovedStatus(publication.get());
                        if(status <= 0){
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Update Approval failed"));
                        }
                    }
                }

                //deleting current user from reviewer list of this publication
                //to stop multiple approval from same reviewer

                //first getting current user id
                String currentUserId= this.getCurrentApplicationUserId();
                //now deleting current user Id from reviewer list
                reviewersRepo.deleteReviewersByAuthorIdAndAndPublicationId(currentUserId,publicationId);

                String message = "Review Score updated successfully";
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }

        } catch (NoSuchElementException e) {
            e.printStackTrace();
            String message = "Publication with id: "+ publicationId +" was not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        }catch (Exception e){
            e.printStackTrace();
            String message = e.toString();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
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
            Optional<Publication> publication = publicationRepo.findById(id);

            if(publication.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            String authorId = this.getCurrentApplicationUserId();
            if(authorId ==null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            //checking if current user is the author of the publication
            else if (!authorId.equals(publication.get().getAuthorId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }


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



        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }

}
