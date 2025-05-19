package mv.desafio.task_service.controller;

import mv.desafio.task_service.dto.TaskCreateDTO;
import mv.desafio.task_service.dto.TaskResponseDTO;
import mv.desafio.task_service.dto.TaskUpdateDTO;
import mv.desafio.task_service.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefas")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> listarTodasTarefas() {
        List<TaskResponseDTO> task = taskService.listarTodos();
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> criarTarefa(@Valid @RequestBody TaskCreateDTO dto) {
        TaskResponseDTO newTask = taskService.criar(dto); 
        return ResponseEntity.status(201).body(newTask);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<TaskResponseDTO>> listarTarefasPorStatusEIdUser(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId) {
        List<TaskResponseDTO> tarefas = taskService.listarTarefasPorStatusEIdUser(status, userId);
        return ResponseEntity.ok(tarefas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> atualizarTarefa(@PathVariable Long id,
            @Valid @RequestBody TaskUpdateDTO dto) {
        TaskResponseDTO response = taskService.atualizarTarefa(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTarefa(@PathVariable Long id) {
        taskService.excluirTarefa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists-by-user/{userId}")
    public ResponseEntity<Boolean> existeTarefaParaUsuario(@PathVariable Long userId) {
        boolean existe = taskService.usuarioPossuiTarefas(userId);
        return ResponseEntity.ok(existe);
    }
}
