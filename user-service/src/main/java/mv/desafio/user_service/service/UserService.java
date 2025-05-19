package mv.desafio.user_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mv.desafio.user_service.util.UserValidator;
import mv.desafio.user_service.client.TaskServiceClient;
import mv.desafio.user_service.dto.UserCreateDTO;
import mv.desafio.user_service.dto.UserResponseDTO;
import mv.desafio.user_service.dto.UserUpdateDTO;
import mv.desafio.user_service.model.User;
import mv.desafio.user_service.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TaskServiceClient taskServiceClient;
    private final UserValidator userValidator;

    @Autowired
    public UserService(UserRepository userRepository, TaskServiceClient taskServiceClient) {
        this.userRepository = userRepository;
        this.taskServiceClient = taskServiceClient;
        this.userValidator = new UserValidator(userRepository);
    }

    public UserResponseDTO salvar(UserCreateDTO dto) {
        userValidator.validarEmailUnico(dto.getEmail());
        var savedUser = userRepository.save(UserMapper.toEntity(dto));
        return UserMapper.toResponseDTO(savedUser);
    }

    public List<UserResponseDTO> listarTodos() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO buscarPorId(Long id) {
        return UserMapper.toResponseDTO(buscarUsuarioOuErro(id));
    }

    public UserResponseDTO editar(Long userId, UserUpdateDTO dto) {
        var existente = buscarUsuarioOuErro(userId);

        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            String email = dto.getEmail().trim();
            if (!email.equals(existente.getEmail())) {
                userValidator.validarEmailUnico(email);
                existente.setEmail(email);
            }
        }

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            existente.setNome(dto.getNome().trim());
        }

        return UserMapper.toResponseDTO(userRepository.save(existente));
    }

    public void excluir(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + userId);
        } else if (taskServiceClient.usuarioPossuiTarefas(userId)) {
            throw new IllegalStateException("Usuário possui tarefas associadas.");
        }

        userRepository.deleteById(userId);
    }

    private User buscarUsuarioOuErro(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
    }
}
