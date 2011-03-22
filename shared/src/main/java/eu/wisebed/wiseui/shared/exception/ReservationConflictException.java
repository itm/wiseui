package eu.wisebed.wiseui.shared.exception;

import java.io.Serializable;

public class ReservationConflictException extends Exception implements Serializable {

    private static final long serialVersionUID = -1880549735099765183L;

    public ReservationConflictException() {
        super();
    }

    public ReservationConflictException(String cause) {
        super(cause);
    }
}
