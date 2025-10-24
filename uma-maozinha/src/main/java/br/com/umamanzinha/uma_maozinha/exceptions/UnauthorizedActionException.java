package br.com.umamanzinha.uma_maozinha.exceptions;

public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message){
        super(message);
    }
}
