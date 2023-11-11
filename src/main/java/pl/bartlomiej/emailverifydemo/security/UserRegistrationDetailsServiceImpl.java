package pl.bartlomiej.emailverifydemo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationDetailsService implements UserDetailsService {
    @Override //loading by email (natural key)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }
}
