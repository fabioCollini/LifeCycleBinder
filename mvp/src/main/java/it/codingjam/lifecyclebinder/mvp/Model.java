package it.codingjam.lifecyclebinder.mvp;

import android.os.Parcel;
import android.os.Parcelable;

public class Model implements Parcelable {
    private Note note;

    public Model() {
    }

    protected Model(Parcel in) {
        note = in.readParcelable(Note.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(note, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
