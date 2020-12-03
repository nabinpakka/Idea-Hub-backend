package com.example.IdeaHub.data.controller;

import com.example.IdeaHub.data.model.FileDB;
import com.example.IdeaHub.data.service.interfaces.FileStorageService;
import com.example.IdeaHub.message.ResponseFile;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ApiOperation(value = "/files",tags = "File Controller")
@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    //getting file by id
    @ApiOperation(value = "returns file download link of given id",response = String.class)
    @ApiResponse(code=200,message = "file loaded")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseFile> getFileResponse(@PathVariable String id){
         return fileStorageService.getFileResponse(id);

    }

    @ApiOperation(value = "returns file",response = String.class)
    @ApiResponse(code=200,message = "file loaded")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id){
        FileDB fileDB = fileStorageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());

    }


}
