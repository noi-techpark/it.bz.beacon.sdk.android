package it.bz.beacon.beaconsuedtirolsdk.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import it.bz.beacon.beaconsuedtirolsdk.data.converter.DateConverter;
import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.Info;

@Entity
@TypeConverters(DateConverter.class)
public class Beacon implements Parcelable {

    public static final String LOCATION_OUTDOOR = "OUTDOOR";
    public static final String LOCATION_INDOOR = "INDOOR";

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Beacon createFromParcel(Parcel in) {
            return new Beacon(in);
        }

        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };

    @NonNull
    @PrimaryKey
    private String id;
    private String address;
    private String cap;
    private String floor;
    private String instanceId;
    private Double latitude;
    private Double longitude;
    private String location;
    private Integer major;
    private Integer minor;
    private String name;
    private String namespace;
    private Date updatedAt;
    private String uuid;
    private String website;

    public Beacon() {
    }

    public static Beacon fromInfo(Info info) {
        Beacon beacon;
        beacon = new Beacon();
        beacon.setId(info.getId());
        beacon.setAddress(info.getAddress());
        beacon.setCap(info.getCap());
        beacon.setFloor(info.getFloor());
        beacon.setInstanceId(info.getInstanceId());
        beacon.setLatitude(info.getLatitude());
        beacon.setLongitude(info.getLongitude());
        beacon.setLocation(info.getLocation());
        beacon.setMajor(info.getMajor());
        beacon.setMinor(info.getMinor());
        beacon.setName(info.getName());
        beacon.setNamespace(info.getNamespace());
        beacon.setUpdatedAt(new Date(info.getUpdatedAt()));
        if (info.getUuid() != null) {
            beacon.setUuid(info.getUuid().toString());
        }
        beacon.setWebsite(info.getWebsite());
        return beacon;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Beacon(Parcel in){
        this.id = in.readString();
        this.address = in.readString();
        this.cap = in.readString();
        this.floor = in.readString();
        this.instanceId = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.location = in.readString();
        this.major = in.readInt();
        this.minor = in.readInt();
        this.name = in.readString();
        this.namespace = in.readString();
        this.updatedAt = new Date(in.readLong());
        this.uuid = in.readString();
        this.website = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.address);
        dest.writeString(this.cap);
        dest.writeString(this.floor);
        dest.writeString(this.instanceId);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.location);
        dest.writeInt(this.major);
        dest.writeInt(this.minor);
        dest.writeString(this.name);
        dest.writeString(this.namespace);
        dest.writeLong(this.updatedAt.getTime());
        dest.writeString(this.uuid);
        dest.writeString(this.website);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
