package com.bit.ProjectApprovalSystem.repository;

import com.bit.ProjectApprovalSystem.entity.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, ObjectId> {
    List<Project> findByGuideId(ObjectId guideId);
    List<Project> findByStatus(com.bit.ProjectApprovalSystem.enums.ProjectStatus status);
    List<Project> findByIdIn(List<ObjectId> ids);
    List<Project> findByStatusAndGuideId(com.bit.ProjectApprovalSystem.enums.ProjectStatus status, ObjectId guideId);
    List<Project> findByStatusAndIdIn(com.bit.ProjectApprovalSystem.enums.ProjectStatus status, List<ObjectId> ids);
    List<Project> findByGuideIdAndIdIn(ObjectId guideId, List<ObjectId> ids);
    List<Project> findByStatusAndGuideIdAndIdIn(com.bit.ProjectApprovalSystem.enums.ProjectStatus status, ObjectId guideId, List<ObjectId> ids);
}
