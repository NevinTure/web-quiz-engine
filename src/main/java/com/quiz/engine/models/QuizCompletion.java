package com.quiz.engine.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "quiz_completion")
public class QuizCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_completion_id")
    private long quizCompletionId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "quiz_id")
    private long quizId;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
