package it.codingjam.lifecyclebinder.mvp;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private String title;

    private String description;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    protected Note(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
