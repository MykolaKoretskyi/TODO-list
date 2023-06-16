package ua.shplusplus.restapidevelopment.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "User essence")
public class TaskDTO {

  @Schema(description = "Identifier")
  private Long id;

  @Schema(description = "Task")
  @NotBlank
  private String what;

  @Schema(description = "Execution date")
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate executionDate;

  @Schema(description = "Execution status")
  @NotNull
  @Enumerated(EnumType.STRING)
  private TaskStatus status;

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

  public TaskDTO() {
  }

  public TaskDTO(Long id, String what, LocalDate executionDate, TaskStatus status) {
    this.id = id;
    this.what = what;
    this.executionDate = executionDate;
    this.status = status;
  }

  public TaskDTO(String what, LocalDate executionDate, TaskStatus status) {
    this.what = what;
    this.executionDate = executionDate;
    this.status = status;
  }

  @Override
  public String toString() {
    return "TaskDTO{" +
        "id=" + id +
        ", what='" + what + '\'' +
        ", executionDate=" + executionDate +
        ", status='" + status + '\'' +
        '}';
  }
}
