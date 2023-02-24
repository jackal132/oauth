package acc.oauth.info;

import acc.oauth.info.impl.GoogleUserInfo;
import acc.oauth.info.impl.KakaoUserInfo;
import acc.oauth.info.impl.NaverUserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOauth2UserInfo(String registrationId, Map<String, Object> attributes){
        switch (registrationId){
            case "google": return new GoogleUserInfo(attributes);
            case "naver": return new NaverUserInfo(attributes);
            case "kakao": return new KakaoUserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type");
        }
    }
}
