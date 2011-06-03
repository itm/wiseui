/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

	/**
	 * Use this handler to execute operations when the async manager calls the ready method.
	 * 
	 * @author Malte Legenhausen
	 *
	 * @param <T>
	 */
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
