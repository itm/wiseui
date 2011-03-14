package eu.wisebed.wiseui.shared.exception;

import java.io.Serializable;

public class ReservationException extends Exception implements Serializable{
	
	private static final long serialVersionUID = 2632988462725380171L;

	public ReservationException() { super(); }
	
	public ReservationException(String cause) { 
		super(cause);
	}
}