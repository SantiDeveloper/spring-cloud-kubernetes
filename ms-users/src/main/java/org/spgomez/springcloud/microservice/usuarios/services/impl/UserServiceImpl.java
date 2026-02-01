package org.spgomez.springcloud.microservice.usuarios.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.spgomez.springcloud.microservice.usuarios.clients.GradeUserRest;
import org.spgomez.springcloud.microservice.usuarios.repositories.UserRepository;
import org.spgomez.springcloud.microservice.usuarios.services.UserService;
import org.spgomez.springcloud.microservice.usuarios.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    /*private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());*/

    @Autowired
    private UserRepository repository;

    @Autowired
    private GradeUserRest client;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> finAllByIds(Iterable<Long> ids) {
        return (List<User>) repository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return repository.queryByEmail(email);
//        return repository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) {

        /*try {
            String json = objectMapper.writeValueAsString(user);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }*/

        return repository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        repository.deleteById(id);
        client.removerGradeUserById(id);

    }
}
