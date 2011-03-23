package eu.wisebed.wiseui.client.testbedselection.view;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

import java.util.List;

public class ConfigurationViewImpl extends Composite implements ConfigurationView {

    private static ConfigurationViewImplUiBinder uiBinder = GWT.create(ConfigurationViewImplUiBinder.class);

    interface ConfigurationViewImplUiBinder extends UiBinder<Widget, ConfigurationViewImpl> {
    }
    @UiField
    CellList<TestbedConfiguration> configurationList;

    public ConfigurationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiFactory
    public CellList<TestbedConfiguration> createTestbedConfigurationCellList() {
        final Cell<TestbedConfiguration> cell = new AbstractCell<TestbedConfiguration>() {

            @Override
            public void render(final Context context,
                               final TestbedConfiguration configuration,
                               final SafeHtmlBuilder builder) {
                builder.appendHtmlConstant("<div class=\"celllist-entry\">");
                builder.appendEscaped(configuration.getName());
                builder.appendHtmlConstant("</div>");
            }
        };
        return new CellList<TestbedConfiguration>(cell);
    }

    @Override
    public void setPresenter(final Presenter presenter) {
    }

    @Override
    public void setConfigurations(final List<TestbedConfiguration> configurations) {
        configurationList.setRowCount(configurations.size());
        configurationList.setRowData(0, configurations);
    }

    @Override
    public void setTestbedConfigurationSelectionModel(final SelectionModel<TestbedConfiguration> selectionModel) {
        configurationList.setSelectionModel(selectionModel);
    }
}
