package com.cezaram28.Assignment1.repository.jdbc;

import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionMapper implements RowMapper<Question> {

    @Override
    public Question mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Question(resultSet.getInt("id"), resultSet.getString("title"),
                            new User(resultSet.getInt("author_id")), resultSet.getString("text"),
                            resultSet.getTimestamp("creation_date"),
                            resultSet.getInt("vote_count"), null);
    }
}
