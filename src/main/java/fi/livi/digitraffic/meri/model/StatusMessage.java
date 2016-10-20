package fi.livi.digitraffic.meri.model;

public class StatusMessage {
    public static final StatusMessage OK = new StatusMessage("OK");

    public final String status;

    private StatusMessage(final String status) {
        this.status = status;
    }
}
