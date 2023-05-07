package com.thss.androidbackend.repository;

import com.thss.androidbackend.model.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface UserRepository extends MongoRepository<User, String> {
    @Query(value = "{ 'name' : :#{#username} }")
    User findByUserName(String username);

    @Query(value = "{ 'email' : :#{#email} }")
    User findByEmail(String email);

    @Query(value = "{ 'phone' : :#{#phone} }")
    User findByPhone(String phone);

    @Query(value = "{ 'phone' : :#{#phone} }")
    User findByssse(String phone);
}
