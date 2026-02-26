package com.fabiana.mapa_viagem.exception;

    import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

	@RestControllerAdvice
	public class ResourceExceptionHandler {

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e) {

	        ValidationError err = new ValidationError();
	        err.setTimestamp(Instant.now());
	        err.setStatus(HttpStatus.BAD_REQUEST.value());
	        err.setError("Erro de validação");
	        err.setPath("/viagens"); // depois podemos melhorar isso

	        e.getBindingResult().getFieldErrors()
	            .forEach(f -> err.addError(f.getField(), f.getDefaultMessage()));

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	    }
	 
	    @ExceptionHandler(RegraNegocioException.class)
	    public ResponseEntity<StandardError> regraNegocio(RegraNegocioException e, HttpServletRequest request) {

	        StandardError err = new StandardError(
	            Instant.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "Erro de regra de negócio",
	            e.getMessage(),
	            request.getRequestURI()
	        );

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	    }
        
	    @ExceptionHandler(RecursoNaoEncontradoException.class)
	    public ResponseEntity<StandardError> recursoNaoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest request) {

	        StandardError err = new StandardError(
	            Instant.now(),
	            HttpStatus.NOT_FOUND.value(),
	            "Recurso não encontrado",
	            e.getMessage(),
	            request.getRequestURI()
	        );

	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	    }

}
