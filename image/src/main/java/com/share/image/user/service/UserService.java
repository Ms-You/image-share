package com.share.image.user.service;

import com.share.image.user.domain.Profile;
import com.share.image.user.domain.User;
import com.share.image.user.dto.JoinRequestDto;
import com.share.image.user.repository.ProfileRepository;
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

    private final ProfileRepository profileRepository;

    public User signUp(JoinRequestDto joinRequestDto){
        User user = joinRequestDto.toEntity();
        nicknameDuplicated(user.getNickName());

        userRepository.save(user);

        Profile profile = new Profile();
        profile.uploadUser(user);
        profileRepository.save(profile);

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
