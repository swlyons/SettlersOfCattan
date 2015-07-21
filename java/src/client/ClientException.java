/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author ddennis
 */
@SuppressWarnings("serial")
public class ClientException extends Exception {

    public ClientException() {
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(Throwable throwable) {
        super(throwable);
    }

    public ClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
