package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import eu.wisebed.wiseui.client.testbedselection.common.UrnPrefixInfo;
import eu.wisebed.wiseui.client.testbedselection.common.UrnPrefixInfo.State;
import eu.wisebed.wiseui.client.util.HasWidgetsDialogBox;

public class LoginDialogViewImpl extends HasWidgetsDialogBox implements LoginDialogView {

    private static LoginDialogViewImplUiBinder uiBinder = GWT.create(LoginDialogViewImplUiBinder.class);

    interface LoginDialogViewImplUiBinder extends UiBinder<Widget, LoginDialogViewImpl> {
    }

    private class UrnPrefixInfoCell extends AbstractEditableCell<UrnPrefixInfo, Boolean> {

        public static final String FAILURE_COLOR = "#FFBBBB";

        public static final String SUCCESS_COLOR = "#BBFFBB";

        public static final String DEFAULT_COLOR = "#FFFFFF";

        private CheckboxCell cell = new CheckboxCell();

        public UrnPrefixInfoCell() {
            super("change", "keydown");
        }

        @Override
        public void onBrowserEvent(final Element parent, final UrnPrefixInfo info, final Object key, final NativeEvent event,
                                   final ValueUpdater<UrnPrefixInfo> valueUpdater) {

            final Element checkboxParent = parent.getFirstChildElement().getFirstChildElement().getFirstChildElement().getFirstChildElement();
            cell.onBrowserEvent(checkboxParent, info.isChecked(), key, event, new ValueUpdater<Boolean>() {
                public void update(final Boolean value) {
                    info.setChecked(value);
                }
            });
            super.onBrowserEvent(parent, info, key, event, valueUpdater);
        }

        @Override
        public void render(final UrnPrefixInfo info, final Object key, final SafeHtmlBuilder sb) {
            // Value can be null, so do a null check..
            if (info == null) {
                return;
            }

            String color = DEFAULT_COLOR;
            if (info.getState().equals(State.FAILED)) {
                color = FAILURE_COLOR;
            } else if (info.getState().equals(State.SUCCESS)) {
                color = SUCCESS_COLOR;
            }

            sb.appendHtmlConstant("<table style='background-color:" + color + "'>");
            sb.appendHtmlConstant("<tr><td rowspan='2'>");
            cell.render(info.isChecked(), key, sb);
            sb.appendHtmlConstant("</td>");
            sb.appendHtmlConstant("<td style='width:100%'>");
            sb.appendEscaped(info.getUrnPrefix());
            sb.appendHtmlConstant("</td></tr><tr><td>");
            sb.appendEscaped(info.getState().toString());
            sb.appendHtmlConstant("</td></tr></table>");
        }

        @Override
        public boolean isEditing(final Element parent, final UrnPrefixInfo value, final Object key) {
            return cell.isEditing(parent, value.isChecked(), key);
        }
    }

    @UiField
    CellList<UrnPrefixInfo> urnPrefixList;

    @UiField
    TextBox username;

    @UiField
    PasswordTextBox password;

    @UiField
    Button submit;

    @UiField
    Button cancel;

    private Presenter presenter;

    @Inject
    public LoginDialogViewImpl() {
        uiBinder.createAndBindUi(this);

        setModal(true);
        setGlassEnabled(true);
        setAnimationEnabled(true);
    }

    @UiFactory
    protected CellList<UrnPrefixInfo> createCellList() {
        return new CellList<UrnPrefixInfo>(new UrnPrefixInfoCell());
    }

    @UiFactory
    protected LoginDialogViewImpl createDialog() {
        return this;
    }

    @UiHandler("submit")
    public void onSubmit(final ClickEvent event) {
        presenter.submit();
    }

    @UiHandler("cancel")
    public void onCancel(final ClickEvent event) {
        presenter.cancel();
    }

    public void setPresenter(final Presenter presenter) {
        this.presenter = presenter;
    }

    public HasText getUsernameText() {
        return username;
    }

    public HasText getPasswordText() {
        return password;
    }

    public HasEnabled getUsernameEnabled() {
        return username;
    }

    public HasEnabled getPasswordEnabled() {
        return password;
    }

    public void show(final String title) {
        setText(title);
        center();
        show();
    }

    public HasData<UrnPrefixInfo> getUrnPrefixList() {
        return urnPrefixList;
    }

    public HasEnabled getSubmitEnabled() {
        return submit;
    }

    public HasEnabled getCancelEnabled() {
        return cancel;
    }
}
