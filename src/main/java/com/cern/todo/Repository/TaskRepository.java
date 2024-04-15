package com.cern.todo.Repository;

import com.cern.todo.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatusIn(List<Integer> statuses);
}
