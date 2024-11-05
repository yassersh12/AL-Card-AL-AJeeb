package com.cotede.interns.task.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);

    @Query("SELECT new com.cotede.interns.task.user.dto.UserScoreDto(0, u.username, u.score) " +
            "FROM User u ORDER BY u.score DESC")
    List<UserScoreDto> findTop10ByScore();

    @Query("SELECT new com.cotede.interns.task.user.dto.UserCreativityDto(0, u.username, u.AvgCreativity) " +
            "FROM User u ORDER BY u.AvgCreativity DESC")
    List<UserCreativityDto> findTop10ByAvgCreativity();
}
