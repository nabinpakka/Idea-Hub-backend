package com.example.IdeaHub.service;

import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.repo.FileDBRepo;
import com.example.IdeaHub.data.service.FileStorageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceImplTest {

    @Mock
    private MultipartFile file;

    @Mock
    private FileDB fileDB;

    @Mock
    private FileDBRepo fileDBRepo;


    @InjectMocks
    private FileStorageServiceImpl fileStorageServiceImpl;


    @Test
    void store() throws IOException {

        FileDB returnedFile = fileStorageServiceImpl.store(file);
        assertThat(returnedFile).isInstanceOf(FileDB.class);
    }


 }
