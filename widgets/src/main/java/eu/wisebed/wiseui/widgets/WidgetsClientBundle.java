/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
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
package eu.wisebed.wiseui.widgets;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface WidgetsClientBundle extends ClientBundle {

    @Source("eu/wisebed/wiseui/widgets/messagebox/success.png")
    ImageResource getSuccessImageResource();

    @Source("eu/wisebed/wiseui/widgets/messagebox/warning.png")
    ImageResource getWarningImageResource();

    @Source("eu/wisebed/wiseui/widgets/messagebox/error.png")
    ImageResource getErrorImageResource();
}
