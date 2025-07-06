package com.jsb.mysite.user;

import com.jsb.mysite.DataNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public SiteUser create(String username, String password, String email) {
    SiteUser siteUser = new SiteUser(username, passwordEncoder.encode(password), email);
    userRepository.save(siteUser);

    return siteUser;
  }

  public SiteUser getUser(String username) {
    Optional<SiteUser> siteUser = userRepository.findByUsername(username);
    if (siteUser.isPresent()) {
      return siteUser.get();
    } else {
      throw new DataNotFoundException("site user not found");
    }
  }
}
