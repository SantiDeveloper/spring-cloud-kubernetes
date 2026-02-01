package org.spgomez.springcloud.microservice.grades.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.spgomez.springcloud.microservice.grades.models.User;
import org.spgomez.springcloud.microservice.grades.services.GradeService;
import org.spgomez.springcloud.microservice.grades.models.entity.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@RestController("/api/v1/grade")
@RestController
@RequestMapping("/api/v1/grade")
public class GradeController {

    @Autowired
    private GradeService service;

    @GetMapping
    private ResponseEntity<List<Grade>> getAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Grade> gradeOpt = service.findByIdExtends(id);
        if(gradeOpt.isPresent()){
            return ResponseEntity.ok(gradeOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Grade grade, BindingResult result){
        if(result.hasErrors()){
            return validateErrors(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(grade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Grade grade, @PathVariable Long id, BindingResult result){
        if(result.hasErrors()){
            return validateErrors(result);
        }
        Optional<Grade> gradeOpt = service.findById(id);
        if(gradeOpt.isPresent()){
            Grade gradeBD = gradeOpt.get();
            gradeBD.setName(grade.getName());
            return ResponseEntity.status(HttpStatus.OK).body(service.save(gradeBD));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Grade> userDb = service.findById(id);
        if(userDb.isPresent()){
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/user/{gradeId}")
    public ResponseEntity<?> addUserToGrade(@RequestBody User user, @PathVariable Long gradeId) {
        Optional<User> opt;

        try {
            opt = service.addUserToGrade(user, gradeId);
        } catch (FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "No existe el usuario a asignar o ha ocurrido" +
                            " un error en la comunicación: " + e.getMessage()));
        }
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{gradeId}")
    public ResponseEntity<?> addNewUserToGrade(@RequestBody User user, @PathVariable Long gradeId) {
        Optional<User> opt;

        try {
            opt = service.addNewUserToGrade(user, gradeId);
        } catch (FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "No se pudo crear el usuario o ha ocurrido " +
                            "un error en la comunicación: " + e.getMessage()));
        }
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/{gradeId}")
    public ResponseEntity<?> removeUserFromGrade(@RequestBody User user, @PathVariable Long gradeId) {
        Optional<User> opt;

        try {
            opt = service.removeUserFromGrade(user, gradeId);
        } catch (FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "No existe el usuario a desasignar o ha ocurrido" +
                            " un error en la comunicación: " + e.getMessage()));
        }
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/grade/{id}")
    public ResponseEntity<?> removerGradeUserById(@PathVariable Long id){
        service.deleteGradeUserById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> validateErrors (BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
