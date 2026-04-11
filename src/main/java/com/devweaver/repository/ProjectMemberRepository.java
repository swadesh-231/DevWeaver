package com.devweaver.repository;

import com.devweaver.entity.ProjectMember;
import com.devweaver.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByProjectId(Long projectId);

    Optional<ProjectMember> findByProjectMemberIdProjectIdAndProjectMemberIdUserId(Long projectId, Long userId);
}
