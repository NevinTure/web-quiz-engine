package com.quiz.engine.view;


import com.quiz.engine.dto.QuizDTO;
import com.quiz.engine.models.Quiz;
import com.quiz.engine.models.QuizCompletion;
import com.quiz.engine.models.User;
import com.quiz.engine.services.QuizService;
import com.quiz.engine.services.UserService;
import com.quiz.engine.util.Feedback;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
@Validated
public class QuizController {


    private final ModelMapper mapper;
    private final QuizService quizService;
    private final UserService userService;

    @Autowired
    public QuizController(ModelMapper mapper, QuizService quizService, UserService userService) {
        this.mapper = mapper;
        this.quizService = quizService;
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<QuizDTO> addQuiz(@Valid @RequestBody QuizDTO quizDTO) {
        User user = getCurrentUser();
        Quiz quiz = mapper.map(quizDTO, Quiz.class);
        quiz.setOwner(user);
        quizService.save(quiz);
        quizDTO = mapper.map(quiz, QuizDTO.class);
        return new ResponseEntity<>(quizDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable(name = "id") long id) {
        Optional<Quiz> optionalQuiz = quizService.findById(id);
        if(optionalQuiz.isPresent()) {
            QuizDTO quizDTO = mapper.map(optionalQuiz.get(), QuizDTO.class);
            quizDTO.setAnswer(null);
            return new ResponseEntity<>(quizDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Page<QuizDTO>> getQuizzes(@RequestParam(name = "page") int page) {
        Page<Quiz> pages = quizService.findAllWithPagination(page);
        Page<QuizDTO> quizDTOs = pages.map(v -> {
            QuizDTO quizDTO = mapper.map(v, QuizDTO.class);
            quizDTO.setAnswer(null);
            return quizDTO;
        });

        return new ResponseEntity<>(quizDTOs, HttpStatus.OK);
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<Feedback> solveQuiz(@PathVariable("id") long id, @RequestBody Map<String, Set<Integer>> answerMap) {
        Optional<Quiz> optionalQuiz = quizService.findById(id);
        if(optionalQuiz.isPresent()) {
            Feedback feedback = new Feedback();
            Quiz quiz = optionalQuiz.get();
            if (Objects.equals(quiz.getAnswer(), answerMap.get("answer"))) {
                feedback.setSuccess(true);
                feedback.setMessage("Congratulations, you're right!");
                quizService.saveCompletedQuiz(getCurrentUser(), quiz);
            } else {
                feedback.setSuccess(false);
                feedback.setMessage("Wrong answer! Please, try again.");
            }
            return new ResponseEntity<>(feedback, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuiz(@PathVariable(name = "id") long id) {
        User user = getCurrentUser();
        List<Quiz> quizzes = user.getQuizzes();
        Optional<Quiz> optionalQuiz = quizService.findById(id);
        if(optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            if(quizzes.contains(quiz)) {
                quizService.delete(quiz);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<QuizDTO>> getCompletedQuizzes(@RequestParam(name = "page") int page) {
        User user = getCurrentUser();
        Page<QuizCompletion> quizzes = quizService.getCompletedQuizzesByUserWithPagination(user, page);
        Page<QuizDTO> quizDTOs = quizzes.map(v -> new QuizDTO(v.getQuizId(), LocalDateTime.now()));

        return new ResponseEntity<>(quizDTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateQuiz(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody QuizDTO quizDTO) {

        User user = getCurrentUser();
        List<Quiz> quizzes = user.getQuizzes();
        Optional<Quiz> optionalQuiz = quizService.findById(id);
        if(optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            if(quizzes.contains(quiz)) {
                quiz = mapper.map(quizDTO, Quiz.class);
                quiz.setId(id);
                quizService.update(quiz);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByEmail(userDetails.getUsername()).get();
    }
}
