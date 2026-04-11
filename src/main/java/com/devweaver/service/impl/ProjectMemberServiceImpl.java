package com.devweaver.service.impl;

import com.devweaver.dto.member.InviteMemberRequest;
import com.devweaver.dto.member.MemberResponse;
import com.devweaver.dto.member.UpdateMemberRoleRequest;
import com.devweaver.entity.Project;
import com.devweaver.entity.ProjectMember;
import com.devweaver.repository.ProjectRepository;
import com.devweaver.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectRepository projectRepository;
    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
       Project project = projectRepository.findAccessibleProjectById(projectId,userId)
               .orElse(null);
       return null;


    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId) {
        return null;
    }
}
