package com.jsb.mysite.answer;

import com.jsb.mysite.question.Question;
import com.jsb.mysite.question.QuestionService;
import com.jsb.mysite.user.SiteUser;
import com.jsb.mysite.user.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

  private final QuestionService questionService;
  private final AnswerService answerService;
  private final UserService userService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create/{id}")
  public String createAnswer(
      @PathVariable("id") int id,
      @Valid AnswerForm answerForm,
      BindingResult bindingResult,
      Model model,
      Principal principal
  ) {
    Question question = questionService.getQuestion(id);
    SiteUser siteUser = userService.getUser(principal.getName());

    if (bindingResult.hasErrors()) {
      model.addAttribute("question", question);
      return "question_detail";
    }

    Answer answer = answerService.create(answerForm.getContent(), question, siteUser);

    return "redirect:/question/detail/%s#answer_%s".formatted(id, answer.getId());
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/modify/{id}")
  public String answerModify(
      @PathVariable("id") int id,
      AnswerForm answerForm,
      Principal principal
  ) {
    Answer answer = answerService.getAnswer(id);

    if (!answer.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 수정 권한이 없습니다.");
    }

    answerForm.setContent(answer.getContent());
    return "answer_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/modify/{id}")
  public String answerModify(
      @PathVariable("id") int id,
      @Valid AnswerForm answerForm,
      BindingResult bindingResult,
      Principal principal
  ) {
    if (bindingResult.hasErrors()) {
      return "answer_form";
    }

    Answer answer = answerService.getAnswer(id);

    if (!answer.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 수정 권한이 없습니다.");
    }

    answerService.modify(answer, answerForm.getContent());
    return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(),
        id);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/delete/{id}")
  public String answerDelete(@PathVariable("id") Integer id, Principal principal) {
    Answer answer = answerService.getAnswer(id);

    if (!answer.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 삭제 권한이 없습니다.");
    }

    answerService.delete(id);

    return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/vote/{id}")
  public String answerVote(Principal principal, @PathVariable("id") int id) {
    Answer answer = answerService.getAnswer(id);
    answerService.vote(answer, userService.getUser(principal.getName()));
    return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(),
        id);
  }
}
