/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.client.testbedselection.view;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.OverviewMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.client.util.AsyncManager;
import eu.wisebed.wiseui.client.util.MapApiLoader;
import eu.wisebed.wiseui.shared.dto.Coordinate;
import eu.wisebed.wiseui.widgets.CaptionPanel;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

@Singleton
public class MapViewImpl extends Composite implements MapView {

    private static MapViewImplUiBinder uiBinder = GWT.create(MapViewImplUiBinder.class);

    interface MapViewImplUiBinder extends UiBinder<Widget, MapViewImpl> {
    }
    private static final int DEFAULT_ZOOM_LEVEL = 2;
    private static final int ZOOM_LEVEL = 18;

	@UiField
	SimplePanel mapContainer;
	@UiField
	CaptionPanel container;

	private MapWidget mapWidget;

    private Marker testbedMarker;
    private Polygon testbedShape;
    private InfoWindow testbedInfoWindow;
    private final AsyncManager<MapWidget> markerManager = new AsyncManager<MapWidget>();
    private final AsyncManager<MapWidget> polygonManager = new AsyncManager<MapWidget>();

    public MapViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        MapApiLoader.get().loadMapApi(new Runnable() {
			@Override
			public void run() {
				initMap();
			}
		});
    }

    @Override
    public void setPresenter(final Presenter presenter) {
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
        mapWidget.setCurrentMapType(MapType.getHybridMap());
        mapWidget.addControl(new OverviewMapControl());
        mapWidget.setContinuousZoom(true);
        mapWidget.setSize("100%", "100%");
        mapWidget.setZoomLevel(DEFAULT_ZOOM_LEVEL);
        mapContainer.add(mapWidget);

        markerManager.ready(mapWidget);
        polygonManager.ready(mapWidget);
    }
    
    public static LatLng convert(final Coordinate coordinate) {
        final double x = coordinate.getX();
        final double y = coordinate.getY();
        return LatLng.newInstance(x, y);
    }
    
    private void drawTestbedCoordinate(final MapWidget mapWidget, final Coordinate coordinate, final String title, final String description) {
    	if (testbedMarker != null && testbedInfoWindow != null) {
            mapWidget.removeOverlay(testbedMarker);
            testbedInfoWindow.close();
        }

        LatLng center = LatLng.newInstance(0.0, 0.0);
        if (coordinate != null) {
            center = convert(coordinate);
            final HTML htmlWidget = new HTML("<b>" + title + "</b><p>" + description + "</p>");
            final InfoWindowContent testbedInfoWindowContent = new InfoWindowContent(htmlWidget);
            testbedMarker = new Marker(center);
            testbedMarker.addMarkerClickHandler(new MarkerClickHandler() {
				@Override
				public void onClick(MarkerClickEvent event) {
					testbedInfoWindow.open(testbedMarker, testbedInfoWindowContent);
				}
            });

            mapWidget.addOverlay(testbedMarker);
            mapWidget.setZoomLevel(ZOOM_LEVEL);

            testbedInfoWindow = mapWidget.getInfoWindow();
            testbedInfoWindow.open(testbedMarker, testbedInfoWindowContent);
        } else {
            mapWidget.setZoomLevel(DEFAULT_ZOOM_LEVEL);
        }
        mapWidget.setCenter(center);
        mapWidget.checkResizeAndCenter();
    }

    @Override
    public void setTestbedCoordinate(final Coordinate coordinate, final String title, final String description) {
        markerManager.setHandler(new AsyncManager.Handler<MapWidget>() {
			@Override
			public void execute(final MapWidget mapWidget) {
				drawTestbedCoordinate(mapWidget, coordinate, title, description);
			}
		});
    }
    
    private void drawTestbedShape(final MapWidget mapWidget, final List<Coordinate> coordinates) {
    	if (testbedShape != null) {
    		mapWidget.removeOverlay(testbedShape);
    	}
    	if (coordinates == null) {
    		return;
    	}
    	
    	final List<LatLng> latLngs = Lists.transform(coordinates, new Function<Coordinate, LatLng>() {
			@Override
			public LatLng apply(final Coordinate input) {
				return convert(input);
			}
		});
    	final List<LatLng> polyPoints = new ArrayList<LatLng>(latLngs.size() + 1);
    	polyPoints.addAll(latLngs);
    	polyPoints.add(polyPoints.get(0));
    	testbedShape = new Polygon(polyPoints.toArray(new LatLng[polyPoints.size()]));
    	mapWidget.addOverlay(testbedShape);
    }

	@Override
	public void setTestbedShape(final List<Coordinate> coordinates) {
		polygonManager.setHandler(new AsyncManager.Handler<MapWidget>() {
			@Override
			public void execute(final MapWidget mapWidget) {
				drawTestbedShape(mapWidget, coordinates);
			}
		});
	}

	@Override
	public HasLoadingIndicator getLoadingIndicator() {
		return container;
	}
}
