package eu.wisebed.wiseui.client.util;

import java.util.ArrayList;
import java.util.List;

public class BindingManager implements Bindable {

	private final List<Bindable> bindings = new ArrayList<Bindable>();
	
	public void add(final Bindable bindable) {
		bindings.add(bindable);
	}
	
	@Override
	public void bind() {
		for (final Bindable bindable : bindings) {
			bindable.bind();
		}
	}
	
	@Override
	public void unbind() {
		for (final Bindable bindable : bindings) {
			bindable.unbind();
		}
	}
}
