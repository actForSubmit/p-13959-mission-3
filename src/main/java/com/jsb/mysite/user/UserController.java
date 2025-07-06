package com.jsb.mysite.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/signup")
  public String signup(UserForm userForm) {
    return "signup_form";
  }

  @PostMapping("/signup")
  public String signup(@Valid UserForm userForm, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "signup_form";
    }

    if (!userForm.getInputPassword().equals(userForm.getConfirmPassword())) {
      bindingResult.reject("confirmPassword", "2개의 패스워드가 일치하지 않습니다.");
      return "signup_form";
    }

    try {
      userService.create(userForm.getUsername(), userForm.getInputPassword(), userForm.getEmail());
    } catch (DataIntegrityViolationException e) {
      e.printStackTrace();
      bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
      return "signup_form";
    }

    return "redirect:/";
  }

  @GetMapping("/login")
  public String login() {
    return "login_form";
  }
}
