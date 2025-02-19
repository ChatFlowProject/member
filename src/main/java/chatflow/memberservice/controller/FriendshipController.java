package chatflow.memberservice.controller;

import chatflow.memberservice.dto.ApiResponse;
import chatflow.memberservice.dto.friendship.request.FriendshipRequest;
import chatflow.memberservice.dto.friendship.response.FriendshipInfoResponse;
import chatflow.memberservice.security.MemberAuthorize;
import chatflow.memberservice.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "친구 API (인증 토큰 필요)")
@RequiredArgsConstructor
@MemberAuthorize
@RestController
@RequestMapping("/friendships")
public class FriendshipController {
    private final FriendshipService friendshipService;

    @Operation(summary = "친구 요청")
    @PostMapping
    public ApiResponse<Long> requestFriendship(@AuthenticationPrincipal User user, @Valid @RequestBody FriendshipRequest request) {
        friendshipService.requestFriendship(UUID.fromString(user.getUsername()), request);
        return ApiResponse.success();
    }

    @Operation(summary = "보낸 친구 요청 목록 조회")
    @GetMapping("/sent")
    public ApiResponse<List<FriendshipInfoResponse>> getSentFriendRequests(@AuthenticationPrincipal User user) {
        return ApiResponse.success(friendshipService.getSentFriendRequests(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "받은 친구 요청 목록 조회")
    @GetMapping("/received")
    public ApiResponse<List<FriendshipInfoResponse>> getReceivedFriendRequests(@AuthenticationPrincipal User user) {
        return ApiResponse.success(friendshipService.getReceivedFriendRequests(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "친구 전체 목록 조회")
    @GetMapping
    public ApiResponse<List<FriendshipInfoResponse>> getAllFriends(@AuthenticationPrincipal User user) {
        return ApiResponse.success(friendshipService.getAllFriends(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "온라인 친구 목록 조회")
    @GetMapping("/online")
    public ApiResponse<List<FriendshipInfoResponse>> getOnlineFriends(@AuthenticationPrincipal User user) {
        return ApiResponse.success(friendshipService.getOnlineFriends(UUID.fromString(user.getUsername())));
    }

    @Operation(summary = "친구 요청 수락")
    @PatchMapping("/{friendshipId}")
    public ApiResponse acceptFriendship(@AuthenticationPrincipal User user, @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.acceptFriendship(UUID.fromString(user.getUsername()), friendshipId);
        return ApiResponse.success();
    }

    @Operation(summary = "친구 요청 거절")
    @DeleteMapping("/{friendshipId}/refuse")
    public ApiResponse refuseFriendship(@AuthenticationPrincipal User user, @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.refuseFriendship(UUID.fromString(user.getUsername()), friendshipId);
        return ApiResponse.success();
    }

    @Operation(summary = "친구 요청 취소")
    @DeleteMapping("/{friendshipId}/cancel")
    public ApiResponse cancelFriendship(@AuthenticationPrincipal User user, @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.cancelFriendship(UUID.fromString(user.getUsername()), friendshipId);
        return ApiResponse.success();
    }

    @Operation(summary = "친구 삭제")
    @DeleteMapping("/{friendshipId}")
    public ApiResponse deleteFriendship(@AuthenticationPrincipal User user, @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.deleteFriendship(UUID.fromString(user.getUsername()), friendshipId);
        return ApiResponse.success();
    }

}
