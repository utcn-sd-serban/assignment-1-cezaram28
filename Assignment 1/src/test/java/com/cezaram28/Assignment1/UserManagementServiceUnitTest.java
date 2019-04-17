package com.cezaram28.Assignment1;

import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.exception.UserNotFoundException;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import com.cezaram28.Assignment1.repository.memory.InMemoryRepositoryFactory;
import com.cezaram28.Assignment1.service.UserManagementService;
import org.junit.Assert;
import org.junit.Test;

public class UserManagementServiceUnitTest {

    private static RepositoryFactory createMockedFactory() {
        RepositoryFactory factory = new InMemoryRepositoryFactory();
        factory.createUserRepository().save(new User("user1", "pass1", "email1"));
        factory.createUserRepository().save(new User("user2", "pass2", "email2"));
        return factory;
    }

    @Test
    public void testRemoveWorksWithExistingId() {
        RepositoryFactory factory = createMockedFactory();
        UserManagementService service = new UserManagementService(factory);

        service.removeUser(1);

        Assert.assertEquals(1, factory.createUserRepository().findAll().size());
        Assert.assertTrue(factory.createUserRepository().findById(2).isPresent());
    }

    @Test(expected = UserNotFoundException.class)
    public void testRemoveThrowsWithNotExistingId() {
        RepositoryFactory factory = createMockedFactory();
        UserManagementService service = new UserManagementService(factory);
        service.removeUser(999);
    }

    @Test
    public void testFindByCredentials(){
        RepositoryFactory factory = createMockedFactory();
        UserManagementService service = new UserManagementService(factory);
        User user = service.findByCredentials("user1", "pass1");
        Assert.assertEquals("user1", user.getUsername());
        Assert.assertEquals("pass1", user.getPassword());

    }
}
