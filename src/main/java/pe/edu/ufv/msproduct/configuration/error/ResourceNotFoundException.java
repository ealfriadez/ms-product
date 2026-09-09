package pe.edu.ufv.msproduct.configuration.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String message;
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
