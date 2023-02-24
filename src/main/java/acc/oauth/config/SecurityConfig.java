package acc.oauth.config;

import acc.oauth.service.PrincipalOAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // 시큐리티 활성화 -> 기본 스프링 필터 체인에 등록
public class SecurityConfig {

    @Autowired
    private PrincipalOAuthUserService principalOAuthUserService;

    @Bean
    private static BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").hasAuthority("MANAGER")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().permitAll()

                .and()
                .formLogin()
                .loginPage("/loginForm") // 미인증자 uri
                .loginProcessingUrl("/login") // login 주소 호출되면 시큐리티가 대신 로그인 진행
                .defaultSuccessUrl("/")

                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .defaultSuccessUrl("/")
                .userInfoEndpoint()
                .userService(principalOAuthUserService); // 구글 로그인 완료뒤 후처리 (엑세스 토큰 + 사용자 프로필 정보)

        return http.build();
    }
}
