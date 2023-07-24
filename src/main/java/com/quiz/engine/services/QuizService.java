package com.quiz.engine.services;

import com.quiz.engine.models.Quiz;
import com.quiz.engine.models.QuizCompletion;
import com.quiz.engine.models.User;
import com.quiz.engine.repositories.QuizCompletionRepository;
import com.quiz.engine.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizCompletionRepository quizCompletionRepository;



    @Autowired
    public QuizService(QuizRepository quizRepository, QuizCompletionRepository quizCompletionRepository) {
        this.quizRepository = quizRepository;
        this.quizCompletionRepository = quizCompletionRepository;
    }

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    public void save(Quiz quiz) {
        quizRepository.saveAndFlush(quiz);
    }

    public Optional<Quiz> findById(long id) {
        return quizRepository.findById(id);
    }

    public void delete(Quiz quiz) {
        quizRepository.delete(quiz);
    }

    public void update(Quiz quiz) {
        quizRepository.save(quiz);
    }

    public Page<Quiz> findAllWithPagination(int page) {
        return quizRepository.findAll(PageRequest.of(page, 10));
    }

    public void saveCompletedQuiz(User user, Quiz quiz) {
        quizCompletionRepository.saveByIds(user.getId(), quiz.getId(), LocalDateTime.now());
    }

    public Page<QuizCompletion> getCompletedQuizzesByUserWithPagination(User user, int page) {
        return quizCompletionRepository.getByUserId(
                user.getId(),
                PageRequest.of(
                        page,
                        10,
                        Sort.by("completedAt").descending()));
    }
}
