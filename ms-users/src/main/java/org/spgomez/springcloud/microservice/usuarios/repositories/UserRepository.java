package org.spgomez.springcloud.microservice.usuarios.repositories;

import org.spgomez.springcloud.microservice.usuarios.models.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email=?1")
    Optional<User> queryByEmail(String email);

    boolean existsByEmail(String email);
}
