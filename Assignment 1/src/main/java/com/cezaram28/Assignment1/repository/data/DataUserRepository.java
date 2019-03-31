package com.cezaram28.Assignment1.repository.data;

import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.repository.UserRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DataUserRepository extends Repository<User, Integer>, UserRepository {

    void delete(User user);

    @Override
    default void remove(User user) {
        delete(user);
    }

    Optional<User> findByUsernameAndPassword(String username, String password);

    @Override
    default Optional<User> findByCredentials(String username, String password) {
        return findByUsernameAndPassword(username, password);
    }
}
