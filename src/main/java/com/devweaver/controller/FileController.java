package com.devweaver.controller;

import com.devweaver.dto.project.FileContentResponse;
import com.devweaver.dto.project.FileNode;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/projects/{projectId}/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable Long projectId) {
        Long userId = jwtUtils.getCurrentUserId();
        return ResponseEntity.ok(fileService.getFileTree(projectId, userId));
    }

    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse> getFile(
            @PathVariable Long projectId,
            @PathVariable String path
    ) {
        Long userId = jwtUtils.getCurrentUserId();
        return ResponseEntity.ok(fileService.getFileContent(projectId, path, userId));
    }
}
