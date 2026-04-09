package com.bit.ProjectApprovalSystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
public class MongoIndexConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void dropObsoleteIndexes() {
        try {
            mongoTemplate.getCollection("projects").dropIndexes();
            System.out.println("Successfully cleared all stale indexes from 'projects' collection!");
        } catch (Exception e) {
            System.out.println("Could not clear indexes from 'projects': " + e.getMessage());
        }

        try {
            mongoTemplate.getCollection("project_members").dropIndexes();
            System.out.println("Successfully cleared all stale indexes from 'project_members' collection!");
        } catch (Exception e) {
            System.out.println("Could not clear indexes from 'project_members': " + e.getMessage());
        }
    }
}
