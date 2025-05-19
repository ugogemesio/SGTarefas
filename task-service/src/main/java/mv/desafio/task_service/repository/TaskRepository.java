// src/main/java/mv/desafio/task_service/repository/TaskRepository.java
package mv.desafio.task_service.repository;
import mv.desafio.task_service.model.Status;
import mv.desafio.task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Status status);
    List<Task> findByUserId(Long userId);
    List<Task> findByStatusAndUserId(Status status, Long userId);
}