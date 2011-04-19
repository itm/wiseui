package eu.wisebed.wiseui.client.testbedlist.view;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;


@Singleton
public class TestbedListViewImpl extends Composite implements TestbedListView {

    private static TestbedListViewImplUiBinder uiBinder = GWT.create(TestbedListViewImplUiBinder.class);

    interface TestbedListViewImplUiBinder extends UiBinder<Widget, TestbedListViewImpl> {
    }
    
    @UiField
    CellList<TestbedConfiguration> configurationList;
    @UiField
    Button loginButton;
    @UiField
    MenuBar menuBar;
    
    private Presenter presenter;
    
    private final MenuItem loginMenuItem;
    
    private final MenuItem testbedEditMenuItem;
    
    private final MenuItem testbedDeleteMenuItem;

    public TestbedListViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        
        final Command loginCommand = new Command() {
			@Override
			public void execute() {
				presenter.showLoginDialog();
			}
		};
		final Command newCommand = new Command() {
			@Override
			public void execute() {
				presenter.showNewTestbedDialog();
			}
		};
		final Command editCommand = new Command() {
			@Override
			public void execute() {
				presenter.showEditTestbedDialog();
			}
		};
		final Command deleteCommand = new Command() {
			@Override
			public void execute() {
				presenter.deleteTestbed();
			}
		};
		final Command refreshCommand = new Command() {
			@Override
			public void execute() {
				presenter.refresh();
			}
		};
        
        final MenuBar menu = new MenuBar(true);
        loginMenuItem = menu.addItem("Login...", loginCommand);
        menu.addSeparator();
        menu.addItem("New...", newCommand);
        testbedEditMenuItem = menu.addItem("Edit...", editCommand);
        testbedDeleteMenuItem = menu.addItem("Delete", deleteCommand);
        menu.addSeparator();
        menu.addItem("Refresh", refreshCommand);
        menuBar.addItem("Menu", menu);
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

    @UiHandler("loginButton")
    public void handleLoginClick(final ClickEvent event) {
        presenter.showLoginDialog();
    }

    @Override
    public void setPresenter(final Presenter presenter) {
    	this.presenter = presenter;
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
    
    @Override
    public HasEnabled getLoginEnabled() {
        return new HasEnabled() {
			
			@Override
			public void setEnabled(boolean enabled) {
				loginButton.setEnabled(enabled);
				loginMenuItem.setEnabled(enabled);
			}
			
			@Override
			public boolean isEnabled() {
				return loginButton.isEnabled() && loginMenuItem.isEnabled();
			}
		};
    }

	@Override
	public HasEnabled getTestbedEditEnabled() {
		return testbedEditMenuItem;
	}

	@Override
	public HasEnabled getTestbedDeleteEnabled() {
		return testbedDeleteMenuItem;
	}
}
