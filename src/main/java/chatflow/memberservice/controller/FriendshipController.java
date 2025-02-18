package chatflow.memberservice.controller;

import chatflow.memberservice.dto.ApiResponse;
import chatflow.memberservice.dto.friendship.FriendshipRequest;
import chatflow.memberservice.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "친구 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/friendship")
public class FriendshipController {
    private final FriendshipService friendshipService;

    @Operation(summary = "친구 요청")
    @PostMapping
    public ApiResponse requestFriendship(@Valid @RequestBody FriendshipRequest request) {
        friendshipService.requestFriendship(request);
        return ApiResponse.success();
    }

    @Operation(summary = "친구 수락")
    @PatchMapping("/{friendshipId}")
    public ApiResponse acceptFriendship(@PathVariable("friendshipId") Long friendshipId) {
        friendshipService.acceptFriendship(friendshipId);
        return ApiResponse.success();
    }

}
