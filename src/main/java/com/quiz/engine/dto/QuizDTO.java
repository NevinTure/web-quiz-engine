package com.quiz.engine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizDTO {


    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @NotNull
    @Size(min = 2)
    private List<String> options;

    private Set<Integer> answer;

    private LocalDateTime completedAt;

    public QuizDTO(Long id, LocalDateTime completedAt) {
        this.id = id;
        this.completedAt = completedAt;
    }
}
