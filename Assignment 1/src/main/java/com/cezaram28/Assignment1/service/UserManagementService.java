package com.cezaram28.Assignment1.service;

import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import com.cezaram28.Assignment1.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final RepositoryFactory repositoryFactory;

    @Transactional
    public List<User> listUsers() {

        List<User> users = repositoryFactory.createUserRepository().findAll();
        if(users.isEmpty()) throw new UserNotFoundException();
        return users;
    }

    @Transactional
    public User addUser(String username, String password, String email) {
        List<User> users;
        try {
            users = listUsers();
        } catch (UserNotFoundException e) {
            return repositoryFactory.createUserRepository().save(new User(username, password, email));
        }
        for(User u : users) {
            if(u.getUsername().equals(username) || u.getEmail().equals(email)){
                throw new UserExistsException();
            }
        }
        return repositoryFactory.createUserRepository().save(new User(username, password, email));
    }

    @Transactional
    public User addUser(User user) { return repositoryFactory.createUserRepository().save(user); }

    @Transactional
    public void removeUser(int id) {
        UserRepository repository = repositoryFactory.createUserRepository();
        User user = repository.findById(id).orElseThrow(UserNotFoundException::new);
        repository.remove(user);
    }

    @Transactional
    public User findById(int id) {
        return repositoryFactory.createUserRepository().findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User findByCredentials(String username, String password) {
        User u = repositoryFactory.createUserRepository().findByCredentials(username,password).orElseThrow(BadCredentialsException::new);
        if(u.getIsBanned()) throw new BannedUserException();
        return u;
    }

    @Transactional
    public User ban(int id, User user) {
        if(!user.getIsAdmin()) throw new NoAdminException();
        User u = findById(id);
        u.setIsBanned(true);
        return u;
    }

    @Transactional
    public User makeAdmin(int id, User user) {
        if(!user.getIsAdmin()) throw new NoAdminException();
        User u = findById(id);
        u.setIsAdmin(true);
        return u;
    }
}
