package eu.wisebed.wiseui.client.reservation.event;

import com.google.gwt.event.shared.GwtEvent;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

public class TestbedSelectedChangedEvent extends GwtEvent<TestbedSelectedChangedEventHandler>{
	public static Type<TestbedSelectedChangedEventHandler> TYPE = 
		new Type<TestbedSelectedChangedEventHandler>();

	private final TestbedConfiguration testbedSelected;
	
	@Override
	public Type<TestbedSelectedChangedEventHandler> getAssociatedType(){
		return TYPE;
	}
	
	@Override
	protected void dispatch(TestbedSelectedChangedEventHandler handler){
		handler.onTestbedSelectedChanged(this);
	}
	
    public TestbedSelectedChangedEvent(final TestbedConfiguration testbedSelected) {
        this.testbedSelected = testbedSelected;
    }
    
    public TestbedConfiguration getTestbedSelected() {
        return testbedSelected;
    }
}
