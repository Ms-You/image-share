package com.share.image.user.service;

import com.share.image.user.domain.User;
import com.share.image.user.dto.JoinRequestDto;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public User signUp(JoinRequestDto joinRequestDto){
        User user = joinRequestDto.toEntity();
        nicknameDuplicated(user.getNickName());

        userRepository.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public boolean nicknameDuplicated(String nickName){
        if(userRepository.existsByNickName(nickName) || nickName.equals(""))
            return false;
        else
            return true;
    }


}
