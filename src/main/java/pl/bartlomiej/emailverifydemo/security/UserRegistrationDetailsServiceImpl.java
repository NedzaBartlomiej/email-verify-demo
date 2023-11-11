package pl.bartlomiej.emailverifydemo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.bartlomiej.emailverifydemo.user.UserService;

@Service
@RequiredArgsConstructor
public class UserRegistrationDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    @Override //loading by email (natural key)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userService.findByEmail(email)
                .map(UserRegistrationDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: "+email+" not found."));
    }
}
