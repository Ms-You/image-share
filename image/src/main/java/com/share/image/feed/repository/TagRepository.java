package com.share.image.feed.repository;

import com.share.image.feed.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> findAll(Pageable pageable);
    Boolean existsByName(String name);
    Tag findByName(String name);
}
