package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author      Tiina Malinen <tiina.malinen @ tuni.fi>
 * @version     1.0
 *
 * @param <T> Any class that can be saved in String form.
 * @param <P> Any class that can be saved in String form.
 */
public class Parser<T, P> {
    List<String> lines = new LinkedList<>();
    List<Item> items = new LinkedList<>();
    String filename;

    /**
     * Constructor for the Parser class.
     *
     * Calls reset method to intialize empty lists and default name for savefile.
     */
    public Parser() {
        reset();
    }

    /**
     * Creates an Item object from two given parameters.
     *
     * @param first Can be of any type. Turned into String and used as a tag.
     * @param second  Can be of any type. Turned into String and used as a property.
     * @return
     */
    public Item createItem(T first, P second) {
        Item item = new Item();
        item.setTag(first.toString());
        item.setProperty(second.toString());
        return item;
    }

    /**
     * Changes the default name for save file.
     *
     * @param filename Name of the file where saving occurs. Must be at least three letters long.
     */
    public void setFilename(String filename) {
        if (filename.length() > 2) {
            this.filename = filename;
        }
    }

    /**
     * Turns an Item into a String which can be used in a JSON file.
     *
     * @param item An Item to be showed in JSON form.
     * @return JSON form of the Item.
     */
    public String createJsonLine(Item item) {
        String line = "  \"" +item.getTag() +"\": \"" +item.getProperty() +"\"";
        return line;
    }

    /**
     * Turns a list of Items into Strings which can be used in a JSON file.
     *
     * @param items List of Items to be showed in JSON form
     * @return List of JSON forms of Items.
     */
    public List<String> convertItemsToJsonLines(List<Item> items) {
        LinkedList<String> list = new LinkedList<>();
        for(Item item: items) {
            list.add(createJsonLine(item));
        }
        return list;
    }

    /**
     * Input an Item for the Parser.
     *
     * Parser turns the Items given into JSON form and saves them as a String list.
     * Items can later be written into a file all at once.
     *
     * @param item An Item that is wished to be added.
     */
    public void addItem(Item item) {
        if (lines.size() > 2) {
            String last = lines.get(lines.size() - 2);
            last = last + ",";
            lines.set(lines.size() -2, last);
        }
        lines.add(lines.size()-1, createJsonLine(item));
    }

    /**
     * Input a list of Items for the Parser.
     *
     * Parser turns the Items given into JSON form and saves them as a String list.
     * Items can later be written into a file all at once.
     *
     * @param items List of Items that are wished to be added.
     */
    public void addAllItems(List<Item> items) {
        for(Item item: items) {
            addItem(item);
        }
    }

    /**
     * Writes the parser JSON strings into a file.
     */
    public void writeToFile() {
        try {
            Path file = Paths.get(filename);
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch(Exception e) { e.printStackTrace(); }
    }

    /**
     * Writes the parser JSON strings into a file.
     *
     * @param filename Name of the file where the information is to be saved.
     */
    public void writeToFile(String filename) {
        setFilename(filename);
        writeToFile();
    }

    /**
     * Takes a list of Items, parses them into JSON and saves to file.
     *
     * @param items List of Items that are wished to be parsed.
     */
    public void writeItemsToFile(List<Item> items) {
        addAllItems(items);
        writeToFile();
    }

    /**
     * Takes a list of Items, parses them into JSON and saves to file.
     *
     * @param items List of Items that are wished to be parsed.
     * @param filename Name of the file where the information is to be saved.
     */
    public void writeItemsToFile(List<Item> items, String filename) {
        addAllItems(items);
        writeToFile(filename);
    }

    /**
     * Tell if there are more parsed Items to receive.
     *
     * @return boolean value of whether there are more Items
     */
    public boolean areMoreItems() {
        return !items.isEmpty();
    }

    /**
     * Fetch the next Item in line if there are any.
     *
     * @return Optional of next Item.
     */
    public Optional<Item> returnItem() {
        Item item = null;
        if (areMoreItems()) {
            if (items.size() == 1) {
                item = items.remove(0);
            }
            else {
                item = items.remove(items.size() - 1);
            }
        }
        return Optional.ofNullable(item);
    }

    /**
     * Fetch all the Items in line if there are any.
     *
     * @return Optional list of Items.
     */
    public Optional<List> returnAllItems() {
        return Optional.ofNullable(items);
    }

    /**
     * Read a JSON file and parse it into Items.
     *
     * Open the default file and read it into a list of Strings.
     * Turn the strings into a list of Items.
     */
    public void readFromFile() {
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch(Exception e) { e.printStackTrace(); }
        items = lines.stream()
                .filter(line -> !"{".equals(line))
                .filter(line -> !"}".equals(line))
                .map(line -> line.replace("  ", ""))
                .map(line -> line.replace("\"",""))
                .map(line -> line.replace(",",""))
                .map(line -> line.split(": "))
                .map(arr -> new Item(arr[0], arr[1]))
                .collect(Collectors.toList());
    }

    /**
     * Read a JSON file and parse it into Items.
     *
     * Open the default file and read it into a list of Strings.
     * Turn the strings into a list of Items.
     *
     * @param filename Name of the file where the information is to be saved.
     */
    public void readFromFile(String filename) {
        setFilename(filename);
        readFromFile();
    }

    /**
     * Intializes all lists empty and returns the default name for savefile.
     */
    public void reset() {
        lines = new LinkedList<String>();
        lines.add("{");
        lines.add("}");
        items = new LinkedList<>();
        filename = "save.json";
    }

    /**
     * Read a JSON file, parse it and return a list of Items.
     *
     * Open the default file and read it into a list of Strings.
     * Turn the strings into a list of Items. Return the list.
     *
     * @param filename Name of the file where the information is saved.
     * @return List of parsed Items.
     */
    public List<Item> fileIntoItems(String filename) {
        readFromFile(filename);
        return items;
    }

    /**
     * Open a file, read it into a list of Strings and return the list.
     *
     * @return List of Strings read from the file.
     */
    public List<String> fileIntoLines() {
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch(Exception e) { e.printStackTrace(); }
        return lines;
    }

    /**
     * Open the file with the given name, read it into a list of Strings and return the list.
     *
     * @param filename Name of the file where the information is saved.
     * @return  List of Strings read from the file.
     */
    public List<String> fileIntoLines(String filename) {
        setFilename(filename);
        return fileIntoLines();
    }

    /**
     * Take a list of Items and return as one JSON formatted String.
     *
     * @param items List of Items to be converted.
     * @return All the Items as one JSON string.
     */
    public String convertToJson(List<Item> items) {
        addAllItems(items);
        return stringify();
    }


    /**
     * Returns all lines given or read as one long String.
     *
     * @return All the lines so far given as one JSON string.
     */
    public String stringify() {
        String answer = "";
        for (String line: lines) {
            answer = answer + line +"\n";
        }
        answer = answer.substring(0, answer.length()-1);
        return answer;
    }

    /**
     * Parses a String with JSON format into an Item.
     *
     * @param json The JSON-formatted String which is wished to be parsed.
     * @return The resultin Item
     */
    public Item jsonToItem(String json) {
        String[] arr = json.replace("{", "")
                    .replace("}", "")
                    .replace("\n", "")
                    .replace("\"", "")
                    .replace(",", "")
                    .replace(" ", "")
                    .split(":");
        Item item = new Item(arr[0], arr[1]);
        return item;
    }

    /**
     * Receives an Object and parses it into a JSON formatted String.
     *
     * @param object Object wished to be parsed.
     * @return The given Object as a JSON line.
     */
    public String createJsonLine(Object object) {
        Class<?> objClass = object.getClass();
        Field[] fields = objClass.getFields();
        String line = "  {\n";
        try {
            for (Field field : fields) {
                String tag = field.getName();
                String value = field.get(object).toString();
                line = line +"    \"" +tag + "\": \"" + value + "\",\n";
            }
        } catch (IllegalAccessException e) { e.printStackTrace(); }
        line = line.substring(0, line.length()-2);
        line = line + "  \n  }";
        return line;
    }

    /**
     * Receives a list of Object and parses them into a JSON formatted String.
     *
     * @param objects List of Objects wished to be parsed.
     * @return The given Objects as a single JSON line.
     */
    public String createJsonLines(List<Object> objects) {
        addObjects(objects);
        return stringify();
    }

    /**
     * Input an Object for the Parser.
     *
     * Parser turns the Object given into JSON form and saves it in a String list.
     * Objects can later be written into a file all at once.
     *
     * @param object An Object that is wished to be added.
     */
    public void addObject(Object object) {
        if (lines.size() > 2) {
            String last = lines.get(lines.size() - 2);
            last = last + ",";
            lines.set(lines.size() -2, last);
        }
        lines.add(lines.size()-1, createJsonLine(object));
    }

    /**
     * Input a list of Objects for the Parser.
     *
     * Parser turns the Objects given into JSON form and saves them as a String list.
     * Objects can later be written into a file all at once.
     *
     * @param objects A list of Objects that are wished to be added.
     */
    public void addObjects(List<Object> objects) {
        lines = new LinkedList<String>();
        lines.add("[");
        lines.add("]");
        for (Object object: objects) {
            addObject(object);
        }
    }

    /**
     * Takes a list of Objects and saves them into a JSON file.
     *
     * @param objects A list of Objects that are wished to be added.
     */
    public void writeToFile(List<Object> objects) {
        addObjects(objects);
        writeToFile();
    }

    /**
     * Takes a list of Objects and saves them into a JSON file with the given name.
     *
     * @param objects A list of Objects that are wished to be added.
     * @param filename Name of the file where the information will be saved.
     */
    public void writeToFile(List<Object> objects, String filename) {
        addObjects(objects);
        writeToFile(filename);
    }

}