package com.share.image.feed.repository;

import com.share.image.feed.domain.Report;
import com.share.image.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByToUser(User toUser);

    Optional<Report> findByFromUserAndToUser(User fromUser, User toUser);
}
