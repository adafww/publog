package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.LoginResponse;
import main.api.response.UserLoginResponse;
import main.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public LoginResponse getLoginResponse(String email, String password){

        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();

        return getLoginResponse(user.getUsername());
    }

    public LoginResponse getLoginResponse(String email) {

        main.model.User currentUser =
                userRepository.findByEmail(
                        email).orElseThrow(
                        () -> new UsernameNotFoundException(email));

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setEmail(currentUser.getEmail());
        userLoginResponse.setName(currentUser.getName());
        userLoginResponse.setModeration(currentUser.isModerator());
        userLoginResponse.setId(currentUser.getId());
        userLoginResponse.setPhoto(currentUser.getPhoto());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);

        return loginResponse;
    }

}
