package com.cezaram28.Assignment1.service;

import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.exception.UserNotFoundException;
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
        return repositoryFactory.createUserRepository().findAll();
    }

    @Transactional
    public User addUser(String username, String password, String email) {
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
    public Optional<User> findById(int id) {
        return repositoryFactory.createUserRepository().findById(id);
    }

    @Transactional
    public Optional<User> findByCredentials(String username, String password) {
        return repositoryFactory.createUserRepository().findByCredentials(username,password);
    }
}
