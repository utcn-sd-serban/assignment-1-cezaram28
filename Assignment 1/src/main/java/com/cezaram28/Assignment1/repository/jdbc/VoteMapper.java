package com.cezaram28.Assignment1.repository.jdbc;

import com.cezaram28.Assignment1.entity.Answer;
import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.entity.Vote;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper implements RowMapper<Vote> {
    @Override
    public Vote mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Vote(resultSet.getInt("id"), resultSet.getString("type"),
                        new Question(resultSet.getInt("question_id")),
                        new Answer(resultSet.getInt("answer_id")),
                        new User(resultSet.getInt("user_id")));
    }
}
