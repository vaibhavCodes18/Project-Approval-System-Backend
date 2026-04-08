package com.bit.ProjectApprovalSystem.repository;

import com.bit.ProjectApprovalSystem.entity.RefreshToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResfreshTokenRepository extends MongoRepository<RefreshToken, ObjectId> {
}
