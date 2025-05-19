package mv.desafio.task_service.dto;


import mv.desafio.task_service.model.Task;

import java.time.LocalDateTime;

public class TaskResponseDTO {
    public TaskResponseDTO(Task savedTask) {

    }

    private Long id;
    private String titulo;
    private String descricao;
    private String status;
    private LocalDateTime dataCriacao;
    private Long userId;

    public TaskResponseDTO() {
    }

    public TaskResponseDTO(Long id, String titulo, String descricao, String status, LocalDateTime dataCriacao,
            Long userId) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
