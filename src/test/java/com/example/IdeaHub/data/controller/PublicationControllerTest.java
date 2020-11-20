package com.example.IdeaHub.data.controller;

import com.example.IdeaHub.config.SecurityConfiguration;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.service.implementations.FileStorageServiceImpl;
import com.example.IdeaHub.data.service.implementations.PublicationServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = PublicationController.class)
public class PublicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockMvcBuilder mockMvcBuilder;

    @MockBean
    private PublicationServiceImpl publicationService;

    @MockBean
    private FileStorageServiceImpl fileStorageService;

    @MockBean
    private SecurityConfiguration securityConfiguration;

    @MockBean
    private WebSecurityConfiguration webSecurityConfiguration;

    @Mock
    private MultipartFile file;


    private String mapToJson(Object object) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    @Test
    public void getPublication() throws Exception {

        String id = "askjdfa-daslkfa";
        Publication publication = new Publication("title1","alkdjlafl","laksdjflkajlk","pakka","fileIds");;
        publication.setReviewScore(2);
        publication.setUuid(id);
        publication.setFileId("akl-adjfilajfl");


        String inputJson = this.mapToJson(publication);

        String uri = "/api/publication/1";

        when( publicationService.getPublication(id)).thenReturn(java.util.Optional.of(publication));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertThat(result.getResponse().getContentAsString()).isNotNull();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(inputJson);
    }


}
