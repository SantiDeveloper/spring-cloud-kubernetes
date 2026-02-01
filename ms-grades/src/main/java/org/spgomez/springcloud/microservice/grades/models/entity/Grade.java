package org.spgomez.springcloud.microservice.grades.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.spgomez.springcloud.microservice.grades.models.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private List<GradeUser> gradeUsers;

    @Transient
    private List<User> users;

    public Grade() {
        gradeUsers = new ArrayList<>();
        users = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGradeUser(GradeUser gradeUser){
        gradeUsers.add(gradeUser);
    }

    public void removeGradeUser(GradeUser gradeUser){
        gradeUsers.remove(gradeUser);
    }

    public List<GradeUser> getGradeUsers() {
        return gradeUsers;
    }

    public void setGradeUsers(List<GradeUser> gradeUsers) {
        this.gradeUsers = gradeUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
