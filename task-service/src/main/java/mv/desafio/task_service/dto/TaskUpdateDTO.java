package mv.desafio.task_service.dto;

import lombok.Data;

@Data
public class TaskUpdateDTO {    
    private String titulo;
    private String descricao;
    private String status;
}
