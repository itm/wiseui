package eu.wisebed.wiseui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;

import eu.wisebed.wiseui.shared.dto.Node;
import eu.wisebed.wiseui.shared.dto.SensorDetails;


public class SensorListWidget extends Composite implements SensorList{
	
	@UiTemplate("SensorListWidget.ui.xml")
	interface SensorListImplUiBinder extends UiBinder<Widget,SensorListWidget>{}
	
	@UiField VerticalPanel sensorListPanel;

	private static SensorListImplUiBinder uiBinder = 
		GWT.create(SensorListImplUiBinder.class);
	
	private Presenter listener;
	private final MultiSelectionModel<SensorDetails> selectionModel = 
		new MultiSelectionModel<SensorDetails>();
	private final SimplePager sensorPager = new SimplePager();
	
	// The sensors' table.
	private CellTable<SensorDetails> sensorTable = new CellTable<SensorDetails>();

	private TextColumn<SensorDetails> urn = new TextColumn<SensorDetails>() {
		@Override
		public String getValue(SensorDetails s){
			return s.getUrn();
		}
	};

	private TextColumn<SensorDetails> descr = new TextColumn<SensorDetails>(){
		@Override
		public String getValue(SensorDetails s){
			return s.getDescription();
		}
	};

	private TextColumn<SensorDetails> type = new TextColumn<SensorDetails>(){
		@Override
		public String getValue(SensorDetails s){
			return s.getType();
		}
	};
	
	public void init(){	
		if (sensorTable.getColumnIndex(urn)>=0) return;
		if (sensorTable.getColumnIndex(type)>=0) return;
		if (sensorTable.getColumnIndex(descr)>=0) return;
		sensorTable.addColumn(urn,"Urn");
		sensorTable.addColumn(type,"Type");
		sensorTable.addColumn(descr,"Description");
	}

	public SensorListWidget(){
		initWidget(uiBinder.createAndBindUi(this));
		// Add a multiple selection model to handle user selection.
		sensorTable.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(
				new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {}
		});
	}
	

	/**
	 * Render sensor's information in cell list. Temporarily rendering only
	 * sensors' URNs.
	 */
	public void renderNodes(final ArrayList<SensorDetails> sensors) {
		sensorTable.setRowCount(sensors.size(), true);
		sensorTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		sensorTable.setRowData(0, sensors);
		sensorTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				Range range = sensorTable.getVisibleRange();
				int start = range.getStart();
				int length = range.getLength();
				List<SensorDetails> toSet = new ArrayList<SensorDetails>(length);
				for (int i=start; i< start + length && i < sensors.size(); i++){
					toSet.add(sensors.get(i));
				}
				sensorTable.setRowData(start, toSet);
			}
		});
		sensorPager.setDisplay(sensorTable);
		sensorListPanel.add(sensorTable);
		sensorListPanel.add(sensorPager);
	}
	
	/**
	 * Get nodes selected in cell list
	 */
	public ArrayList<Node> getNodesSelected() {
		// TODO FIXME
		//return new ArrayList<Node>(selectionModel.getSelectedSet());
        return new ArrayList<Node>();
	}

	public void setListener(Presenter listener) {
		this.listener = listener;
	}

	public Presenter getListener() {
		return listener;
	}
}
