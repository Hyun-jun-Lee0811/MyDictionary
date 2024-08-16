package com.github.Hyun_jun_Lee0811.dictionary.controller;

import com.github.Hyun_jun_Lee0811.dictionary.model.UserThinkForm;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.UserThinkDTO;
import com.github.Hyun_jun_Lee0811.dictionary.security.JwtAuthenticationFilter;
import com.github.Hyun_jun_Lee0811.dictionary.service.UserThinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-thinks")
@RequiredArgsConstructor
public class UserThinkController {

  private final UserThinkService userThinkService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @PostMapping
  public ResponseEntity<Void> saveUserThink(@Valid @RequestBody UserThinkForm userThinkForm) {

    userThinkService.saveUserThink(userThinkForm);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/my-thoughts/{username}")
  public ResponseEntity<Page<UserThinkDTO>> getUserThoughts(@Valid
  @PathVariable("username") String username,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {

    if (!jwtAuthenticationFilter.isUserAuthenticated(username)) {
      return ResponseEntity.status(403).build();
    }

    return ResponseEntity.ok(
        userThinkService.getUserThoughts(username, PageRequest.of(page, size)));
  }

  @GetMapping("/public/{username}")
  public ResponseEntity<List<UserThinkDTO>> getPublicThoughts(
      @Valid @PathVariable("username") String username) {

    return ResponseEntity.ok(userThinkService.getPublicThoughts(username));
  }

  @GetMapping("/word/{username}/{wordId}")
  public ResponseEntity<List<UserThinkDTO>> getUserThinksByWord(
      @Valid @PathVariable("username") String username, @PathVariable("wordId") String wordId) {

    return ResponseEntity.ok(userThinkService.getUserThinksByWord(username, wordId));
  }

  @PutMapping("/change-think/{id}")
  public ResponseEntity<Void> changeUserThink(
      @PathVariable("id") Long id,
      @RequestBody UserThinkForm userThinkForm) {

    userThinkService.changeUserThink(id, userThinkForm);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete/{username}/{id}")
  public ResponseEntity<Void> deleteUserThink(@Valid @PathVariable("username") String username,
      @PathVariable("id") Long id) {

    userThinkService.deleteUserThink(username, id);
    return ResponseEntity.noContent().build();
  }
}