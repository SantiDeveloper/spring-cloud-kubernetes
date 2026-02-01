package org.spgomez.springcloud.microservice.grades.services.impl;

import org.spgomez.springcloud.microservice.grades.clients.UserClientRest;
import org.spgomez.springcloud.microservice.grades.models.User;
import org.spgomez.springcloud.microservice.grades.models.entity.Grade;
import org.spgomez.springcloud.microservice.grades.models.entity.GradeUser;
import org.spgomez.springcloud.microservice.grades.repository.GradeRepository;
import org.spgomez.springcloud.microservice.grades.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository repository;

    @Autowired
    private UserClientRest userclient;

    @Override
    @Transactional(readOnly = true)
    public List<Grade> findAll() {
        return (List<Grade>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Grade> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Grade save(Grade grade) {
        return repository.save(grade);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteGradeUserById(Long id) {
        repository.removeGradeUserFromId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Grade> findByIdExtends(Long id) {
        Optional<Grade> opt = repository.findById(id);
        if(opt.isPresent()){
            Grade grade = opt.get();
            if(!grade.getGradeUsers().isEmpty()){
                List<Long> ids = grade.getGradeUsers().stream().map(GradeUser::getUserId).toList();
//                List<Long> ids = grade.getGradeUsers().stream().map(gradeUser -> gradeUser.getUserId()).collect(Collectors.toList());
                List<User> users = userclient.findAllByIds(ids);
                grade.setUsers(users);
            }
            return Optional.of(grade);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> addUserToGrade(User user, Long gradeId) {
        Optional<Grade> opt = repository.findById(gradeId);
        if(opt.isPresent()){
            User userMs = userclient.finById(user.getId());

            Grade grade = opt.get();
            GradeUser gradeUser = new GradeUser();
            gradeUser.setUserId(userMs.getId());

            grade.addGradeUser(gradeUser);
            repository.save(grade);
            return Optional.of(userMs);

        }
        return Optional.empty();
    }

    @Override
    public Optional<User> addNewUserToGrade(User user, Long gradeId) {
        Optional<Grade> opt = repository.findById(gradeId);
        if(opt.isPresent()){
            User userNewMs = userclient.create(user);

            Grade grade = opt.get();
            GradeUser gradeUser = new GradeUser();
            gradeUser.setUserId(userNewMs.getId());

            grade.addGradeUser(gradeUser);
            repository.save(grade);
            return Optional.of(userNewMs);

        }
        return Optional.empty();
    }

    @Override
    public Optional<User> removeUserFromGrade(User user, Long gradeId) {
        Optional<Grade> opt = repository.findById(gradeId);
        if(opt.isPresent()){
            User userMs = userclient.finById(user.getId());

            Grade grade = opt.get();
            GradeUser gradeUser = new GradeUser();
            gradeUser.setUserId(userMs.getId());

            grade.removeGradeUser(gradeUser);
            repository.save(grade);
            return Optional.of(userMs);

        }
        return Optional.empty();
    }
}
