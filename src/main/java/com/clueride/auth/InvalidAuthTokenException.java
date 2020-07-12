package com.clueride.auth;

/**
 * Thrown when the Auth Token is invalid.
 *
 * Details of the reason are provided within the detailMessage.
 */
public class InvalidAuthTokenException extends RuntimeException {

    public InvalidAuthTokenException(String detailMessage) {
        super(detailMessage);
    }

}
