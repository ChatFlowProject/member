package chatflow.memberservice.controller;

import chatflow.memberservice.dto.ApiResponse;
import chatflow.memberservice.dto.friendship.FriendshipRequest;
import chatflow.memberservice.dto.friendship.SentFriendResponse;
import chatflow.memberservice.security.MemberAuthorize;
import chatflow.memberservice.service.FriendshipService;
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
@RequestMapping("/friendships")
public class FriendshipController {
    private final FriendshipService friendshipService;

    @Operation(summary = "친구 요청")
    @PostMapping
    public ApiResponse requestFriendship(@AuthenticationPrincipal User user, @Valid @RequestBody FriendshipRequest request) {
        friendshipService.requestFriendship(UUID.fromString(user.getUsername()), request);
        return ApiResponse.success();
    }

    @Operation(summary = "친구 수락")
    @PatchMapping("/{friendshipId}")
    public ApiResponse acceptFriendship(@AuthenticationPrincipal User user, @PathVariable("friendshipId") Long friendshipId) {
        friendshipService.acceptFriendship(UUID.fromString(user.getUsername()), friendshipId);
        return ApiResponse.success();
    }

    @Operation(summary = "내가 보낸 친구 요청 목록 조회")
    @GetMapping("/sent")
    public ApiResponse<SentFriendResponse> getSentFriendRequests(@AuthenticationPrincipal User user) {
        return ApiResponse.success(friendshipService.getSentFriendRequests(UUID.fromString(user.getUsername())));
    }

//    @Operation(summary = "받은 친구 요청 목록 조회")
//    @GetMapping("/received")
//    public ApiResponse getReceivedFriendRequests(@AuthenticationPrincipal User user) {
//        UUID memberId = UUID.fromString(user.getUsername());
//        List<Member> receivedRequests = friendshipService.getReceivedFriendRequests(memberId);
//        return ApiResponse.success(receivedRequests.stream().map(MemberResponse::from).collect(Collectors.toList()));
//    }

//    @Operation(summary = "친구 목록 조회")
//    @GetMapping
//    public ApiResponse getFriends(@AuthenticationPrincipal User user) {
//        UUID memberId = UUID.fromString(user.getUsername());
//        List<Member> friends = friendshipService.getFriends(memberId);
//        return ApiResponse.success(friends.stream().map(MemberResponse::from).collect(Collectors.toList()));
//    }

//    @Operation(summary = "친구 요청 거절")
//    @Operation(summary = "친구 요청 취소")
}
