package com.github.Hyun_jun_Lee0811.dictionary.controller;

import com.github.Hyun_jun_Lee0811.dictionary.model.UserForm;
import com.github.Hyun_jun_Lee0811.dictionary.security.JwtTokenProvider;
import com.github.Hyun_jun_Lee0811.dictionary.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(@Valid @RequestBody UserForm.SignUp request) {
    return ResponseEntity.ok(userService.singUp(request));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@Valid @RequestBody UserForm.SignIn request) {
    var user = userService.singIn(request);
    return ResponseEntity.ok(this.jwtTokenProvider.generateToken(user.getUsername()));
  }

  @PutMapping("/change-username")
  public ResponseEntity<?> changeUsername(@Valid @RequestBody UserForm.ChangeUsername request) {
    return ResponseEntity.ok(userService.changeUsername(request));
  }

  @PutMapping("/change-password")
  public ResponseEntity<?> changePassword(@Valid @RequestBody UserForm.ChangePassword request) {
    return ResponseEntity.ok(userService.changePassword(request));
  }

  @DeleteMapping("/delete-account")
  public void deleteAccount(@Valid @RequestBody UserForm.DeleteAccount request) {
    userService.deleteAccount(request);
  }
}
