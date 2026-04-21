package com.devweaver.controller;

import com.devweaver.dto.member.InviteMemberRequest;
import com.devweaver.dto.member.MemberResponse;
import com.devweaver.dto.member.UpdateMemberRoleRequest;
import com.devweaver.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/members")
@RequiredArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @GetMapping
    @PreAuthorize("@security.canViewMembers(#projectId)")
    public ResponseEntity<@NonNull List<MemberResponse>> getProjectMembers(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId));
    }

    @PostMapping
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public ResponseEntity<MemberResponse> inviteMember(
            @PathVariable Long projectId,
            @Valid @RequestBody InviteMemberRequest request
    ) {
        return ResponseEntity.ok(projectMemberService.inviteMember(projectId, request));
    }

    @PatchMapping("/{memberId}")
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @Valid @RequestBody UpdateMemberRoleRequest request
    ) {
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId, memberId, request));
    }

    @DeleteMapping("/{memberId}")
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public ResponseEntity<Void> removeMemberRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId
    ) {
        projectMemberService.deleteProjectMember(projectId, memberId);
        return ResponseEntity.noContent().build();
    }
}
