package com.example.IdeaHub.data.repo;

import com.example.IdeaHub.data.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("publication")
public interface PublicationRepo extends JpaRepository<Publication, String> {
}
