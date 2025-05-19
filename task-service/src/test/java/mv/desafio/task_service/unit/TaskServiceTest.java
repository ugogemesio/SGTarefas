package mv.desafio.task_service.unit;
import mv.desafio.task_service.model.Status;
import mv.desafio.task_service.dto.TaskCreateDTO;
import mv.desafio.task_service.dto.TaskResponseDTO;
import mv.desafio.task_service.dto.TaskUpdateDTO;
import mv.desafio.task_service.model.Task;
import mv.desafio.task_service.repository.TaskRepository;
import mv.desafio.task_service.service.TaskService;
import mv.desafio.task_service.service.UserClientService;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserClientService userClientService;

    private Task task;
    private TaskCreateDTO createDTO;
    private TaskUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitulo("Teste");
        task.setDescricao("Descrição teste");
        task.setStatus(Status.PENDENTE);
        task.setUserId(10L);
        task.setDataCriacao(LocalDateTime.now());

        createDTO = new TaskCreateDTO();
        createDTO.setTitulo("Nova tarefa");
        createDTO.setDescricao("Descrição nova tarefa");
        createDTO.setStatus("PENDENTE");
        createDTO.setUserId(10L);

        updateDTO = new TaskUpdateDTO();
        updateDTO.setTitulo("Atualizado");
        updateDTO.setDescricao("Descrição atualizada");
        updateDTO.setStatus("EM_ANDAMENTO");
    }

    @Test
    void listarTodos_deveRetornarListaDTO() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskResponseDTO> result = taskService.listarTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task.getTitulo(), result.get(0).getTitulo());

        verify(taskRepository, times(1)).findAll();
    }

    // @Test
    // void criar_deveCriarTarefaQuandoUsuarioExiste() {
    //     when(userClientService.userExists(createDTO.getUserId())).thenReturn(true);
    //     when(taskRepository.save(any(Task.class))).thenAnswer(i -> {
    //         Task t = i.getArgument(0);
    //         t.setId(100L);
    //         return t;
    //     });

    //     TaskResponseDTO created = taskService.criar(createDTO);

    //     assertNotNull(created);
    //     assertEquals(createDTO.getTitulo(), created.getTitulo());
    //     assertEquals(Status.PENDENTE, created.getStatus());
    //     assertEquals(createDTO.getUserId(), created.getUserId());

    //     verify(userClientService, times(1)).userExists(createDTO.getUserId());
    //     verify(taskRepository, times(1)).save(any(Task.class));
    // }

    @Test
    void criar_deveLancarExcecaoQuandoUsuarioNaoExiste() {
        when(userClientService.userExists(createDTO.getUserId())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.criar(createDTO);
        });

        assertEquals("Usuário com ID " + createDTO.getUserId() + " não existe.", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void listarTarefasPorStatusEIdUser_comStatusEUserId() {
        when(taskRepository.findByStatusAndUserId(Status.PENDENTE, 10L)).thenReturn(List.of(task));

        List<TaskResponseDTO> result = taskService.listarTarefasPorStatusEIdUser("PENDENTE", 10L);

        assertEquals(1, result.size());
        assertEquals(task.getTitulo(), result.get(0).getTitulo());

        verify(taskRepository, times(1)).findByStatusAndUserId(Status.PENDENTE, 10L);
    }

    @Test
    void atualizarTarefa_deveAtualizarQuandoStatusValido() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        TaskResponseDTO updated = taskService.atualizarTarefa(task.getId(), updateDTO);

        assertEquals(updateDTO.getTitulo(), updated.getTitulo());
        assertEquals(updateDTO.getStatus(), updated.getStatus());

        verify(taskRepository, times(1)).findById(task.getId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void atualizarTarefa_deveLancarExcecaoSeTarefaNaoEncontrada() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            taskService.atualizarTarefa(1L, updateDTO);
        });

        assertEquals("Tarefa não encontrada", exception.getMessage());
    }

    @Test
    void atualizarTarefa_deveLancarExcecaoSeStatusInvalido() {
        task.setStatus(Status.CONCLUIDO);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            taskService.atualizarTarefa(task.getId(), updateDTO);
        });

        assertEquals("Apenas tarefas pendentes ou em andamento podem ser editadas.", exception.getMessage());
    }

    @Test
    void excluirTarefa_deveExcluirQuandoExiste() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.excluirTarefa(1L));

        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void excluirTarefa_deveLancarExcecaoQuandoNaoExiste() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.excluirTarefa(1L);
        });

        assertEquals("Tarefa não encontrada", exception.getMessage());
    }

    @Test
    void usuarioPossuiTarefas_deveRetornarTrueQuandoHaTarefas() {
        when(taskRepository.findByUserId(10L)).thenReturn(List.of(task));

        boolean result = taskService.usuarioPossuiTarefas(10L);

        assertTrue(result);
    }

    @Test
    void usuarioPossuiTarefas_deveRetornarFalseQuandoNaoHaTarefas() {
        when(taskRepository.findByUserId(10L)).thenReturn(Collections.emptyList());

        boolean result = taskService.usuarioPossuiTarefas(10L);

        assertFalse(result);
    }
}