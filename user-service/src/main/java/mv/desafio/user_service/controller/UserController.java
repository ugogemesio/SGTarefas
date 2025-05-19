package mv.desafio.user_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mv.desafio.user_service.dto.UserCreateDTO;
import mv.desafio.user_service.dto.UserResponseDTO;
import mv.desafio.user_service.dto.UserUpdateDTO;
import mv.desafio.user_service.service.UserService;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarUsuarios() {
        List<UserResponseDTO> users = userService.listarTodos();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> buscarPorId(@PathVariable Long userId) {
        UserResponseDTO responseDTO = userService.buscarPorId(userId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> criarUsuario(@RequestBody @Valid UserCreateDTO dto) {
        UserResponseDTO responseDTO = userService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> editarUsuario(@PathVariable Long userId, @RequestBody UserUpdateDTO dto) {
        UserResponseDTO responseDTO = userService.editar(userId, dto);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long userId) {
        userService.excluir(userId);
        return ResponseEntity.noContent().build();
    }
}