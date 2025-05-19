
package mv.desafio.task_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class UserClientService {

    private final RestTemplate restTemplate;

    public UserClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean userExists(Long userId) {
        try {
            restTemplate.getForObject("http://user-service:8080/usuarios/" + userId, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
