package com.security.EduNext.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exam implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int idExam;
    private String examTitle;
    private String examDescription;
    private int examDuration;
    private int totalMarks;
    private int passingScore;
    private LocalDate scheduledDate;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Question> questions = new ArrayList<>();
   // @ManyToMany(cascade = CascadeType.ALL)
    //@JsonIgnore
    //@JoinTable(name = "exam_users")
    //private List<User> users;


  /* @OneToMany(mappedBy = "exam")
    private List<Certificate> certificates;*/
}

