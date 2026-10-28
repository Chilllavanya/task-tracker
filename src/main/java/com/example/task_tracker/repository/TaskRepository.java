package com.example.task_tracker.repository;

import com.example.task_tracker.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class TaskRepository {

    private final List<Task> tasks = new ArrayList<>();
    private Long nextId = 1L;

    public Task save (Task task) {
        if (task.getId() == null) {
            task.setId(nextId++);
        }
        tasks.add(task);
        return task;
    }

    public List<Task> findAll () {
        return new ArrayList<> (tasks) ;
    }
    public Optional<Task> findById(Long id) {
        return tasks.stream()
                .filter (task -> task.getId().equals(id))
                .findFirst();
    }
    public boolean deleteById(Long id) {
        return tasks.removeIf(task-> task.getId().equals(id));
    }
    public boolean existsById(Long id) {
        return tasks.stream()
                .anyMatch(task -> task.getId().equals(id));
    }
}