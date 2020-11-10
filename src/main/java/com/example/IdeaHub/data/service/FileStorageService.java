package com.example.IdeaHub.data.service;

import com.example.IdeaHub.data.model.FileDB;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    public FileDB store(MultipartFile file)throws IOException;

    public FileDB getFile(String id );
}
