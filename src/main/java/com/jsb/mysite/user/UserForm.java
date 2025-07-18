package com.jsb.mysite.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

  @Size(min = 3, max = 25)
  @NotEmpty(message = "사용자ID는 필수항목입니다.")
  private String username;

  @NotEmpty(message = "비밀번호는 필수항목입니다.")
  private String inputPassword;

  @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
  private String confirmPassword;

  @NotEmpty(message = "이메일은 필수항목입니다.")
  @Email
  private String email;
}
