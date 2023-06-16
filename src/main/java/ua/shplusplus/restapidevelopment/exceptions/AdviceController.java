package ua.shplusplus.restapidevelopment.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AdviceController {

  private static final Logger logger = LoggerFactory.getLogger(AdviceController.class);

  @ExceptionHandler(BindException.class)
  @ResponseBody
  public ResponseEntity<ErrorDto> handleBindExceptionHandler(BindException ex) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
    logger.error(bundle.getString("validError"), errors);

    return new ResponseEntity<>(
        new ErrorDto(
            LocalDateTime.now(), HttpStatus.BAD_REQUEST,
            ex.getAllErrors().toString()), HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(JsonMappingException.class)
  public ResponseEntity<ErrorDto> handleException(JsonMappingException e) {

    return new ResponseEntity<>(new ErrorDto(
        LocalDateTime.now(), HttpStatus.BAD_REQUEST,
        e.getOriginalMessage()), HttpStatus.BAD_REQUEST);
  }
}