package ua.shplusplus.restapidevelopment.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.shplusplus.restapidevelopment.model.SuccessDTO;
import ua.shplusplus.restapidevelopment.model.Task;
import ua.shplusplus.restapidevelopment.model.TaskDTO;
import ua.shplusplus.restapidevelopment.services.TaskService;


@OpenAPIDefinition(
    info = @Info(
        title = "TODO List System Api",
        description = "Documentation System",
        version = "1.0.0",
        contact = @Contact(
            name = "Mykola Koretskyi",
            email = "mikolakoreckij45@gmail.com"
        )
    )
)

@SecurityScheme(
    name = "Java in use api",
    scheme = "Basic",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER)

@RestController
public class TaskController {

  private final TaskService taskService;


  @Autowired
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }


  @SecurityRequirement(name = "Java in use api")
  @Tag(name = "Controller get all", description = "The controller receives all objects")
  @Operation(summary = "Get everyone out", description = "Display a list of all objects")
  @GetMapping("/task")
  public List<Task> getAllTask(@RequestParam(value = "language", defaultValue = "en")
  @Parameter(description = "Locale specified by the user") Locale local) {
    Locale.setDefault(local);
    return taskService.getAllTasks();
  }


  @SecurityRequirement(name = "Java in use api")
  @Tag(name = "Controller get one", description = "The controller receives one object by id")
  @Operation(summary = "Get one", description = "Get a single item by its id")
  @GetMapping("/task/{id}")
  public Task getTask(
      @PathVariable("id") @Min(1) @Parameter(description = "Object id number") Long id,
      @RequestParam(value = "language", defaultValue = "en")
      @Parameter(description = "Locale specified by the user") Locale local) {
    Locale.setDefault(local);
    return taskService.getOneTask(id);
  }


  @SecurityRequirement(name = "Java in use api")
  @Tag(name = "Controller add object", description = "The controller adds a new object")
  @Operation(summary = "Add object", description = "Add one new object to the end of the list")
  @PostMapping(value = "/task", consumes = "application/json")
  public SuccessDTO addTask(@Valid @RequestBody TaskDTO newTaskDTO, BindingResult bindingResult,
      @RequestParam(value = "language", defaultValue = "en")
      @Parameter(description = "Locale specified by the user") Locale local) {
    Locale.setDefault(local);
    return taskService.addOneTask(newTaskDTO, bindingResult);
  }


  @SecurityRequirement(name = "Java in use api")
  @Tag(name = "Controller change", description = "The controller changes the object by id")
  @Operation(summary = "Change object", description = "Change the object by the specified id")
  @PutMapping(value = "/task/{id}", consumes = "application/json")
  public Task updateTask(
      @PathVariable("id") @Min(1) @Parameter(description = "Object id number") Long id,
      @Valid @RequestBody TaskDTO taskDTO, BindingResult bindingResult,
      @RequestParam(value = "language", defaultValue = "en")
      @Parameter(description = "Locale specified by the user") Locale local) {
    Locale.setDefault(local);
    return taskService.updateOneTask(id, taskDTO, bindingResult);
  }


  @SecurityRequirement(name = "Java in use api")
  @Tag(name = "Controller delete one", description = "The controller deletes one element by id")
  @Operation(summary = "Delete one", description = "Deletes one object by its id")
  @DeleteMapping("/task/{id}")
  public SuccessDTO deleteTask(
      @PathVariable("id") @Min(1) @Parameter(description = "Object id number") Long id,
      @RequestParam(value = "language", defaultValue = "en")
      @Parameter(description = "Locale specified by the user") Locale local) {
    Locale.setDefault(local);
    return taskService.deleteOneTask(id);
  }


  @SecurityRequirement(name = "Java in use api")
  @Tag(name = "Controller delete all", description = "The controller removes all items from the list")
  @Operation(summary = "Delete all", description = "Deletes all objects in the list")
  @DeleteMapping("/task/delete/all")
  public SuccessDTO deleteTask(@RequestParam(value = "language", defaultValue = "en")
  @Parameter(description = "Locale specified by the user") Locale local) {
    Locale.setDefault(local);
    return taskService.deleteAllTask();
  }

}
