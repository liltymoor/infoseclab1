package lilty.infra.infoseclab1.core.service;

import lilty.infra.infoseclab1.core.entity.LabUser;
import lilty.infra.infoseclab1.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LabUser u = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User
                .withUsername(u.getUsername())
                .password(u.getPasswordHash())
                .authorities(u.getRole())
                .build();
    }

    public Boolean checkIfUserExists(String username) {
        return userRepository.existsByUsername(username.toLowerCase());
    }

    public UUID registerUser(LabUser labUser) {
        return userRepository.save(labUser).getId();
    };
}
