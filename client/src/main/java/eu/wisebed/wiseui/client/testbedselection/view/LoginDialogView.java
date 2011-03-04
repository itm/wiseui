package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.view.client.HasData;
import eu.wisebed.wiseui.client.testbedselection.TestbedSelectionPlace;
import eu.wisebed.wiseui.client.testbedselection.common.UrnPrefixInfo;

public interface LoginDialogView {

    HasText getUsernameText();

    HasText getPasswordText();

    HasEnabled getUsernameEnabled();

    HasEnabled getPasswordEnabled();

    HasEnabled getSubmitEnabled();

    HasEnabled getCancelEnabled();

    HasData<UrnPrefixInfo> getUrnPrefixList();

    void setPresenter(Presenter presenter);

    void show(String title);

    void hide();

    public interface Presenter {

        void setPlace(TestbedSelectionPlace place);

        void submit();

        void cancel();
    }
}
