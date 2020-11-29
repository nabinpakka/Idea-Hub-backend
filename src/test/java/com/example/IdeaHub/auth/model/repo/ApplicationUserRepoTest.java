package com.example.IdeaHub.auth.model.repo;


import com.example.IdeaHub.auth.model.ApplicationUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ApplicationUserRepoTest {

    @Autowired
    private ApplicationUserRepo applicationUserRepo;

    private ApplicationUser populateDatabase(){
        ApplicationUser applicationUser = new ApplicationUser("nabin","password","AUTHOR");
        applicationUserRepo.save(applicationUser);
        ApplicationUser applicationUser1 = new ApplicationUser("nabin1","password","AUTHOR");
        applicationUserRepo.save(applicationUser1);
        return applicationUser;
    }
    @Test
    void findByUsername(){
        ApplicationUser applicationUser = populateDatabase();

        Optional<ApplicationUser> applicationUser1 = applicationUserRepo.findByUsername(applicationUser.getUsername());

        assertThat(applicationUser1).isNotNull();
        assertThat(applicationUser1.get().getUsername()).isEqualTo(applicationUser.getUsername());

    }

    @Test
    void findAllByRole(){
        ApplicationUser applicationUser = populateDatabase();

        List<ApplicationUser> applicationUsers = applicationUserRepo.findAllByRole("AUTHOR");
        assertThat(applicationUsers).isNotNull();
        assertThat(applicationUsers.size()).isEqualTo(2);
        assertThat(applicationUsers.get(0).getUsername()).isEqualTo(applicationUser.getUsername());
    }
}
