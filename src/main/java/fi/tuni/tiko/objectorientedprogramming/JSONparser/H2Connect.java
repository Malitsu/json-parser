package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Iterator;
import java.util.List;

public class H2Connect {

    SessionFactory factory;

    public H2Connect() {
        // Create configuration object
        Configuration cfg = new Configuration();

        // Populate the data of the default configuration file which name
        // is hibernate.cfg.xml
        cfg.configure();

        // Read the annotations
        cfg.addAnnotatedClass(Item.class);

        // Create SessionFactory that can be used to open a session
        factory = cfg.buildSessionFactory();
    }

    public void saveItem(Item item) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(item);
        tx.commit();
        session.close();
    }

    public void saveItems(List<Item> items) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        for(Item item: items) {
            session.persist(item);
        }
        tx.commit();
        session.close();
    }

    public List<Item> fetchItems() {
        Session session = factory.openSession();
        session.beginTransaction();
        List list = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public void close() {
        factory.close();
    }

}
