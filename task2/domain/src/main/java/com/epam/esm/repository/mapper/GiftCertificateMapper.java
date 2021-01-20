package com.epam.esm.repository.mapper;


import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong("id"));
        giftCertificate.setName(rs.getString("gift_certificate.name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getLong("price"));
        giftCertificate.setDuration(rs.getInt("duration"));
        giftCertificate.setCreationDate(rs.getTimestamp("create_date").toLocalDateTime());
        giftCertificate.setLastUpdateDate(rs.getTimestamp("last_update_date").toLocalDateTime());
        return giftCertificate;
    }
}
