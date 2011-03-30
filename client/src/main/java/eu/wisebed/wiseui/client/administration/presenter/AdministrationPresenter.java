package eu.wisebed.wiseui.client.administration.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

import eu.wisebed.wiseui.client.administration.AdministrationPlace;
import eu.wisebed.wiseui.client.administration.view.AdministrationView;
import eu.wisebed.wiseui.client.administration.view.AdministrationView.Presenter;

public class AdministrationPresenter implements Presenter {

	private final EventBus eventBus;
	private final AdministrationView view;
	
	@Inject
	public AdministrationPresenter(final EventBus eventBus, final AdministrationView view) {
		this.eventBus = eventBus;
		this.view = view;
		bind();
	}
	
	private void bind() {
		
	}
	
	@Override
	public void setPlace(AdministrationPlace place) {
		
	}

	@Override
	public void create() {
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

}
