package com.share.image.feed.service;

import com.share.image.config.exception.ErrorCode;
import com.share.image.config.exception.GlobalException;
import com.share.image.feed.domain.Feed;
import com.share.image.feed.domain.Tag;
import com.share.image.feed.dto.FeedRequestDto;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FeedService {

    private final FeedRepository feedRepository;

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }


    @Value("${feedImg.path}")
    private String uploadFolder;

    public void createFeed(User user, FeedRequestDto feedRequestDto, Tag tag, MultipartFile multipartFile) throws UnsupportedEncodingException {

        Feed feed = Feed.createFeed(user, feedRequestDto, tag);
        updateFeedImage(user, feed, multipartFile);

        feedRepository.save(feed);
    }


    public void updateFeed(User user, Feed feed, FeedRequestDto feedRequestDto, Tag tag, MultipartFile multipartFile) throws UnsupportedEncodingException {

        feed.updateFeed(user, feedRequestDto.getTitle(), feedRequestDto.getDescription(), tag);
        updateFeedImage(user, feed, multipartFile);

        feedRepository.save(feed);
    }


    public void updateFeedImage(User user, Feed feed, MultipartFile multipartFile) throws UnsupportedEncodingException {

        // 한글 파일 명 깨짐 처리
        String fileName = URLEncoder.encode(user.getId() + "_" + multipartFile.getOriginalFilename(), "UTF-8");
        Path imageFilePath = Paths.get(uploadFolder + fileName);
        log.info("fileName: {}", fileName);

        // 파일 업로드 여부 확인
        if (multipartFile.getSize() != 0){
            try{
                if (feed.getFeedImageUrl() != null) {
                    File file = new File(uploadFolder + feed.getFeedImageUrl());
                    file.delete();
                }
                Files.write(imageFilePath, multipartFile.getBytes());
            } catch (Exception e){
                e.printStackTrace();
            }
            feed.updateFeedImageUrl(fileName);
        }
    }

    // 피드 조회수 증가 로직
    public Feed adjustViewCnt(Long feedId, HttpServletRequest req, HttpServletResponse resp){
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new GlobalException(ErrorCode.FEED_ERROR)
        );

        Cookie[] cookies = req.getCookies();
        Cookie cookie = null;
        boolean flag = false;

        int idx = 0;
        while(idx < cookies.length && cookies != null){
            // 조회수 관련 쿠키가 있을 때
            if (cookies[idx].getName().equals("feedView")) {
                // cookie에 저장
                cookie = cookies[idx];

                // 안 본 피드일 때 조회수 증가
                if (!cookie.getValue().contains("[" + feed.getId() + "]")){
                    feed.increaseView();
                    cookie.setValue(cookie.getValue() + "[" + feed.getId() + "]");
                }
                flag = true;
                break;
            }
            idx++;
        }

        // feedView 쿠키가 없으면 새로 생성
        if (!flag){
            feed.increaseView();
            cookie = new Cookie("feedView", "[" + feed.getId() + "]");
        }

        feedRepository.save(feed);

        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        resp.addCookie(cookie);

        return feed;
    }

}
