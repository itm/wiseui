package eu.wisebed.wiseui.client.util;


/**
 * This class can be used when waiting for an async object but want to execute synchron methods.
 * Simple add a handler that will be called when the ready method was called.
 * 
 * @author Malte Legenhausen
 *
 * @param <T> The type of the ready object.
 */
public class AsyncManager<T> {

	public interface Handler<T> {
		void execute(T value);
	}
	
	private T value = null;
	
	private Handler<T> handler = null;
	
	public void setHandler(final Handler<T> handler) {
		if (value != null) {
			handler.execute(value);
		} else {
			this.handler = handler;
		}
	}
	
	public void ready(final T value) {
		this.value = value;
		if (handler != null) {
			handler.execute(value);
		}
	}
}
