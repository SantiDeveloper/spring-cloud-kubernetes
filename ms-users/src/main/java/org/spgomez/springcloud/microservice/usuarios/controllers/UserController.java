package org.spgomez.springcloud.microservice.usuarios.controllers;

import jakarta.validation.Valid;
import org.spgomez.springcloud.microservice.usuarios.models.entity.User;
import org.spgomez.springcloud.microservice.usuarios.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@RestController("/api/v1/user")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) List<Long> ids) {
        if(ids !=null && !ids.isEmpty()){
            return ResponseEntity.ok(service.finAllByIds(ids));
        }
        //Collections.singletonMap("users", service.findAll());
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<User> userOpt = service.findById(id);
        if(userOpt.isPresent()){
            return ResponseEntity.ok(userOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result){
        if(result.hasErrors()){
            return validateErrors(result);
        }

        if(!user.getEmail().isEmpty() && service.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico."));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody User user, @PathVariable Long id, BindingResult result){
        if(result.hasErrors()){
            return validateErrors(result);
        }
        Optional<User> userOpt = service.findById(id);
        if(userOpt.isPresent()){
            User userDb = userOpt.get();
            if(!user.getEmail().equalsIgnoreCase(userDb.getEmail()) && service.findByEmail(user.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico."));
            }
            userDb.setName(user.getName());
            userDb.setEmail(user.getEmail());
            userDb.setPassword(user.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(service.save(userDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<User> userDb = service.findById(id);
        if(userDb.isPresent()){
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    private ResponseEntity<?> validateErrors (BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}