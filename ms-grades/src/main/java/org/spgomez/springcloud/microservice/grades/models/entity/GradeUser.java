package org.spgomez.springcloud.microservice.grades.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name="grades_users")
public class GradeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", unique = true)
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
//        if(this == obj || (obj instanceof GradeUser && ((GradeUser)obj).getUserId().equals(this.userId))){
        if(this == obj){
            return true;
        }
        if(!(obj instanceof GradeUser o)){
            return false;
        }
        return this.userId != null && this.userId.equals(o.getUserId());
    }
}
