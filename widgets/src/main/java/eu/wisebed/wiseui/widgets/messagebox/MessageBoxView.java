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
package eu.wisebed.wiseui.widgets.messagebox;

import com.google.gwt.user.client.ui.IsWidget;

public interface MessageBoxView extends IsWidget {

    void setPresenter(Presenter presenter);

    String getTitle();

    void setCaption(String title);

    String getMessage();

    void setMessage(String message);

    void setMessageImageUrl(String url);

    void setButtons(String... buttons);

    void hide();

    void show();

    void setStacktrace(String stacktrace);

    void setStacktracePanelVisible(boolean isVisible);

    public interface Presenter {

        void buttonClicked(String button);

        void setStacktrace(String stacktrace);

        void setStacktracePanelVisible(boolean isVisible);
    }
}
