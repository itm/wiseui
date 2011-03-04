package eu.wisebed.wiseui.client.util;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.util.gin.UtilGinjector;
import eu.wisebed.wiseui.client.util.view.MessageBoxView;
import eu.wisebed.wiseui.client.util.view.MessageBoxView.Presenter;

public class MessageBox implements Presenter {

    public enum Type {

        INFO, WARNING, ERROR, SUCCESS
    }

    public enum Button {

        OK("OK"),
        CANCEL("Cancel"),
        YES("Yes"),
        NO("No");
        private final String value;

        private Button(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Button fromValue(final String value) {
            for (Button button : Button.values()) {
                if (button.getValue().equals(value)) {
                    return button;
                }
            }
            throw new IllegalArgumentException("Unknown Button value: " + value);
        }
    }

    public interface Callback {

        void onButtonClicked(final Button button);
    }

    private static final UtilGinjector INJECTOR = GWT.create(UtilGinjector.class);
    private static final UtilClientBundle BUNDLE = GWT.create(UtilClientBundle.class);
    private final MessageBoxView view;
    private Callback callback;

    @Inject
    public MessageBox(final MessageBoxView view) {
        this.view = view;
        view.setPresenter(this);
    }

    public static void warning(final String title, final String message, final Callback callback) {
        final MessageBox messageBox = INJECTOR.getMessageBox();
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setMessageType(Type.WARNING);
        messageBox.setButtons(Button.OK, Button.CANCEL);
        messageBox.setCallback(callback);
        messageBox.show();
    }

    public static void success(final String title, final String message, final Callback callback) {
        final MessageBox messageBox = INJECTOR.getMessageBox();
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setMessageType(Type.SUCCESS);
        messageBox.setButtons(Button.OK);
        messageBox.setCallback(callback);
        messageBox.show();
    }

    public static void error(final String title, final String message, final Throwable throwable, final Callback callback) {
        final MessageBox messageBox = INJECTOR.getMessageBox();
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setMessageType(Type.ERROR);
        messageBox.setButtons(Button.OK);
        messageBox.setCallback(callback);
        if (throwable != null) {
            messageBox.setStacktrace(StacktraceUtil.stacktraceToString(throwable));
            messageBox.setStacktracePanelVisible(true);
        }
        messageBox.show();
    }

    public static void info(final String title, final String message, final Callback callback) {
        final MessageBox messageBox = INJECTOR.getMessageBox();
        messageBox.setTitle(title);
        messageBox.setMessage(message);
        messageBox.setMessageType(Type.SUCCESS);
        messageBox.setButtons(Button.OK);
        messageBox.setCallback(callback);
        messageBox.show();
    }

    public void buttonClicked(final String button) {
        if (callback != null) {
            callback.onButtonClicked(Button.fromValue(button));
        }
        view.hide();
    }

    public void setMessageType(final Type type) {
        String url = BUNDLE.getSuccessImageResource().getURL();
        if (type.equals(Type.ERROR)) {
            url = BUNDLE.getErrorImageResource().getURL();
        } else if (type.equals(Type.WARNING)) {
            url = BUNDLE.getErrorImageResource().getURL();
        }
        view.setMessageImageUrl(url);
    }

    public void setButtons(final Button... buttons) {
        final String[] strings = new String[buttons.length];
        int i = 0;
        for (final Button button : buttons) {
            strings[i++] = button.getValue();
        }
        view.setButtons(strings);
    }

    public void setTitle(final String title) {
        view.setCaption(title);
    }

    public void setMessage(final String message) {
        view.setMessage(message);
    }

    public void setCallback(final Callback callback) {
        this.callback = callback;
    }

    public void show() {
        view.show();
    }

    public void setStacktrace(final String stacktrace) {
        view.setStacktrace(stacktrace);
    }

    public void setStacktracePanelVisible(final boolean isVisible) {
        view.setStacktracePanelVisible(isVisible);
    }
}
