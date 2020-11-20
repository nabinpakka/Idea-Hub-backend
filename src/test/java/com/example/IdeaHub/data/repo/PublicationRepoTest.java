package com.example.IdeaHub.data.repo;

import com.example.IdeaHub.data.model.Publication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PublicationRepoTest {

    @Autowired
    private PublicationRepo publicationRepo;

    private Publication populateDatabase(){
        Publication publication = new Publication("title1","alkdjlafl","laksdjflkajlk","pakka","fileIds");
        publication.setAuthorId("lkkkj-jjhjhk-5i85");
        publication.setReviewScore(2);
        publication.setFileId("jk-jk55nm-jknknk");

        publicationRepo.save(publication);
        return publication;
    }

    @Test
    void findAllByApprovedAndPublicationHouse(){
        Publication publication = populateDatabase();

        List<Publication> publications = publicationRepo.findAllByApprovedAndPublicationHouse(false,"pakka");
        //List<Publication> publication = publicationRepo.findAllByApprovedAndPublicationHouse(false,"pakka");
        assertThat(publications).isNotNull();
        assertThat(publications.get(0).getPublicationHouse()).isEqualTo(publication.getPublicationHouse());

    }

    @Test
    void findAllByAuthorId(){
        Publication publication = populateDatabase();

        List<Publication> publications = publicationRepo.findAllByAuthorId(publication.getAuthorId());
        assertThat(publications).isNotNull();
        assertThat(publications.get(0).getAuthorId()).isEqualTo(publication.getAuthorId());

    }

    @Test
    void findAllByApproved(){
        Publication publication = populateDatabase();

        List<Publication> publications = publicationRepo.findAllByApproved(false);

        assertThat(publications).isNotNull();
        assertThat(publications.get(0).getApproved()).isEqualTo(publication.getApproved());
    }

    @Test
    void findByUuid(){
        Publication publication = populateDatabase();

        Publication publications = publicationRepo.findByUuid(publication.getUuid());

        assertThat(publications).isNotNull();
        assertThat(publications.getUuid()).isEqualTo(publication.getUuid());
    }

//    //@Query(nativeQuery = true,value="select * from publication p where exists ( select pr.publication_uuid from publication_reviewers pr where pr.publication_uuid=p.uuid and pr.reviewers=?1)")
//    Publication findByUuid(String publicationId);

}
