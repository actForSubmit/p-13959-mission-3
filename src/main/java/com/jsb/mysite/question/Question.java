package com.jsb.mysite.question;

import com.jsb.mysite.answer.Answer;
import com.jsb.mysite.user.SiteUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(length = 200)
  @Setter
  private String subject;

  @Column(columnDefinition = "TEXT")
  @Setter
  private String content;

  private LocalDateTime createDate;

  @Setter
  private LocalDateTime modifyDate;

  @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
  private List<Answer> answerList;

  @ManyToOne
  private SiteUser author;

  @ManyToMany
  private Set<SiteUser> voter;

  public Question(String subject, String content, SiteUser siteUser) {
    this.subject = subject;
    this.content = content;
    this.createDate = LocalDateTime.now();
    this.author = siteUser;
  }
}
