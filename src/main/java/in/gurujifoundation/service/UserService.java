package in.gurujifoundation.service;

import in.gurujifoundation.domain.User;
import in.gurujifoundation.dto.LoginResponse;
import in.gurujifoundation.dto.LoginUserRequest;
import in.gurujifoundation.dto.CreateUserRequest;
import in.gurujifoundation.response.ResponseMessage;

public interface UserService {
    ResponseMessage createUser(CreateUserRequest input);

    User createAndReturnUser(CreateUserRequest createUserRequest);

    LoginResponse authenticateUser(LoginUserRequest input);
}
