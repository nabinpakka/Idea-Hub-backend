package com.example.IdeaHub.data.service;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.repo.PublicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublicationService {

    private PublicationRepo publicationRepo;

    @Autowired
    public PublicationService(@Qualifier("publication") PublicationRepo publicationRepo) {
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
}
