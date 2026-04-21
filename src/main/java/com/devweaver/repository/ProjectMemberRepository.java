package com.devweaver.repository;

import com.devweaver.entity.ProjectMember;
import com.devweaver.entity.ProjectMemberId;
import com.devweaver.entity.enums.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByProjectMemberIdProjectId(Long projectId);

    @Query("""
            SELECT pm.projectRole FROM ProjectMember pm
            WHERE pm.projectMemberId.projectId = :projectId AND pm.projectMemberId.userId = :userId
            """)
    Optional<ProjectRole> findRoleByProjectIdAndUserId(@Param("projectId") Long projectId,
                                                       @Param("userId") Long userId);


    @Query("""
            SELECT COUNT(pm) FROM ProjectMember pm
            WHERE pm.projectMemberId.userId = :userId AND pm.projectRole = com.devweaver.entity.enums.ProjectRole.OWNER
            """)
    int countProjectOwnedByUser(@Param("userId") Long userId);
}
