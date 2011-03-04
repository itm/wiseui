package eu.wisebed.wiseui.client.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import eu.wisebed.wiseui.client.activity.WiseUiPlaceHistoryMapper;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class WiseUiPlace extends Place {

    private Map<String, Place> places = new TreeMap<String, Place>();
    private String current;

    public WiseUiPlace() {
    }

    private WiseUiPlace(final Map<String, Place> places) {
        this.places = places;
    }

    public Collection<Place> getPlaces() {
        return places.values();
    }

    public Place get(final Class<? extends Place> place) {
        return places.get(place.getName());
    }

    public Place getCurrent() {
        return places.get(current);
    }

    public void setCurrent(final Place place) {
        current = place.getClass().getName();
        places.put(current, place);
    }

    public WiseUiPlace update(final Place place) {
        places.put(place.getClass().getName(), place);
        return new WiseUiPlace(places);
    }

    public static class Tokenizer implements PlaceTokenizer<WiseUiPlace> {

        private static final String SEPARATOR = "&";
        private final WiseUiPlaceHistoryMapper mapper = GWT.create(WiseUiPlaceHistoryMapper.class);

        private String[] removeLast(final String[] array) {
            final String[] result = new String[array.length - 1];
            System.arraycopy(array, 0, result, 0, array.length - 1);
            return result;
        }

        public String getToken(final WiseUiPlace place) {
            final StringBuilder buffer = new StringBuilder();
            for (Place entry : place.getPlaces()) {
                buffer.append(mapper.getToken(entry)).append(SEPARATOR);
            }
            final String current = place.getCurrent().getClass().getName();
            buffer.append(current);
            return buffer.toString();
        }

        public WiseUiPlace getPlace(final String token) {
            final WiseUiPlace wiseUiPlace = new WiseUiPlace();
            final String[] tokens = token.split(SEPARATOR);
            if (tokens.length >= 2) {
                final String[] entries = removeLast(tokens);
                final String current = tokens[tokens.length - 1];
                for (String entry : entries) {
                    final Place place = mapper.getPlace(entry);
                    wiseUiPlace.update(place);
                    if (current.equals(place.getClass().getName())) {
                        wiseUiPlace.setCurrent(place);
                    }
                }
            }
            return wiseUiPlace;
        }
    }
}
