package com.example.IdeaHub.repo;

import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.repo.PublicationRepo;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=false"
})

@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
public class PublicationRepoTest {

    @Autowired
    private PublicationRepo publicationRepo;


    @Test
    @DatabaseSetup("createPublication.xml")
    void findAllByApprovedAndPublicationHouse(){
        List<Publication> publication = publicationRepo.findAllByApprovedAndPublicationHouse(false,"pakka");
        assertThat(publication).isNotNull();

    }

}
