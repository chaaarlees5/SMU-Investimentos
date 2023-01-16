/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.smuinvestimentos.provaestar.exceptionhandler;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 *
 * @author charlô
 */
@ControllerAdvice
public class MessageControllerAdvice {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageExceptionHandler   > parametersNotValid(MethodArgumentNotValidException notValid) {

        String errorMessage = notValid.getMessage();
        String defaultMessage = "";

        int indexError;
        int i = 0;

        while (i >= 0) {
            i++;

            indexError = errorMessage.indexOf("default message");
            errorMessage = errorMessage.substring(indexError + 17);

            if (i % 2 == 0) {
                indexError = errorMessage.indexOf("]");
                defaultMessage = errorMessage.substring(0, indexError);

                i = -1;
            }
        }

        MessageExceptionHandler error = new MessageExceptionHandler(defaultMessage);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public ResponseEntity<MessageExceptionHandler> formatInvalid2(BindException notValid) {

        String errorMessage = notValid.getMessage();
        String defaultMessage = "";

        System.out.println(errorMessage);

        int indexError;
        int i = 0;

        while (i >= 0) {
            i++;

            indexError = errorMessage.indexOf("default message");
            errorMessage = errorMessage.substring(indexError + 17);

            if (i % 2 == 0) {
                indexError = errorMessage.indexOf("]");
                defaultMessage = errorMessage.substring(0, indexError);

                i = -1;
            }
        }
        System.out.println(defaultMessage);

        MessageExceptionHandler error = new MessageExceptionHandler(defaultMessage);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<MessageExceptionHandler> persistenceInvalid(jakarta.validation.ConstraintViolationException notValid) {

        String errorMessage = notValid.getMessage();
        String messageTemplate;

        int indexError;

        indexError = errorMessage.indexOf("messageTemplate='");
        errorMessage = errorMessage.substring(indexError + 17);

        indexError = errorMessage.indexOf("'}");
        messageTemplate = errorMessage.substring(0, indexError);

        MessageExceptionHandler error = new MessageExceptionHandler(messageTemplate);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
