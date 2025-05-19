package mv.desafio.task_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class TaskCreateDTO {    
    @NotBlank(message = "Título não pode ser vazio")
    private String titulo;
    
    @NotBlank(message = "Descrição não pode ser vazio")
    private String descricao;
    
    @NotBlank
    private String status; 
    
    @NotNull
    private Long userId; 
}
