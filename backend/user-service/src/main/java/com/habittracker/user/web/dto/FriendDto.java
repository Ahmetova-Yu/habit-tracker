package com.habittracker.user.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendDto {
    private Long id;
    private String username;
    private String avatarUrl;
}