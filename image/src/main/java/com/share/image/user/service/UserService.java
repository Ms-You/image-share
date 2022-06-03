package com.share.image.user.service;

import com.share.image.user.domain.RoleType;
import com.share.image.user.domain.User;
import com.share.image.user.dto.JoinRequestDto;
import com.share.image.user.dto.UpdateRequestDto;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User signUp(JoinRequestDto joinRequestDto){
        if (userRepository.existsByEmail(joinRequestDto.getEmail()))
            throw new IllegalStateException("이미 존재하는 이메일입니다.");

        User user = User.builder()
                .email(joinRequestDto.getEmail())
                .nickName(joinRequestDto.getNickName())
                .password(bCryptPasswordEncoder.encode(joinRequestDto.getPassword()))
                .intro(null)
                .profileImageUrl(null)
                .role(RoleType.ROLE_USER)
                .build();

        nicknameDuplicated(user.getNickName());

        return userRepository.save(user);

    }

    @Transactional(readOnly = true)
    public boolean nicknameDuplicated(String nickName){
        if(userRepository.existsByNickName(nickName) || nickName.equals(""))
            return false;
        else
            return true;
    }


    @Value("${profileImg.path}")
    private String uploadFolder;

    public void updateProfile(User user, UpdateRequestDto updateRequestDto, MultipartFile multipartFile){

        String fileName = user.getId() + "_" + multipartFile.getOriginalFilename();
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
