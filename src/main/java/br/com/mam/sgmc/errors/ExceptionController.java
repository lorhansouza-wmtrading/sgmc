package br.com.mam.sgmc.errors;

import java.time.Instant;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;

import br.com.mam.sgmc.errors.dto.ErrorDetailsDTO;
import br.com.mam.sgmc.errors.dto.FieldErrorDetails;
import br.com.mam.sgmc.errors.dto.ValidationErrorDetails;

@ControllerAdvice
public class ExceptionController {
    private static final String BAD_REQUEST = "Bad Request";
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDetails> handleMethodValidationExceptions(
            MethodArgumentNotValidException exception) {
        List<FieldErrorDetails> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDetails(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();

        ValidationErrorDetails errorResponse = new ValidationErrorDetails(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                "Erro de validação nos campos informados",
                errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsDTO> handleResourceNotFoundException(ResourceNotFoundException exception) {
        String error = exception.getMessage();
        ErrorDetailsDTO errorResponse = new ErrorDetailsDTO(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                error);
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetailsDTO> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        ErrorDetailsDTO errorResponse = new ErrorDetailsDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                "Tipo informado não existe!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetailsDTO> handleOperacaoJaCadastrada(ResponseStatusException exception) {
        ErrorDetailsDTO errorResponse = new ErrorDetailsDTO(
                Instant.now(),
                exception.getStatusCode().value(),
                BAD_REQUEST,
                exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode().value()).body(errorResponse);
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<ErrorDetailsDTO> handleConsultaOmieException(ServerErrorException exception) {
        ErrorDetailsDTO errorResponse = new ErrorDetailsDTO(
                Instant.now(),
                exception.getStatusCode().value(),
                "Erro no Servidor Externo",
                exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode().value()).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDetailsDTO> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        ErrorDetailsDTO errorResponse = new ErrorDetailsDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetailsDTO> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {

        ErrorDetailsDTO errorResponse = new ErrorDetailsDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                exception.getMessage());

        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorDetailsDTO> handleIllegalArgumentException(
                IllegalArgumentException exception) {
                ErrorDetailsDTO errorResponse = new ErrorDetailsDTO(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        BAD_REQUEST,
                        exception.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDetailsDTO> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        ErrorDetailsDTO errorResponse = new ErrorDetailsDTO(
                Instant.now(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                exception.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

}
