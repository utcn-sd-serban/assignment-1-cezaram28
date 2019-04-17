package com.cezaram28.Assignment1;

import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.exception.BadCredentialsException;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import com.cezaram28.Assignment1.repository.memory.InMemoryRepositoryFactory;
import com.cezaram28.Assignment1.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class IntegrationTests {

    //private EntityManager entityManager;
    private RepositoryFactory repositoryFactory = new InMemoryRepositoryFactory();
    private UserManagementService userManagementService = new UserManagementService(repositoryFactory);

    private QuestionManagementService questionManagementService = new QuestionManagementService(repositoryFactory);

    private AnswerManagementService answerManagementService = new AnswerManagementService(repositoryFactory);

    private TagManagementService tagManagementService = new TagManagementService(repositoryFactory);

    private VoteManagementService voteManagementService = new VoteManagementService(repositoryFactory, questionManagementService, userManagementService, answerManagementService);

    public void createMockedData(){
        if(repositoryFactory.createUserRepository().findAll().isEmpty()){
            repositoryFactory.createUserRepository().save(new User("user1", "pass1", "email1"));
            repositoryFactory.createUserRepository().save(new User("user2", "pass2", "email2"));
            repositoryFactory.createUserRepository().save(new User("user3", "pass3", "email3"));
        }
    }

    @Test(expected = BadCredentialsException.class)
    public void testLogin(){
        createMockedData();
        userManagementService.findByCredentials("usern", "passn");
    }
}
