package com.example.task_tracker.service;

import com.example.task_tracker.model.Task;
import com.example.task_tracker.repository.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    @Test
    void getTaskByIdReturnsTaskWhenFound() {

        // Arrange
        Task task = new Task(1L, "Learn Mockito", "Practice mocks", false);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // Act
        Optional<Task> result = taskService.getTaskById(1L);
        verify (taskRepository).findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Learn Mockito", result.get().getTitle());
    }
    @Test
    void getTaskByIdReturnsEmptyWhenTaskNotFound() {

        // Arrange
        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act
        Optional<Task> result = taskService.getTaskById(99L);

        // Assert
        assertTrue(result.isEmpty());
    }
    @Test
    void createTaskReturnsSavedTask() {

        // Arrange
        Task task = new Task(null, "Learn Mockito", "Practice save", false);

        when(taskRepository.save(task))
                .thenReturn(task);

        // Act
        Task result = taskService.createTask(task);

        // Assert
        assertEquals("Learn Mockito", result.getTitle());
        verify(taskRepository).save(task);
    }
    @Test
    void updateTaskReturnsUpdatedTaskWhenFound() {

        // Arrange
        Task existingTask = new Task(
                1L, "Old title", "Old description", false);

        Task updatedTask = new Task(
                1L, "New title", "New description", true);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        // Act
        Optional<Task> result =
                taskService.updateTask(1L, updatedTask);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("New title", result.get().getTitle());
        assertEquals("New description", result.get().getDescription());
        assertTrue(result.get().isCompleted());
        verify(taskRepository).findById(1L);
    }

    @Test
    void updateTaskReturnsEmptyWhenTaskNotFound() {

        // Arrange
        Task updatedTask = new Task(
                99L, "New title", "New description", true);

        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act
        Optional<Task> result =
                taskService.updateTask(99L, updatedTask);

        // Assert
        assertTrue(result.isEmpty());

        verify(taskRepository).findById(99L);
    }

    @Test
    void deleteTaskReturnsTrueWhenTaskDeleted() {

        // Arrange
        when(taskRepository.deleteById(1L))
                .thenReturn(true);

        // Act
        boolean result = taskService.deleteTask(1L);

        // Assert
        assertTrue(result);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTaskReturnsFalseWhenTaskNotFound() {

        // Arrange
        when(taskRepository.deleteById(99L))
                .thenReturn(false);

        // Act
        boolean result = taskService.deleteTask(99L);

        // Assert
        assertFalse(result);

        verify(taskRepository).deleteById(99L);
    }
}