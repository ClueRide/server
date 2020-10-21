package com.clueride.imp.gpx;


public class Trackpoint implements PointWithElevation {
    private static final double EARTH_RADIUS_MILES = 3963.0D;
    private static final double PI_180 = 0.017453292519943295D;
    private double latDouble;
    private double lonDouble;
    private double altitude;

    public Trackpoint(double lat, double lon) {
        this.latDouble = lat;
        this.lonDouble = lon;
    }

    public Trackpoint(double lat, double lon, double altitude) {
        this.latDouble = lat;
        this.lonDouble = lon;
        this.altitude = altitude;
        if (lat <= 90.0D && lat >= -90.0D) {
            if (lon > -20.0D || lon < -180.0D) {
                throw new IllegalArgumentException("Longitude outside of bounds");
            }
        } else {
            throw new IllegalArgumentException("Latitude outside of bounds");
        }
    }

    public void setLatDouble(double latDouble) {
        this.latDouble = latDouble;
    }

    public void setLonDouble(double lonDouble) {
        this.lonDouble = lonDouble;
    }

    public double getLatDouble() {
        return this.latDouble;
    }

    public double getLonDouble() {
        return this.lonDouble;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getDistance(Trackpoint trackpoint) {
        double distance = Math.acos(
                Math.sin(this.latDouble * PI_180) *
                        Math.sin(trackpoint.getLatDouble() * PI_180) +
                        Math.cos(this.latDouble * PI_180) *
                                Math.cos(trackpoint.getLatDouble() * PI_180) *
                                Math.cos(trackpoint.getLonDouble() * PI_180 - this.lonDouble * PI_180));
        return Double.isNaN(distance) ? 0.0D : distance * 3963.0D;
    }

    public String toString() {
        String TAB = "  ";
        StringBuffer retValue = new StringBuffer();
        retValue.append("\nTrackpoint ( ")
                .append("latDouble = ")
                .append(this.latDouble)
                .append("  ")
                .append("lonDouble = ")
                .append(this.lonDouble)
                .append("  ")
                .append("altitude = ")
                .append(this.altitude)
                .append("  ")
                .append(" )");
        return retValue.toString();
    }

    public int getElevationInFeet() {
        return (int) Math.round(this.altitude * 3.2808D);
    }

    public int getElevationInMeters() {
        return (int) Math.round(this.altitude);
    }

    public boolean hasData() {
        return false;
    }
}
