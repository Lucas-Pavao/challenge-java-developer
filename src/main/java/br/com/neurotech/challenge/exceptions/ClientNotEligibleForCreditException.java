package br.com.neurotech.challenge.exceptions;

public class ClientNotEligibleForCreditException extends RuntimeException {
    public ClientNotEligibleForCreditException(String message) {
        super(message);
    }
}
