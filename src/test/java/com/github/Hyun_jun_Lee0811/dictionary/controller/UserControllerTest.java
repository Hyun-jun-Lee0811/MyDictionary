package com.github.Hyun_jun_Lee0811.dictionary.controller;

import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.PASSWORD_UN_MATCH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Hyun_jun_Lee0811.dictionary.expection.ErrorResponse;
import com.github.Hyun_jun_Lee0811.dictionary.model.UserForm;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.UserDto;
import com.github.Hyun_jun_Lee0811.dictionary.security.JwtTokenProvider;
import com.github.Hyun_jun_Lee0811.dictionary.service.UserService;
import com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class UserControllerTest {

  @Mock
  private UserService userService;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    this.objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("회원 가입 성공")
  void signUpSuccess() throws Exception {
    UserForm.SignUp signUpForm = new UserForm.SignUp();
    signUpForm.setUsername("grace");
    signUpForm.setPassword("password");

    UserDto user = new UserDto();
    user.setUsername("grace");

    when(userService.signUp(any(UserForm.SignUp.class))).thenReturn(user);

    mockMvc.perform(post("/user/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpForm)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("회원 가입 실패")
  void signUpFail() throws Exception {
    UserForm.SignUp signUpForm = new UserForm.SignUp();
    signUpForm.setUsername("");
    signUpForm.setPassword("password");

    mockMvc.perform(post("/user/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpForm)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("로그인 성공")
  void signInSuccess() throws Exception {
    UserForm.SignIn signInForm = new UserForm.SignIn();
    signInForm.setUsername("grace");
    signInForm.setPassword("password");

    UserDto user = new UserDto();
    user.setUsername("grace");

    when(userService.signIn(any(UserForm.SignIn.class))).thenReturn(user);
    when(jwtTokenProvider.generateToken(any(String.class))).thenReturn("token");

    mockMvc.perform(post("/user/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signInForm)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("로그인 실패")
  void signInFail() throws Exception {
    UserForm.SignIn signInForm = new UserForm.SignIn();
    signInForm.setUsername("grace");
    signInForm.setPassword("wrongPassword");

    when(userService.signIn(any(UserForm.SignIn.class))).thenThrow(
        new ErrorResponse(PASSWORD_UN_MATCH));

    mockMvc.perform(post("/user/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signInForm)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("사용자 이름 변경 성공")
  void changeUsernameSuccess() throws Exception {
    UserForm.ChangeUsername changeUsernameForm = new UserForm.ChangeUsername();
    changeUsernameForm.setOldUsername("grace");
    changeUsernameForm.setPassword("password");
    changeUsernameForm.setNewUsername("john");

    UserDto user = new UserDto();
    user.setUsername("john");

    when(userService.changeUsername(any(UserForm.ChangeUsername.class))).thenReturn(user);

    mockMvc.perform(put("/user/change-username")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(changeUsernameForm)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("사용자 이름 변경 실패")
  void changeUsernameFail() throws Exception {
    UserForm.ChangeUsername changeUsernameForm = new UserForm.ChangeUsername();
    changeUsernameForm.setOldUsername("grace");
    changeUsernameForm.setPassword("wrongPassword");
    changeUsernameForm.setNewUsername("john");

    doThrow(new ErrorResponse(PASSWORD_UN_MATCH)).when(userService)
        .changeUsername(any(UserForm.ChangeUsername.class));

    mockMvc.perform(put("/user/change-username")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(changeUsernameForm)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("비밀번호 변경 성공")
  void changePasswordSuccess() throws Exception {
    UserForm.ChangePassword changePasswordForm = new UserForm.ChangePassword();
    changePasswordForm.setUsername("grace");
    changePasswordForm.setOldPassword("password");
    changePasswordForm.setNewPassword("newPassword");

    UserDto user = new UserDto();
    user.setUsername("grace");

    when(userService.changePassword(any(UserForm.ChangePassword.class))).thenReturn(user);

    mockMvc.perform(put("/user/change-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(changePasswordForm)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("비밀번호 변경 실패")
  void changePasswordFail() throws Exception {
    UserForm.ChangePassword changePasswordForm = new UserForm.ChangePassword();
    changePasswordForm.setUsername("grace");
    changePasswordForm.setOldPassword("wrongPassword");
    changePasswordForm.setNewPassword("newPassword");

    doThrow(new ErrorResponse(PASSWORD_UN_MATCH)).when(userService)
        .changePassword(any(UserForm.ChangePassword.class));

    mockMvc.perform(put("/user/change-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(changePasswordForm)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("계정 삭제 성공")
  void deleteAccountSuccess() throws Exception {
    UserForm.DeleteAccount deleteAccountForm = new UserForm.DeleteAccount();
    deleteAccountForm.setUsername("grace");
    deleteAccountForm.setPassword("password");

    mockMvc.perform(delete("/user/accounts/grace")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deleteAccountForm)))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("계정 삭제 실패")
  void deleteAccountFail() throws Exception {
    UserForm.DeleteAccount deleteAccountForm = new UserForm.DeleteAccount();
    deleteAccountForm.setUsername("grace");
    deleteAccountForm.setPassword("wrongPassword");

    doThrow(new ErrorResponse(PASSWORD_UN_MATCH)).when(userService)
        .deleteAccount(any(UserForm.DeleteAccount.class));

    mockMvc.perform(delete("/user/accounts/grace")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deleteAccountForm)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("사용자 전체 조회 성공")
  void getAllUsersSuccess() throws Exception {
    UserDto user1 = new UserDto();
    user1.setUsername("grace");

    UserDto user2 = new UserDto();
    user2.setUsername("john");

    when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

    mockMvc.perform(get("/user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username").value("grace"))
        .andExpect(jsonPath("$[1].username").value("john"));
  }

  @Test
  @DisplayName("사용자 전체 조회 실패")
  void getAllUsersFail() throws Exception {
    doThrow(new ErrorResponse(ErrorCode.SERVICE_EXCEPTION))
        .when(userService).getAllUsers();

    mockMvc.perform(get("/user"))
        .andExpect(status().isInternalServerError());
  }
}