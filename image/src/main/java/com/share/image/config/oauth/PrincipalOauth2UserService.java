package com.share.image.config.oauth;

import com.share.image.config.PrincipalDetails;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import com.share.image.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes: {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();    // google, kakao, naver

        User user = userRepository.findByEmailAndProvider(oAuth2User.getAttribute("email"), provider);

        if (user == null)   // email, provider 에 해당하는 계정이 없는 경우 생성
            user = userService.oauthSignUp(provider, oAuth2User);

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

}
