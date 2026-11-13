package com.yuvaintern.taskmanagement.repository;

import com.yuvaintern.taskmanagement.entity.Task;
import com.yuvaintern.taskmanagement.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByOwner_Email(String email, Pageable pageable);
    Page<Task> findAllByOwner_EmailAndStatus(String email, TaskStatus status, Pageable pageable);
    Optional<Task> findByIdAndOwner_Email(Long id, String email);
    long countByOwner_Email(String email);
}
