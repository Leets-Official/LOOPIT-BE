package com.example.loopitbe.repository;

import com.example.loopitbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(String kakaoId);
    boolean existsUserByKakaoId(String kakaoId);
    boolean existsUserByNickname(String nickname);
    boolean existsUserByEmail(String email);
}
