package edu.brooklyn.cisc3130.taskboard.controller;

import edu.brooklyn.cisc3130.taskboard.dto.TaskRequest;
import edu.brooklyn.cisc3130.taskboard.dto.TaskResponse;
import edu.brooklyn.cisc3130.taskboard.model.Task;
import edu.brooklyn.cisc3130.taskboard.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(TaskResponse.fromEntity(task));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest taskRequest) {

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.getCompleted() != null ? taskRequest.getCompleted() : false);
        task.setDeleted(false);
        task.setPriority(Task.Priority.valueOf(
                taskRequest.getPriority() != null ?
                        taskRequest.getPriority().toUpperCase() : "MEDIUM"));

        Task createdTask = taskService.createTask(task);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TaskResponse.fromEntity(createdTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Integer id,
            @Valid @RequestBody TaskRequest taskRequest) {

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.getCompleted() != null ? taskRequest.getCompleted() : false);
        task.setPriority(Task.Priority.valueOf(
                taskRequest.getPriority() != null ?
                        taskRequest.getPriority().toUpperCase() : "MEDIUM"));

        Task updatedTask = taskService.updateTask(id, task);

        return ResponseEntity.ok(TaskResponse.fromEntity(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<TaskResponse> restoreTask(@PathVariable Integer id) {
        taskService.restoreTask(id);
        Task restoredTask = taskService.getTaskById(id);
        return ResponseEntity.ok(TaskResponse.fromEntity(restoredTask));
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getCompletedTasks() {
        List<Task> tasks = taskService.getCompletedTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/incomplete")
    public ResponseEntity<List<Task>> getIncompleteTasks() {
        List<Task> tasks = taskService.getIncompleteTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(
            @PathVariable String priority) {

        Task.Priority priorityEnum = Task.Priority.valueOf(priority.toUpperCase());
        List<Task> tasks = taskService.getTasksByPriority(priorityEnum);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String keyword) {
        List<Task> tasks = taskService.searchTasks(keyword);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Task>> getTasksPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Task> tasks = taskService.getAllTasks(pageable);

        return ResponseEntity.ok(tasks);
    }
}