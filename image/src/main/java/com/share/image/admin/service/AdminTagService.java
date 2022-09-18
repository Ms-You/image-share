package com.share.image.admin.service;

import com.share.image.admin.dto.TagRequestDto;
import com.share.image.feed.domain.Tag;
import com.share.image.feed.repository.TagRepository;
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
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AdminTagService {

    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }


    @Value("${tagImg.path}")
    private String uploadFolder;

    public void createTag(TagRequestDto tagRequestDto, MultipartFile multipartFile) throws UnsupportedEncodingException {

        Tag tag = Tag.createTag(tagRequestDto.getName());
        updateTagImage(tag, multipartFile);

        tagRepository.save(tag);
    }


    public void updateTag(Tag tag, TagRequestDto tagRequestDto, MultipartFile multipartFile) throws UnsupportedEncodingException {

        tag.updateTag(tagRequestDto.getName());
        updateTagImage(tag, multipartFile);

        tagRepository.save(tag);
    }


    public void updateTagImage(Tag tag, MultipartFile multipartFile) throws UnsupportedEncodingException {

        // 한글 파일 명 깨짐 처리
        String fileName = URLEncoder.encode(UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename(), "UTF-8");
        Path imageFilePath = Paths.get(uploadFolder + fileName);
        log.info("fileName: {}", fileName);

        // 파일 업로드 여부 확인
        if (multipartFile.getSize() != 0){
            try{
                if (tag.getTagImageUrl() != null) {
                    File file = new File(uploadFolder + tag.getTagImageUrl());
                    file.delete();
                }
                Files.write(imageFilePath, multipartFile.getBytes());
            } catch (Exception e){
                e.printStackTrace();
            }
            tag.updateTagImageUrl(fileName);
        }

    }


}
