package com.yuvaintern.taskmanagement.service;

import com.yuvaintern.taskmanagement.dto.*;
import com.yuvaintern.taskmanagement.entity.*;
import com.yuvaintern.taskmanagement.repository.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {
    private final TaskRepository tasks;
    private final UserRepository users;

    public TaskService(TaskRepository tasks, UserRepository users) {
        this.tasks = tasks; this.users = users;
    }

    @Transactional
    public TaskResponse create(String email, TaskRequest request) {
        AppUser owner = users.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Task task = new Task(request.title().trim(), request.description(),
            request.status() == null ? TaskStatus.TODO : request.status(), owner);
        return TaskResponse.from(tasks.save(task));
    }

    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> getMyTasks(String email, int page, int size,
                                                  String sortBy, String direction, TaskStatus status) {
        validatePage(page, size, sortBy, direction);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> result = status == null
            ? tasks.findAllByOwner_Email(email, pageable)
            : tasks.findAllByOwner_EmailAndStatus(email, status, pageable);
        return PageResponse.from(result.map(TaskResponse::from));
    }

    @Transactional(readOnly = true)
    public TaskResponse getById(String email, Long id) {
        Task task = tasks.findByIdAndOwner_Email(id, email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse update(String email, Long id, TaskRequest request) {
        Task task = tasks.findByIdAndOwner_Email(id, email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        task.setTitle(request.title().trim());
        task.setDescription(request.description());
        if (request.status() != null) task.setStatus(request.status());
        return TaskResponse.from(tasks.save(task));
    }

    @Transactional
    public void delete(String email, Long id) {
        Task task = tasks.findByIdAndOwner_Email(id, email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        tasks.delete(task);
    }

    private void validatePage(int page, int size, String sortBy, String direction) {
        if (page < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page must be zero or greater");
        if (size < 1 || size > 100) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size must be between 1 and 100");
        if (!java.util.Set.of("id", "title", "status", "createdAt", "updatedAt").contains(sortBy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported sort field");
        }
        if (!direction.equalsIgnoreCase("ASC") && !direction.equalsIgnoreCase("DESC")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Direction must be ASC or DESC");
        }
    }
}
