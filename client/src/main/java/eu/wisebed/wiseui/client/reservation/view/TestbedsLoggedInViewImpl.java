package eu.wisebed.wiseui.client.reservation.view;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.shared.TestbedConfiguration;

@Singleton
public class TestbedsLoggedInViewImpl extends Composite 
	implements TestbedsLoggedInView{

	@UiTemplate("TestbedsLoggedInViewImpl.ui.xml")
    interface TestbedsLoggedInViewImplUiBinder extends
            UiBinder<Widget, TestbedsLoggedInViewImpl> {
    }
	
	private static TestbedsLoggedInViewImplUiBinder uiBinder = GWT
    	.create(TestbedsLoggedInViewImplUiBinder.class);

	public TestbedsLoggedInViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField HTMLPanel testbedPanel;
	@UiField CellList<TestbedConfiguration> testbedList;
	
	@UiFactory
	public CellList<TestbedConfiguration> testbedsLoggedIn(){
		final Cell<TestbedConfiguration> testbed = 
			new AbstractCell<TestbedConfiguration>() {
            public void render(final Context context,
                    final TestbedConfiguration testbed,
                    final SafeHtmlBuilder builder) {
                for(int i=0; i<testbed.getUrnPrefixList().size(); i++){
                	builder.appendHtmlConstant("<div class=\"celllist-entry\">");
                	builder.appendEscaped(testbed.getName());
                	builder.appendHtmlConstant("</div>");
                }
            }
		};
		return  new CellList<TestbedConfiguration>(testbed);		
	}

	public CellList<TestbedConfiguration> getTestbedListPanel(){
		return testbedList;
	}

    @Override
    public void renderTestbeds(final List<TestbedConfiguration> testbeds) {
        testbedList.setRowCount(testbeds.size());
        testbedList.setRowData(0, testbeds);
    }
    
    public HasData<TestbedConfiguration> getTestbedList(){
    	return testbedList;
    }
    
    @Override
    public void setTestbedSelectionModel(
    		final SelectionModel<TestbedConfiguration> selectionModel) {
        testbedList.setSelectionModel(selectionModel);
    }
}
