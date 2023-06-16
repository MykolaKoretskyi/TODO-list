package ua.shplusplus.restapidevelopment.services;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.shplusplus.restapidevelopment.entities.SuccessDTO;
import ua.shplusplus.restapidevelopment.entities.Task;
import ua.shplusplus.restapidevelopment.entities.TaskDTO;
import ua.shplusplus.restapidevelopment.exceptions.RequestException;
import ua.shplusplus.restapidevelopment.repositories.TaskRepository;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
  public static final String MESSAGES = "messages";
  public static final String OBJECT_EXISTS = "errorObjectExists";
  public static final String ID_EXISTS = "objectIdExists";
  public static final String ID_EXISTS_FOR_LOG = "objectIdExists{}";
  private ResourceBundle bundle;

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }


  public List<Task> getAllTasks() {

    bundle = ResourceBundle.getBundle(MESSAGES, Locale.getDefault());
    List<Task> findAll = taskRepository.findAll();

    if (findAll.isEmpty()) {

      URI location = ServletUriComponentsBuilder.fromCurrentRequest().
          buildAndExpand(findAll).toUri();
      logger.error(bundle.getString(OBJECT_EXISTS), location);
      throw new RequestException(HttpStatus.NOT_FOUND,
          bundle.getString(OBJECT_EXISTS) + location);
    }
    return findAll;
  }

  public Task getOneTask(Long id) {

    bundle = ResourceBundle.getBundle(MESSAGES, Locale.getDefault());

    return taskRepository.findById(id).orElseThrow(() -> {
      logger.error(bundle.getString(ID_EXISTS_FOR_LOG), id);
      return new RequestException(HttpStatus.NOT_FOUND,
          bundle.getString(ID_EXISTS) + id);
    });
  }

  public SuccessDTO addOneTask(TaskDTO taskDTO, BindingResult bindingResult) {

    bundle = ResourceBundle.getBundle(MESSAGES, Locale.getDefault());

    if (bindingResult.hasErrors()) {
      createExceptionNoValidDTO(bindingResult);
    } else if (taskDTO.getId() != null) {
      createExceptionNoId(taskDTO);
    }
    Task savedTask = taskRepository.save(update(taskDTO, new Task()));

    URI location = ServletUriComponentsBuilder.fromCurrentRequest().
        path("/{id}").buildAndExpand(savedTask.getId()).toUri();

    return new SuccessDTO(LocalDateTime.now(), HttpStatus.OK,
        bundle.getString("newCreated"), location.toString());
  }

  public Task updateOneTask(Long id, TaskDTO taskDTO, BindingResult bindingResult) {

    bundle = ResourceBundle.getBundle(MESSAGES, Locale.getDefault());
    Task task = checkAndGetTask(id, taskDTO, bindingResult, taskDTO.getId());

    return update(taskDTO, task);
  }

  private Task checkAndGetTask(
      Long id,
      TaskDTO taskDTO,
      BindingResult bindingResult,
      Long idObject
  ) {
    if (bindingResult.hasErrors()) {
      createExceptionNoValidDTO(bindingResult);
    } else if (idObject != null && !Objects.equals(idObject, id)) {
      createExceptionDifferentId(id, idObject);
    }
    Task task = taskRepository.findById(id).orElseThrow(() -> {
      logger.error(bundle.getString(ID_EXISTS_FOR_LOG), id);
      return new RequestException(HttpStatus.BAD_REQUEST,
          bundle.getString(ID_EXISTS) + id);
    });
    if (!task.getStatus().getAvailableStatuses().contains(taskDTO.getStatus())) {
      logger.error(bundle.getString("cannotChangeStatus"), taskDTO.getStatus());
      throw new RequestException(HttpStatus.BAD_REQUEST,
          bundle.getString("cannotChangeStatus") + taskDTO.getStatus());
    }
    return task;
  }

  public SuccessDTO deleteOneTask(Long id) {

    bundle = ResourceBundle.getBundle(MESSAGES, Locale.getDefault());

    if (taskRepository.findById(id).isEmpty()) {
      logger.error(bundle.getString(ID_EXISTS_FOR_LOG), id);
      throw new RequestException(HttpStatus.NOT_FOUND, bundle.getString(ID_EXISTS) + id);
    }
    String location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .buildAndExpand(id)
        .toString();
    taskRepository.deleteById(id);

    return new SuccessDTO(LocalDateTime.now(), HttpStatus.OK,
        bundle.getString("deletedObj") + id, location);
  }

  public SuccessDTO deleteAllTask() {

    bundle = ResourceBundle.getBundle(MESSAGES, Locale.getDefault());

    if (taskRepository.findAll().isEmpty()) {

      logger.error(bundle.getString(OBJECT_EXISTS));
      throw new RequestException(HttpStatus.NOT_FOUND, bundle.getString(OBJECT_EXISTS));
    }
    taskRepository.deleteAll();
    return new SuccessDTO(LocalDateTime.now(), HttpStatus.OK,
        bundle.getString("deletedAll"), "/task/delete/all");
  }

  public Task update(TaskDTO taskDTO, Task task) {

    if (taskDTO.getId() != null) {
      task.setId(taskDTO.getId());
    }
    if (taskDTO.getWhat() != null) {
      task.setWhat(taskDTO.getWhat());
    }
    if (taskDTO.getExecutionDate() != null) {
      task.setExecutionDate(taskDTO.getExecutionDate());
    }
    if (taskDTO.getStatus() != null) {
      task.setStatus(taskDTO.getStatus());
    }
    taskRepository.flush();
    return task;
  }

  private void createExceptionNoId(TaskDTO taskDTO) {
    logger.error(bundle.getString("invalidRequest"));
    throw new RequestException(HttpStatus.BAD_REQUEST,
        bundle.getString("invalidRequest") + taskDTO.getId());
  }

  private void createExceptionNoValidDTO(BindingResult bindingResult) {

    Map<String, String> errorsMap = getValidatorMessages(bindingResult);
    logger.error(bundle.getString("validError"), errorsMap);
    throw new RequestException(HttpStatus.BAD_REQUEST,
        bundle.getString("validationError") + errorsMap);
  }

  private void createExceptionDifferentId(Long id, Long idObject) {
    logger.error(bundle.getString("requestDifferentFromObj"), id, idObject);
    throw new RequestException(HttpStatus.BAD_REQUEST,
        bundle.getString("errorRequestId") + id +
            bundle.getString("isDifferentFromObj") + idObject);
  }

  public static Map<String, String> getValidatorMessages(BindingResult bindingResult) {

    Map<String, String> errorsMap = new HashMap<>();
    bindingResult.getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errorsMap.put(fieldName, errorMessage);
    });
    return errorsMap;
  }

}
