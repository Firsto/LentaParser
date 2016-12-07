package ru.firsto.lentaparser.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "item", strict = false)
public class Article implements Parcelable {

    @Element public String guid;
    @Element public String title;
    @Element public String link;
    @Element public String description;
    @Element public String pubDate;
    @Element public String category;
    @Element(required = false) public Enclosure enclosure;

    @Root(name = "enclosure", strict = false)
    public static class Enclosure implements Serializable {

        @Attribute(name = "url")
        public String url;

        @Attribute(name = "length")
        public long length;

        @Attribute(name = "type")
        public String type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.guid);
        dest.writeString(this.title);
        dest.writeString(this.link);
        dest.writeString(this.description);
        dest.writeString(this.pubDate);
        dest.writeString(this.category);
        dest.writeSerializable(this.enclosure);
    }

    public Article() {
    }

    protected Article(Parcel in) {
        this.guid = in.readString();
        this.title = in.readString();
        this.link = in.readString();
        this.description = in.readString();
        this.pubDate = in.readString();
        this.category = in.readString();
        this.enclosure = (Enclosure) in.readSerializable();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}