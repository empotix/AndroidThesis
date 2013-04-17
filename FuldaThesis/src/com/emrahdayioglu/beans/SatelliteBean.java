package com.emrahdayioglu.beans;

public class SatelliteBean {
	
	public int prn;
	public float azimuth;
	public float elevation;
	public float snr;
	public boolean hasAlmanac;
	public boolean hasEphermis;
	public boolean usedInFix;
	
	public int getPrn() {
		return prn;
	}
	public void setPrn(int prn) {
		this.prn = prn;
	}
	public float getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(float azimuth) {
		this.azimuth = azimuth;
	}
	public float getElevation() {
		return elevation;
	}
	public void setElevation(float elevation) {
		this.elevation = elevation;
	}
	public float getSnr() {
		return snr;
	}
	public void setSnr(float snr) {
		this.snr = snr;
	}
	public boolean isHasAlmanac() {
		return hasAlmanac;
	}
	public void setHasAlmanac(boolean hasAlmanac) {
		this.hasAlmanac = hasAlmanac;
	}
	public boolean isHasEphermis() {
		return hasEphermis;
	}
	public void setHasEphermis(boolean hasEphermis) {
		this.hasEphermis = hasEphermis;
	}
	public boolean isUsedInFix() {
		return usedInFix;
	}
	public void setUsedInFix(boolean usedInFix) {
		this.usedInFix = usedInFix;
	}

	
	
	

}
