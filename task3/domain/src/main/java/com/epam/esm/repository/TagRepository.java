package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    @Query(value = "select t.id, t.name, count(t.name) as cnt from (" +
            "select u.id, tag.name from user as u " +
            "join certificate_order as o on u.id = o.user_id " +
            "join order_certificates oc on o.id = oc.gift_certificate_id " +
            "join gift_certificate gift on oc.order_id = gift.id " +
            "join gift_certificates_tags gt on gift.id = gt.gift_certificate_id " +
            "join tag on gt.utag_id = tag.id" +
            ") t " +
            "where t.id = :userId " +
            "group by t.name order by count(t.name) desc limit 1", nativeQuery = true)
    List<Tag> findMostUsedUserTag(@Param("userId") Long userId);

    int removeAllById(long id);

    Tag findByName(String name);
}
