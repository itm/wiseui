package eu.wisebed.wiseui.client.testbedselection.view;

import com.alexgorbatchev.syntaxhighlighter.client.Highlighter;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.IsWidget;

public interface RawWisemlView extends IsWidget {

	Highlighter getHighlighter();
	
	void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
	}
}
