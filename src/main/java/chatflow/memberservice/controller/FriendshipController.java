package chatflow.memberservice.controller;

import chatflow.memberservice.dto.ApiResponse;
import chatflow.memberservice.dto.friendship.FriendshipRequest;
import chatflow.memberservice.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
