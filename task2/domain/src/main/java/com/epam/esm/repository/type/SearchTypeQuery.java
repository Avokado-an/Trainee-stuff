package com.epam.esm.repository.type;

public enum SearchTypeQuery {
    SEARCH_BY_TAG("SELECT * FROM gift_certificate INNER JOIN gift_certificate_tag " +
            "ON gift_certificate.id = gift_certificate_tag.gift_certificate_id " +
            "INNER JOIN tag ON gift_certificate_tag.tag_id = tag.id WHERE tag.name = '?'",
            " INNER JOIN gift_certificate_tag " +
                    "ON gift_certificate.id = gift_certificate_tag.gift_certificate_id " +
                    "INNER JOIN tag ON gift_certificate_tag.tag_id = tag.id WHERE tag.name = '?'"),
    SEARCH_BY_NAME_DESCRIPTION("SELECT * FROM gift_certificate WHERE gift_certificate.name LIKE '?%' " +
            "OR gift_certificate.description LIKE '?%'",
            " AND (gift_certificate.name LIKE '?%' OR gift_certificate.description LIKE '?%')");

    private String standaloneQuery;
    private String continuingQuery;

    SearchTypeQuery(String standaloneQuery, String continuingQuery) {
        this.standaloneQuery = standaloneQuery;
        this.continuingQuery = continuingQuery;
    }

    public String getStandaloneQuery() {
        return standaloneQuery;
    }

    public String getContinuingQuery() {
        return continuingQuery;
    }
}
