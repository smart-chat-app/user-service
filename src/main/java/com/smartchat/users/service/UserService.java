package com.smartchat.users.service;

import com.smartchat.users.domain.UserDocument;
import com.smartchat.users.dto.UpdateProfileRequest;
import com.smartchat.users.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Component
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public Mono<UserDocument> getOrCreateByUserId(String userId) {
        return null;
/*        return repo.findByUserId(userId)
                .switchIfEmpty(repo.save(new UserDocument(userId, "user_" + userId.substring(0, 6), "New User")));*/
    }

    public Mono<UserDocument> updateProfile(String userId, UpdateProfileRequest req) {
        return getOrCreateByUserId(userId).flatMap(u -> {
            if (req.displayName != null) u.setDisplayName(req.displayName);
            if (req.bio != null) u.setBio(req.bio);
            if (req.avatarUrl != null) u.setAvatarUrl(req.avatarUrl);
            return repo.save(u);
        });
    }

    public Mono<UserDocument> getPublic(String idOrUsername) {
        return repo.findByUserId(idOrUsername)
                .switchIfEmpty(repo.findByUsername(idOrUsername));
    }
}
