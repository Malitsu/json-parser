package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.hibernate.*;
import org.hibernate.cfg.*;

import java.util.List;

public class App {
	public static void main(String[] args) {

		Parser parser = new Parser();
		parser.readFromFile();
		List<Item> items = parser.returnAllItems().get();

		H2Connect h2 = new H2Connect();
		for (Item item: items) {
			h2.saveItem(item);
		}
		List<Item> newItems = h2.fetchItems();
		for(Item item: newItems) {
			System.out.println(item);
		}

		h2.close();
	}
}
