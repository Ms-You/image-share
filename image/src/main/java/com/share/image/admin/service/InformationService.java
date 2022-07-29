package com.share.image.admin.service;

import com.share.image.admin.domain.Information;
import com.share.image.admin.dto.InfoRequestDto;
import com.share.image.admin.repository.InformationRepository;
import com.share.image.user.domain.User;
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

    public void enrollInfo(User writer, InfoRequestDto infoRequestDto) {
        Information information = Information.builder()
                .title(infoRequestDto.getTitle())
                .content(infoRequestDto.getContent())
                .informationType(infoRequestDto.getInformationType())
                .writer(writer)
                .build();

        informationRepository.save(information);
    }

    public void updateInfo(Information information, InfoRequestDto infoRequestDto) {
        information.updateInfo(infoRequestDto.getTitle(), infoRequestDto.getContent(), infoRequestDto.getInformationType());

        informationRepository.save(information);
    }

}
