package com.github.Hyun_jun_Lee0811.dictionary.service;

import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.ID_NOT_EXIT;
import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.PASSWORD_UN_MATCH;
import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.UNABLE_TO_GET_CURRENT_USER_ID;
import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.USER_NAME_ALREADY_EXIT;
import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.USER_NOT_EXIT;

import com.github.Hyun_jun_Lee0811.dictionary.expection.ErrorResponse;
import com.github.Hyun_jun_Lee0811.dictionary.model.UserForm;
import com.github.Hyun_jun_Lee0811.dictionary.model.UserForm.ChangePassword;
import com.github.Hyun_jun_Lee0811.dictionary.model.UserForm.ChangeUsername;
import com.github.Hyun_jun_Lee0811.dictionary.model.UserForm.DeleteAccount;
import com.github.Hyun_jun_Lee0811.dictionary.model.dto.UserDto;
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.User;
import com.github.Hyun_jun_Lee0811.dictionary.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDto getUserDtoByUsername(String username) {
    User user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new ErrorResponse(USER_NOT_EXIT));
    return new UserDto(user.getUserId(), user.getUsername(), user.getPassword());
  }

  public UserDto signUp(UserForm.SignUp request) {
    if (this.userRepository.existsByUsername(request.getUsername())) {
      throw new ErrorResponse(USER_NAME_ALREADY_EXIT);
    }

    request.setPassword(this.passwordEncoder.encode(request.getPassword()));
    User user = this.userRepository.save(request.toEntity());

    return new UserDto(user.getUserId(), user.getUsername(), user.getPassword());
  }

  public UserDto signIn(UserForm.SignIn request) {
    User user = getUserByUsername(request.getUsername());
    if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new ErrorResponse(PASSWORD_UN_MATCH);
    }
    return new UserDto(user.getUserId(), user.getUsername(), user.getPassword());
  }

  public UserDto changeUsername(ChangeUsername request) {
    //1. 현재 사용자 ID로 사용자 조회
    User user = getUserById(getCurrentUserId());

    //2. 현재 사용자 이름 확인, 비밀번호 확인, 새 사용자 이름이 존재하는지 확인
    validateOldUsername(user, request.getOldUsername());
    validatePassword(user, request.getPassword());
    checkIfUsernameExists(request.getNewUsername());

    //3. 사용자 이름 변경
    user.setUsername(request.getNewUsername());
    User updatedUser = this.userRepository.save(user);
    return new UserDto(updatedUser.getUserId(), updatedUser.getUsername(),
        updatedUser.getPassword());
  }

  public UserDto changePassword(ChangePassword request) {
    //1. 사용자 조회
    User user = getUserByUsername(request.getUsername());

    //2. 기존 비밀번호 확인
    validatePassword(user, request.getOldPassword());

    //3. 비밀번호 변경
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

    User updatedUser = this.userRepository.save(user);
    return new UserDto(updatedUser.getUserId(), updatedUser.getUsername(),
        updatedUser.getPassword());
  }

  public void deleteAccount(DeleteAccount request) {
    //1. 사용자 조회
    User user = getUserByUsername(request.getUsername());

    //2. 비밀번호 확인
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new ErrorResponse(PASSWORD_UN_MATCH);
    }

    //3. 계정 삭제
    user.setDeletedAt(LocalDateTime.now());
    this.userRepository.delete(user);
  }

  public List<UserDto> getAllUsers() {
    List<UserDto> userDtos = new ArrayList<>();

    for (User user : userRepository.findAll()) {
      UserDto userDto = new UserDto(user.getUserId(), user.getUsername(), user.getPassword());
      userDtos.add(userDto);
    }

    return userDtos;
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDto)) {
      throw new ErrorResponse(UNABLE_TO_GET_CURRENT_USER_ID);
    }

    return ((UserDto) authentication.getPrincipal()).getId();
  }

  private User getUserByUsername(String username) {
    return this.userRepository.findByUsername(username)
        .orElseThrow(() -> new ErrorResponse(USER_NOT_EXIT));
  }

  private User getUserById(Long userId) {
    return this.userRepository.findById(userId)
        .orElseThrow(() -> new ErrorResponse(ID_NOT_EXIT));
  }

  private void validateOldUsername(User currentUser, String oldUsername) {
    if (!currentUser.getUsername().equals(oldUsername)) {
      throw new ErrorResponse(USER_NOT_EXIT);
    }
  }

  private void validatePassword(User currentUser, String password) {
    if (!passwordEncoder.matches(password, currentUser.getPassword())) {
      throw new ErrorResponse(PASSWORD_UN_MATCH);
    }
  }

  private void checkIfUsernameExists(String newUsername) {
    if (this.userRepository.existsByUsername(newUsername)) {
      throw new ErrorResponse(USER_NAME_ALREADY_EXIT);
    }
  }

}