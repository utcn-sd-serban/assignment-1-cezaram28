package com.cezaram28.Assignment1.repository.memory;

import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements UserRepository{
    private final Map<Integer, User> data = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null){
            user.setId(currentId.incrementAndGet());
        }
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public void remove(User user) {
        data.remove(user.getId());
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> findByCredentials(String username, String password) {
        for(User user:data.values()){
            if(user.getUsername().equals(username)&&user.getPassword().equals(password))
                return Optional.ofNullable(user);
        }
        return Optional.empty();
    }
}
