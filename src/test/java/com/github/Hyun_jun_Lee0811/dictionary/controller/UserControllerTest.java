package com.github.Hyun_jun_Lee0811.dictionary.controller;

import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.PASSWORD_UN_MATCH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Hyun_jun_Lee0811.dictionary.expection.ErrorResponse;
import com.github.Hyun_jun_Lee0811.dictionary.model.UserForm;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.User;
import com.github.Hyun_jun_Lee0811.dictionary.security.JwtTokenProvider;
import com.github.Hyun_jun_Lee0811.dictionary.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
  void signUpSuccess() throws Exception {
    UserForm.SignUp signUpForm = new UserForm.SignUp();
    signUpForm.setUsername("grace");
    signUpForm.setPassword("password");

    User user = new User();
    user.setUsername("grace");

    when(userService.singUp(any(UserForm.SignUp.class))).thenReturn(user);

    mockMvc.perform(post("/user/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpForm)))
        .andExpect(status().isOk());
  }

  @Test
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
  void signInSuccess() throws Exception {
    UserForm.SignIn signInForm = new UserForm.SignIn();
    signInForm.setUsername("grace");
    signInForm.setPassword("password");

    User user = new User();
    user.setUsername("grace");

    when(userService.singIn(any(UserForm.SignIn.class))).thenReturn(user);
    when(jwtTokenProvider.generateToken(any(String.class))).thenReturn("token");

    mockMvc.perform(post("/user/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signInForm)))
        .andExpect(status().isOk());
  }

  @Test
  void signInFail() throws Exception {
    UserForm.SignIn signInForm = new UserForm.SignIn();
    signInForm.setUsername("grace");
    signInForm.setPassword("wrongPassword");

    when(userService.singIn(any(UserForm.SignIn.class))).thenThrow(
        new ErrorResponse(PASSWORD_UN_MATCH));

    mockMvc.perform(post("/user/sign-in")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signInForm)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void changeUsernameSuccess() throws Exception {
    UserForm.ChangeUsername changeUsernameForm = new UserForm.ChangeUsername();
    changeUsernameForm.setOldUsername("grace");
    changeUsernameForm.setPassword("password");
    changeUsernameForm.setNewUsername("john");

    User user = new User();
    user.setUsername("john");

    when(userService.changeUsername(any(UserForm.ChangeUsername.class))).thenReturn(user);

    mockMvc.perform(put("/user/change-username")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(changeUsernameForm)))
        .andExpect(status().isOk());
  }

  @Test
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
  void changePasswordSuccess() throws Exception {
    UserForm.ChangePassword changePasswordForm = new UserForm.ChangePassword();
    changePasswordForm.setUsername("grace");
    changePasswordForm.setOldPassword("password");
    changePasswordForm.setNewPassword("newPassword");

    User user = new User();
    user.setUsername("grace");

    when(userService.changePassword(any(UserForm.ChangePassword.class))).thenReturn(user);

    mockMvc.perform(put("/user/change-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(changePasswordForm)))
        .andExpect(status().isOk());
  }

  @Test
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
  void deleteAccountSuccess() throws Exception {
    UserForm.DeleteAccount deleteAccountForm = new UserForm.DeleteAccount();
    deleteAccountForm.setUsername("grace");
    deleteAccountForm.setPassword("password");

    mockMvc.perform(delete("/user/delete-account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deleteAccountForm)))
        .andExpect(status().isOk());
  }

  @Test
  void deleteAccountFail() throws Exception {
    UserForm.DeleteAccount deleteAccountForm = new UserForm.DeleteAccount();
    deleteAccountForm.setUsername("grace");
    deleteAccountForm.setPassword("wrongPassword");

    doThrow(new ErrorResponse(PASSWORD_UN_MATCH)).when(userService)
        .deleteAccount(any(UserForm.DeleteAccount.class));

    mockMvc.perform(delete("/user/delete-account")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deleteAccountForm)))
        .andExpect(status().isUnauthorized());
  }
}