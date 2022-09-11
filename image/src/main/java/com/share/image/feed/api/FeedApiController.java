package com.share.image.feed.api;

import com.share.image.feed.domain.Feed;
import com.share.image.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FeedApiController {
    private final FeedRepository feedRepository;

    @Value("${feedImg.path}")
    private String uploadFolder;

    @GetMapping("/download/{feedId}")
    public ResponseEntity<Resource> download(@PathVariable(name = "feedId") Long feedId){
        Feed feed = feedRepository.findById(feedId).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 피드입니다.");
        });

        String url = feed.getFeedImageUrl();
        String path = uploadFolder + url;

        try {
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath));  // 파일 경로

            File file = new File(path);
            int idx = file.getName().indexOf("_");
            String fileName = file.getName().substring(idx + 1);
            log.info("fileName:{}", fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fileName).build());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

    }

}