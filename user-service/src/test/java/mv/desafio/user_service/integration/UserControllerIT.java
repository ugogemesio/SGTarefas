package mv.desafio.user_service.integration;
import com.fasterxml.jackson.databind.ObjectMapper;

import mv.desafio.user_service.controller.UserController;
import mv.desafio.user_service.dto.UserCreateDTO;
import mv.desafio.user_service.dto.UserResponseDTO;
import mv.desafio.user_service.dto.UserUpdateDTO;
import mv.desafio.user_service.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDTO getFakeUserResponse() {
        return new UserResponseDTO(1L, "João Silva", "joao@email.com", LocalDateTime.now());
    }

    @Test
    void deveListarTodosUsuarios() throws Exception {
        List<UserResponseDTO> usuarios = List.of(getFakeUserResponse());

        Mockito.when(userService.listarTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        UserResponseDTO user = getFakeUserResponse();
        Mockito.when(userService.buscarPorId(1L)).thenReturn(user);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void deveCriarUsuario() throws Exception {
        UserCreateDTO dto = new UserCreateDTO("João Silva", "joao@email.com");

        Mockito.when(userService.salvar(any(UserCreateDTO.class))).thenReturn(getFakeUserResponse());

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void deveEditarUsuario() throws Exception {
        UserUpdateDTO dto = new UserUpdateDTO("João Atualizado", "joao@email.com");

        Mockito.when(userService.editar(eq(1L), any(UserUpdateDTO.class))).thenReturn(
                new UserResponseDTO(1L, "João Atualizado", "joao@email.com", LocalDateTime.now()));

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"));
    }

    @Test
    void deveExcluirUsuario() throws Exception {
        Mockito.doNothing().when(userService).excluir(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}
