package eu.wisebed.wiseui.client.testbedlist.view;

import java.util.Map;
import java.util.TreeMap;

import eu.maydu.gwt.validation.client.i18n.ValidationMessages;

public class TestbedEditValidationMessages extends ValidationMessages {

	private static final Map<String, String> DESCRIPTIONS = new TreeMap<String, String>();
	
	static {
		DESCRIPTIONS.put("nameDescription", "Please enter at least 3 characters.");
		DESCRIPTIONS.put("testbedUrlDescription", "Please enter at least 3 characters.");
		DESCRIPTIONS.put("snaaEndpointUrlDescription", "Please enter at least 3 characters.");
	}
	
	@Override
	public String getDescriptionMessage(final String msgKey) {
		return DESCRIPTIONS.get(msgKey);
	}
}
