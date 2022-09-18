package com.share.image.admin.service;

import com.share.image.admin.domain.Information;
import com.share.image.admin.dto.InfoRequestDto;
import com.share.image.admin.repository.InformationRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class InformationService {

    private final InformationRepository informationRepository;

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    @Value("${infoImg.path}")
    private String uploadFolder;

    public void enrollInfo(User writer, InfoRequestDto infoRequestDto, MultipartFile multipartFile) throws UnsupportedEncodingException {
        Information information = Information.createInformation(infoRequestDto.getTitle(), infoRequestDto.getContent(), infoRequestDto.getInformationType(), writer);
        updateInformationImage(information, multipartFile);

        informationRepository.save(information);
    }

    public void updateInfo(Information information, InfoRequestDto infoRequestDto, MultipartFile multipartFile) throws UnsupportedEncodingException {
        information.updateInfo(infoRequestDto.getTitle(), infoRequestDto.getContent(), infoRequestDto.getInformationType());
        updateInformationImage(information, multipartFile);

        informationRepository.save(information);
    }

    public void updateInformationImage(Information information, MultipartFile multipartFile) throws UnsupportedEncodingException {

        // 한글 파일 명 깨짐 처리
        String fileName = URLEncoder.encode(information.getWriter().getId() + "_" + multipartFile.getOriginalFilename(), "UTF-8");
        Path imageFilePath = Paths.get(uploadFolder + fileName);
        log.info("fileName: {}", fileName);

        // 파일 업로드 여부 확인
        if (multipartFile.getSize() != 0){
            try{
                if (information.getInfoImageUrl() != null) {
                    File file = new File(uploadFolder + information.getInfoImageUrl());
                    file.delete();
                }
                Files.write(imageFilePath, multipartFile.getBytes());
            } catch (Exception e){
                e.printStackTrace();
            }
            information.updateInfoImageUrl(fileName);
        }

    }

}
