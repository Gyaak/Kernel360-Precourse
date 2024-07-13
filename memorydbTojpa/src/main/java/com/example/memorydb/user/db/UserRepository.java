package com.example.memorydb.user.db;

import com.example.memorydb.user.model.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.PanelUI;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public List<UserEntity> findAllByScoreGreaterThanEqual(int score);

    public List<UserEntity> findAllByScoreGreaterThanEqualAndScoreLessThanEqual(int min, int max);

    @Query(
            value = "select * from user as u where u.score >= :min AND u.score <= :max",
            nativeQuery = true
    )
    List<UserEntity> score(
            @Param(value = "min") int min,
            @Param(value = "max") int max
    );
}