package com.jsb.mysite.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Integer> {

  Optional<SiteUser> findByUsername(String username);
}
