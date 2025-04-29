package in.gurujifoundation.controller;

import in.gurujifoundation.dto.LoginResponse;
import in.gurujifoundation.dto.LoginUserRequest;
import in.gurujifoundation.dto.CreateUserRequest;
import in.gurujifoundation.response.APIResponse;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auth")
@RestController
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest createUserRequest) {
        ResponseMessage responseMessage = userService.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.builder()
                        .status(Boolean.TRUE)
                        .messages(List.of(responseMessage))
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserRequest loginUserRequest) {
        LoginResponse loginResponse = userService.authenticateUser(loginUserRequest);
        return ResponseEntity.ok(APIResponse.builder()
                .status(Boolean.TRUE)
                .data(loginResponse).build());
    }
}
