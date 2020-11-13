package com.example.IdeaHub.data.controller;

import com.example.IdeaHub.data.message.ResponseMessage;
import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    //getting file by id
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id){
        FileDB fileDB = fileStorageService.getFile(id);
        System.out.println(fileDB);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }


}
