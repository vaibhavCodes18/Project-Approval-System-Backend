package com.bit.ProjectApprovalSystem.repository;

import com.bit.ProjectApprovalSystem.entity.Approval;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ApprovalRepository extends MongoRepository<Approval, ObjectId> {
    List<Approval> findByProjectIdOrderByActionAtAsc(ObjectId projectId);
    List<Approval> findByProjectId(ObjectId projectId);
}
