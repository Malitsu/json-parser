package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Iterator;
import java.util.List;

/**
 * @author      Tiina Malinen <tiina.malinen @ tuni.fi>
 * @version     1.0
 */
public class H2Connect {

    private SessionFactory factory;

    /**
     * Constructor of the Class H2Connect.
     *
     * Creates a configuration and reads the annotation of the Item class.
     */
    public H2Connect() {
        // Create configuration object
        Configuration cfg = new Configuration();

        // Populate the data of the default configuration file hibernate.cfg.xml
        cfg.configure();

        // Read the annotations
        cfg.addAnnotatedClass(Item.class);

        // Create SessionFactory that can be used to open a session
        factory = cfg.buildSessionFactory();
    }

    /**
     * Saves the received Item into the H2 database.
     *
     * @param item The Item wished to be saved.
     */
    public void saveItem(Item item) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(item);
        tx.commit();
        session.close();
    }

    /**
     * Saves the received list of Items into the H2 database.
     *
     * @param items The list of Items wished to be saved.
     */
    public void saveItems(List<Item> items) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        for(Item item: items) {
            session.persist(item);
        }
        tx.commit();
        session.close();
    }

    /**
     * Fetches all Items in the H2 database and returns them as a list.
     *
     * @return List of Items found from the database.
     */
    public List<Item> fetchItems() {
        Session session = factory.openSession();
        session.beginTransaction();
        List list = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    /**
     * Removes the matching Item's information from the H2 database.
     *
     * @param item Item wished to be deleted.
     */
    public void removeItem(Item item) {
        Session session = factory.openSession();
        session.beginTransaction();
        session.delete(item);
        session.getTransaction().commit();
        session.close();
    }


    /**
     * Removes all matching Items' information from the H2 database.
     *
     * @param items Items wished to be deleted.
     */
    public void removeItems(List<Item> items) {
        Session session = factory.openSession();
        session.beginTransaction();
        for(Item item: items) {
            session.delete(item);
        }
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Erases all saved Items from the H2 database.
     */
    public void removeAll() {
        removeItems(fetchItems());
    }

    /**
     * Closes the connection to H2 database.
     */
    public void close() {
        factory.close();
    }

}
