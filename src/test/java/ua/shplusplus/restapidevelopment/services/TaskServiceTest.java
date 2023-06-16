package ua.shplusplus.restapidevelopment.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ua.shplusplus.restapidevelopment.model.Task;
import ua.shplusplus.restapidevelopment.model.TaskDTO;
import ua.shplusplus.restapidevelopment.model.TaskStatus;
import ua.shplusplus.restapidevelopment.exceptions.RequestException;
import ua.shplusplus.restapidevelopment.repositories.TaskRepository;

class TaskServiceTest {

  @Mock
  TaskRepository taskRepository;
  @Mock
  BindingResult bindingResult;
  AutoCloseable closeable;

  @BeforeAll
  static void setup() {
    Locale.setDefault(Locale.ENGLISH);
  }

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
  }

  @After(value = "")
  void releaseMocks() throws Exception {
    closeable.close();
  }

  final Logger logger = LoggerFactory.getLogger(TaskServiceTest.class);


  /**
   * The verify method of the Mockito class checks the number of calls to some command.
   */
  @Test
  void getOneTask() {

    TaskService taskService = new TaskService(taskRepository);
    when(taskRepository.findById(1L)).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));
    taskService.getOneTask(1L);

    verify(taskRepository, times(1)).findById(1L);
  }

  /**
   * The method checks which execution status of the request receives an exception.
   */
  @Test
  void getOneTaskInvalidId() {

    TaskService taskService = new TaskService(taskRepository);

    taskRepository.findById(5L);

    RequestException exception = Assertions.assertThrows(RequestException.class, () ->
        taskService.getOneTask(5L));
    logger.info("Test service. Status request:  {}", exception.getStatus());

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
  }

  /**
   * The verify method of the Mockito class checks the number of calls to some command.
   */
  @Test
  void getAllTask() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findAll()).thenReturn(new ArrayList<>(
        Arrays.asList(buildTask(TaskStatus.PLANNED),
            buildTask(TaskStatus.WORK_IN_PROGRESS))));

    taskService.getAllTasks();

    verify(taskRepository, times(1)).findAll();
  }

  /**
   * The method checks which execution status of the request receives an exception.
   */
  @Test
  void getAllTaskNotFound() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findAll()).thenReturn(new ArrayList<>());

    RequestException e = Assertions.assertThrows(RequestException.class, taskService::getAllTasks);
    logger.info("Test service. Status request:  {}", e.getStatus());

    assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
  }

  /**
   * The verify method of the Mockito class checks the number of calls to some command.
   */
  @Test
  void addOneTask() {

    TaskService taskService = new TaskService(taskRepository);
    TaskDTO taskDTONoId = buildNoIdTaskDTO(TaskStatus.PLANNED);
    Task task = buildTask(TaskStatus.PLANNED);
    TaskDTO taskDTO = buildTaskDTO(TaskStatus.PLANNED);

    doNothing().when(taskRepository).flush();
    when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

    taskService.addOneTask(taskDTONoId, bindingResult);

    verify(taskRepository, times(1)).save(taskService.update(taskDTO, new Task()));
    verify(taskRepository, times(1)).flush();
  }

  /**
   * The method checks which execution status of the request receives an exception.
   */
  @Test
  void addOneTaskIdPresent() {

    TaskService taskService = new TaskService(taskRepository);
    TaskDTO taskDTO = buildTaskDTO(TaskStatus.PLANNED);

    RequestException e = Assertions.assertThrows(RequestException.class, () -> {
      taskService.addOneTask(taskDTO, bindingResult);
    });
    logger.info("Test service. Status request:  {}", e.getStatus());

    assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
  }

  /**
   * The verify method of the Mockito class checks the number of calls to some command.
   */
  @Test
  void deleteOneTask() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(1L)).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    taskService.deleteOneTask(1L);

    verify(taskRepository, times(1)).findById(1L);
    verify(taskRepository, times(1)).deleteById(1L);
  }

  /**
   * The verify method of the Mockito class checks the number of calls to some command.
   */
  @Test
  void deleteAllTask() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findAll()).thenReturn(new ArrayList<>(
        Arrays.asList(buildTask(TaskStatus.PLANNED), buildTask(TaskStatus.WORK_IN_PROGRESS))));

    taskService.deleteAllTask();

    verify(taskRepository, times(1)).findAll();
    verify(taskRepository, times(1)).deleteAll();
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskPlannedToInProgress() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToInProgress = taskService.updateOneTask(
        1L, buildTaskDTO(TaskStatus.WORK_IN_PROGRESS), bindingResult).getStatus();

    TaskStatus expectedInProgress = TaskStatus.WORK_IN_PROGRESS;

    assertEquals(actualPlannedToInProgress, expectedInProgress);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskPlannedToPostponed() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToPostponed = taskService.updateOneTask(
        1L, buildTaskDTO(TaskStatus.POSTPONED), bindingResult).getStatus();

    TaskStatus expectedPostponed = TaskStatus.POSTPONED;

    assertEquals(actualPlannedToPostponed, expectedPostponed);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskPlannedToNotified() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToNotified = taskService.updateOneTask(
        1L, buildTaskDTO(TaskStatus.NOTIFIED), bindingResult).getStatus();

    TaskStatus expectedNotified = TaskStatus.NOTIFIED;

    assertEquals(actualPlannedToNotified, expectedNotified);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskPlannedToSigned() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToSigned = taskService.updateOneTask(
        1L, buildTaskDTO(TaskStatus.SIGNED), bindingResult).getStatus();

    TaskStatus expectedSigned = TaskStatus.SIGNED;

    assertEquals(actualPlannedToSigned, expectedSigned);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskPlannedToDone() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToDone = taskService.updateOneTask(
        1L, buildTaskDTO(TaskStatus.DONE), bindingResult).getStatus();

    TaskStatus expectedDone = TaskStatus.DONE;

    assertEquals(actualPlannedToDone, expectedDone);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskPlannedToCancelled() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.PLANNED)));

    TaskStatus actualPlannedToCancelled = taskService.updateOneTask(
        1L, buildTaskDTO(TaskStatus.CANCELLED), bindingResult).getStatus();

    TaskStatus expectedCancelled = TaskStatus.CANCELLED;

    assertEquals(actualPlannedToCancelled, expectedCancelled);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskSignedToDone() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.SIGNED)));

    TaskStatus actualPlannedToCancelled = taskService.updateOneTask(
        1L, buildTaskDTO(TaskStatus.DONE), bindingResult).getStatus();

    TaskStatus expectedDone = TaskStatus.DONE;

    assertEquals(actualPlannedToCancelled, expectedDone);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskInProgressToPostponed() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(
        Optional.of(buildTask(TaskStatus.WORK_IN_PROGRESS)));

    HttpStatus httpStatus = HttpStatus.OK;
    try {
      taskService.updateOneTask(1L, buildTaskDTO(TaskStatus.POSTPONED), bindingResult);

    } catch (RequestException e) {
      httpStatus = e.getStatus();
      logger.info("Test service. {}", e.getMessage());
    }
    assertEquals(HttpStatus.BAD_REQUEST, httpStatus);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskNotifiedToSigned() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(
        Optional.of(buildTask(TaskStatus.NOTIFIED)));

    HttpStatus httpStatus = HttpStatus.OK;
    try {
      taskService.updateOneTask(1L, buildTaskDTO(TaskStatus.SIGNED), bindingResult);

    } catch (RequestException e) {
      httpStatus = e.getStatus();
      logger.info("Test service. {}", e.getMessage());
    }
    assertEquals(HttpStatus.BAD_REQUEST, httpStatus);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskDoneToCancelled() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(buildTask(TaskStatus.DONE)));

    HttpStatus httpStatus = HttpStatus.OK;
    try {
      taskService.updateOneTask(1L, buildTaskDTO(TaskStatus.CANCELLED), bindingResult);

    } catch (RequestException e) {
      httpStatus = e.getStatus();
      logger.info("Test service. {}", e.getMessage());
    }
    assertEquals(HttpStatus.BAD_REQUEST, httpStatus);
  }

  /**
   * Checks for a task status change.
   */
  @Test
  void updateTaskCancelledToInProgress() {

    TaskService taskService = new TaskService(taskRepository);

    when(taskRepository.findById(anyLong())).thenReturn(
        Optional.of(buildTask(TaskStatus.CANCELLED)));

    HttpStatus httpStatus = HttpStatus.OK;
    try {
      taskService.updateOneTask(1L, buildTaskDTO(TaskStatus.WORK_IN_PROGRESS), bindingResult);

    } catch (RequestException e) {
      httpStatus = e.getStatus();
      logger.info("Test service. {}", e.getMessage());
    }
    assertEquals(HttpStatus.BAD_REQUEST, httpStatus);
  }


  @Test
  void update() {

    TaskService taskService = new TaskService(taskRepository);

    Task actualPlannedToInProgress =
        taskService.update(buildTaskDTO(TaskStatus.POSTPONED),
            buildTask(TaskStatus.SIGNED));

    Task expectedInProgress = buildTask(TaskStatus.POSTPONED);

    assertEquals(actualPlannedToInProgress.getWhat(), expectedInProgress.getWhat());
    assertEquals(actualPlannedToInProgress.getExecutionDate(),
        expectedInProgress.getExecutionDate());
    assertEquals(actualPlannedToInProgress.getStatus(), expectedInProgress.getStatus());
  }


  Task buildTask(TaskStatus status) {
    return new Task(1L, "To complete the second practical on Spring",
        LocalDate.of(2023, 3, 17), status);
  }

  TaskDTO buildTaskDTO(TaskStatus status) {
    return new TaskDTO(1L, "To complete the second practical on Spring",
        LocalDate.of(2023, 3, 17), status);
  }

  TaskDTO buildNoIdTaskDTO(TaskStatus status) {
    return new TaskDTO("To complete the second practical on Spring",
        LocalDate.of(2023, 1, 27), status);
  }

}