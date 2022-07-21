package com.share.image.admin.service;

import com.share.image.config.PrincipalDetails;
import com.share.image.user.domain.User;
import com.share.image.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockUserService {
    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;

    public void temporaryBlockUser(Long userId){
        User findUser = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        if (findUser.getTemporaryLocked().equals("해제하기")){  // 현재 일시 정지 상태
            findUser.setTemporaryLocked("정지하기"); // 정지 -> 해제
        } else {    // 현재 정지 상태 x
            findUser.setTemporaryLocked("해제하기");
            findUser.setPermanentLocked("정지하기");    // 영구 정지였던 경우 -> 일시 정지
            findUser.recordTemporarySuspendTime();

            List<PrincipalDetails> principals = sessionRegistry.getAllPrincipals()
                    .stream().map(o -> (PrincipalDetails) o).collect(Collectors.toList());

            for (PrincipalDetails principal : principals) {
                User user = principal.getUser();
                if (user.getId() == findUser.getId()){
                    List<SessionInformation> sessionList = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessionList){
                        session.expireNow();
                    }
                }

            }
        }
    }

    public void permanentBlockUser(Long userId){
        User findUser = userRepository.findById(userId).orElseThrow(()->{
            return new UsernameNotFoundException("일치하는 사용자를 찾을 수 없습니다.");
        });

        if (findUser.getPermanentLocked().equals("해제하기")){   // 현재 영구 정지 상태
            findUser.setPermanentLocked("정지하기"); // 정지 -> 해제
        } else {
            findUser.setPermanentLocked("해제하기");
            findUser.setTemporaryLocked("정지하기");    // 일시 정지였던 경우 -> 영구 정지
            findUser.recordPermanentSuspendTime();

            List<PrincipalDetails> principals = sessionRegistry.getAllPrincipals()
                    .stream().map(o -> (PrincipalDetails) o).collect(Collectors.toList());

            for (PrincipalDetails principal : principals) {
                User user = principal.getUser();
                if (user.getId() == findUser.getId()){
                    List<SessionInformation> sessionList = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessionList){
                        session.expireNow();
                    }
                }

            }
        }
    }

    // 일시정지 7일 뒤 해제
    public void liftReportScheduler(){
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getTemporaryLocked().equals("해제하기")){   // 임시 정지된 사용자
                Duration duration = Duration.between(user.getTemporarySuspendedDate(), LocalDateTime.now());
                if (duration.getSeconds() >= 604800){   // 7일후 정지 해제
                    temporaryBlockUser(user.getId());
                }
            }

        }


    }


}
