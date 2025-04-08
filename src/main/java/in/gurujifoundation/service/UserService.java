package in.gurujifoundation.service;

import in.gurujifoundation.dto.LoginResponse;
import in.gurujifoundation.dto.LoginUserRequest;
import in.gurujifoundation.dto.RegisterUserRequest;
import in.gurujifoundation.response.ResponseMessage;

public interface UserService {
    ResponseMessage createUser(RegisterUserRequest input);
    LoginResponse authenticateUser(LoginUserRequest input);
}
