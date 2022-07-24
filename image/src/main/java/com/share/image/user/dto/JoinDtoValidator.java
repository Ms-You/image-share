package com.share.image.user.dto;

import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class JoinDtoValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(JoinRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        JoinRequestDto joinRequestDto = (JoinRequestDto) object;
        if (userRepository.existsByEmail(joinRequestDto.getEmail())){
            errors.rejectValue("email", "email", new Object[]{joinRequestDto.getEmail()}, "이미 사용중인 이메일입니다.");
        }
        if (!joinRequestDto.getPassword().equals(joinRequestDto.getPassword_confirm())){
            errors.rejectValue("password_confirm", "key","비밀번호가 일치하지 않습니다.");
        }
    }



}
