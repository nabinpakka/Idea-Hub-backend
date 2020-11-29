package com.example.IdeaHub.data.service;

import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.model.repo.FileDBRepo;
import com.example.IdeaHub.data.service.implementations.FileStorageServiceImpl;
import com.example.IdeaHub.message.ResponseFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceImplTest {

    @Mock
    private MultipartFile file;

    @Mock
    private FileDB fileDB;

    @Mock
    private FileDBRepo fileDBRepo;

    private final UUID uuid = UUID.randomUUID();
    private final String id  = uuid.toString();

    @InjectMocks
    private FileStorageServiceImpl fileStorageServiceImpl;


//    @Test
//    void storeFile() throws IOException {
//        byte[] bytes = mock(new byte())
//
//        when(file.getOriginalFilename()).thenReturn("fileName");
//        when(file.getBytes()).thenReturn(new byte[]);
//        when(new FileDB("fileName", file.getContentType(),file.getBytes())).thenReturn(fileDB);
//        assertThat(fileStorageServiceImpl.storeFile(file)).isNotNull();
//        assertThat(fileStorageServiceImpl.storeFile(file)).isInstanceOf(FileDB.class);
//
//    }

    //this is not complete
    @Test
    void getFile(){

        when(fileDBRepo.findById(id)).thenReturn(java.util.Optional.of(fileDB));

        FileDB file = fileStorageServiceImpl.getFile(id);
        assertThat(file).isNotNull();
        assertThat(file).isInstanceOf(FileDB.class);

    }

    @Test
    void deleteFile(){
        int status = fileStorageServiceImpl.deleteFile(id);
        assertThat(status).isEqualTo(1);

    }


 }
