package com.yuvaintern.taskmanagement.service;

import com.yuvaintern.taskmanagement.dto.*;
import com.yuvaintern.taskmanagement.entity.*;
import com.yuvaintern.taskmanagement.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock TaskRepository taskRepository;
    @Mock UserRepository userRepository;
    @InjectMocks TaskService taskService;

    @Test
    void createsTaskForAuthenticatedOwner() {
        AppUser user = new AppUser("user@example.com", "hash", Role.USER);
        Task saved = new Task("Write tests", "Week 4", TaskStatus.TODO, user);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskResponse response = taskService.create("user@example.com",
            new TaskRequest("Write tests", "Week 4", TaskStatus.TODO));

        assertEquals("Write tests", response.title());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void returnsPaginatedTasks() {
        AppUser user = new AppUser("user@example.com", "hash", Role.USER);
        Task task = new Task("A", "B", TaskStatus.TODO, user);
        Page<Task> page = new PageImpl<>(List.of(task), PageRequest.of(0, 10), 1);
        when(taskRepository.findAllByOwner_Email(eq("user@example.com"), any(Pageable.class)))
            .thenReturn(page);

        PageResponse<TaskResponse> result =
            taskService.getMyTasks("user@example.com", 0, 10, "createdAt", "DESC", null);

        assertEquals(1, result.totalElements());
        assertEquals(1, result.content().size());
    }

    @Test
    void rejectsTooLargePageSize() {
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
            () -> taskService.getMyTasks("user@example.com", 0, 101, "createdAt", "DESC", null));
    }

    @Test
    void rejectsUnsupportedSortColumn() {
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
            () -> taskService.getMyTasks("user@example.com", 0, 10, "password", "ASC", null));
    }
}
