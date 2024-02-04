package ua.shplusplus.restapidevelopment.exceptions;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class ErrorDto {

  private LocalDateTime dateTime;
  private int code;
  private String status;
  private String message;


  public ErrorDto(LocalDateTime dateTime, HttpStatus httpStatus, String message) {

    this.dateTime = dateTime;
    this.code = httpStatus.value();
    this.status = httpStatus.name();
    this.message = message;
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

  @Override
  public String toString() {
    return "Error: {" +
        "dateTime=" + dateTime +
        ", code=" + code +
        ", status='" + status + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}
