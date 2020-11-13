package com.example.IdeaHub.data.service;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.model.Publication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public interface PublicationService {

    public ResponseEntity<ResponseMessage> upload(Publication publication);

    public Optional<Publication> getPublication(String id);

    public ResponseEntity<ResponseMessage> updateReviewScore(String id);

    public ResponseEntity<List<Publication>> getPublicationToReview(String id);

    public String storeFile(MultipartFile file) throws IOException;

    public ResponseEntity<ResponseMessage> deletePublication(String id);

}
