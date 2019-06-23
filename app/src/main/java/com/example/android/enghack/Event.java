package com.example.android.enghack;


import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    String id;
    String EventName;
    String Description;
    Double Latitude;
    Double Longitude;
    String Type;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Event createFromParcel(Parcel in){return new Event(in);}

        public Event[] newArray(int size){return new Event[size];}
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(id);
        dest.writeString(EventName);
        dest.writeString(Description);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
        dest.writeString(Type);
    }

    Event(String id, String EventName, String Description, Double Latitude, Double Longitude, String Type){
        this.id = id;
        this.EventName = EventName;
        this.Description =  Description;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Type = Type;
    }

    Event(Parcel source){
        this.id = source.readString();
        this.EventName = source.readString();
        this.Description =  source.readString();
        this.Latitude = source.readDouble();
        this.Longitude = source.readDouble();
        this.Type = source.readString();
    }

    @Override
    public String toString() {
        return String.format("Name:%s Description: %s Latitude %s, Longitude %s", getEventName(), getDescription(), getLatitude(), getLongitude());
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
