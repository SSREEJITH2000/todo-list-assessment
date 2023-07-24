package com.sreejith.todolist.contract;

import com.sreejith.todolist.validation.ValidDate;
import com.sreejith.todolist.validation.ValidName;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;

    @ValidName private String name;

    @NotBlank(message = "Description can't be empty")
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ValidDate private LocalDate dueDate;

    public enum Status {
        COMPLETED,
        PENDING,
        IN_PROGRESS
    }
}
