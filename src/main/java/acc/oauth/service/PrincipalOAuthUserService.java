package acc.oauth.service;

import acc.oauth.entity.PrincipalDetails;
import acc.oauth.entity.Role;
import acc.oauth.entity.User;
import acc.oauth.info.OAuth2UserInfo;
import acc.oauth.info.OAuth2UserInfoFactory;
import acc.oauth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * OAuth2-client 라이브러리가 code 단계 처리후 OAuth2UserRequest 객체에 엑세스 토큰, 플랫폼 사용자 공유 key 값을 반환해줌
 * OAuth2UserRequest 객체의 상위타입인 OAuth2User 객체에 요청한 사용자 정보를 반환해줌
 * 요청한 사용자 정보에 대한 Response 의 형태는 다름
 * OAuth2-client 가 지원하는 provider(구글, 페이스북 등)는 Map(key, Object) 형태로 반환
 * 지원하지 않는 provider(네이버, 카카오) 는 json 형태로 반환
 */
@Slf4j
@Service
public class PrincipalOAuthUserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션 생성
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        log.debug("===> getClientRegistraion : {}",  userRequest.getClientRegistration());
        log.debug("===> getAccessToken : {}", userRequest.getAccessToken().getTokenValue());
        log.debug("===> getAttributes: {}", super.loadUser(userRequest).getAttributes());
        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리가 받아줌) -> code를 통해서 AccessToken요청(access토큰 받음)
        // 회원 프로필을 받을때 사용되는것이 loadUser 함수 -> 구글로부터 회원 프로필을 제공받음

        // OAuth 로그인 회원가입
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOauth2UserInfo(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes());

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("NO_PASS"); // 그냥 지정함
        String email = oAuth2UserInfo.getEmail();
        Role role = Role.USER;

        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            LocalDateTime crerateTime = LocalDateTime.now();
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .createdAt(crerateTime)
                    .build();

            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
