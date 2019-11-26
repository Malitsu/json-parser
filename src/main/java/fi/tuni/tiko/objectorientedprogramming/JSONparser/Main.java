package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class Main {
	public static void main(String [] args) {

		// Create configuration object
		Configuration cfg = new Configuration();

		// Populate the data of the default configuration file which name
		// is hibernate.cfg.xml
		cfg.configure();

		// Read the annotations
		cfg.addAnnotatedClass(Item.class);

		// Create SessionFactory that can be used to open a session
		SessionFactory factory = cfg.buildSessionFactory();

		// Session is an interface between Java app and Database
		// Session is used to create, read, delete operations
		Session session = factory.openSession();

		// Transaction is mandatory in hibernate. When commiting
		// The sql is done in the database. If errors, transaction
		// can be canceled and the database can be restored
		Transaction tx = session.beginTransaction();

		// Create pojo
		Item item = new Item();

		// Save to database
		session.persist(item);
		tx.commit();

		// Close connection
		factory.close();
	}
}
