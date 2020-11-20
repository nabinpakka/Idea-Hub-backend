package com.example.IdeaHub.data.repo;

import com.example.IdeaHub.data.model.Reviewers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ReviewersRepo extends JpaRepository<Reviewers,String > {

    List<Reviewers> findAllByAuthorId(String authorId);

    void deleteReviewersByAuthorIdAndAndPublicationId(String authorId, String publicationId);
}
