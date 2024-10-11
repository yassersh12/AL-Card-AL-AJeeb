package com.example.demo.userAttack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttackRepository extends JpaRepository<UserAttack, Long> {
}
