package com.share.image.feed.repository;

import com.share.image.feed.domain.Subscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    // 구독 여부 확인
    @Query(value = "select subscribe_id from subscribe where from_user_id = :from_user_id and to_user_id = :to_user_id", nativeQuery = true)
    Long findSubscribeByUserId(Long from_user_id, Long to_user_id);

    // 구독
    @Modifying
    @Query(value = "insert into subscribe(from_user_id, to_user_id) values(:from_user_id, :to_user_id)", nativeQuery = true)
    void subscribeUser(Long from_user_id, Long to_user_id);

    // 구독 취소
    @Modifying
    @Query(value = "delete from subscribe where from_user_id = :from_user_id and to_user_id = :to_user_id", nativeQuery = true)
    void unSubscribeUser(Long from_user_id, Long to_user_id);

    Page<Subscribe> findByFromUserId(Long from_user_id, Pageable pageable);
}
