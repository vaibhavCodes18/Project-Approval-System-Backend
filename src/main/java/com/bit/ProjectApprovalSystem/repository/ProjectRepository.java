package com.bit.ProjectApprovalSystem.repository;

import com.bit.ProjectApprovalSystem.entity.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, ObjectId> {
    List<Project> findByGuideId(ObjectId guideId);
}
