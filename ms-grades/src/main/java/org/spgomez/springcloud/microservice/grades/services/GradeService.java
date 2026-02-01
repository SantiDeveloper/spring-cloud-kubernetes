package org.spgomez.springcloud.microservice.grades.services;

import org.spgomez.springcloud.microservice.grades.models.User;
import org.spgomez.springcloud.microservice.grades.models.entity.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeService {

    List<Grade> findAll();
    Optional<Grade> findById(Long id);
    Grade save(Grade grade);
    void delete (Long id);
    void deleteGradeUserById(Long id);
    Optional<Grade> findByIdExtends(Long id);

    Optional<User> addUserToGrade(User user, Long gradeId);
    Optional<User> addNewUserToGrade(User user, Long gradeId);
    Optional<User> removeUserFromGrade(User user, Long gradeId);

}
