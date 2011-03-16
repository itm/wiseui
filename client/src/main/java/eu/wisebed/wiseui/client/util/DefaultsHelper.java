package eu.wisebed.wiseui.client.util;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import eu.wisebed.wiseui.shared.wiseml.Defaults;
import eu.wisebed.wiseui.shared.wiseml.Node;
import eu.wisebed.wiseui.shared.wiseml.NodeProperties;
import eu.wisebed.wiseui.shared.wiseml.Setup;


/**
 * Helper class for applying all defaults from the WiseML Setup object.
 * 
 * @author Malte Legenhausen
 */
public class DefaultsHelper {

	private final Setup setup;
	
	private DefaultsHelper(final Setup setup) {
		this.setup = setup;
	}
	
	public static @Nullable Setup apply(@Nullable final Setup setup) {
		final DefaultsHelper helper = new DefaultsHelper(setup);
		return helper.applyDefaults();
	}
	
	private Setup applyDefaults() {
		if (setup != null) {
			final Defaults defaults = setup.getDefaults();
			if (defaults != null) {
				final NodeProperties nodeProperties = defaults.getNode();
				if (nodeProperties != null && setup.getNode() != null) {
					applyNodeProperties(nodeProperties);
				}
			}
		}
		return setup;
	}
	
	private void applyNodeProperties(final NodeProperties properties) {
		final List<Node> nodes = Lists.transform(setup.getNode(), new Function<Node, Node>() {
			@Override
			public Node apply(final Node node) {
				if (Strings.isNullOrEmpty(node.getDescription())) {
					node.setDescription(properties.getDescription());
				}
				if (node.isGateway() == null) {
					node.setGateway(properties.getGateway());
				}
				if (Strings.isNullOrEmpty(node.getProgramDetails())) {
					node.setProgramDetails(properties.getProgramDetails());
				}
				if (Strings.isNullOrEmpty(node.getNodeType())) {
					node.setNodeType(properties.getNodeType());
				}
				if (node.getPosition() == null) {
					node.setPosition(properties.getPosition());
				}
				if (Lists2.isNullorEmpty(node.getCapability())) {
					node.setCapability(properties.getCapability());
				}
				return node;
			}
		});
		setup.setNode(nodes);
	}
}
