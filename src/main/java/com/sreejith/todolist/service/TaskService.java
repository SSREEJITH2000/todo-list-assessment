package com.sreejith.todolist.service;

import com.sreejith.todolist.contract.TaskResponse;
import com.sreejith.todolist.exception.ResourceNotFoundException;
import com.sreejith.todolist.model.Task;
import com.sreejith.todolist.repository.TaskRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    public List<TaskResponse> getAllTasksSortedByDueDate() {
        List<Task> tasks = taskRepository.findAll();
        tasks.sort(Comparator.comparing(Task::getDueDate));
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getAllTasksSortedByStatus(TaskResponse.Status status) {
        List<Task> allTasks = taskRepository.findAll();

        List<Task> tasksFilteredByStatus =
                allTasks.stream()
                        .filter(task -> task.getStatus().equals(status.name()))
                        .sorted(Comparator.comparing(Task::getStatus))
                        .toList();

        return tasksFilteredByStatus.stream()
                .map(task -> modelMapper.map(task, TaskResponse.class))
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long id) {
        Task task =
                this.taskRepository
                        .findById(id)
                        .orElseThrow(
                                () -> {
                                    log.error("Book with id: {} not found", id);
                                    return new ResourceNotFoundException(id);
                                });
        return modelMapper.map(task, TaskResponse.class);
    }

    public TaskResponse createTask(TaskResponse task) {
        Task createdTask = taskRepository.save(modelMapper.map(task, Task.class));
        return modelMapper.map(createdTask, TaskResponse.class);
    }

    public TaskResponse updateTask(Long id, TaskResponse task) {
        Task existingTask =
                taskRepository
                        .findById(id)
                        .orElseThrow(
                                () -> {
                                    log.error("Book with id: {} not found", id);
                                    return new ResourceNotFoundException(id);
                                });
        modelMapper.map(task, existingTask);
        Task updatedTask = taskRepository.save(existingTask);
        return modelMapper.map(updatedTask, TaskResponse.class);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
