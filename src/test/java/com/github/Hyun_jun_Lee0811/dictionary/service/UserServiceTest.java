package com.github.Hyun_jun_Lee0811.dictionary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.github.Hyun_jun_Lee0811.dictionary.expection.ErrorResponse;
import com.github.Hyun_jun_Lee0811.dictionary.model.UserForm;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.UserDto;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.User;
import com.github.Hyun_jun_Lee0811.dictionary.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private Authentication authentication;

  private AutoCloseable closeable;

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @Test
  @DisplayName("회원 가입 성공")
  void SignUp_Success() {
    UserForm.SignUp request = new UserForm.SignUp();
    request.setUsername("grace");
    request.setPassword("password123");

    User user = new User();
    user.setUsername("grace");
    user.setPassword("encodedPassword");

    when(userRepository.existsByUsername(anyString())).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserDto result = userService.signUp(request);

    assertNotNull(result);
    assertEquals("grace", result.getUsername());
    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("회원 가입 실패 - 사용자 이미 존재")
  void SignUp_Fail() {
    UserForm.SignUp request = new UserForm.SignUp();
    request.setUsername("grace");
    request.setPassword("password123");

    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    ErrorResponse exception = assertThrows(ErrorResponse.class, () -> userService.signUp(request));
    assertEquals("존재하는 사용자 이름입니다.", exception.getMessage());
  }

  @Test
  @DisplayName("로그인 성공")
  void SignIn_Success() {
    UserForm.SignIn request = new UserForm.SignIn();
    request.setUsername("grace");
    request.setPassword("password123");

    User user = new User();
    user.setUsername("grace");
    user.setPassword("encodedPassword");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    UserDto result = userService.signIn(request);

    assertNotNull(result);
    assertEquals("grace", result.getUsername());
  }

  @Test
  @DisplayName("로그인 실패 - 잘못된 비밀번호")
  void SignIn_Fail() {
    UserForm.SignIn request = new UserForm.SignIn();
    request.setUsername("grace");
    request.setPassword("wrongPassword");

    User user = new User();
    user.setUsername("grace");
    user.setPassword("encodedPassword");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    ErrorResponse exception = assertThrows(ErrorResponse.class, () -> userService.signIn(request));
    assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("사용자 이름 변경 성공")
  void ChangeUsername_Success() {
    UserForm.ChangeUsername request = new UserForm.ChangeUsername();
    request.setOldUsername("oldUsername");
    request.setNewUsername("newUsername");
    request.setPassword("password");

    User currentUser = new User();
    currentUser.setUsername("oldUsername");
    currentUser.setPassword("encodedPassword");
    currentUser.setUserId(1L);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(currentUser));
    when(userRepository.existsByUsername(anyString())).thenReturn(false);
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(userRepository.save(any(User.class))).thenReturn(currentUser);

    // Mock current user
    when(authentication.getPrincipal()).thenReturn(currentUser);

    UserDto result = userService.changeUsername(request);

    assertNotNull(result);
    assertEquals("newUsername", result.getUsername());
    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("사용자 이름 변경 실패: 비밀번호 불일치")
  void ChangeUsername_Fail_InvalidPassword() {
    UserForm.ChangeUsername request = new UserForm.ChangeUsername();
    request.setOldUsername("oldUsername");
    request.setNewUsername("newUsername");
    request.setPassword("wrongPassword");

    User currentUser = new User();
    currentUser.setUsername("oldUsername");
    currentUser.setPassword("encodedPassword");
    currentUser.setUserId(1L);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(currentUser));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    // Mock current user
    when(authentication.getPrincipal()).thenReturn(currentUser);

    ErrorResponse exception = assertThrows(ErrorResponse.class,
        () -> userService.changeUsername(request));
    assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("비밀번호 변경 성공")
  void ChangePassword_Success() {
    UserForm.ChangePassword request = new UserForm.ChangePassword();
    request.setUsername("grace");
    request.setOldPassword("oldPassword");
    request.setNewPassword("newPassword");

    User user = new User();
    user.setUsername("grace");
    user.setPassword("OldPassword");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(passwordEncoder.encode(anyString())).thenReturn("OldPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserDto result = userService.changePassword(request);

    assertNotNull(result);
    assertEquals("OldPassword", result.getPassword());
    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("비밀번호 변경 실패: 기존 비밀번호 불일치")
  void ChangePassword_Fail_InvalidOldPassword() {
    UserForm.ChangePassword request = new UserForm.ChangePassword();
    request.setUsername("grace");
    request.setOldPassword("wrongOldPassword");
    request.setNewPassword("newPassword");

    User user = new User();
    user.setUsername("grace");
    user.setPassword("encodedOldPassword");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    ErrorResponse exception = assertThrows(ErrorResponse.class,
        () -> userService.changePassword(request));
    assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("계정 삭제 성공")
  void DeleteAccount_Success() {
    UserForm.DeleteAccount request = new UserForm.DeleteAccount();
    request.setUsername("grace");
    request.setPassword("password123");

    User user = new User();
    user.setUsername("grace");
    user.setPassword("encodedPassword");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    userService.deleteAccount(request);

    verify(userRepository).delete(user);
  }

  @Test
  @DisplayName("계정 삭제 실패 - 잘못된 비밀번호")
  void DeleteAccount_Fail() {
    UserForm.DeleteAccount request = new UserForm.DeleteAccount();
    request.setUsername("grace");
    request.setPassword("wrongPassword");

    User user = new User();
    user.setUsername("grace");
    user.setPassword("encodedPassword");

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    ErrorResponse exception = assertThrows(ErrorResponse.class,
        () -> userService.deleteAccount(request));
    assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
  }

  @AfterEach
  void tearDown() throws Exception {
    closeable.close();
  }
}