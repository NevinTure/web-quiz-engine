package com.quiz.engine.models;


import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"answer", "owner"})
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "options")
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<String> options;

    @Column(name = "answer")
    @ElementCollection
    private Set<Integer> answer;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User owner;


    public void setAnswer(Set<Integer> answer) {
        this.answer = Objects.requireNonNullElseGet(answer, HashSet::new);
    }
}
