package com.example.IdeaHub.auth.model.repo;

import com.example.IdeaHub.auth.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ApplicationUserRepo extends JpaRepository<ApplicationUser, String > {

    Optional<ApplicationUser> findByUsername(String username);

    List<ApplicationUser> findAllByRole(String role);
}
