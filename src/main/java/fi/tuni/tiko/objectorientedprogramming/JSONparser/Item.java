package fi.tuni.tiko.objectorientedprogramming.JSONparser;

public class Item<T, P> {

    T tag;
    P property;

    public Item() {}

    public Item(T tag, P property) {
        this.tag = tag;
        this.property = property;
    }

    public T getTag() {
        return tag;
    }

    public void setTag(T tag) {
        this.tag = tag;
    }

    public P getProperty() {
        return property;
    }

    public void setProperty(P property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "Item{" +
                "tag=" + tag +
                ", property=" + property +
                '}';
    }
}