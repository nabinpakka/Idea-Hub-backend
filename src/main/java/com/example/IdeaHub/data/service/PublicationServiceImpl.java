package com.example.IdeaHub.data.service;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.repo.PublicationRepo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//this automatically constructs constructor with all the required repo
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
            publicationRepo.save(publication);
            String message= "Upload Successful";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
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
    public ResponseEntity<List<Publication>> getPublicationToReview(String id) {
        List<Publication> publications = new ArrayList<>();

        try{
            //we want only the publication that are yet to be approved
            publications = publicationRepo.findAllByApprovedAndPublicationHouse(false,id);
            return ResponseEntity.status(HttpStatus.OK).body(publications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(publications);
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
