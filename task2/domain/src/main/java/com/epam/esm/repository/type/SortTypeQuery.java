package com.epam.esm.repository.type;

public enum SortTypeQuery {
    BY_NAME_ASC(" ORDER BY gift_certificate.name ASC", ", gift_certificate.name ASC"),
    BY_NAME_DESC(" ORDER BY gift_certificate.name DESC", ", gift_certificate.name DESC"),
    BY_DATE_ASC(" ORDER BY gift_certificate.create_date ASC", ", gift_certificate.create_date ASC"),
    BY_DATE_DESC(" ORDER BY gift_certificate.create_date DESC", ", gift_certificate.create_date DESC");

    private String standaloneQuery;
    private String continuationQuery;

    SortTypeQuery(String standaloneQuery, String continuationQuery) {
        this.standaloneQuery = standaloneQuery;
        this.continuationQuery = continuationQuery;
    }

    public String getStandaloneQuery() {
        return standaloneQuery;
    }

    public String getContinuationQuery() {
        return continuationQuery;
    }
}
