package mv.desafio.user_service.util;

import mv.desafio.user_service.repository.UserRepository;

public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validarEmailUnico(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }

        if (userRepository.findByEmail(email.trim()).isPresent()) {
            throw new IllegalArgumentException("Email já está em uso");
        }
    }
}
