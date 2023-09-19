package estevao.market;

import estevao.market.dto.ObjectErrorDTO;
import estevao.market.exception.MarketException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler {

    // Tem que implementar essa exceção dentro do projeto
    // Capturando exceção customizada do sistema MarketException
    @ExceptionHandler(MarketException.class)
    public ResponseEntity<Object> handleExceptionCustom(MarketException e) {
        ObjectErrorDTO obj = new ObjectErrorDTO();

        obj.setError(e.getMessage());
        obj.setCode(HttpStatus.OK.toString());

        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // Captura exceções do projeto
    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ObjectErrorDTO errorDTO = new ObjectErrorDTO();

        StringBuilder str = new StringBuilder();

        if (ex instanceof MethodArgumentNotValidException) {
            List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
            for (ObjectError obj: list) {
                str.append(obj.getDefaultMessage()).append("\n");
            }
        } else {
            str.append(ex.getMessage());
        }

        errorDTO.setError(str.toString());
        errorDTO.setCode(status.value() + " ==> " + status.getReasonPhrase());

        ex.printStackTrace();

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // captura exceção da parte do banco de dados
    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLDataException.class, SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {

        ObjectErrorDTO errorDTO = new ObjectErrorDTO();

        StringBuilder str = new StringBuilder();

        if (ex instanceof SQLException) {
            str.append("Erro de SQL no banco: ").append(ex.getCause().getCause().getMessage());
        } else {
            str.append(ex.getMessage());
        }if (ex instanceof DataIntegrityViolationException) {
            str.append("Erro de Integridade no banco: ").append(ex.getCause().getCause().getMessage());
        } else {
            str.append(ex.getMessage());
        }if (ex instanceof ConstraintViolationException) {
            str.append("Erro de chave estrangeira: ").append(ex.getCause().getCause().getMessage());
        } else {
            str.append(ex.getMessage());
        }if (ex instanceof SQLDataException) {
            str.append("Erro de dados retornados no SQL: ").append(ex.getCause().getCause().getMessage());
        } else {
            str.append(ex.getMessage());
        }

        errorDTO.setError(str.toString());
        errorDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

        ex.printStackTrace();

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
