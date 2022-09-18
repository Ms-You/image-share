package com.share.image.feed.service;

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

    public Feed createFeed(User user, FeedRequestDto feedRequestDto, Tag tag, MultipartFile multipartFile) throws UnsupportedEncodingException {

        Feed feed = Feed.createFeed(user, feedRequestDto, tag);
        updateFeedImage(user, feed, multipartFile);

        return feedRepository.save(feed);
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

}
