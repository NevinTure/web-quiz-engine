package com.quiz.engine.repositories;

import com.quiz.engine.models.QuizCompletion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface QuizCompletionRepository extends JpaRepository<QuizCompletion, Long> {

    Page<QuizCompletion> getByUserId(long id, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "insert into quiz_completion(user_id, quiz_id, completed_at) values(?1, ?2, ?3)", nativeQuery = true)
    void saveByIds(long userId, long quizId, LocalDateTime completedAt);
}
