package com.example.IdeaHub.data.model.repo;

import com.example.IdeaHub.data.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository("publication")
public interface PublicationRepo extends JpaRepository<Publication, String> {

    List<Publication> findAllByApprovedAndPublicationHouse(Boolean isApproved,String publicationHouse);

    List<Publication> findAllByAuthorId(String authorId);

    List<Publication> findAllByApproved(Boolean isApproved);

    List<Publication> findAllByPublicationHouse(String publicationHouse);

    //@Query(nativeQuery = true,value="select * from publication p where exists ( select pr.publication_uuid from publication_reviewers pr where pr.publication_uuid=p.uuid and pr.reviewers=?1)")
    Publication findByUuid(String publicationId);

    Publication findByTitle(String title);
}
