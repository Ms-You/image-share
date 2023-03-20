package com.share.image.feed.repository;

import com.share.image.feed.domain.Feed;
import com.share.image.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedQueries {

    private final EntityManager em;

    public void save(Feed feed) {
        em.persist(feed);
    }

    public Optional<Feed> findById(Long id) {
        return Optional.ofNullable(em.find(Feed.class, id));
    }

    public void delete(Feed feed){
        em.remove(feed);
    }


    // 조회수 내림차순하여 조회
    public List<Feed> findByViewDesc(int offset){
        return em.createQuery("select f from Feed f group by f order by f.view desc")
                .setFirstResult(offset)
                .setMaxResults(5)
                .getResultList();
    }

    // 좋아요 개수 내림차순하여 조회
    public List<Feed> findByLikesDesc(int offset){
        return em.createQuery("select fl.feed from FeedLike fl group by fl.feed order by count(fl.feed) desc")
                .setFirstResult(offset)
                .setMaxResults(5)
                .getResultList();
    }

    // 최신피드부터 조회
    public List<Feed> findByCreatedDateDesc(int offset){
        return em.createQuery("select f from Feed f group by f order by f.createdDate desc")
                .setFirstResult(offset)
                .setMaxResults(5)
                .getResultList();
    }

    // 좋아요 표시한 피드 목록
    public List<Feed> findByUserAndLikesDesc(User user, int offset){
        return em.createQuery("select fl.feed from FeedLike fl where fl.user = :user group by fl.feed order by count(fl.feed) desc")
                .setParameter("user", user)
                .setFirstResult(offset)
                .setMaxResults(5)
                .getResultList();
    }

    // totalPages 조회
    public List<Long> findSizeOfFeedByUser(User user){
        return em.createQuery("select fl.feed.id from FeedLike fl where fl.user = :user")
                .setParameter("user", user)
                .getResultList();
    }



}
