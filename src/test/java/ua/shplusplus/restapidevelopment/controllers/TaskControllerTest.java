package ua.shplusplus.restapidevelopment.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import ua.shplusplus.restapidevelopment.entities.Task;
import ua.shplusplus.restapidevelopment.entities.TaskDTO;
import ua.shplusplus.restapidevelopment.entities.TaskStatus;
import ua.shplusplus.restapidevelopment.exceptions.RequestException;
import ua.shplusplus.restapidevelopment.repositories.TaskRepository;
import ua.shplusplus.restapidevelopment.services.TaskService;


class TaskControllerTest {

  @BeforeAll
  static void setup() {
    Locale.setDefault(Locale.ENGLISH);
  }

  @Mock
  TaskRepository taskRepository;
  @InjectMocks
  TaskService taskService;
  @Mock
  BindingResult bindingResult;
  AutoCloseable closeable;

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
  }

  @After(value = "")
  void releaseMocks() throws Exception {
    closeable.close();
  }

  final Logger logger = LoggerFactory.getLogger(TaskControllerTest.class);

  /**
   * Checks the updateTask(...) method.
   */
  @Test
  void updateTaskPlannedToInProgress() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToInProgress = controller.updateTask(
        1L, buildTaskDTO(TaskStatus.WORK_IN_PROGRESS), bindingResult, Locale.ENGLISH).getStatus();

    TaskStatus expectedInProgress = TaskStatus.WORK_IN_PROGRESS;

    assertEquals(actualPlannedToInProgress, expectedInProgress);
  }

  /**
   * Checks the updateTask(...) method.
   */
  @Test
  void updateTaskPlannedToPostponed() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToPostponed = controller.updateTask(
        1L, buildTaskDTO(TaskStatus.POSTPONED), bindingResult, Locale.ENGLISH).getStatus();

    TaskStatus expectedPostponed = TaskStatus.POSTPONED;

    assertEquals(actualPlannedToPostponed, expectedPostponed);
  }

  /**
   * Checks the updateTask(...) method.
   */
  @Test
  void updateTaskPlannedToNotified() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToNotified = controller.updateTask(
        1L, buildTaskDTO(TaskStatus.NOTIFIED), bindingResult, Locale.ENGLISH).getStatus();

    TaskStatus expectedNotified = TaskStatus.NOTIFIED;

    assertEquals(actualPlannedToNotified, expectedNotified);
  }

  /**
   * Checks the updateTask(...) method.
   */
  @Test
  void updateTaskPlannedToSigned() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToSigned = controller.updateTask(
        1L, buildTaskDTO(TaskStatus.SIGNED), bindingResult, Locale.ENGLISH).getStatus();

    TaskStatus expectedSigned = TaskStatus.SIGNED;

    assertEquals(actualPlannedToSigned, expectedSigned);
  }

  /**
   * Checks the updateTask(...) method.
   */
  @Test
  void updateTaskPlannedToDone() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToDone = controller.updateTask(
        1L, buildTaskDTO(TaskStatus.DONE), bindingResult, Locale.ENGLISH).getStatus();

    TaskStatus expectedDone = TaskStatus.DONE;

    assertEquals(actualPlannedToDone, expectedDone);
  }

  /**
   * Checks the updateTask(...) method.
   */
  @Test
  void updateTaskPlannedToCancelled() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToCancelled = controller.updateTask(
        1L, buildTaskDTO(TaskStatus.CANCELLED), bindingResult, Locale.ENGLISH).getStatus();

    TaskStatus expectedCancelled = TaskStatus.CANCELLED;

    assertEquals(actualPlannedToCancelled, expectedCancelled);
  }

  /**
   * Checks the updateTask(...) method.
   */
  @Test
  void updateTaskSignedToDone() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.SIGNED)));

    TaskStatus actualPlannedToCancelled = controller.updateTask(
        1L, buildTaskDTO(TaskStatus.DONE), bindingResult, Locale.ENGLISH).getStatus();

    TaskStatus expectedDone = TaskStatus.DONE;

    assertEquals(actualPlannedToCancelled, expectedDone);
  }

  /**
   * Checks for an exception in the updateTask(...) method.
   */
  @Test
  void updateTaskInProgressToPostponed() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(
        Optional.of(buildTask(TaskStatus.WORK_IN_PROGRESS)));

    HttpStatus httpStatus = HttpStatus.OK;
    try {
      controller.updateTask(1L, buildTaskDTO(TaskStatus.POSTPONED),
          bindingResult, Locale.ENGLISH);

    } catch (RequestException e) {
      httpStatus = e.getStatus();
      logger.info("Test controller. {}", e.getMessage());
    }
    assertEquals(HttpStatus.BAD_REQUEST, httpStatus);
  }

  /**
   * Checks for an exception in the updateTask(...) method.
   */
  @Test
  void updateTaskNotifiedToSigned() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(
        Optional.of(buildTask(TaskStatus.NOTIFIED)));

    HttpStatus httpStatus = HttpStatus.OK;
    try {
      controller.updateTask(1L, buildTaskDTO(TaskStatus.SIGNED), bindingResult, Locale.ENGLISH);

    } catch (RequestException e) {
      httpStatus = e.getStatus();
      logger.info("Test controller. {}", e.getMessage());
    }
    assertEquals(HttpStatus.BAD_REQUEST, httpStatus);
  }

  /**
   * Checks for an exception in the updateTask(...) method.
   */
  @Test
  void updateTaskDoneToCancelled() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.DONE)));

    HttpStatus httpStatus = HttpStatus.OK;
    try {
      controller.updateTask(1L, buildTaskDTO(TaskStatus.CANCELLED), bindingResult, Locale.ENGLISH);

    } catch (RequestException e) {
      httpStatus = e.getStatus();
      logger.info("Test controller. {}", e.getMessage());
    }
    assertEquals(HttpStatus.BAD_REQUEST, httpStatus);
  }

  /**
   * Checks for an exception in the updateTask(...) method.
   */
  @Test
  void updateTaskCancelledToInProgress() {

    TaskController controller = new TaskController(taskService);

    when(taskRepository.findById(anyLong())).thenReturn(
        Optional.of(buildTask(TaskStatus.CANCELLED)));

    HttpStatus httpStatus = HttpStatus.OK;
    try {
      controller.updateTask(1L, buildTaskDTO(TaskStatus.WORK_IN_PROGRESS), bindingResult,
          Locale.ENGLISH);

    } catch (RequestException e) {
      httpStatus = e.getStatus();
      logger.info("Test controller. {}", e.getMessage());
    }
    assertEquals(HttpStatus.BAD_REQUEST, httpStatus);
  }

  /**
   * Checks the validation of DTO fields.
   */
  @Test
  void checkValidDTO() {

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    Set<ConstraintViolation<TaskDTO>> validate = validator.validate(
        buildTaskDTO(TaskStatus.POSTPONED));
    validatorFactory.close();

    assertTrue(validate.isEmpty());
  }

  /**
   * Checks the validation of DTO fields.
   */
  @Test
  void checkNoValidWhat() {

    TaskDTO taskDTO = new TaskDTO(1L, " ",
        LocalDate.of(2023, 4, 5), TaskStatus.WORK_IN_PROGRESS);

    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    Set<ConstraintViolation<TaskDTO>> validate = validator.validate(taskDTO);
    validatorFactory.close();

    List<String> errorKeysList = new ArrayList<>(validate).stream().
        map(ConstraintViolation::getMessageTemplate).collect(Collectors.toList());

    assertThat(errorKeysList, hasSize(1));
    assertThat(errorKeysList,
        containsInAnyOrder("{javax.validation.constraints.NotBlank.message}"));
  }


  Task buildTask(TaskStatus status) {
    return new Task(1L, "To complete the second practical on Spring",
        LocalDate.of(2023, 3, 17), status);
  }

  TaskDTO buildTaskDTO(TaskStatus status) {
    return new TaskDTO(1L, "To complete the second practical on Spring",
        LocalDate.of(2023, 3, 17), status);
  }

}