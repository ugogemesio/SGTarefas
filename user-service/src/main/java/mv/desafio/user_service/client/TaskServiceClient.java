package mv.desafio.user_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TaskServiceClient {

    private final RestTemplate restTemplate;

    public TaskServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public boolean usuarioPossuiTarefas(Long userId) {
        String url = "http://task-service:8080/tarefas/exists-by-user/" + userId;
        return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
    }
}
