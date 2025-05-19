package mv.desafio.user_service.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserDTO {

    
    private LocalDateTime dataCriacao;
    private Long id;
    private String nome;
    private String email;

 }
