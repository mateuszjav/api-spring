package pl.impreska.service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import pl.impreska.exception.UserNotFoundException;
import pl.impreska.repository.UserRepository;

import java.util.Collections;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        pl.impreska.model.User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika " + username));
        return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
