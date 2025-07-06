package com.jsb.mysite.question;

import com.jsb.mysite.answer.AnswerForm;
import com.jsb.mysite.user.SiteUser;
import com.jsb.mysite.user.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

  private final QuestionService questionService;
  private final UserService userService;

  @GetMapping("/list")
  public String list(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "kw", defaultValue = "") String kw,
      Model model) {
    Page<Question> questionPage = questionService.getList(page, kw);
    model.addAttribute("questionPage", questionPage);
    model.addAttribute("kw", kw);
    return "question_list";
  }

  @GetMapping("/detail/{id}")
  public String detail(@PathVariable("id") int id, Model model, AnswerForm answerForm) {
    Question question = questionService.getQuestion(id);
    model.addAttribute("question", question);
    return "question_detail";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/create")
  public String questionCreate(QuestionForm questionForm) {
    return "question_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create")
  public String questionCreate(
      @Valid QuestionForm questionForm,
      BindingResult bindingResult,
      Principal principal
  ) {
    if (bindingResult.hasErrors()) {
      return "question_form";
    }

    SiteUser siteUser = userService.getUser(principal.getName());
    questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
    return "redirect:/question/list";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/modify/{id}")
  public String questionModify(
      @PathVariable("id") int id,
      QuestionForm questionForm,
      Principal principal
  ) {
    Question question = questionService.getQuestion(id);
    if (!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }

    questionForm.setSubject(question.getSubject());
    questionForm.setContent(question.getContent());
    return "question_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/modify/{id}")
  public String questionModify(
      @Valid QuestionForm questionForm,
      @PathVariable("id") Integer id,
      BindingResult bindingResult,
      Principal principal
  ) {
    if (bindingResult.hasErrors()) {
      return "question_form";
    }

    Question question = this.questionService.getQuestion(id);

    if (!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }

    this.questionService.modify(questionForm.getSubject(), questionForm.getContent(), question);
    return String.format("redirect:/question/detail/%s", id);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("delete/{id}")
  public String questionDelete(@PathVariable("id") int id, Principal principal) {
    if (!questionService.getQuestion(id).getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
    }

    questionService.delete(id);
    return "redirect:/";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/vote/{id}")
  public String questionVote(@PathVariable("id") Integer id, Principal principal) {
    questionService.vote(questionService.getQuestion(id), userService.getUser(principal.getName()));
    return String.format("redirect:/question/detail/%s", id);
  }
}
