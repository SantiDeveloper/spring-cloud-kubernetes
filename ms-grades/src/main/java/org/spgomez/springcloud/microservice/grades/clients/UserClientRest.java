package org.spgomez.springcloud.microservice.grades.clients;

import org.spgomez.springcloud.microservice.grades.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="ms-users", url="${ms.users.url}")
public interface UserClientRest {

    @GetMapping("/{id}")
    public User finById(@PathVariable Long id);

    @PostMapping
    public User create (@RequestBody User user);

    @GetMapping
    public List<User> findAllByIds(@RequestParam(required = false) Iterable<Long> ids);
}
