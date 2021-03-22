package com.sinolec.challenge.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface GeneralTaskRepository extends JpaRepository<GeneralTask, String>, JpaSpecificationExecutor<GeneralTask> {

    @Modifying
    @Query("delete from generalTask task where task.isExecuted = false and task.creationDate < :providedDate")
    Integer deleteNonExecutedTasksOlderThanProvidedDate(@Param("providedDate") Date providedDate);
}
