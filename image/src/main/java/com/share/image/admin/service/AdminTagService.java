package com.share.image.admin.service;

import com.share.image.admin.dto.TagRequestDto;
import com.share.image.feed.domain.Tag;
import com.share.image.feed.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AdminTagService {

    private final TagRepository tagRepository;

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    public void createTag(TagRequestDto tagRequestDto){
        Tag tag = Tag.builder()
                .name(tagRequestDto.getName())
                .path(tagRequestDto.getPath())
                .build();

        tagRepository.save(tag);
    }


}
