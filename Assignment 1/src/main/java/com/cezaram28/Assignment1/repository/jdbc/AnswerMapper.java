package com.cezaram28.Assignment1.repository.jdbc;

import com.cezaram28.Assignment1.entity.Answer;
import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerMapper implements RowMapper<Answer> {

    @Override
    public Answer mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Answer(resultSet.getInt("id"), new Question(resultSet.getInt("question_id")),
                new User(resultSet.getInt("author_id")), resultSet.getString("text"),
                resultSet.getTimestamp("creation_date"), resultSet.getInt("vote_count"));
    }
}
