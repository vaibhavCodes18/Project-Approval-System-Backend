package com.bit.ProjectApprovalSystem.repository;

import com.bit.ProjectApprovalSystem.entity.User;
import com.bit.ProjectApprovalSystem.enums.UserRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByRoleAndStatus(UserRole role, UserStatus status);

    boolean existsByEmail(String email);
}
