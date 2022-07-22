package com.share.image.user.dto;

import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UpdateDtoValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UpdateRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        UpdateRequestDto updateRequestDto = (UpdateRequestDto) object;

        // dto 에 들어온 닉네임이 기존 사용하던 닉네임과 같을 경우는 pass
        if (updateRequestDto.getNickName().equals(updateRequestDto.getFormerNickName())){
            return;
        }
        else if (userRepository.existsByNickName(updateRequestDto.getNickName())){
            errors.rejectValue("nickName", "nickName", new Object[]{updateRequestDto.getNickName()}, "이미 사용중인 닉네임입니다.");
        }

    }
}
