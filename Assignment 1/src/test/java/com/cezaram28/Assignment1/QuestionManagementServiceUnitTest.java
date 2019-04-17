package com.cezaram28.Assignment1;

import com.cezaram28.Assignment1.entity.Question;
import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.exception.QuestionNotFoundException;
import com.cezaram28.Assignment1.exception.UserNotFoundException;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import com.cezaram28.Assignment1.repository.memory.InMemoryRepositoryFactory;
import com.cezaram28.Assignment1.service.QuestionManagementService;
import com.cezaram28.Assignment1.service.UserManagementService;
import org.junit.Assert;
import org.junit.Test;

public class QuestionManagementServiceUnitTest {
    private static RepositoryFactory createMockedFactory() {
        RepositoryFactory factory = new InMemoryRepositoryFactory();
        factory.createQuestionRepository().save(new Question("need help", new User("user1", "pass1", "user1"), "i don't know how to java"));
        factory.createQuestionRepository().save(new Question("got errors", new User("user1", "pass1", "user1"), "i got errors in my code"));
        return factory;
    }

    @Test
    public void testRemoveWorksWithExistingId() {
        RepositoryFactory factory = createMockedFactory();
        QuestionManagementService service = new QuestionManagementService(factory);

        User user = new User();
        user.setIsAdmin(true);
        service.removeQuestion(1, user);

        Assert.assertEquals(1, factory.createQuestionRepository().findAll().size());
        Assert.assertTrue(factory.createQuestionRepository().findById(2).isPresent());
    }

    @Test(expected = QuestionNotFoundException.class)
    public void testRemoveThrowsWithNotExistingId() {
        RepositoryFactory factory = createMockedFactory();
        QuestionManagementService service = new QuestionManagementService(factory);

        User user = new User();
        user.setIsAdmin(true);
        service.removeQuestion(999, user);
    }

    @Test
    public void testFindById(){
        RepositoryFactory factory = createMockedFactory();
        QuestionManagementService service = new QuestionManagementService(factory);
        Question question = service.findById(1);
        Assert.assertEquals("need help", question.getTitle());
        Assert.assertEquals("i don't know how to java", question.getText());

    }
}
