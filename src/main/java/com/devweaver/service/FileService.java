package com.devweaver.service;

import com.devweaver.dto.project.FileContentResponse;
import com.devweaver.dto.project.FileNode;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(Long projectId, Long userId);
    FileContentResponse getFileContent(Long projectId, String path, Long userId);
}
