package com.share.image.user.service;

import com.share.image.config.oauth.OAuth2UserInfo;
import com.share.image.user.domain.RoleType;
import com.share.image.user.domain.User;
import com.share.image.user.dto.JoinRequestDto;
import com.share.image.user.dto.UpdateRequestDto;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 회원가입 시, 유효성 체크
    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }


    public User signUp(JoinRequestDto joinRequestDto){

        User user = User.builder()
                .email(joinRequestDto.getEmail())
                .nickName(joinRequestDto.getNickName())
                .password(bCryptPasswordEncoder().encode(joinRequestDto.getPassword()))
                .provider("image-share")
                .providerId(UUID.randomUUID().toString())
                .build();

        return userRepository.save(user);
    }


    public User oauthSignUp(OAuth2UserInfo oAuth2UserInfo){
        User user = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .nickName(oAuth2UserInfo.getName())
                .password(bCryptPasswordEncoder().encode(oAuth2UserInfo.getEmail()))  // 비밀번호는 의미가 없어서 이메일로 암호화만 해서 넣어줌
                .provider(oAuth2UserInfo.getProvider())
                .providerId(oAuth2UserInfo.getProviderId())
                .build();

        return userRepository.save(user);
    }


    @Value("${profileImg.path}")
    private String uploadFolder;

    public void updateProfile(User user, UpdateRequestDto updateRequestDto, MultipartFile multipartFile) throws UnsupportedEncodingException {

        // 한글 파일 명 깨짐 처리
        String fileName = URLEncoder.encode(user.getId() + "_" + multipartFile.getOriginalFilename(), "UTF-8");
        Path imageFilePath = Paths.get(uploadFolder + fileName);
        log.info("fileName: {}", fileName);

        // 파일 업로드 여부 확인
        if (multipartFile.getSize() != 0){
            try{
                if (user.getProfileImageUrl() != null) {
                    File file = new File(uploadFolder + user.getProfileImageUrl());
                    file.delete();
                }
                Files.write(imageFilePath, multipartFile.getBytes());
            } catch (Exception e){
                e.printStackTrace();
            }
            user.updateProfileImageUrl(fileName);
        }

        user.updateProfile(updateRequestDto.getNickName(), updateRequestDto.getIntro());
    }


}
