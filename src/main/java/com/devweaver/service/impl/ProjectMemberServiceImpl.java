package com.devweaver.service.impl;

import com.devweaver.dto.member.InviteMemberRequest;
import com.devweaver.dto.member.MemberResponse;
import com.devweaver.dto.member.UpdateMemberRoleRequest;
import com.devweaver.entity.Project;
import com.devweaver.entity.ProjectMember;
import com.devweaver.entity.ProjectMemberId;
import com.devweaver.entity.User;
import com.devweaver.mapper.ProjectMapper;
import com.devweaver.mapper.ProjectMemberMapper;
import com.devweaver.repository.ProjectMemberRepository;
import com.devweaver.repository.ProjectRepository;
import com.devweaver.repository.UserRepository;
import com.devweaver.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMemberMapper projectMemberMapper;
    private final UserRepository userRepository;
    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(projectId, userId)
                .orElse(null);
        List<MemberResponse> projectMembers = new ArrayList<>();
        projectMembers.add(projectMemberMapper.toMemberResponseFromCreator(project.getCreator()));
        projectMembers.addAll(
                projectMemberRepository.findByProjectId(projectId)
                        .stream()
                        .map(projectMemberMapper::toProjectMemberResponseFromMember)
                        .toList());
        return projectMembers;

    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(projectId, userId)
                .orElse(null);
        if (!project.getCreator().getId().equals(userId)) {
            throw new RuntimeException("Not Allowed to invite member");
        }
        User invitedUser = userRepository.findByEmail(request.email())
                .orElse(null);
        if (invitedUser.getId().equals(userId)) {
            throw new RuntimeException("Not Allowed to invite member");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitedUser.getId());
        if (projectMemberRepository.existsById(projectMemberId)) {
            throw new RuntimeException("Not Allowed to invite member");
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
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(projectId, userId)
                .orElse(null);
        if (!project.getCreator().getId().equals(userId)) {
            throw new RuntimeException("Not Allowed to invite member");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember projectMember  =  projectMemberRepository.findById(projectMemberId).orElse(null);
        projectMember.setProjectRole(request.role());
        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    public void deleteProjectMember(Long projectId, Long memberId, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(projectId, userId)
                .orElse(null);
        if (!project.getCreator().getId().equals(userId)) {
            throw new RuntimeException("Not Allowed to invite member");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        if (projectMemberRepository.existsById(projectMemberId)) {
            throw new RuntimeException("Not Allowed to invite member");
        }
        projectMemberRepository.deleteById(projectMemberId);

    }
}
