package com.thss.androidbackend.repository;

import com.thss.androidbackend.model.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserRepository extends MongoRepository<User, String> {
    @Query
    User findByUsername(String username);

    @Query(value = "{ 'email' : :#{#email} }")
    User findByEmail(String email);

    @Query(value = "{ 'phone' : :#{#phone} }")
    User findByPhone(String phone);

    @Query
    List<User> findAllByUsernameContaining(String keyword);
    @Query
    List<User> findByUsernameContainingIgnoreCase(String keyword);

}
