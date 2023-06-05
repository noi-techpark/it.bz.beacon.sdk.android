// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: AGPL-3.0-or-later

/**
 * Beacon Suedtirol API
 * The API for the Beacon Suedtirol project for configuring beacons and accessing beacon data.
 *
 * OpenAPI spec version: 0.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package it.bz.beacon.beaconsuedtirolsdk.swagger.client.model;

import it.bz.beacon.beaconsuedtirolsdk.swagger.client.model.InfoUpdate;
import java.util.UUID;
import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;

@ApiModel(description = "")
public class BeaconUpdate {
  
  @SerializedName("description")
  private String description = null;
  @SerializedName("eddystoneEid")
  private Boolean eddystoneEid = null;
  @SerializedName("eddystoneEtlm")
  private Boolean eddystoneEtlm = null;
  @SerializedName("eddystoneTlm")
  private Boolean eddystoneTlm = null;
  @SerializedName("eddystoneUid")
  private Boolean eddystoneUid = null;
  @SerializedName("eddystoneUrl")
  private Boolean eddystoneUrl = null;
  @SerializedName("group")
  private Long group = null;
  @SerializedName("iBeacon")
  private Boolean iBeacon = null;
  @SerializedName("info")
  private InfoUpdate info = null;
  @SerializedName("instanceId")
  private String instanceId = null;
  @SerializedName("interval")
  private Integer interval = null;
  @SerializedName("lat")
  private Float lat = null;
  @SerializedName("lng")
  private Float lng = null;
  @SerializedName("locationDescription")
  private String locationDescription = null;
  public enum LocationTypeEnum {
     OUTDOOR,  INDOOR, 
  };
  @SerializedName("locationType")
  private LocationTypeEnum locationType = null;
  @SerializedName("major")
  private Integer major = null;
  @SerializedName("minor")
  private Integer minor = null;
  @SerializedName("name")
  private String name = null;
  @SerializedName("namespace")
  private String namespace = null;
  @SerializedName("telemetry")
  private Boolean telemetry = null;
  @SerializedName("txPower")
  private Integer txPower = null;
  @SerializedName("url")
  private String url = null;
  @SerializedName("uuid")
  private UUID uuid = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Boolean getEddystoneEid() {
    return eddystoneEid;
  }
  public void setEddystoneEid(Boolean eddystoneEid) {
    this.eddystoneEid = eddystoneEid;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Boolean getEddystoneEtlm() {
    return eddystoneEtlm;
  }
  public void setEddystoneEtlm(Boolean eddystoneEtlm) {
    this.eddystoneEtlm = eddystoneEtlm;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Boolean getEddystoneTlm() {
    return eddystoneTlm;
  }
  public void setEddystoneTlm(Boolean eddystoneTlm) {
    this.eddystoneTlm = eddystoneTlm;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Boolean getEddystoneUid() {
    return eddystoneUid;
  }
  public void setEddystoneUid(Boolean eddystoneUid) {
    this.eddystoneUid = eddystoneUid;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Boolean getEddystoneUrl() {
    return eddystoneUrl;
  }
  public void setEddystoneUrl(Boolean eddystoneUrl) {
    this.eddystoneUrl = eddystoneUrl;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Long getGroup() {
    return group;
  }
  public void setGroup(Long group) {
    this.group = group;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Boolean getIBeacon() {
    return iBeacon;
  }
  public void setIBeacon(Boolean iBeacon) {
    this.iBeacon = iBeacon;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public InfoUpdate getInfo() {
    return info;
  }
  public void setInfo(InfoUpdate info) {
    this.info = info;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getInstanceId() {
    return instanceId;
  }
  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  /**
   * minimum: 100
   * maximum: 10240
   **/
  @ApiModelProperty(required = true, value = "")
  public Integer getInterval() {
    return interval;
  }
  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  /**
   * minimum: -90.0
   * maximum: 90.0
   **/
  @ApiModelProperty(required = true, value = "")
  public Float getLat() {
    return lat;
  }
  public void setLat(Float lat) {
    this.lat = lat;
  }

  /**
   * minimum: -180.0
   * maximum: 180.0
   **/
  @ApiModelProperty(required = true, value = "")
  public Float getLng() {
    return lng;
  }
  public void setLng(Float lng) {
    this.lng = lng;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getLocationDescription() {
    return locationDescription;
  }
  public void setLocationDescription(String locationDescription) {
    this.locationDescription = locationDescription;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public LocationTypeEnum getLocationType() {
    return locationType;
  }
  public void setLocationType(LocationTypeEnum locationType) {
    this.locationType = locationType;
  }

  /**
   * minimum: 0
   * maximum: 65535
   **/
  @ApiModelProperty(required = true, value = "")
  public Integer getMajor() {
    return major;
  }
  public void setMajor(Integer major) {
    this.major = major;
  }

  /**
   * minimum: 0
   * maximum: 65535
   **/
  @ApiModelProperty(required = true, value = "")
  public Integer getMinor() {
    return minor;
  }
  public void setMinor(Integer minor) {
    this.minor = minor;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getNamespace() {
    return namespace;
  }
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Boolean getTelemetry() {
    return telemetry;
  }
  public void setTelemetry(Boolean telemetry) {
    this.telemetry = telemetry;
  }

  /**
   * minimum: 1
   * maximum: 7
   **/
  @ApiModelProperty(required = true, value = "")
  public Integer getTxPower() {
    return txPower;
  }
  public void setTxPower(Integer txPower) {
    this.txPower = txPower;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public UUID getUuid() {
    return uuid;
  }
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeaconUpdate beaconUpdate = (BeaconUpdate) o;
    return (this.description == null ? beaconUpdate.description == null : this.description.equals(beaconUpdate.description)) &&
        (this.eddystoneEid == null ? beaconUpdate.eddystoneEid == null : this.eddystoneEid.equals(beaconUpdate.eddystoneEid)) &&
        (this.eddystoneEtlm == null ? beaconUpdate.eddystoneEtlm == null : this.eddystoneEtlm.equals(beaconUpdate.eddystoneEtlm)) &&
        (this.eddystoneTlm == null ? beaconUpdate.eddystoneTlm == null : this.eddystoneTlm.equals(beaconUpdate.eddystoneTlm)) &&
        (this.eddystoneUid == null ? beaconUpdate.eddystoneUid == null : this.eddystoneUid.equals(beaconUpdate.eddystoneUid)) &&
        (this.eddystoneUrl == null ? beaconUpdate.eddystoneUrl == null : this.eddystoneUrl.equals(beaconUpdate.eddystoneUrl)) &&
        (this.group == null ? beaconUpdate.group == null : this.group.equals(beaconUpdate.group)) &&
        (this.iBeacon == null ? beaconUpdate.iBeacon == null : this.iBeacon.equals(beaconUpdate.iBeacon)) &&
        (this.info == null ? beaconUpdate.info == null : this.info.equals(beaconUpdate.info)) &&
        (this.instanceId == null ? beaconUpdate.instanceId == null : this.instanceId.equals(beaconUpdate.instanceId)) &&
        (this.interval == null ? beaconUpdate.interval == null : this.interval.equals(beaconUpdate.interval)) &&
        (this.lat == null ? beaconUpdate.lat == null : this.lat.equals(beaconUpdate.lat)) &&
        (this.lng == null ? beaconUpdate.lng == null : this.lng.equals(beaconUpdate.lng)) &&
        (this.locationDescription == null ? beaconUpdate.locationDescription == null : this.locationDescription.equals(beaconUpdate.locationDescription)) &&
        (this.locationType == null ? beaconUpdate.locationType == null : this.locationType.equals(beaconUpdate.locationType)) &&
        (this.major == null ? beaconUpdate.major == null : this.major.equals(beaconUpdate.major)) &&
        (this.minor == null ? beaconUpdate.minor == null : this.minor.equals(beaconUpdate.minor)) &&
        (this.name == null ? beaconUpdate.name == null : this.name.equals(beaconUpdate.name)) &&
        (this.namespace == null ? beaconUpdate.namespace == null : this.namespace.equals(beaconUpdate.namespace)) &&
        (this.telemetry == null ? beaconUpdate.telemetry == null : this.telemetry.equals(beaconUpdate.telemetry)) &&
        (this.txPower == null ? beaconUpdate.txPower == null : this.txPower.equals(beaconUpdate.txPower)) &&
        (this.url == null ? beaconUpdate.url == null : this.url.equals(beaconUpdate.url)) &&
        (this.uuid == null ? beaconUpdate.uuid == null : this.uuid.equals(beaconUpdate.uuid));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (this.description == null ? 0: this.description.hashCode());
    result = 31 * result + (this.eddystoneEid == null ? 0: this.eddystoneEid.hashCode());
    result = 31 * result + (this.eddystoneEtlm == null ? 0: this.eddystoneEtlm.hashCode());
    result = 31 * result + (this.eddystoneTlm == null ? 0: this.eddystoneTlm.hashCode());
    result = 31 * result + (this.eddystoneUid == null ? 0: this.eddystoneUid.hashCode());
    result = 31 * result + (this.eddystoneUrl == null ? 0: this.eddystoneUrl.hashCode());
    result = 31 * result + (this.group == null ? 0: this.group.hashCode());
    result = 31 * result + (this.iBeacon == null ? 0: this.iBeacon.hashCode());
    result = 31 * result + (this.info == null ? 0: this.info.hashCode());
    result = 31 * result + (this.instanceId == null ? 0: this.instanceId.hashCode());
    result = 31 * result + (this.interval == null ? 0: this.interval.hashCode());
    result = 31 * result + (this.lat == null ? 0: this.lat.hashCode());
    result = 31 * result + (this.lng == null ? 0: this.lng.hashCode());
    result = 31 * result + (this.locationDescription == null ? 0: this.locationDescription.hashCode());
    result = 31 * result + (this.locationType == null ? 0: this.locationType.hashCode());
    result = 31 * result + (this.major == null ? 0: this.major.hashCode());
    result = 31 * result + (this.minor == null ? 0: this.minor.hashCode());
    result = 31 * result + (this.name == null ? 0: this.name.hashCode());
    result = 31 * result + (this.namespace == null ? 0: this.namespace.hashCode());
    result = 31 * result + (this.telemetry == null ? 0: this.telemetry.hashCode());
    result = 31 * result + (this.txPower == null ? 0: this.txPower.hashCode());
    result = 31 * result + (this.url == null ? 0: this.url.hashCode());
    result = 31 * result + (this.uuid == null ? 0: this.uuid.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeaconUpdate {\n");
    
    sb.append("  description: ").append(description).append("\n");
    sb.append("  eddystoneEid: ").append(eddystoneEid).append("\n");
    sb.append("  eddystoneEtlm: ").append(eddystoneEtlm).append("\n");
    sb.append("  eddystoneTlm: ").append(eddystoneTlm).append("\n");
    sb.append("  eddystoneUid: ").append(eddystoneUid).append("\n");
    sb.append("  eddystoneUrl: ").append(eddystoneUrl).append("\n");
    sb.append("  group: ").append(group).append("\n");
    sb.append("  iBeacon: ").append(iBeacon).append("\n");
    sb.append("  info: ").append(info).append("\n");
    sb.append("  instanceId: ").append(instanceId).append("\n");
    sb.append("  interval: ").append(interval).append("\n");
    sb.append("  lat: ").append(lat).append("\n");
    sb.append("  lng: ").append(lng).append("\n");
    sb.append("  locationDescription: ").append(locationDescription).append("\n");
    sb.append("  locationType: ").append(locationType).append("\n");
    sb.append("  major: ").append(major).append("\n");
    sb.append("  minor: ").append(minor).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  namespace: ").append(namespace).append("\n");
    sb.append("  telemetry: ").append(telemetry).append("\n");
    sb.append("  txPower: ").append(txPower).append("\n");
    sb.append("  url: ").append(url).append("\n");
    sb.append("  uuid: ").append(uuid).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
