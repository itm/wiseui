package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.view.client.HasData;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.client.testbedselection.common.UrnPrefixInfo;
import eu.wisebed.wiseui.client.util.Bindable;

@ImplementedBy(LoginDialogViewImpl.class)
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

    public interface Presenter extends Bindable {

        void submit();

        void cancel();
    }
}
