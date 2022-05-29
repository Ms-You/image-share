package com.share.image.user.service;

import com.share.image.user.domain.RoleType;
import com.share.image.user.domain.User;
import com.share.image.user.dto.JoinRequestDto;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User signUp(JoinRequestDto joinRequestDto){

        User user = User.builder()
                .email(joinRequestDto.getEmail())
                .password(bCryptPasswordEncoder.encode(joinRequestDto.getPassword()))
                .gender(joinRequestDto.getGender())
                .nickName(joinRequestDto.getNickName())
                .role(RoleType.USER)
                .build();

        nicknameDuplicated(user.getNickName());

        userRepository.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public void nicknameDuplicated(String nickName){
        if(userRepository.existsByNickName(nickName))
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
    }


}
