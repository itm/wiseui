package eu.wisebed.wiseui.client.activity;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

import eu.wisebed.wiseui.client.main.WiseUiPlace;

/**
 * PlaceHistoryMapper interface is used to attach all places which the
 * PlaceHistoryHandler should be aware of. This is done via the @WithTokenizers
 * annotation or by extending PlaceHistoryMapperWithFactory and creating a
 * separate TokenizerFactory.
 */
@WithTokenizers({WiseUiPlace.Tokenizer.class})
public interface WiseUiPlaceHistoryMapper extends PlaceHistoryMapper {
}
