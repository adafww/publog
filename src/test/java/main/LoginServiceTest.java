package main;

import main.repository.UserRepository;
import main.service.LoginService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование LoginService")
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void getLoginResponseTest(){

        String email = "email";
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(Mockito.isA(Authentication.class))).thenReturn(authentication);

        User user = new User(email, "pass", true, true, true, true, authentication.getAuthorities());

        Mockito.when((User) authentication.getPrincipal()).thenReturn(user);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(new main.model.User(new Date(), "user", email, "pass")));

        new LoginService(userRepository, authenticationManager).getLoginResponse(email, "pass");
        Assertions.assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }
}
