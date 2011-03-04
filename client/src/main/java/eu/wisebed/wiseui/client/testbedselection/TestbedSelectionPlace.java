package eu.wisebed.wiseui.client.testbedselection;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TestbedSelectionPlace extends Place {

    private Integer selection;

    public TestbedSelectionPlace() {
        this.selection = null;
    }

    public TestbedSelectionPlace(final Integer selection) {
        this.selection = selection;
    }

    public void setSelection(final Integer selection) {
        this.selection = selection;
    }

    public Integer getSelection() {
        return selection;
    }

    public static class Tokenizer implements PlaceTokenizer<TestbedSelectionPlace> {

        public String getToken(final TestbedSelectionPlace place) {
            final StringBuilder builder = new StringBuilder();
            if (place.getSelection() != null) {
                builder.append("selection=").append(place.getSelection());
            }
            return builder.toString();
        }

        public TestbedSelectionPlace getPlace(final String token) {
            final String[] tokens = token.split("=");
            if (tokens.length > 1) {
                return new TestbedSelectionPlace(Integer.parseInt(tokens[1]));
            }
            return new TestbedSelectionPlace();
        }
    }
}
