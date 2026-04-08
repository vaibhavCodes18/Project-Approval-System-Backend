package com.bit.ProjectApprovalSystem.repository;

import com.bit.ProjectApprovalSystem.entity.RefreshToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, ObjectId> {

    Optional<RefreshToken> findByToken(String token);

    boolean existsByToken(String token);

    List<RefreshToken> findAllByUserIdAndIsRevokedFalse(ObjectId userId);

    List<RefreshToken> findAllByUserId(ObjectId userId);

    void deleteByUserId(ObjectId userId);

    void deleteByToken(String token);
}
