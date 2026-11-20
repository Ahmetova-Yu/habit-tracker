package com.habittracker.user.service;

import com.habittracker.common.exception.BusinessException;
import com.habittracker.common.exception.NotFoundException;
import com.habittracker.user.domain.Friendship;
import com.habittracker.user.domain.FriendshipStatus;
import com.habittracker.user.domain.User;
import com.habittracker.user.repository.FriendshipRepository;
import com.habittracker.user.repository.UserRepository;
import com.habittracker.user.web.dto.FriendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendRequest(Long requesterId, Long addresseeId) {
        if (requesterId.equals(addresseeId)) {
            throw new BusinessException("You cannot add yourself as a friend");
        }

        // Проверяем, существует ли юзер
        userRepository.findById(addresseeId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Проверяем, есть ли уже заявка или дружба
        if (friendshipRepository.findBetweenUsers(requesterId, addresseeId).isPresent()) {
            throw new BusinessException("Friend request already exists or you are already friends");
        }

        Friendship friendship = Friendship.builder()
                .requesterId(requesterId)
                .addresseeId(addresseeId)
                .status(FriendshipStatus.PENDING)
                .build();

        friendshipRepository.save(friendship);
    }

    @Transactional
    public void acceptRequest(Long userId, Long requesterId) {
        Friendship friendship = friendshipRepository.findBetweenUsers(userId, requesterId)
                .orElseThrow(() -> new NotFoundException("Friend request not found"));

        // Принимать может только тот, кому отправили заявку (addressee)
        if (!friendship.getAddresseeId().equals(userId)) {
            throw new BusinessException("You can only accept requests sent to you");
        }

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new BusinessException("Request is already processed");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
    }

    @Transactional(readOnly = true)
    public List<FriendDto> getFriends(Long userId) {
        return friendshipRepository.findAllByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED)
                .stream()
                .map(f -> {
                    Long friendId = f.getRequesterId().equals(userId) ? f.getAddresseeId() : f.getRequesterId();
                    User user = userRepository.findById(friendId)
                            .orElseThrow(() -> new NotFoundException("User not found"));
                    return new FriendDto(user.getId(), user.getUsername(), user.getAvatarUrl());
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendDto> getPendingRequests(Long userId) {
        return friendshipRepository.findAllByAddresseeIdAndStatus(userId, FriendshipStatus.PENDING)
                .stream()
                .map(f -> {
                    User user = userRepository.findById(f.getRequesterId())
                            .orElseThrow(() -> new NotFoundException("User not found"));
                    return new FriendDto(user.getId(), user.getUsername(), user.getAvatarUrl());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendRequestByUsername(Long requesterId, String targetUsername) {
        // 1. Ищем юзера по нику
        User addressee = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new NotFoundException("User with username '" + targetUsername + "' not found"));

        // 2. Вызываем уже готовый метод sendRequest
        sendRequest(requesterId, addressee.getId());
    }

    @Transactional
    public void cancelOrDeclineRequest(Long userId, Long otherUserId) {
        // 1. Ищем заявку между ними в любую сторону
        Friendship friendship = friendshipRepository.findBetweenUsers(userId, otherUserId)
                .orElseThrow(() -> new NotFoundException("Friend request not found"));

        // 2. Проверяем, что она в статусе PENDING (нельзя отменить уже принятую дружбу)
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new BusinessException("You can only cancel or decline pending requests");
        }

        // 3. Удаляем запись
        friendshipRepository.delete(friendship);
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        // 1. Ищем заявку между ними
        Friendship friendship = friendshipRepository.findBetweenUsers(userId, friendId)
                .orElseThrow(() -> new NotFoundException("Friendship not found"));

        // 2. Проверяем, что они реально друзья
        if (friendship.getStatus() != FriendshipStatus.ACCEPTED) {
            throw new BusinessException("You can only remove users from your friends list");
        }

        // 3. Удаляем запись
        friendshipRepository.delete(friendship);
    }
}