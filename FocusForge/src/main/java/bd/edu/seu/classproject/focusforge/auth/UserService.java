package bd.edu.seu.classproject.focusforge.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return email != null && userRepository.existsByEmailIgnoreCase(email.trim());
    }

    public AppUser register(AppUser user) {
        user.setEmail(normalizeEmail(user.getEmail()));
        user.setFullName(user.getFullName().trim());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public Optional<AppUser> findByEmail(String email) {
        if (email == null || email.isBlank()) return Optional.empty();
        return userRepository.findByEmailIgnoreCase(email.trim());
    }

    public String displayName(String email) {
        return findByEmail(email)
                .map(AppUser::getFullName)
                .filter(name -> !name.isBlank())
                .orElse(email == null ? "Student" : email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No account found for " + email));

        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(appUser.getRole())
                .disabled(!appUser.isEnabled())
                .build();
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}
