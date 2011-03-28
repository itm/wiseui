package eu.wisebed.wiseui.client.main.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

@ImplementedBy(WiseUiViewImpl.class)
public interface WiseUiView extends IsWidget {

    AcceptsOneWidget getNavigationPanel();

    AcceptsOneWidget getContentPanel();
}
