package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.hibernate.*;
import org.hibernate.cfg.*;

import java.util.List;

public class App {
	public static void main(String[] args) {

		String json = "{\n  \"apples\": \" 3 kg\",\n}";
		Parser parser = new Parser();
		Item item = parser.jsonToItem(json);
		System.out.println(item.getTag());
		System.out.println(item.getProperty());
	}
}
