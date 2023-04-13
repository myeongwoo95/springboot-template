package io.vitasoft.dvorakbackend.config.oauth.provider;

import lombok.RequiredArgsConstructor;
import java.util.Map;

@RequiredArgsConstructor
public class FacebookUserInfo implements OAuth2UserInfo{

    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String)attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }
}