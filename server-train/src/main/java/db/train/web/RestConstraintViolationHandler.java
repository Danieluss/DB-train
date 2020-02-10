package db.train.web;

import org.hibernate.JDBCException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RestConstraintViolationHandler extends ResponseEntityExceptionHandler {

    private List<String> getErrors(PSQLException sqlException) {
        List<String> errors = new ArrayList<>();
        do {
            errors.add(sqlException.getServerErrorMessage().toString());
        } while((sqlException = (PSQLException) sqlException.getNextException()) != null);
        return errors;
    }

    @ExceptionHandler(value = { JDBCException.class})
    protected ResponseEntity<Object> handleConflict(JDBCException ex, WebRequest request) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        Map<String, List<String>> returnObject = new HashMap<>();
        List<String> errorList = getErrors((PSQLException) ex.getSQLException());
        returnObject.put("errors", errorList);
        return handleExceptionInternal(ex, returnObject,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
