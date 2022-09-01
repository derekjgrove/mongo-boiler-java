package com.mdb.ps.da.listeners;

import com.mongodb.TagSet;
import com.mongodb.connection.ServerDescription;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListener;

public class ServerListenerRegion implements ServerListener {

    private String currentRegion = "";
    private Tags primaryTags = new Tags();

    @Override
    public synchronized void serverDescriptionChanged(final ServerDescriptionChangedEvent event) {

        ServerDescription currentServerDescription = event.getNewDescription();
        if (currentServerDescription.getType().toString().equals("REPLICA_SET_PRIMARY")) {
            Tags tempTagSet = new Tags(currentServerDescription.getTagSet());
            if (primaryTags.compareTo(tempTagSet) != 0) {
                primaryTags = tempTagSet;
                System.out.println("{ Region: " + primaryTags.getRegion() + ", Provider: " + primaryTags.getProvider() + " }");
            }
        }

    }
}

class Tags implements Comparable<Tags>{
    private String nodeType;
    private String provider;
    private String region;
    private String workloadType;

    public String getRegion() {
        return this.region;
    }

    public String getProvider() {
        return this.provider;
    }

    public Tags(TagSet tagSet) {
        tagSet.forEach(tag -> {
            switch(tag.getName()) {
                case "nodeType":
                    this.nodeType = tag.getValue();
                    break;
                case "provider":
                    this.provider = tag.getValue();
                    break;
                case "region":
                    this.region = tag.getValue();
                    break;
                case "workloadType":
                    this.workloadType = tag.getValue();
                    break;
            }
        });
    }

    public Tags() {
        this.nodeType = "";
        this.provider = "";
        this.region = "";
        this.workloadType = "";
    }

    @Override
    public int compareTo(Tags tagSet) {
        if (this.provider.equals(tagSet.provider) && this.region.equals(tagSet.region) ) {
            return 0;
        } else {
            return 1;
        }
    }

}