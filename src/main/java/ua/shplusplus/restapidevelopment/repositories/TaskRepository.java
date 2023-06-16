package ua.shplusplus.restapidevelopment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shplusplus.restapidevelopment.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
