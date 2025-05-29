package chatflow.memberservice.presentation.controller.friendship;

import chatflow.memberservice.infrastructure.security.MemberAuthorize;
import chatflow.memberservice.presentation.dto.ApiResponse;
import chatflow.memberservice.presentation.dto.friendship.request.FriendshipRequest;
import chatflow.memberservice.presentation.dto.friendship.response.FriendshipResponse;
import chatflow.memberservice.service.friendship.FriendshipServiceV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "친구 API (인증 토큰 필요)")
@RequiredArgsConstructor
@MemberAuthorize
@RestController
@RequestMapping("/v2/friendships")
public class FriendshipControllerV2 {
    private final FriendshipServiceV2 friendshipService;

    @Operation(summary = "친구 요청")
    @PostMapping
    public ApiResponse<FriendshipResponse> requestFriendship(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody FriendshipRequest request) {
        return ApiResponse.success(friendshipService.requestFriendshipByNickname(UUID.fromString(user.getUsername()), request));
    }

    @Operation(summary = "친구 요청 수락")
    @PatchMapping("/{friendshipId}")
    public ApiResponse acceptFriendship(
            @AuthenticationPrincipal User user,
            @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.acceptFriendship(UUID.fromString(user.getUsername()), friendshipId);
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
