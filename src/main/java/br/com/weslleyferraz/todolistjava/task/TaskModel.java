package br.com.weslleyferraz.todolistjava.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private UUID userId;
    @Column(length = 100)
    private String title;
    @Column(length = 1000)
    private String description;
    private String priority;
    private String startAt;
    private String endAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
