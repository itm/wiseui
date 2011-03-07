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
