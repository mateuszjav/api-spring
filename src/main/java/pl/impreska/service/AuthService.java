package pl.impreska.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.impreska.auth.AuthRequest;
import pl.impreska.exception.UserAlreadyExistsException;
import pl.impreska.exception.UserNotFoundException;
import pl.impreska.model.User;
import pl.impreska.repository.UserRepository;
import pl.impreska.security.JwtUtil;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(AuthRequest request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("Użytkownik o nazwie " + request.getUsername() + " juz istnieje");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public String login(AuthRequest request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Nie ma uzytkownika o takiej nazwie"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Złe hasło");
        }

        return jwtUtil.generateToken(user.getUsername());
    }


}
