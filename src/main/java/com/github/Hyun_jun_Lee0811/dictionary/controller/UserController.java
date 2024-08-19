package com.github.Hyun_jun_Lee0811.dictionary.controller;

import com.github.Hyun_jun_Lee0811.dictionary.model.UserForm;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.UserDto;
import com.github.Hyun_jun_Lee0811.dictionary.security.JwtTokenProvider;
import com.github.Hyun_jun_Lee0811.dictionary.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    return ResponseEntity.ok(userService.signUp(request));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@Valid @RequestBody UserForm.SignIn request) {
    String token = this.jwtTokenProvider.generateToken(userService.signIn(request).getUsername());
    return ResponseEntity.ok(token);
  }

  @PutMapping("/change-username")
  public ResponseEntity<?> changeUsername(@Valid @RequestBody UserForm.ChangeUsername request) {
    return ResponseEntity.ok(userService.changeUsername(request));
  }

  @PutMapping("/change-password")
  public ResponseEntity<?> changePassword(@Valid @RequestBody UserForm.ChangePassword request) {
    return ResponseEntity.ok(userService.changePassword(request));
  }

  @DeleteMapping("/accounts/{username}")
  public ResponseEntity<?> deleteAccount(@RequestBody UserForm.DeleteAccount request) {
    userService.deleteAccount(request);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }
}