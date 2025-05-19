package mv.desafio.task_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/user_service",
    "spring.datasource.username=postgres",
    "spring.datasource.password=postgres",
    "spring.datasource.driver-class-name=org.postgresql.Driver"
})
class TaskServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
