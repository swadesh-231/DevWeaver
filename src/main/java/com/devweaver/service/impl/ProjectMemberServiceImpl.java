package com.devweaver.service.impl;

import com.devweaver.dto.member.InviteMemberRequest;
import com.devweaver.dto.member.MemberResponse;
import com.devweaver.dto.member.UpdateMemberRoleRequest;
import com.devweaver.entity.Project;
import com.devweaver.entity.ProjectMember;
import com.devweaver.entity.ProjectMemberId;
import com.devweaver.entity.User;
import com.devweaver.exception.BadRequestException;
import com.devweaver.exception.DuplicateResourceException;
import com.devweaver.exception.ResourceNotFoundException;
import com.devweaver.exception.UnauthorizedAccessException;
import com.devweaver.exception.UserNotFoundException;
import com.devweaver.mapper.ProjectMemberMapper;
import com.devweaver.repository.ProjectMemberRepository;
import com.devweaver.repository.ProjectRepository;
import com.devweaver.repository.UserRepository;
import com.devweaver.security.jwt.JwtUtils;
import com.devweaver.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMemberMapper projectMemberMapper;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    @PreAuthorize("@security.canViewMembers(#projectId)")
    public List<MemberResponse> getProjectMembers(Long projectId) {
        return projectMemberRepository.findByProjectMemberIdProjectId(projectId)
                .stream()
                .map(projectMemberMapper::toProjectMemberResponseFromMember)
                .toList();
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        Long userId = jwtUtils.getCurrentUserId();
        Project project = projectRepository.findAccessibleProjectById(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        User invitedUser = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("email", request.email()));
        if (invitedUser.getId().equals(userId)) {
            throw new BadRequestException("Cannot invite yourself to the project");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitedUser.getId());
        if (projectMemberRepository.existsById(projectMemberId)) {
            throw new DuplicateResourceException("Member", "email", request.email());
        }
        ProjectMember projectMember = ProjectMember
                .builder()
                .projectMemberId(projectMemberId)
                .project(project)
                .user(invitedUser)
                .projectRole(request.role())
                .invited_at(Instant.now())
                .build();
        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request) {
        Long userId = jwtUtils.getCurrentUserId();
        Project project = projectRepository.findAccessibleProjectById(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
        projectMember.setProjectRole(request.role());
        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public void deleteProjectMember(Long projectId, Long memberId) {
        Long userId = jwtUtils.getCurrentUserId();
        Project project = projectRepository.findAccessibleProjectById(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        if (!projectMemberRepository.existsById(projectMemberId)) {
            throw new ResourceNotFoundException("Member", "id", memberId);
        }
        projectMemberRepository.deleteById(projectMemberId);
    }
}
