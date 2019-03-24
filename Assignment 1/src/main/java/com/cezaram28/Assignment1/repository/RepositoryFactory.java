package com.cezaram28.Assignment1.repository;

public interface RepositoryFactory {
    UserRepository createUserRepository();
    QuestionRepository createQuestionRepository();
    AnswerRepository createAnswerRepository();
    TagRepository createTagRepository();
    VoteRepository createVoteRepository();
}
