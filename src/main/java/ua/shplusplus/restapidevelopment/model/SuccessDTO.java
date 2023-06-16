package ua.shplusplus.restapidevelopment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class SuccessDTO {

  @Schema(description = "Current date and time")
  private LocalDateTime dateTime;
  @Schema(description = "Request status code")
  private int code;
  @Schema(description = "Request status message")
  private String status;
  @Schema(description = "Notification of request execution")
  private String message;
  @Schema(description = "Request path")
  private String path;

  public SuccessDTO() {
  }

  public SuccessDTO(LocalDateTime dateTime,
      HttpStatus httpStatus,
      String message,
      String path) {

    this.dateTime = dateTime;
    this.code = httpStatus.value();
    this.status = httpStatus.name();
    this.message = message;
    this.path = path;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public String toString() {
    return "Success{" +
        "dateTime=" + dateTime +
        ", code=" + code +
        ", status='" + status + '\'' +
        ", message='" + message + '\'' +
        ", path='" + path + '\'' +
        '}';
  }
}
