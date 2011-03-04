package eu.wisebed.wiseui.client.util.view;

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
import eu.wisebed.wiseui.client.util.HasWidgetsDialogBox;

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

    public void setPresenter(final Presenter presenter) {
        this.presenter = presenter;
    }

    public String getMessage() {
        return message.getText();
    }

    public void setMessage(final String text) {
        message.setText(text);
    }

    public void setMessageImageUrl(final String url) {
        image.setUrl(url);
    }

    public void setButtons(final String... buttons) {
        buttonTable.clear();
        int i = 0;
        for (String label : buttons) {
            final Button button = new Button(label, this);
            button.setWidth("75px");
            buttonTable.setWidget(0, i++, button);
        }
    }

    public void onClick(final ClickEvent event) {
        final Button button = (Button) event.getSource();
        presenter.buttonClicked(button.getText());
    }

    public void setCaption(final String title) {
        setText(title);
    }

    @Override
    public void show() {
        super.show();
        center();
    }

    public void setStacktrace(final String stacktrace) {
        stacktraceLabel.setText(stacktrace);
    }

    public void setStacktracePanelVisible(final boolean isVisible) {
        stacktracePanel.setVisible(isVisible);
    }
}
