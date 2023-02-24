package acc.oauth.info.impl;

import acc.oauth.info.OAuth2UserInfo;

import java.util.Map;

public class KakaoUserInfo extends OAuth2UserInfo{
    public KakaoUserInfo(Map<String, Object> attributes){
        super(attributes);
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("account_email"));
    }

    @Override
    public String getName() {

        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");

        if (properties == null) {
            return null;
        }

        return (String) properties.get("nickname");
    }
}
