package com.smartchat.users.dto.user;

import jakarta.validation.constraints.Size;


public record UpdateProfileRequest(@Size(min = 1, max = 40) String displayName, @Size(max = 200) String bio,
                                   String avatarUrl) {
}
