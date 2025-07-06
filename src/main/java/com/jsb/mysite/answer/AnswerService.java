package com.jsb.mysite.answer;

import com.jsb.mysite.DataNotFoundException;
import com.jsb.mysite.question.Question;
import com.jsb.mysite.user.SiteUser;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerService {

  private final AnswerRepository answerRepository;

  public Answer create(String content, Question question, SiteUser siteUser) {
    Answer answer = new Answer(content, question, siteUser);
    return answerRepository.save(answer);
  }

  public Answer getAnswer(int id) {
    Optional<Answer> answer = answerRepository.findById(id);

    if (answer.isPresent()) {
      return answer.get();
    } else {
      throw new DataNotFoundException("answer not found");
    }
  }

  public void modify(Answer answer, String content) {
    answer.setContent(content);
    answer.setModifyDate(LocalDateTime.now());
    answerRepository.save(answer);
  }

  public void delete(int id) {
    answerRepository.deleteById(id);
  }

  public void vote(Answer answer, SiteUser siteUser) {
    answer.getVoter().add(siteUser);
    answerRepository.save(answer);
  }
}
