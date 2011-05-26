/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.reservation.event;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import eu.wisebed.wiseui.shared.dto.Node;

public class UpdateNodesSelectedEvent extends GwtEvent<UpdateNodesSelectedEvent.Handler> {

	public interface Handler extends EventHandler {
		void onUpdateNodesSelected(UpdateNodesSelectedEvent event);
	}

	public static final Type<Handler> TYPE = new Type<Handler>();

	private final Set<Node> nodes;

	public UpdateNodesSelectedEvent(final Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Set<Node> getNodes(){
		return this.nodes;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final Handler handler) {
		handler.onUpdateNodesSelected(this);
	}
}
