package org.spgomez.springcloud.microservice.usuarios.services;

import org.spgomez.springcloud.microservice.usuarios.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    List<User> finAllByIds(Iterable<Long> ids);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
    void delete(Long id);

}
