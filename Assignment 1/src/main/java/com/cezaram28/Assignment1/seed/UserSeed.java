package com.cezaram28.Assignment1.seed;

import com.cezaram28.Assignment1.entity.User;
import com.cezaram28.Assignment1.repository.RepositoryFactory;
import com.cezaram28.Assignment1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "a1.repository-type", havingValue = "MEMORY")
public class UserSeed implements CommandLineRunner {

    private final RepositoryFactory repositoryFactory;

    @Override
    public void run(String... args) throws Exception {
        UserRepository userRepository = repositoryFactory.createUserRepository();
        if(userRepository.findAll().isEmpty()){
            userRepository.save(new User(null, "user1", "pass1", "email", 0, true, false));
            userRepository.save(new User(null, "user2", "pass2", "email", 0, false, false));
            userRepository.save(new User(null, "user3", "pass3", "email", 0, true, true));
        }
    }
}
