package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class TagRepository implements CrdRepository<Tag> { //todo should deletion and search be by id or by name???
    private static final String INSERT = "INSERT INTO tag " +
            "(name) VALUES (?)";
    private static final String SELECT_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM tag";
    private static final String REMOVE_BY_INDEX = "DELETE FROM tag WHERE id = ?";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean create(Tag tag) {
        return jdbcTemplate.update(INSERT, tag.getName()) == 1;
    }

    @Override
    public Optional<Tag> read(long index) {
        Optional<Tag> resultWrapper = Optional.empty();
        try {
            Tag tag = jdbcTemplate.queryForObject(SELECT_BY_ID, Tag.class, index, new TagMapper());
            if (tag != null) {
                resultWrapper = Optional.of(tag);
            }
        } catch (EmptyResultDataAccessException ignored) {

        }
        return resultWrapper;
    }

    @Override
    public Set<Tag> read() {
        return new HashSet<>(jdbcTemplate.query(SELECT_ALL, new TagMapper()));
    }

    @Override
    public boolean delete(long index) {
        return jdbcTemplate.update(REMOVE_BY_INDEX, index) == 1;
    }
}
