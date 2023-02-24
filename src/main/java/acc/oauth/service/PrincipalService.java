package acc.oauth.service;

import acc.oauth.entity.PrincipalDetails;
import acc.oauth.entity.User;
import acc.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 시큐리티 설정 "LoginProcessingUrl("/login");
 * "/login" 요청이 오면 자동으로 UserDetailsService 타입으로 loc 되어있는 loadUserByUsername 함수 실행
 */
@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * 시큐리티 session => Authentication => UserDetails
     * 리턴된 값이  Authentication 안에 들어감. (리턴 될때 들어감 )
     * 시큐리티 session 안에  Authentication 이 들어감
     * 함수 종료시 @AuthenticationPrincipal 어노테이션 생성
     *
     * ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
     * ★★★★ 사용자의 아이디를 입력받는 name의 값을 "username"으로 해야함 ★★★★
     * ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
     * 사용자의 패스워드 인증이 정상적으로 처리된 사용자의 이름을 반환
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username);
        if(findUser != null) {
            return new PrincipalDetails(findUser);
        }
        return null;
    }
}
