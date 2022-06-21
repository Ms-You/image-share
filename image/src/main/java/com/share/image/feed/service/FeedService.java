package com.share.image.feed.service;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.dto.FeedRequestDto;
import com.share.image.feed.repository.FeedRepository;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.List;
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

    public void createFeed(User user, FeedRequestDto feedRequestDto, MultipartFile multipartFile) throws UnsupportedEncodingException {

        Feed feed = Feed.builder()
                .title(feedRequestDto.getTitle())
                .description(feedRequestDto.getDescription())
                .tag(feedRequestDto.getTag())
                .writer(user)
                .build();

        String fileName = user.getId() + "_" + multipartFile.getOriginalFilename();
        // 한글 파일 명 깨짐 처리
        fileName = URLEncoder.encode(fileName, "UTF-8");
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

        feedRepository.save(feed);
    }

    public void updateFeed(User user, Feed feed, FeedRequestDto feedRequestDto, MultipartFile multipartFile) throws UnsupportedEncodingException {

        feed.modifyFeed(feedRequestDto.getTitle(), feedRequestDto.getDescription(), feedRequestDto.getTag());

        String fileName = user.getId() + "_" + multipartFile.getOriginalFilename();
        // 한글 파일 명 깨짐 처리
        fileName = URLEncoder.encode(fileName, "UTF-8");
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

        feedRepository.save(feed);
    }


    public Page<Feed> search(String keyword, Pageable pageable){
        Page<Feed> feedList = feedRepository.findByTitleContaining(keyword, pageable);

        return feedList;
    }


}
