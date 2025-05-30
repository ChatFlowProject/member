package chatflow.memberservice.presentation.controller.friendship;

import chatflow.memberservice.presentation.dto.ApiResponse;
import chatflow.memberservice.presentation.dto.friendship.request.FriendshipRequest;
import chatflow.memberservice.presentation.dto.friendship.response.FriendshipInfoResponse;
import chatflow.memberservice.presentation.dto.friendship.response.FriendshipResponse;
import chatflow.memberservice.infrastructure.security.MemberAuthorize;
import chatflow.memberservice.service.friendship.FriendshipService;
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
    public ApiResponse<FriendshipResponse> requestFriendship(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody FriendshipRequest request) {
        return ApiResponse.success(friendshipService.requestFriendshipByNickname(UUID.fromString(user.getUsername()), request));
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

    @Operation(summary = "친구 관계 조회")
    @GetMapping("/me")
    public ApiResponse<Boolean> checkFriendship(
            @AuthenticationPrincipal User user,
            @RequestParam("friendId") UUID friendId) {
        return ApiResponse.success(friendshipService.isFriend(UUID.fromString(user.getUsername()), friendId));
    }

    @Operation(summary = "친구 요청 수락")
    @PatchMapping("/{friendshipId}")
    public ApiResponse acceptFriendship(
            @AuthenticationPrincipal User user,
            @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.acceptFriendship(UUID.fromString(user.getUsername()), friendshipId);
        return ApiResponse.success();
    }

    @Operation(summary = "친구 요청 거절")
    @DeleteMapping("/{friendshipId}/refuse")
    public ApiResponse refuseFriendship(
            @AuthenticationPrincipal User user,
            @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.refuseFriendship(UUID.fromString(user.getUsername()), friendshipId);
        return ApiResponse.success();
    }

    @Operation(summary = "친구 요청 취소")
    @DeleteMapping("/{friendshipId}/cancel")
    public ApiResponse cancelFriendship(
            @AuthenticationPrincipal User user,
            @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.cancelFriendship(UUID.fromString(user.getUsername()), friendshipId);
        return ApiResponse.success();
    }

    @Operation(summary = "친구 삭제")
    @DeleteMapping("/{friendshipId}")
    public ApiResponse deleteFriendship(
            @AuthenticationPrincipal User user,
            @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.deleteFriendship(UUID.fromString(user.getUsername()), friendshipId);
        return ApiResponse.success();
    }

}
