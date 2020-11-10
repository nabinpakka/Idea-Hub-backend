package com.example.IdeaHub.data.service;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.repo.PublicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublicationServiceImpl implements PublicationService{

    private PublicationRepo publicationRepo;

    @Autowired
    public PublicationServiceImpl(@Qualifier("publication") PublicationRepo publicationRepo) {
        this.publicationRepo = publicationRepo;
    }

    public ResponseEntity<ResponseMessage> upload(Publication publication){
        String message="";
        try{
            publicationRepo.save(publication);
            message= "Upload Successful";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            message= "Upload Failed";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }

    }

    public Optional<Publication> getPublication(String id){
        return publicationRepo.findById(id);
    }

    @Override
    public int updateReviewScore(String id, Integer reviewScore) {
        int approveThreshold =3;
        try{
            Optional<Publication> publication = publicationRepo.findById(id);
            if (publication.isPresent()){
                publication.get().setReviewScore(reviewScore);
                publicationRepo.save(publication.get());
                if (publication.get().getReviewScore() >=approveThreshold){
                    updateApprovedStatus(id,true);
                }

                return 1;
            }
            else{
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Publication> getPublicationToReview(String id) {
        List<Publication> publications = new ArrayList<Publication>();

        try{
            //we want only the publication that are yet to be approved
            publications = publicationRepo.findAllByApprovedAndPublicationHouse(false,id);
            return publications;
        } catch (Exception e) {
            e.printStackTrace();
            return publications;
        }

    }


    //you should add this in utilities later
    //updating approve status after analysing reviewScore
    private int updateApprovedStatus(String id, Boolean isApproved) {

        try{
            Optional<Publication> publication = publicationRepo.findById(id);

            if (publication.isPresent()){
                publication.get().setApproved(isApproved);
                publicationRepo.save(publication.get());
                return 1;
            }
            else{
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
