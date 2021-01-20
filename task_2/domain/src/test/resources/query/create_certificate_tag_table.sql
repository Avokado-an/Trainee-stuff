CREATE TABLE gift_certificate_tag
(
    gift_certificate_id BIGINT NOT NULL,
    tag_id         BIGINT NOT NULL,
    PRIMARY KEY (gift_certificate_id, tag_id)
);