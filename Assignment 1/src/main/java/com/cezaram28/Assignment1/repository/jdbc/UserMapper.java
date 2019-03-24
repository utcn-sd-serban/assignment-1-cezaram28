package com.cezaram28.Assignment1.repository.jdbc;

import com.cezaram28.Assignment1.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        return new User(resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getString("password"), resultSet.getString("email"),
                        resultSet.getInt("score"), resultSet.getBoolean("is_admin"),
                        resultSet.getBoolean("is_banned"));
    }
}
