package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import javax.persistence.*;
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;
    public String tag;
    public String property;

    public Item() {}

    public Item(String tag, String property) {
        this.tag = tag;
        this.property = property;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}