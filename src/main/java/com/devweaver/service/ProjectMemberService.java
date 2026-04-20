package com.devweaver.service;

import com.devweaver.dto.member.InviteMemberRequest;
import com.devweaver.dto.member.MemberResponse;
import com.devweaver.dto.member.UpdateMemberRoleRequest;
import com.devweaver.entity.ProjectMember;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId);
    MemberResponse inviteMember(Long projectId, InviteMemberRequest request);
    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request);
    void deleteProjectMember(Long projectId, Long memberId);
}
