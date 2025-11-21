package com.smartchat.users.dto.user;

import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {
    @Size(min = 1, max = 40)
    public String displayName;
    @Size(max = 200)
    public String bio;
    public String avatarUrl;
}
