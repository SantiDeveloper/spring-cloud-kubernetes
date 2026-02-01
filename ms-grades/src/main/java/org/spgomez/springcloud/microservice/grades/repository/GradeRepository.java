package org.spgomez.springcloud.microservice.grades.repository;

import org.spgomez.springcloud.microservice.grades.models.entity.Grade;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GradeRepository extends CrudRepository<Grade,Long> {

    @Modifying
    @Query("delete from GradeUser gu where gu.userId=?1")
    void removeGradeUserFromId(Long id);
}
