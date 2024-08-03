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
import com.github.Hyun_jun_Lee0811.dictionary.model.entity.User;
import com.github.Hyun_jun_Lee0811.dictionary.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).orElseThrow(
        () -> new ErrorResponse(USER_NOT_EXIT, username)
    );
  }

  public User singUp(UserForm.SignUp request) {
    if (this.userRepository.existsByUsername(request.getUsername())) {
      throw new ErrorResponse(USER_NAME_ALREADY_EXIT);
    }
    request.setPassword(this.passwordEncoder.encode(request.getPassword()));
    return this.userRepository.save(request.toEntity());
  }

  public User singIn(UserForm.SignIn request) {
    var getUser = getUserByUsername(request.getUsername());

    if (!this.passwordEncoder.matches(request.getPassword(), getUser.getPassword())) {
      throw new ErrorResponse(PASSWORD_UN_MATCH);
    }

    return getUser;
  }

  public User changeUsername(ChangeUsername request) {
    //1. 현재 사용자 ID로 사용자 조회
    User user = getUserById(getCurrentUserId());

    //2. 현재 사용자 이름 확인, 비밀번호 확인, 새 사용자 이름이 존재하는지 확인
    validateOldUsername(user, request.getOldUsername());
    validatePassword(user, request.getPassword());
    checkIfUsernameExists(request.getNewUsername());

    //3. 사용자 이름 변경
    user.setUsername(request.getNewUsername());
    return this.userRepository.save(user);
  }

  public User changePassword(ChangePassword request) {
    //1. 사용자 조회
    User user = getUserByUsername(request.getUsername());

    //2. 기존 비밀번호 확인
    validatePassword(user, request.getOldPassword());

    //3. 비밀번호 변경
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    return this.userRepository.save(user);
  }

  public void deleteAccount(DeleteAccount request) {
    //1. 사용자 조회
    User user = getUserByUsername(request.getUsername());

    //2. 비밀번호 확인
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new ErrorResponse(PASSWORD_UN_MATCH);
    }

    //3. 계정 삭제
    this.userRepository.delete(user);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
      throw new ErrorResponse(UNABLE_TO_GET_CURRENT_USER_ID);
    }

    return ((User) authentication.getPrincipal()).getUserId();
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
