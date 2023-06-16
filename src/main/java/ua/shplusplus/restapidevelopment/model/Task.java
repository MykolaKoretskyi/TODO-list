package ua.shplusplus.restapidevelopment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "todo_list")
public class Task {

  @Schema(description = "Identifier")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Schema(description = "Task")
  @Column(name = "what")
  private String what;

  @Schema(description = "Execution date")
  @Column(name = "execution_date")
  private LocalDate executionDate;

  @Schema(description = "Execution status")
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  public Task() {
  }

  public Task(Long id, String what, LocalDate executionDate, TaskStatus status) {
    this.id = id;
    this.what = what;
    this.executionDate = executionDate;
    this.status = status;
  }

  public Task(String what, LocalDate executionDate, TaskStatus status) {
    this.what = what;
    this.executionDate = executionDate;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getWhat() {
    return what;
  }

  public void setWhat(String what) {
    this.what = what;
  }

  public LocalDate getExecutionDate() {
    return executionDate;
  }

  public void setExecutionDate(LocalDate executionDate) {
    this.executionDate = executionDate;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "Task{" +
        "id=" + id +
        ", what='" + what + '\'' +
        ", executionDate=" + executionDate +
        ", status='" + status + '\'' +
        '}';
  }
}
