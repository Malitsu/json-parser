package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import javax.persistence.*;

/**
 * @author      Tiina Malinen <tiina.malinen @ tuni.fi>
 * @version     1.0
 */
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;
    private String tag;
    private String property;

    /**
     * Default constructor which does nothing.
     */
    public Item() {}

    /**
     * Contructor for Item. Initializes the two basic values.
     *
     * @param tag
     * @param property
     */
    public Item(String tag, String property) {
        this.tag = tag;
        this.property = property;
    }

    /**
     * Getter for tag.
     *
     * @return The value stored in tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Setter for tag.
     *
     * @param tag The value to be given for tag.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Getter for property.
     *
     * @return The value stored in property.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Setter for property.
     *
     * @param property The value to be given for property.
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Returns all the values of the Item in a single String.
     *
     * @return The String that contains values of the Item.
     */
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", property='" + property + '\'' +
                '}';
    }
}