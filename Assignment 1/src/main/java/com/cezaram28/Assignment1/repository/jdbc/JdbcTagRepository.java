package com.cezaram28.Assignment1.repository.jdbc;

import com.cezaram28.Assignment1.entity.Tag;
import com.cezaram28.Assignment1.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcTagRepository implements TagRepository {

    private final JdbcTemplate template;

    @Override
    public List<Tag> findAll() {
        return template.query("SELECT * FROM tag", ((resultSet, i) -> new Tag(resultSet.getInt("id"), resultSet.getString("name"))));
    }

    @Override
    public Tag save(Tag tag) {
        List<Tag> t = template.query("SELECT * FROM tag WHERE name=?", ((resultSet, i) -> new Tag(resultSet.getInt("id"), resultSet.getString("name"))), tag.getName());
        if (t.isEmpty()) {
            tag.setId(insert(tag));
        } else {
            //tag already exists
            return t.get(0);
        }
        return tag;
    }

    @Override
    public void remove(Tag tag) {
        template.update("DELETE FROM tag WHERE id = ?", tag.getId());
    }

    @Override
    public Optional<Tag> findById(int id) {
        List<Tag> tags = template.query("SELECT * FROM tag WHERE id = ?", ((resultSet, i) -> new Tag(resultSet.getInt("id"), resultSet.getString("name"))), id);
        return tags.isEmpty() ? Optional.empty() : Optional.ofNullable(tags.get(0));
    }

    private int insert(Tag tag) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("tag");
        insert.usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();
        map.put("name", tag.getName());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(Tag tag) {
        template.update("UPDATE tag SET name = ? WHERE id = ?", tag.getName(), tag.getId());
    }
}
