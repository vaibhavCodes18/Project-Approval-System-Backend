package com.bit.ProjectApprovalSystem.repository;

import com.bit.ProjectApprovalSystem.entity.ProjectMember;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends MongoRepository<ProjectMember, ObjectId> {

    List<ProjectMember> findByProjectId(ObjectId projectId);

    List<ProjectMember> findByStudentId(ObjectId studentId);

    Optional<ProjectMember> findByProjectIdAndStudentId(ObjectId projectId, ObjectId studentId);

    boolean existsByProjectIdAndStudentId(ObjectId projectId, ObjectId studentId);

    long countByProjectId(ObjectId projectId);
}
