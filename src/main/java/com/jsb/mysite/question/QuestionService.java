package com.jsb.mysite.question;

import com.jsb.mysite.DataNotFoundException;
import com.jsb.mysite.user.SiteUser;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionService {

  private final QuestionRepository questionRepository;

  public Question getQuestion(int id) {
    Optional<Question> question = questionRepository.findById(id);
    if (question.isPresent()) {
      return question.get();
    } else {
      throw new DataNotFoundException("question not found");
    }
  }

  public void create(String subject, String content, SiteUser siteUser) {
    questionRepository.save(new Question(subject, content, siteUser));
  }

  public Page<Question> getList(int page, String kw) {
    return questionRepository.findAllByKeyword(kw,
        PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createDate")));
  }

  public void modify(String subject, String content, Question question) {
    question.setSubject(subject);
    question.setContent(content);
    question.setModifyDate(LocalDateTime.now());
    questionRepository.save(question);
  }

  public void delete(int id) {
    questionRepository.deleteById(id);
  }

  public void vote(Question question, SiteUser siteUser) {
    question.getVoter().add(siteUser);
    questionRepository.save(question);
  }
}
