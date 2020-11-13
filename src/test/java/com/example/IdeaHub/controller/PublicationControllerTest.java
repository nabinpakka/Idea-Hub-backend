package com.example.IdeaHub.controller;

import com.example.IdeaHub.data.controller.PublicationController;
import com.example.IdeaHub.data.model.Publication;
import com.example.IdeaHub.data.service.FileStorageServiceImpl;
import com.example.IdeaHub.data.service.PublicationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicationController.class)
public class PublicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private PublicationServiceImpl publicationService;

    @MockBean
    private FileStorageServiceImpl fileStorageService;

    @Mock
    private MultipartFile file;

    @Test
    void whenValidInput_thenReturns200() throws Exception{
        mockMvc.perform(get("/api/publication/{id}","dasf-daf-aaa")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }


}
