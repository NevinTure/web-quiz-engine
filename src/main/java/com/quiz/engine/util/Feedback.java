package com.quiz.engine.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Feedback {

    @JsonProperty("success")
    private boolean isSuccess;

    @JsonProperty("feedback")
    private String message;
}
