package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.User;
import in.gurujifoundation.dto.LoginResponse;
import in.gurujifoundation.dto.LoginUserRequest;
import in.gurujifoundation.dto.RegisterUserRequest;
import in.gurujifoundation.exception.UserNotFoundException;
import in.gurujifoundation.repository.UserRepository;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseMessage createUser(RegisterUserRequest input) {
        User user = User.builder()
                .email(input.getEmail())
                .username(input.getEmail())
                .role(input.getUserRole())
                .password(passwordEncoder.encode(input.getPassword()))
                .build();

        userRepository.save(user);
        return ResponseMessage.builder().message(ErrorCodeConstant.USER_CREATED_SUCCESSFULLY).build();

    }

    @Override
    public LoginResponse authenticateUser(LoginUserRequest input) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
        Optional<User> userOpt = userRepository.findByEmail(input.getEmail());
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found for given email : "+ input.getEmail());
        }
        User user = userOpt.get();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        String jwtToken = jwtService.generateToken(extraClaims, user);

        return LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .role(user.getRole())
                .build();
    }
}
