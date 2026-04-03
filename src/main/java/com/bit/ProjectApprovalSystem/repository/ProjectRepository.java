package com.bit.ProjectApprovalSystem.repository;

import com.bit.ProjectApprovalSystem.entity.Project;
import com.bit.ProjectApprovalSystem.entity.ProjectMember;
import com.bit.ProjectApprovalSystem.enums.ProjectStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, ObjectId> {

    List<ProjectMember> findByProjectId(ObjectId projectId);

    Optional<ProjectMember> findByProjectIdAndStudentId(ObjectId projectId, ObjectId studentId);

    boolean existsByProjectIdAndStudentId(ObjectId projectId, ObjectId studentId);

    long countByProjectId(ObjectId projectId);

    boolean existsByStudentId(ObjectId studentId);
}
