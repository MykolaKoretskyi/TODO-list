package ua.shplusplus.restapidevelopment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shplusplus.restapidevelopment.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
