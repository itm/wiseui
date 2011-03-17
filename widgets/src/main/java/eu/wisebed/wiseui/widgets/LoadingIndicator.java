package eu.wisebed.wiseui.widgets;

import com.google.common.base.Preconditions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * General loading indicator that can be overlayed on any <code>Element</code>.
 * 
 * @author Malte Legenhausen
 */
public class LoadingIndicator extends Composite implements HasText {
	
    private static LoadingIndicatorUiBinder uiBinder = GWT.create(LoadingIndicatorUiBinder.class);

    interface LoadingIndicatorUiBinder extends UiBinder<Widget, LoadingIndicator> {
    }
    
    private static LoadingIndicator INSTANCE;
	
	@UiField
	Label msg;
	@UiField
	Element indicator;
	
	private Element relativeElement;
	
	private LoadingIndicator() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private static LoadingIndicator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LoadingIndicator();
		}
		return INSTANCE;
	}
	
	public Element getRelativeElement() {
		return relativeElement;
	}

	public void setRelativeElement(Element relativeElement) {
		this.relativeElement = relativeElement;
	}
	
	@Override
	public String getText() {
		return msg.getText();
	}

	@Override
	public void setText(String text) {
		msg.setText(text);
	}
	
	public LoadingIndicator show(final String text) {
		setText(text);
		
		final int left = relativeElement.getAbsoluteLeft();
		indicator.getStyle().setLeft(left, Unit.PX);
		
		final int top = relativeElement.getAbsoluteTop();
		indicator.getStyle().setTop(top, Unit.PX);
		
		final int width = relativeElement.getOffsetWidth();
		indicator.getStyle().setWidth(width, Unit.PX);
		
		final int height = relativeElement.getOffsetHeight();
		indicator.getStyle().setHeight(height, Unit.PX);
		
		RootPanel.get().add(this);
		return this;
	}
	
	public void hide() {
		RootPanel.get().remove(this);
	}
	
	public static LoadingIndicator on(final Widget widget) {
		return on(widget.getElement());
	}
	
	public static LoadingIndicator on(final Element element) {
		Preconditions.checkArgument(element != null, "Null element is now allowed for on-method");
		final LoadingIndicator indicator = getInstance();
		indicator.setRelativeElement(element);		
		return indicator;
	}
	
}
