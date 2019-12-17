package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.h2.store.fs.FileUtils;
import org.junit.Test;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ParserTest {

	Parser<String, String> parser = new Parser();
	Item item = new Item("apples", "5");
	JavaObject javaobject = new JavaObject("apples", "5");
	List<Item> items;
	List<String> lines;
	List<Object> objects;
	String jsonLineItem = "  \"apples\": \"5\"";
	String jsonLineObject = "  {\n" +
			"    \"id\": \"0\",\n" +
			"    \"tag\": \"apples\",\n" +
			"    \"property\": \"5\"" +
			"  \n  }";

	@Test
	public void testCreateItem() {
		Item item2 = parser.createItem("apples", "5");
		Assert.assertTrue("Strings should match", item2.toString().equals(item.toString()));
	}

	@Test
	public void testCreateJsonLine() {
		Assert.assertTrue("Strings should match", jsonLineItem.equals(parser.createJsonLine(item)));
		Assert.assertEquals("Strings should match", jsonLineObject, parser.createJsonLine(javaobject));
	}

	@Test
	public void testConvertItemsToJsonLines() {
		parser.readFromFile();
		items = parser.returnAllItems().get();
		lines = parser.convertItemsToJsonLines(items);
		Assert.assertTrue(lines.get(0).equals(jsonLineItem));
	}

	@Test
	public void addItem() {
		parser.reset();
		parser.addItem(item);
		Assert.assertTrue(parser.lines.get(1).equals(jsonLineItem));
	}

	@Test
	public void testAddAll() {
		parser.reset();
		items = new LinkedList<>();
		items.add(new Item("apples", "5"));
		items.add(new Item("cheese", "block"));
		items.add(new Item("juice", "1 l"));
		items.add(new Item("bread", "bag"));
		parser.addAllItems(items);
		Assert.assertTrue(parser.lines.get(1).equals(jsonLineItem +","));

		/*parser.reset();
		objects = new LinkedList<>();
		objects.add(new JavaObject("apples", "5"));
		objects.add(javaobject);
		objects.add(item);
		parser.addAll(objects);
		Assert.assertTrue(parser.lines.get(1).equals(jsonLineObject +",")); */
	}

	@Test
	public void testAddAllItems() {
		parser.reset();
		items = new LinkedList<>();
		items.add(new Item("apples", "5"));
		items.add(new Item("cheese", "block"));
		items.add(new Item("juice", "1 l"));
		items.add(new Item("bread", "bag"));
		parser.addAllItems(items);
		Assert.assertTrue(parser.lines.get(1).equals(jsonLineItem +","));
	}

	@Test
	public void testAreMoreItems() {
		parser.reset();
		Assert.assertFalse(parser.areMoreItems());
		parser.readFromFile();
		Assert.assertTrue(parser.areMoreItems());
	}

	@Test
	public void testReturnItem() {
		parser.reset();
		parser.readFromFile();
		Assert.assertTrue(parser.returnItem().get().getTag().equals("bread"));
	}

	@Test
	public void testReset() {
		parser.reset();
		Assert.assertTrue(parser.filename.equals("save.json"));
		Assert.assertTrue(parser.items.isEmpty());
		Assert.assertTrue(parser.lines.size() == 2);
	}

	@Test
	public void testReturnAllItems() {
		parser.reset();
		parser.readFromFile();
		items = new ArrayList<>();
		items.add(new Item("apples", "5"));
		items.add(new Item("cheese", "block"));
		items.add(new Item("juice", "1 l"));
		items.add(new Item("bread", "bag"));
		Assert.assertEquals(parser.returnAllItems().get().toString(), items.toString());
	}

	@Test
	public void testFileIntoItems() {
		parser.reset();
		items = new ArrayList<>();
		items.add(new Item("apples", "5"));
		items.add(new Item("cheese", "block"));
		items.add(new Item("juice", "1 l"));
		items.add(new Item("bread", "bag"));
		List<Item> testItems = parser.fileIntoItems("test.txt");
		Assert.assertEquals(testItems.toString(), items.toString());
	}

	@Test
	public void testFileIntoLines() {
		lines = new ArrayList<>();
		lines.add("{");
		lines.add("  \"apples\": \"5\",");
		lines.add("  \"cheese\": \"block\",");
		lines.add("  \"juice\": \"1 l\",");
		lines.add("  \"bread\": \"bag\"");
		lines.add("}");
		parser.reset();
		List<String> testLines = parser.fileIntoLines("test.txt");
		Assert.assertEquals(testLines.toString(), lines.toString());
	}

	@Test
	public void testStringify() {
		parser.reset();
		List<Object> objects = new ArrayList<>();
		objects.add(new JavaObject("apples", "5"));
		objects.add(new JavaObject("cheese", "block"));
		objects.add(new JavaObject("juice", "1 l"));
		objects.add(new JavaObject("bread", "bag"));
		String test = "[\n" +
				"  {\n" +
				"    \"id\": \"0\",\n" +
				"    \"tag\": \"apples\",\n" +
				"    \"property\": \"5\"  \n" +
				"  },\n" +
				"  {\n" +
				"    \"id\": \"0\",\n" +
				"    \"tag\": \"cheese\",\n" +
				"    \"property\": \"block\"  \n" +
				"  },\n" +
				"  {\n" +
				"    \"id\": \"0\",\n" +
				"    \"tag\": \"juice\",\n" +
				"    \"property\": \"1 l\"  \n" +
				"  },\n" +
				"  {\n" +
				"    \"id\": \"0\",\n" +
				"    \"tag\": \"bread\",\n" +
				"    \"property\": \"bag\"  \n" +
				"  }\n" +
				"]";
		parser.addObjects(objects);
		String result = parser.stringify();
		Assert.assertEquals(test, result);
	}

	@Test
	public void testJsonToItem() {
		parser.reset();
		item = new Item("apples", "5");
		jsonLineItem = "  \"apples\": \"5\"";
		Assert.assertEquals(parser.jsonToItem(jsonLineItem).toString(), item.toString());
	}

	@Test
	public void testJsonToItems() {
		parser.reset();
		String jsonString = "{\n" +
				"  \"apples\": \"5\",\n" +
				"  \"cheese\": \"block\",\n" +
				"  \"juice\": \"1 l\",\n" +
				"  \"bread\": \"bag\"\n" +
				"}";
		items = parser.jsonToItems(jsonString);
		Assert.assertTrue(items.size() == 4);
	}

	@Test
	public void testAddObject() {
		parser.reset();
		parser.addObject(javaobject);
		Assert.assertEquals(parser.lines.get(1), jsonLineObject);
	}

}
