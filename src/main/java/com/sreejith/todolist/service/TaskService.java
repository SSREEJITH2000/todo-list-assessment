package com.sreejith.todolist.service;

import com.sreejith.todolist.contract.TaskResponse;
import com.sreejith.todolist.exception.ResourceNotFoundException;
import com.sreejith.todolist.model.Task;
import com.sreejith.todolist.repository.TaskRepository;
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

    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = this.taskRepository.findAll();
        return tasks.stream()
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
