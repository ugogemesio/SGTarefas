package mv.desafio.task_service.util;
import mv.desafio.task_service.model.Status;

import java.util.Arrays;

public class StatusValidator {
    public static Status validarEConverter(String statusString) {
        if (statusString == null || statusString.isBlank()) {
            throw new IllegalArgumentException("Status não pode ser nulo ou vazio.");
        }

        try {
            return Status.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido. Valores permitidos: " +
                    Arrays.toString(Status.values()));
        }
    }
}
