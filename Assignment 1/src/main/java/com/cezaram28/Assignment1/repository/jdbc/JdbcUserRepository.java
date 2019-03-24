package com.cezaram28.Assignment1.repository.jdbc;

import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate template;

    @Override
    public List<User> findAll() {
        return template.query("SELECT * FROM user", new UserMapper());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(insert(user));
        } else {
            update(user);
        }
        return user;
    }

    @Override
    public void remove(User user) {
        template.update("DELETE FROM user WHERE id = ?", user.getId());
    }

    @Override
    public Optional<User> findById(int id) {

        List<User> users = template.query("SELECT * FROM user WHERE id = ?", new UserMapper(), id);
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    @Override
    public Optional<User> findByCredentials(String username, String password) {
        List<User> users = template.query("SELECT * FROM user WHERE username = ? AND password = ?", new UserMapper(), username, password);
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    private int insert(User user) {
        // we use the SimpleJdbcInsert to easily retrieve the generated ID
        SimpleJdbcInsert insert = new SimpleJdbcInsert(template);
        insert.setTableName("user");
        insert.usingGeneratedKeyColumns("id");
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("password", user.getPassword());
        map.put("email", user.getEmail());
        map.put("score", user.getScore());
        map.put("is_admin", user.getIsAdmin());
        map.put("is_banned", user.getIsBanned());
        return insert.executeAndReturnKey(map).intValue();
    }

    private void update(User user) {
        template.update("UPDATE user SET username = ?, password = ?, email = ?, score = ?, is_admin = ?, is_banned = ? WHERE id = ?",
                user.getUsername(), user.getPassword(), user.getEmail(), user.getScore(), user.getIsAdmin(), user.getIsBanned(), user.getId());
    }
}
