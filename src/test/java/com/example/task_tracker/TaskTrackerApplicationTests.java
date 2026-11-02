package com.example.task_tracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class TaskTrackerApplicationTests {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	com.example.task_tracker.repository.TaskRepository taskRepository;

	@BeforeEach
	void setUp() {
	    taskRepository.clearAll();
	}

	@Test
	void contextLoads() {
	}

	@Test 
	void healthEndpointReturnsUp () throws Exception {
		mockMvc.perform(get("/health"))
				.andExpect(status().isOk())
				.andExpect(content().string("UP"));
	}

	@Test
	void getAllTasksReturnsEmptyList() throws Exception {
		mockMvc.perform(get("/api/tasks"))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	void createTaskReturnsCreatedStatus() throws Exception {
		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
							"title" : "Learn Spring Boot",
							"description" : "Practice MockMvc",
							"completed" : false
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.title").value("Learn Spring Boot"))
				.andExpect(jsonPath("$.description").value("Practice MockMvc"))
				.andExpect(jsonPath("$.completed").value(false));	
	}
	@Test
	void getTaskByIdReturnsTask() throws Exception {
	    String response = mockMvc.perform(post("/api/tasks")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("""
	                    {
	                        "title": "Find my task",
	                        "description": "Practice GET by ID",
	                        "completed": false
	                    }
	                    """))
	            .andExpect(status().isCreated())
	            .andReturn()
	            .getResponse()
	            .getContentAsString();

		String id = tools.jackson.databind.json.JsonMapper
		        .builder()
		        .build()
		        .readTree(response)
		        .get("id")
		        .asText();

	    mockMvc.perform(get("/api/tasks/" + id))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.title").value("Find my task"))
	            .andExpect(jsonPath("$.description").value("Practice GET by ID"));
	}
	@Test
	void updateTaskReturnsUpdatedTask() throws Exception {
	    mockMvc.perform(post("/api/tasks")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("""
	                    {
	                        "title": "Old title",
	                        "description": "Old description",
	                        "completed": false
	                    }
	                    """))
	            .andExpect(status().isCreated());

	    mockMvc.perform(put("/api/tasks/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("""
	                    {
	                        "title": "Updated title",
	                        "description": "Updated description",
	                        "completed": true
	                    }
	                    """))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.title").value("Updated title"))
	            .andExpect(jsonPath("$.description").value("Updated description"))
	            .andExpect(jsonPath("$.completed").value(true));
	}

	@Test
	void deleteTaskReturnsNoContent() throws Exception {
	    mockMvc.perform(post("/api/tasks")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("""
	                    {
	                        "title": "Task to delete",
	                        "description": "Temporary task",
	                        "completed": false
	                    }
	                    """))
	            .andExpect(status().isCreated());

	    mockMvc.perform(delete("/api/tasks/1"))
	            .andExpect(status().isNoContent());

	    mockMvc.perform(get("/api/tasks/1"))
	            .andExpect(status().isNotFound());
	}
	@Test
	void missingTaskReturnsNotFound() throws Exception {
	    mockMvc.perform(get("/api/tasks/999"))
	            .andExpect(status().isNotFound());

	    mockMvc.perform(put("/api/tasks/999")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("""
	                    {
	                        "title": "Missing task",
	                        "description": "Does not exist",
	                        "completed": false
	                    }
	                    """))
	            .andExpect(status().isNotFound());

	    mockMvc.perform(delete("/api/tasks/999"))
	            .andExpect(status().isNotFound());
	}

}
