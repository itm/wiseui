package eu.wisebed.wiseui.shared.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationDetails implements Serializable {

    private static final long serialVersionUID = -6856543672600986002L;

    private Date startTime;
    private Date stopTime;
    private long duration;
    private List<String> nodes;
    private String imageFileName;
    private String imageFileNameField;
    private String secretReservationKey;
    private String urnPrefix;
    private Set<SensorDetails> sensors;

    public ReservationDetails() {
    }

    public ReservationDetails(Date startTime, Date stopTime,
                              long duration, List<String> nodes, String imageFileName,
                              String imageFileNameField) {
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.duration = duration;
        this.nodes = nodes;
        this.imageFileName = imageFileName;
        this.imageFileNameField = imageFileNameField;
    }

    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStopTime(final Date stopTime) {
        this.stopTime = stopTime;
    }

    public Date getStopTime() {
        return this.stopTime;
    }

    public void setDuration(final long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public List<String> getNodes() {
        return this.nodes;
    }

    public void setImageFileName(final String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileNameField(final String imageFileNameField) {
        this.imageFileNameField = imageFileNameField;
    }

    public String getImageFileNameField() {
        return imageFileNameField;
    }

    public void setSensors(final Set<SensorDetails> sensors) {
        this.sensors = sensors;
    }

    public Set<SensorDetails> getSensors() {
        return this.sensors;
    }

    public void addSensor(SensorDetails sensor) {
        if (sensors == null) {
            sensors = new HashSet<SensorDetails>();
        }
        sensors.add(sensor);
    }

    public void removeRecord(SensorDetails sensor) {
        if (sensors == null) {
            return;
        }
        sensors.remove(sensor);
    }

    public void setSecretReservationKey(final String secretReservationKey) {
        this.secretReservationKey = secretReservationKey;
    }

    public String getSecretReservationKey() {
        return secretReservationKey;
    }

    public void setUrnPrefix(final String urnPrefix) {
        this.urnPrefix = urnPrefix;
    }

    public String getUrnPrefix() {
        return urnPrefix;
    }
}
