package com.example.IdeaHub.data.model.repo;


import com.example.IdeaHub.data.model.Reviewers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ReviewerRepoTest {

    @Autowired
    private ReviewersRepo reviewersRepo;

    Reviewers populateDatabase(){
        Reviewers reviewers= new Reviewers("alakdlf-asldfja8ao89afdl-aldfaj","alks-5afd55a5ad-kja");
        reviewersRepo.save(reviewers);

        return reviewers;
    }

    @Test
    void findAllByAuthorId(){
        Reviewers reviewers = populateDatabase();
        List<Reviewers> reviewersList = reviewersRepo.findAllByAuthorId(reviewers.getAuthorId());

        assertThat(reviewersList).isNotNull();
        assertThat(reviewersList.get(0).getAuthorId()).isEqualTo(reviewers.getAuthorId());
    }

    @Test
    void deleteReviewerByAuthorIdAndPublicationId(){
        Reviewers reviewers = populateDatabase();
        Integer deleted= reviewersRepo.deleteReviewersByAuthorIdAndAndPublicationId(reviewers.getAuthorId(),reviewers.getPublicationId());
        assertThat(deleted).isNotNull();
    }
}
