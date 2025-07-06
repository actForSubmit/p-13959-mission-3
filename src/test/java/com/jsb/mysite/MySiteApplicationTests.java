package com.jsb.mysite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jsb.mysite.answer.Answer;
import com.jsb.mysite.answer.AnswerRepository;
import com.jsb.mysite.question.Question;
import com.jsb.mysite.question.QuestionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MySiteApplicationTests {

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private AnswerRepository answerRepository;

  @Test
  @Transactional
  void testJpa() {
    Optional<Question> oq = this.questionRepository.findById(2);
    assertTrue(oq.isPresent());
    Question q = oq.get();

    List<Answer> answerList = q.getAnswerList();

    assertEquals(1, answerList.size());
    assertEquals("2번 질문 답변", answerList.get(0).getContent());
  }
}
