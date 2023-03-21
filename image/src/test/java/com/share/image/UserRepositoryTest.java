//package com.share.image;
//
//import com.share.image.user.domain.RoleType;
//import com.share.image.user.domain.User;
//import com.share.image.user.repository.UserRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.After;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserRepositoryTest {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @After
//    public void cleanUp(){
//        userRepository.deleteAll();
//    }
//
//    @Test
//    public void insertBaseTimeEntity(){
//        // given
//        LocalDateTime now = LocalDateTime.now();
//        userRepository.save(User.builder()
//                .email("test@naver.com")
//                .nickName("test")
//                .password("1234")
//                .role(RoleType.ROLE_USER)
//                .intro("hi")
//                .profileImageUrl("test")
//                .build());
//
//        // when
//        List<User> users = userRepository.findAll();
//
//        // then
//        User user = users.get(0);
//        System.out.println(">>>>>>  createdDate=" + user.getCreatedDate());
//        System.out.println(">>>>>>  modifiedDate=" + user.getModifiedDate());
//
//        assertThat(user.getCreatedDate()).isAfter(now);
//        assertThat(user.getModifiedDate()).isAfter(now);
//
//    }
//
//}
