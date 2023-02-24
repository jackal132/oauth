package acc.oauth.controller;

import acc.oauth.entity.PrincipalDetails;
import acc.oauth.entity.Role;
import acc.oauth.entity.User;
import acc.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @ResponseBody
    @GetMapping("/test/info")
    public String testInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){

        // 방법1
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.debug("authentication : {}", principalDetails.getUser());

        // 방법2
        log.debug("userDetails:" + userDetails.getUser());

        return "check session info";
    }

    @ResponseBody
    @GetMapping("/test/oauth/info")
    public String testOAuthInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User){

        // 방법1
        OAuth2User oAuthUser = (OAuth2User) authentication.getPrincipal();
        log.debug("authentication : {}", oAuthUser.getAttributes());

        // 방법2
        log.debug("OAuth2User : {}", oAuthUser.getAttributes());

        return "check oauth2 session info";
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @ResponseBody
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        log.debug("PrincipalDetails : {} ", principalDetails);
        return "user";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @ResponseBody
    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }

    @ResponseBody
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute User user){
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        user.setPassword(encPassword);
        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }
}
