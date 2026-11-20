package com.habittracker.user.web;

import com.habittracker.common.security.SecurityConstants;
import com.habittracker.user.service.FriendshipService;
import com.habittracker.user.web.dto.FriendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendshipService friendshipService;

    // 1. Отправить заявку
    @PostMapping("/request/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendRequest(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long myId,
            @PathVariable Long userId) {
        friendshipService.sendRequest(myId, userId);
    }

    // 2. Принять заявку
    @PostMapping("/accept/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptRequest(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long myId,
            @PathVariable Long userId) {
        friendshipService.acceptRequest(myId, userId);
    }

    // 3. Мои друзья
    @GetMapping
    public List<FriendDto> getFriends(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long myId) {
        return friendshipService.getFriends(myId);
    }

    // 4. Входящие заявки (кого можно принять)
    @GetMapping("/requests")
    public List<FriendDto> getPendingRequests(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long myId) {
        return friendshipService.getPendingRequests(myId);
    }

    @PostMapping("/request/by-username")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendRequestByUsername(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long myId,
            @RequestParam String username) {
        friendshipService.sendRequestByUsername(myId, username);
    }

    @DeleteMapping("/request/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrDeclineRequest(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long myId,
            @PathVariable Long userId) {
        friendshipService.cancelOrDeclineRequest(myId, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long myId,
            @PathVariable Long userId) {
        friendshipService.removeFriend(myId, userId);
    }
}