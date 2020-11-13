package com.example.IdeaHub.service;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.repo.PublicationRepo;
import com.example.IdeaHub.data.service.PublicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublicationServiceImplTest {

    @Mock
    private PublicationRepo publicationRepo;

    @Mock
    private Publication publication;

    @InjectMocks
    private PublicationServiceImpl publicationService;
//
//    @BeforeEach
//    void initPublicationService(){
//        publicationService = new PublicationServiceImpl(publicationRepo);
//
//    }

    @Test
    void upload(){

        ResponseEntity<ResponseMessage> upload = publicationService.upload(publication);
        assertThat(upload.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    @Test
    void updateReviewScore(){
        String id = "asdfa-fasdf-afs-fas-df";
        when(publicationRepo.findById(id)).thenReturn(
                Optional.ofNullable(publication)
        );
        ResponseEntity<ResponseMessage> returnedResponse= publicationService.updateReviewScore(id);
        assertThat(Objects.requireNonNull(returnedResponse.getBody()).getMessage()).isEqualTo(
                "Review Score updated successfully"
        );

    }


    @Test
    public void getPublicationToReview(){
        String id = "kasj-asdf-a-daf";
        when(publicationRepo.findAllByApprovedAndPublicationHouse(false,id)).thenReturn(
                //study this method
                Collections.singletonList(publication)
        );
        ResponseEntity<List<Publication>> publications = publicationService.getPublicationToReview(id);
        assertThat(publications.getBody().get(0)).isInstanceOf(Publication.class);
    }



//    @Test
//    public void updateApprovedStatus(){
//        String id = "sadf-dasfka-asdfalkfa";
//        assertThat(publicationService)
//
//    }
}
