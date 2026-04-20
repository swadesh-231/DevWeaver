package com.devweaver.repository;

import com.devweaver.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
        SELECT p FROM Project p
            JOIN ProjectMember pm ON pm.project.id = p.id
            WHERE pm.user.id = :userId
              AND p.deletedAt IS NULL
            ORDER BY p.updatedAt DESC
      """)
    List<Project> findAllAccessibleByUser(@Param("userId") Long userId);

    @Query("""
        SELECT p FROM Project p
            WHERE p.id = :projectId
              AND p.deletedAt IS NULL
              AND EXISTS (
                  SELECT 1 FROM ProjectMember pm
                  WHERE pm.projectMemberId.userId = :userId
                  AND pm.projectMemberId.projectId = :projectId
              )
      """)
    Optional<Project> findAccessibleProjectById(@Param("projectId") Long projectId,
                                         @Param("userId") Long userId);
}
