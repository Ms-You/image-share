package com.share.image.feed.controller;

import com.share.image.config.PrincipalDetails;
import com.share.image.feed.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping("/subscribe/feed/{feed_id}")
    public String subscribeUser(@PathVariable(name = "feed_id") Long feedId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("subscribe");
        subscribeService.subscribe(principalDetails.getUser().getId(), feedId);

        return "redirect:/user/feed/" + feedId;
    }

    @PostMapping("/unSubscribe/feed/{feed_id}")
    public String unSubscribeUser(@PathVariable(name = "feed_id") Long feedId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        subscribeService.unSubscribe(principalDetails.getUser().getId(), feedId);

        return "redirect:/user/feed/" + feedId;
    }

}
