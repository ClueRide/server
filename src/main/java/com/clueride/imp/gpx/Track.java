package com.clueride.imp.gpx;

import java.util.ArrayList;

public class Track {
    private ArrayList<Trackpoint> trackpoints = new ArrayList();
    private String name = null;
    private String sourceUrl = null;
    private String displayName = null;

    public Track() {
    }

    public ArrayList<Trackpoint> getTrackpoints() {
        return this.trackpoints;
    }

    public void setTrackpoints(ArrayList<Trackpoint> trackpoints) {
        this.trackpoints = trackpoints;
    }

    public void addTrackpoint(Trackpoint trkpt) {
        this.trackpoints.add(trkpt);
    }

    public void clear() {
        this.trackpoints.clear();
        this.setSourceUrl((String)null);
        this.setDisplayName((String)null);
    }

    public double getTotalDistance() {
        if (this.trackpoints.size() < 2) {
            return 0.0D;
        } else {
            double distance = 0.0D;

            for(int i = 0; i < this.trackpoints.size() - 1; ++i) {
                Trackpoint trackPoint = (Trackpoint)this.trackpoints.get(i);
                distance += trackPoint.getDistance((Trackpoint)this.trackpoints.get(i + 1));
            }

            return distance;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        String TAB = "\n  ";
        StringBuffer retValue = new StringBuffer();
        retValue.append("Track ( ")
                .append("name = ")
                .append(this.name)
                .append("\n  ")
                .append("sourceUrl = ")
                .append(this.sourceUrl)
                .append("\n  ")
                .append("trackpoints = ")
                .append(this.trackpoints)
                .append("\n  ")
                .append(" )");
        return retValue.toString();
    }

    public String getSourceUrl() {
        return this.sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
