package com.yuvaintern.taskmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuvaintern.taskmanagement.dto.*;
import com.yuvaintern.taskmanagement.entity.Role;
import com.yuvaintern.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import com.yuvaintern.taskmanagement.repository.TaskRepository;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskManagementIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired
private TaskRepository taskRepository;
@AfterEach
void clean() {
    taskRepository.deleteAll();
    userRepository.deleteAll();
}

    private String registerAndGetToken(String email) throws Exception {
        RegisterRequest request = new RegisterRequest(email, "StrongPass123");
        String json = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(json).get("token").asText();
    }

    @Test
    void protectedTasksRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/tasks"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void userCanCreateAndListOwnTasks() throws Exception {
        String token = registerAndGetToken("owner@example.com");
        TaskRequest request = new TaskRequest("Prepare Week 3", "External API and tests", null);

        mockMvc.perform(post("/api/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Prepare Week 3"));

        mockMvc.perform(get("/api/tasks?page=0&size=5&sortBy=createdAt&direction=DESC")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void userCannotAccessAdminEndpoint() throws Exception {
        String token = registerAndGetToken("regular@example.com");
        mockMvc.perform(get("/api/admin/health")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isForbidden());
    }

    @Test
    void registrationRejectsWeakPassword() throws Exception {
        RegisterRequest request = new RegisterRequest("weak@example.com", "123");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
