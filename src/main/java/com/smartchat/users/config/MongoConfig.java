package com.smartchat.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoConfig {

    /**
     * Custom conversions (example: OffsetDateTime <-> Date).
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Object> converters = new ArrayList<>();

        // Write: OffsetDateTime -> Date
        converters.add(new org.springframework.core.convert.converter.Converter<OffsetDateTime, Date>() {
            @Override public Date convert(OffsetDateTime source) { return Date.from(source.toInstant()); }
        });

        // Read: Date -> OffsetDateTime (UTC)
        converters.add(new org.springframework.core.convert.converter.Converter<Date, OffsetDateTime>() {
            @Override public OffsetDateTime convert(Date source) { return OffsetDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC); }
        });

        return new MongoCustomConversions(converters);
    }

    /**
     * Post-process the MappingMongoConverter that Spring Boot creates for reactive Mongo:
     *  - remove the _class field
     *  - register our custom conversions
     */
    @Bean
    public BeanPostProcessor mappingMongoConverterCustomizer(MongoCustomConversions conversions) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                if (bean instanceof MappingMongoConverter converter) {
                    converter.setTypeMapper(new DefaultMongoTypeMapper(null)); // drop _class
                    converter.setCustomConversions(conversions);
                    converter.afterPropertiesSet();
                }
                return bean;
            }
        };
    }

    /**
     * ReactiveMongoTemplate using the reactive factory and the (auto-configured) converter.
     * Spring Boot provides both when you have spring-boot-starter-data-mongodb-reactive on the classpath
     * and spring.data.mongodb.uri is configured.
     */
    @Bean
    @Primary
    public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory reactiveFactory,
                                                       MappingMongoConverter converter) {
        return new ReactiveMongoTemplate(reactiveFactory, converter);
    }

}
