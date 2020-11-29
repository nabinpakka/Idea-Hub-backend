package com.example.IdeaHub.data.service.interfaces;

import com.example.IdeaHub.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public interface PublicationService {

    ResponseEntity<ResponseMessage> uploadPublication(Publication publication);

    ResponseEntity<Publication> getPublication(String id);

    ResponseEntity<ResponseMessage> updateReviewScore(String id,boolean isApproved);

    ResponseEntity<List<Publication>> getPublicationToReview();

    String storeFile(MultipartFile file) throws IOException;

    ResponseEntity<ResponseMessage> deletePublication(String id);

    ResponseEntity <List<Publication>> getAuthorPublications();

    ResponseEntity <List<Publication>> getPublicationHousePublications();

    ResponseEntity <List<Publication>> getApprovedPublications();

    ResponseEntity<List<Publication>> getPublicationsToReviewByAuthor();

    ResponseEntity<ResponseMessage> assignReviewers(String id, List<String> reviewers);

}
