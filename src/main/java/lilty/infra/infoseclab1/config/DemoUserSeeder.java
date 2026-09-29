package lilty.infra.infoseclab1.config;

import lilty.infra.infoseclab1.core.entity.LabUser;
import lilty.infra.infoseclab1.core.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DemoUserSeeder implements CommandLineRunner {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final String demoUsername;
    private final String demoPassword;

    public DemoUserSeeder(
            UserService userService,
            PasswordEncoder passwordEncoder,
            @Value("${demo.username}") String demoUsername,
            @Value("${demo.password}") String demoPassword) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.demoUsername = demoUsername;
        this.demoPassword = demoPassword;
    }

    @Override
    public void run(String... args) {
        if (userService.checkIfUserExists(demoUsername)) {
            return;
        }
        userService.registerUser(LabUser.builder()
                .username(demoUsername)
                .passwordHash(passwordEncoder.encode(demoPassword))
                .role("USER")
                .build());
    }
}
