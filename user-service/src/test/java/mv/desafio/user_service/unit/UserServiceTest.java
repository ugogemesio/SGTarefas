package mv.desafio.user_service.unit;

import mv.desafio.user_service.client.TaskServiceClient;
import mv.desafio.user_service.dto.UserCreateDTO;
import mv.desafio.user_service.dto.UserUpdateDTO;
import mv.desafio.user_service.model.User;
import mv.desafio.user_service.repository.UserRepository;
import mv.desafio.user_service.service.UserService;
import mv.desafio.user_service.dto.UserResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskServiceClient taskServiceClient;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setNome("João Silva");
        user.setEmail("joao@email.com");
    }

    @Test
    void salvar_DeveCriarUsuarioComSucesso() {
        UserCreateDTO dto = new UserCreateDTO("João Silva", "joao@email.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.salvar(dto);

        assertNotNull(response);
        assertEquals("João Silva", response.getNome());
        assertEquals("joao@email.com", response.getEmail());
    }

    @Test
    void salvar_DeveLancarExcecao_QuandoEmailJaExiste() {
        UserCreateDTO dto = new UserCreateDTO("Maria", "joao@email.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.salvar(dto));
    }

    @Test
    void buscarPorId_DeveRetornarUsuario() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.buscarPorId(1L);

        assertEquals("João Silva", result.getNome());
    }

    @Test
    void buscarPorId_DeveLancarExcecaoSeNaoEncontrado() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.buscarPorId(2L));
    }

    @Test
    void editar_DeveAtualizarNomeEEEmail() {
        UserUpdateDTO dto = new UserUpdateDTO("Novo Nome", "novo@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.editar(1L, dto);

        assertEquals("Novo Nome", result.getNome());
        assertEquals("novo@email.com", result.getEmail());
    }

    @Test
    void excluir_DeveDeletarUsuario() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskServiceClient.usuarioPossuiTarefas(1L)).thenReturn(false);

        assertDoesNotThrow(() -> userService.excluir(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test
    void excluir_DeveLancarExcecao_QuandoUsuarioPossuiTarefas() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskServiceClient.usuarioPossuiTarefas(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> userService.excluir(1L));
    }

    @Test
    void listarTodos_DeveRetornarListaDeUsuarios() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.listarTodos();

        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getNome());
    }

    // @Test
    // void listarPaginado_DeveRetornarPagina() {
    //     PageRequest pageable = PageRequest.of(0, 10);
    //     Page<User> page = new PageImpl<>(List.of(user));

    //     when(userRepository.findAll(pageable)).thenReturn(page);

    //     Page<User> result = userService.listarPaginado(pageable);

    //     assertEquals(1, result.getTotalElements());
    // }
}
