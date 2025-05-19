package mv.desafio.user_service.service;

import mv.desafio.user_service.dto.UserCreateDTO;
import mv.desafio.user_service.dto.UserResponseDTO;
import mv.desafio.user_service.model.User;

public class UserMapper {

    public static User toEntity(UserCreateDTO dto) {
        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getDataCriacao()
        );
    }
}
