package com.example.IdeaHub.data.repo;

import com.example.IdeaHub.data.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("file")
public interface FileDBRepo extends JpaRepository<FileDB, String> {
}
