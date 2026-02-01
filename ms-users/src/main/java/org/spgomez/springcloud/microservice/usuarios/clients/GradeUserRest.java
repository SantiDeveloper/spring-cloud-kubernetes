package org.spgomez.springcloud.microservice.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ms-grades", url="${ms.grades.url}")
public interface GradeUserRest {

    @DeleteMapping("/user/grade/{id}")
    void removerGradeUserById(@PathVariable Long id);
}
