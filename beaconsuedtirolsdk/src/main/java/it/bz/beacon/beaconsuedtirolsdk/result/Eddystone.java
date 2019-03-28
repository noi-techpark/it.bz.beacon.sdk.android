package it.bz.beacon.beaconsuedtirolsdk.result;

import com.kontakt.sdk.android.ble.spec.Telemetry;
import it.bz.beacon.beaconsuedtirolsdk.data.entity.Beacon;

import java.net.MalformedURLException;
import java.net.URL;

public class Eddystone implements IBeaconInfo {

    private String namespace;
    private String instanceId;
    private URL url;
    private String eid;
    private String encryptedTelemetry;
    private Double batteryVoltage;
    private Double temperature;
    private Beacon info;

    public Eddystone(String namespace, String instanceId, String url, String eid, String encryptedTelemetry, Telemetry telemetry, Beacon info) {
        this.namespace = namespace;
        this.instanceId = instanceId;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
        }
        this.eid = eid;
        this.encryptedTelemetry = encryptedTelemetry;
        if (telemetry != null) {
            this.temperature = telemetry.getTemperature();
            this.batteryVoltage = telemetry.getBatteryVoltage();
        }
        this.info = info;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public URL getUrl() {
        return url;
    }

    public String getEid() {
        return eid;
    }

    public String getEncryptedTelemetry() {
        return encryptedTelemetry;
    }

    public Double getBatteryVoltage() {
        return batteryVoltage;
    }

    public Double getTemperature() {
        return temperature;
    }

    @Override
    public Beacon getInfo() {
        return info;
    }
}
