package com.example.IdeaHub.data.service;

import com.example.IdeaHub.auth.service.ApplicationUserDetails;
import com.example.IdeaHub.data.model.repo.ReviewersRepo;
import com.example.IdeaHub.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.model.repo.PublicationRepo;
import com.example.IdeaHub.data.service.implementations.PublicationServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublicationServiceImplTest {

    private final UUID uuid = UUID.randomUUID();

    private final String id = uuid.toString();

    @Mock
    private PublicationRepo publicationRepo;

    @Mock
    private Publication publication;

    private final List<Publication> publications  = new ArrayList<>();


    @InjectMocks
    private PublicationServiceImpl publicationService;


    @Mock
    private ReviewersRepo reviewersRepo;

    @BeforeEach
    void initPublicationList(){
        publications.add(new Publication());
        publications.add(new Publication());
    }

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }

    // these are the mocks needed for mocking security context holder
    private void configSecurityContext(){
        ApplicationUserDetails applicationUser = mock(ApplicationUserDetails.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);

    }



    @Test
    void uploadPublication(){
        this.configSecurityContext();
        Publication publication = mock(Publication.class);
        when(publicationRepo.findByTitle(publication.getTitle())).thenReturn(null);
        when(publicationService.getCurrentApplicationUserId()).thenReturn(id);
        ResponseEntity<ResponseMessage> response = publicationService.uploadPublication(publication);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void getPublication(){
        when(publicationRepo.findById(id)).thenReturn(Optional.of(publication));
        ResponseEntity<Publication> publication1= publicationService.getPublication(id);
        assertThat(publication1.getStatusCode()).isNotNull();
        assertThat(publication1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(publication1.getBody()).isInstanceOf(Publication.class);
    }


    //get user with role author for id inside getMyPublication
    @Test
    void getAuthorPublications(){
        this.configSecurityContext();

        when(publicationRepo.findAllByAuthorId(id)).thenReturn(publications);
        when(publicationService.getCurrentApplicationUserId()).thenReturn(id);
        ResponseEntity<List<Publication>> response = publicationService.getAuthorPublications();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get(0)).isInstanceOf(Publication.class);

    }


    @Test
    void getApprovedPublications(){
        when(publicationRepo.findAllByApproved(true)).thenReturn(publications);
        ResponseEntity<List<Publication>> response = publicationService.getApprovedPublications();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get(0)).isInstanceOf(Publication.class);
    }

    @Test
    void updateReviewScore(){
//        String id = "asdfa-fasdf-afs-fas-df";
        this.configSecurityContext();
        when(publicationRepo.findById(id)).thenReturn(
                Optional.ofNullable(publication)
        );
        when(publicationService.getCurrentApplicationUserId()).thenReturn(id);
        ResponseEntity<ResponseMessage> returnedResponse= publicationService.updateReviewScore(id,true);
        assertThat(Objects.requireNonNull(returnedResponse.getBody()).getMessage()).isEqualTo(
                "Review Score updated successfully"
        );

    }


    @Test
    public void getPublicationToReview(){
        this.configSecurityContext();
        when(publicationService.getCurrentApplicationUserId()).thenReturn(id);
        when(publicationRepo.findAllByApprovedAndPublicationHouse(false,id)).thenReturn(
                //study this method
                Collections.singletonList(publication)
        );
        ResponseEntity<List<Publication>> publications = publicationService.getPublicationToReview();
        assertThat(publications.getBody().get(0)).isInstanceOf(Publication.class);
    }


    @Test
    public void getPublicationsToReviewByAuthor(){
        this.configSecurityContext();

        when(publicationService.getCurrentApplicationUserId()).thenReturn(id);
        ResponseEntity<List<Publication>> response = publicationService.getPublicationsToReviewByAuthor();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody().get(0)).isInstanceOf(Publication.class);

    }

    private List<String> getIds(){
        List<String> ids =new ArrayList<>();
        ids.add(id+"1");
        ids.add(id);
        ids.add(id+"1");
        ids.add(id);
        ids.add(id);
        return ids;
    }


//    ResponseEntity<ResponseMessage> assignReviewers(String publicationId, List<String> reviewers)

    @Test
    public void assignReviewers(){

        List<String> ids = getIds();
        when(publicationRepo.findById(id)).thenReturn(Optional.of(publication));
        ResponseEntity<ResponseMessage> response = publicationService.assignReviewers(id,ids);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
