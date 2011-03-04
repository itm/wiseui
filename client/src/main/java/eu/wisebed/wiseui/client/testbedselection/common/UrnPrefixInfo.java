package eu.wisebed.wiseui.client.testbedselection.common;


public class UrnPrefixInfo {

    public enum State {
        NOT_AUTHENTICATED("Not authenticated"),
        AUTHENTICATE("Authenticate..."),
        SUCCESS("Successful"),
        FAILED("Failed due an error"),
        CANCELED("Canceled"),
        SKIPPED("Skipped");

        private final String value;

        private State(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private final String urnPrefix;

    private State state;

    private boolean checked;

    public UrnPrefixInfo(final String urnPrefix) {
        this.urnPrefix = urnPrefix;
        state = State.NOT_AUTHENTICATED;
        checked = true;
    }

    public String getUrnPrefix() {
        return urnPrefix;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(final boolean checked) {
        this.checked = checked;
    }
}
