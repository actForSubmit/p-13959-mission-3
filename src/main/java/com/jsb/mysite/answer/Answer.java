package com.jsb.mysite.answer;

import com.jsb.mysite.question.Question;
import com.jsb.mysite.user.SiteUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(columnDefinition = "TEXT")
  @Setter
  private String content;

  private LocalDateTime createDate;

  @Setter
  private LocalDateTime modifyDate;

  @ManyToOne
  private Question question;

  @ManyToOne
  private SiteUser author;

  @ManyToMany
  private Set<SiteUser> voter;

  public Answer(String content, Question question, SiteUser author) {
    this.content = content;
    this.createDate = LocalDateTime.now();
    this.question = question;
    this.author = author;
  }
}
