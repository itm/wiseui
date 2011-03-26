package eu.wisebed.wiseui.shared.exception;

import java.io.Serializable;

public class ExperimentationException extends Exception implements Serializable{

	private static final long serialVersionUID = -7254113422008099645L;

	public ExperimentationException() { super(); }
	
	public ExperimentationException(String cause) { 
		super(cause);
	}
	
	public ExperimentationException(String cause, Throwable throwable){
		super(cause,throwable);
	}
}
