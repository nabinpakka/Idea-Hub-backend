package com.example.IdeaHub.data.service;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import org.springframework.http.ResponseEntity;

import java.util.*;

public interface PublicationService {

    public ResponseEntity<ResponseMessage> upload(Publication publication);

    public Optional<Publication> getPublication(String id);

    public int updateReviewScore(String id, Integer reviewScore);

    public List<Publication> getPublicationToReview(String id);
}
