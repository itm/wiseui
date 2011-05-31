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
package eu.wisebed.wiseui.widgets.messagebox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import eu.wisebed.wiseui.widgets.HasWidgetsDialogBox;

public class MessageBoxViewImpl extends HasWidgetsDialogBox implements MessageBoxView, ClickHandler {

    private static MessageBoxViewImplUiBinder uiBinder = GWT.create(MessageBoxViewImplUiBinder.class);

    interface MessageBoxViewImplUiBinder extends UiBinder<Widget, MessageBoxViewImpl> {
    }

    private Presenter presenter;
    @UiField
    Label message;
    @UiField
    Image image;
    @UiField
    FlexTable buttonTable;
    @UiField
    DisclosurePanel stacktracePanel;
    @UiField
    Label stacktraceLabel;

    public MessageBoxViewImpl() {
        uiBinder.createAndBindUi(this);

        setModal(true);
        setGlassEnabled(true);
        setAnimationEnabled(true);
        setWidth("350px");
    }

    @UiFactory
    protected MessageBoxViewImpl createDialog() {
        return this;
    }

    @Override
    public void setPresenter(final Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getMessage() {
        return message.getText();
    }

    @Override
    public void setMessage(final String text) {
        message.setText(text);
    }

    @Override
    public void setMessageImageUrl(final String url) {
        image.setUrl(url);
    }

    @Override
    public void setButtons(final String... buttons) {
        buttonTable.clear();
        int i = 0;
        for (String label : buttons) {
            final Button button = new Button(label, this);
            button.setWidth("75px");
            buttonTable.setWidget(0, i++, button);
        }
    }

    @Override
    public void onClick(final ClickEvent event) {
        final Button button = (Button) event.getSource();
        presenter.buttonClicked(button.getText());
    }

    @Override
    public void setCaption(final String title) {
        setText(title);
    }

    @Override
    public void show() {
        super.show();
        center();
    }

    @Override
    public void setStacktrace(final String stacktrace) {
        stacktraceLabel.setText(stacktrace);
    }

    @Override
    public void setStacktracePanelVisible(final boolean isVisible) {
        stacktracePanel.setVisible(isVisible);
    }
}
