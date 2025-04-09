package in.gurujifoundation.service.impl;

import in.gurujifoundation.constants.ErrorCodeConstant;
import in.gurujifoundation.domain.User;
import in.gurujifoundation.dto.LoginResponse;
import in.gurujifoundation.dto.LoginUserRequest;
import in.gurujifoundation.dto.CreateUserRequest;
import in.gurujifoundation.exception.AuthenticationException;
import in.gurujifoundation.exception.DuplicateEmailException;
import in.gurujifoundation.exception.UserNotFoundException;
import in.gurujifoundation.repository.UserRepository;
import in.gurujifoundation.response.ResponseMessage;
import in.gurujifoundation.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseMessage createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + createUserRequest.getEmail());
        }
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .username(createUserRequest.getEmail())
                .role(createUserRequest.getUserRole())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .build();

        userRepository.save(user);
        return ResponseMessage.builder().message(ErrorCodeConstant.USER_CREATED_SUCCESSFULLY).build();

    }

    @Override
    public User createAndReturnUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + createUserRequest.getEmail());
        }
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .username(createUserRequest.getEmail())
                .role(createUserRequest.getUserRole())
                .isActive(Boolean.TRUE)
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .build();

        return userRepository.save(user);
    }

    @Override
    public LoginResponse authenticateUser(LoginUserRequest input) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
            Optional<User> userOpt = userRepository.findByEmail(input.getEmail());
            if (userOpt.isEmpty()) {
                throw new UserNotFoundException("User not found for given email : " + input.getEmail());
            }
            User user = userOpt.get();
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", user.getRole());
            String jwtToken = jwtService.generateToken(extraClaims, user);

            return LoginResponse.builder()
                    .email(user.getEmail())
                    .name(user.getUsername())
                    .token(jwtToken)
                    .expiresIn(jwtService.getExpirationTime())
                    .role(user.getRole())
                    .build();
        } catch (BadCredentialsException ex) {
            throw new AuthenticationException("Invalid username or password");
        } catch (Exception ex) {
            throw new AuthenticationException("Authentication failed: " + ex.getMessage());
        }
    }
}
