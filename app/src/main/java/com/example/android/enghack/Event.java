package com.example.android.enghack;


public class Event {
    String id;
    String EventName;
    String Description;
    Double Latitude;
    Double Longitude;
    String Type;

    Event(String id, String EventName, String Description, Double Latitude, Double Longitude, String Type){
        this.id = id;
        this.EventName = EventName;
        this.Description =  Description;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Type = Type;
    }

    String getId() {
        return id;
    }

    String getEventName() {
        return EventName;
    }

    String getDescription() {
        return Description;
    }

    Double getLatitude() {
        return Latitude;
    }

    Double getLongitude() {
        return Longitude;
    }

    String getType() {
        return Type;
    }
}
