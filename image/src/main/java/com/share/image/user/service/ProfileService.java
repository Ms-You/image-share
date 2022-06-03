package com.share.image.user.service;

import com.share.image.user.domain.User;
import com.share.image.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Value("${profileImg.path}")
    private String uploadFolder;

    public void updateProfile(User user, String nickName, String intro, MultipartFile multipartFile){

        String fileName = user.getId() + "_" + multipartFile.getOriginalFilename();
        Path imageFilePath = Paths.get(uploadFolder + fileName);
        log.info("fileName: {}", fileName);

        // 파일 업로드 여부 확인
        if (multipartFile.getSize() != 0){
            try{
                if (user.getProfile().getProfileImageUrl() != null) {
                    File file = new File(uploadFolder + user.getProfile().getProfileImageUrl());
                    file.delete();
                }
                Files.write(imageFilePath, multipartFile.getBytes());
            } catch (Exception e){
                e.printStackTrace();
            }
            user.getProfile().updateProfileImageUrl(fileName);
        }

        user.getProfile().updateProfile(nickName, intro);

    }




}
