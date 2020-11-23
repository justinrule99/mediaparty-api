package com.mediaparty.api.repositories;

import java.util.List;

import com.mediaparty.api.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{

    User findById(long id);
    User findByUsername(String username);

    User deleteByUsername(String username);
}
