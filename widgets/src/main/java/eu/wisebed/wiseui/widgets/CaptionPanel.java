package eu.wisebed.wiseui.widgets;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CaptionPanel extends Composite implements HasWidgets {

	private static CaptionPanelUiBinder uiBinder = GWT.create(CaptionPanelUiBinder.class);

    interface CaptionPanelUiBinder extends UiBinder<Widget, CaptionPanel> {
    }
	
	@UiField
	Label caption;
	
	@UiField
	SimplePanel content;
	
	public CaptionPanel() {
		this("");
	}
	
	@UiConstructor
	public CaptionPanel(final String caption) {
		initWidget(uiBinder.createAndBindUi(this));
		this.caption.setText(caption);
	}
	
	public HasText getCaption() {
		return caption;
	}
	
	@Override
	public void add(Widget widgets) {
		content.add(widgets);
	}

	@Override
	public void clear() {
		content.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return content.iterator();
	}

	@Override
	public boolean remove(Widget widgets) {
		return content.remove(widgets);
	}
}
