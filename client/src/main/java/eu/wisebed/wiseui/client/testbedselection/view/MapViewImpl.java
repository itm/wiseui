package eu.wisebed.wiseui.client.testbedselection.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import eu.wisebed.wiseui.shared.wiseml.Coordinate;
import eu.wisebed.wiseui.shared.wiseml.Node;

public class MapViewImpl extends Composite implements MapView {
	
	private static MapViewImplUiBinder uiBinder = GWT
			.create(MapViewImplUiBinder.class);

	interface MapViewImplUiBinder extends UiBinder<Widget, MapViewImpl> {
	}
	
    private static final int DEFAULT_ZOOM_LEVEL = 2;

    private static final int ZOOM_LEVEL = 8;
	
	@UiField
	SimplePanel mapContainer;
	
	private MapWidget mapWidget;
	
    private Marker testbedMarker;
    
    private InfoWindow testbedInfoWindow;
    
    private Coordinate coordinate;
    
    private String title;
    
    private String description;
    
    private List<Node> nodes;
    
    private List<Marker> nodeMarkers = new ArrayList<Marker>();
	
	public MapViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				initMap();
			}
		});
	}

	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub

	}
	
	public void initMap() {
		final Size size = Size.newInstance(100, 100);
		final MapUIOptions options = MapUIOptions.newInstance(size);
		options.setScrollwheel(true);
		options.setMapTypeControl(true);
		options.setHybridMapType(true);
		options.setLargeMapControl3d(true);
		options.setPhysicalMapType(true);
		options.setSatelliteMapType(true);
		options.setScaleControl(true);
		options.setDoubleClick(true);
		options.setNormalMapType(true);
		options.setKeyboard(true);		
		
		mapWidget = new MapWidget();
		mapWidget.setUI(options);
        mapWidget.setSize("100%", "100%");
        mapWidget.setContinuousZoom(true);
        mapContainer.add(mapWidget);
        
        updateMap();
        updateNodes();
	}
	
    private void updateMap() {
    	if (testbedMarker != null && testbedInfoWindow != null) {
            mapWidget.removeOverlay(testbedMarker);
            testbedInfoWindow.close();
        }
    	
    	LatLng center = LatLng.newInstance(0.0, 0.0);
    	if (coordinate != null) {
	        testbedMarker = new Marker(convert(coordinate));
	        
	        mapWidget.addOverlay(testbedMarker);
	        mapWidget.setZoomLevel(ZOOM_LEVEL);
	        
	        final HTML htmlWidget = new HTML("<b>" + title + "</b><p>" + description + "</p>");
	        testbedInfoWindow = mapWidget.getInfoWindow();
	        testbedInfoWindow.open(testbedMarker, new InfoWindowContent(htmlWidget));
    	} else {
    		mapWidget.setZoomLevel(DEFAULT_ZOOM_LEVEL);
    	}
        mapWidget.setCenter(center);
        
	    
    }
    
    private void updateNodes() {
		for (final Marker marker : nodeMarkers) {
			mapWidget.removeOverlay(marker);
		}
		nodeMarkers.clear();
    	
		if (nodes != null) {
	    	final Icon icon = Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_red.png");
		    icon.setShadowURL("http://labs.google.com/ridefinder/images/mm_20_shadow.png");
		    icon.setIconSize(Size.newInstance(12, 20));
		    icon.setShadowSize(Size.newInstance(22, 20));
		    icon.setIconAnchor(Point.newInstance(6, 20));
		    icon.setInfoWindowAnchor(Point.newInstance(5, 1));
		    
		    final MarkerOptions options = MarkerOptions.newInstance();
		    options.setIcon(icon);
		    
		    final LatLng center = convert(coordinate);
	    	for (final Node node : nodes) {
	    		final LatLng point = LatLng.newInstance(center.getLatitude() + node.getPosition().getX(), 
	    				center.getLongitude() + node.getPosition().getY());

	    		final Marker marker = new Marker(point, options);
	    		mapWidget.addOverlay(marker);
	    		nodeMarkers.add(marker);
	    	}
		}
    }

	public void setTestbedCoordinate(final Coordinate coordinate, final String title, final String description) {
		this.coordinate = coordinate;
		this.title = title;
		this.description = description;
		
		if (mapWidget != null) {
			updateMap();
		}	
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
		
		if (mapWidget != null) {
			updateNodes();
		}
	}

	public static LatLng convert(final Coordinate coordinate) {
		final double x = coordinate.getX();
        final double y = coordinate.getY();
        return LatLng.newInstance(x, y);
	}
}
