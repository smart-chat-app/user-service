package com.smartchat.users.config;

import com.smartchat.users.domain.UserDocument;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import reactor.core.publisher.Mono;

@Configuration
public class MongoConfig {

    @Bean
    public ApplicationRunner ensureIndexes(ReactiveMongoTemplate template) {
        return args -> {
            template.indexOps(UserDocument.class)
                    .ensureIndex(new Index().on("username", org.springframework.data.domain.Sort.Direction.ASC).unique())
                    .then(template.indexOps(UserDocument.class)
                            .ensureIndex(new Index().on("userId", org.springframework.data.domain.Sort.Direction.ASC).unique()))
                    .onErrorResume(e -> Mono.empty())
                    .subscribe();
        };
    }
}
