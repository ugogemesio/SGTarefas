package mv.desafio.task_service.service;

import mv.desafio.task_service.dto.TaskCreateDTO;
import mv.desafio.task_service.dto.TaskResponseDTO;
import mv.desafio.task_service.dto.TaskUpdateDTO;
import mv.desafio.task_service.repository.TaskRepository;
import mv.desafio.task_service.util.StatusValidator;
import mv.desafio.task_service.model.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserClientService userClientService;

    public List<TaskResponseDTO> listarTodos() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TaskResponseDTO criar(TaskCreateDTO dto) {
        if (!userClientService.userExists(dto.getUserId())) {
            throw new IllegalArgumentException("Usuário com ID " + dto.getUserId() + " não existe.");
        }

        Task task = new Task();
        task.setTitulo(dto.getTitulo());
        task.setDescricao(dto.getDescricao());
        task.setStatus(StatusValidator.validarEConverter(dto.getStatus()));
        task.setUserId(dto.getUserId());

        Task savedTask = taskRepository.save(task);

        return new TaskResponseDTO(savedTask);
    }

    public List<TaskResponseDTO> listarTarefasPorStatusEIdUser(String status, Long userId) {
        List<Task> tasks;

        Status statusEnum = null;
        if (status != null && !status.isEmpty()) {
            statusEnum = StatusValidator.validarEConverter(status);
        }

        if (statusEnum != null && userId != null) {
            tasks = taskRepository.findByStatusAndUserId(statusEnum, userId);
        } else if (statusEnum != null) {
            tasks = taskRepository.findByStatus(statusEnum);
        } else if (userId != null) {
            tasks = taskRepository.findByUserId(userId);
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TaskResponseDTO atualizarTarefa(Long id, TaskUpdateDTO dto) {
        Task tasks = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));

        if (tasks.getStatus() != Status.PENDENTE && tasks.getStatus() != Status.EM_ANDAMENTO) {
            throw new IllegalStateException("Apenas tarefas pendentes ou em andamento podem ser editadas.");
        }
        if (dto.getTitulo() != null && !dto.getTitulo().isBlank()) {
            tasks.setTitulo(dto.getTitulo());
        }
        if (dto.getDescricao() != null && !dto.getDescricao().isBlank()) {
            tasks.setDescricao(dto.getDescricao());
        }

        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            tasks.setStatus(StatusValidator.validarEConverter(dto.getStatus()));
        }

        return toDTO(taskRepository.save(tasks));
    }

    public void excluirTarefa(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Tarefa não encontrada");
        }
        taskRepository.deleteById(id);
    }

    public boolean usuarioPossuiTarefas(Long userId) {
        return !taskRepository.findByUserId(userId).isEmpty();
    }

    private TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitulo(),
                task.getDescricao(),
                task.getStatus().name(),
                task.getDataCriacao(),
                task.getUserId());
    }
}
