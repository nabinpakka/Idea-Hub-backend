package com.example.IdeaHub.auth.repo;

import com.example.IdeaHub.auth.model.ApplicationUser;
import com.example.IdeaHub.data.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ApplicationUserRepo extends JpaRepository<ApplicationUser, String > {

    public Optional<ApplicationUser> findByUsername(String username);
}
