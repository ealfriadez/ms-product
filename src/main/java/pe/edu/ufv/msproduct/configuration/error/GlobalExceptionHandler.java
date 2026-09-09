package pe.edu.ufv.msproduct.configuration.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pe.edu.ufv.msproduct.model.dto.ErrorResponseDto;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorResponseDto> handleConflict(Exception ex, WebRequest request) {
        log.error("Exception {}", ex.getMessage(), ex);
        var error = new ErrorResponseDto(ex.getMessage(), " " + HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now());
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<ErrorResponseDto> handleConflict(HttpClientErrorException ex, WebRequest request) {
        log.error("HttpClientErrorException " + ex.getMessage(), ex);
        var json = convertToJson(ex.getMessage());
        var error = new ErrorResponseDto(json.get("message").asText(), "" + ex.getStatusCode().value(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatusCode().value()));
    }

    private JsonNode convertToJson(String message) {
        var messageTmp = message.split(": \"")[1];
        messageTmp = messageTmp.substring(0, messageTmp.length() - 1);

        return mapper.readTree(messageTmp);
    }
}
