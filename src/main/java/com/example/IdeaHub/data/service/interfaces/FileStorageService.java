package com.example.IdeaHub.data.service.interfaces;

import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.message.ResponseFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    FileDB storeFile(MultipartFile file)throws IOException;

    FileDB getFile(String id );

    int deleteFile(String id);

    ResponseEntity<ResponseFile> getFileResponse(String id);
}
