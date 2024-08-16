package com.socialLogin.socialLogin.repository;

import com.socialLogin.socialLogin.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> { // 어떠한 entity? - pk가 무슨 타입?

}
