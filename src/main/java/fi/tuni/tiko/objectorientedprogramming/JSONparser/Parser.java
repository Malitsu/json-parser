package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Parser<T, P> {
    List<String> lines = new LinkedList<>();
    List<Item> items = new LinkedList<>();
    String filename;

    public Parser() {
        reset();
    }

    public Item createItem(T first, P second) {
        Item item = new Item();
        item.setTag(first.toString());
        item.setProperty(second.toString());
        return item;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String createJsonLine(Item item) {
        String line = "  \"" +item.getTag() +"\": \"" +item.getProperty() +"\"";
        return line;
    }

    public List<String> createJsonLines(HashMap<T, P> map) {
        LinkedList<String> list = new LinkedList<String>();
        for (T object: map.keySet()) {
            String line = "  \"" +object.toString() +"\": \"" +map.get(object).toString() +"\"";
            list.add(line);
        }
        return list;
    }

    public List<String> convertItemsToJsonLines(List<Item> items) {
        LinkedList<String> list = new LinkedList<>();
        for(Item item: items) {
            list.add(createJsonLine(item));
        }
        return list;
    }

    public void addItem(Item item) {
        if (lines.size() > 2) {
            String last = lines.get(lines.size() - 2);
            last = last + ",";
            lines.set(lines.size() -2, last);
        }
        lines.add(lines.size()-1, createJsonLine(item));
    }

    public void addAllItems(List<Item> items) {
        for(Item item: items) {
            addItem(item);
        }
    }

    public void writeToFile() {
        try {
            Path file = Paths.get(filename);
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void writeToFile(String filename) {
        setFilename(filename);
        writeToFile();
    }

    public void writeItemsToFile(List<Item> items) {
        addAllItems(items);
        writeToFile();
    }

    public void writeItemsToFile(List<Item> items, String filename) {
        addAllItems(items);
        writeToFile(filename);
    }

    public boolean areMoreItems() {
        return !items.isEmpty();
    }

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

    public Optional<List> returnAllItems() {
        return Optional.ofNullable(items);
    }

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

    public void readFromFile(String filename) {
        setFilename(filename);
        readFromFile();
    }

    public void reset() {
        lines = new LinkedList<String>();
        lines.add("{");
        lines.add("}");
        items = new LinkedList<>();
        filename = "save.json";
    }

    public List<Item> fileIntoItems(String filename) {
        readFromFile(filename);
        return items;
    }

    public List<String> fileIntoLines() {
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch(Exception e) { e.printStackTrace(); }
        return lines;
    }

    public List<String> fileIntoLines(String filename) {
        setFilename(filename);
        return fileIntoLines();
    }

    public String convertToJson(List<Item> items) {
        addAllItems(items);
        return stringify();
    }

    public String stringify() {
        String answer = "";
        for (String line: lines) {
            answer = answer + line +"\n";
        }
        answer = answer.substring(0, answer.length()-1);
        return answer;
    }

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

    public List<Item> jsonToItems(String json) {
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
        return items;
    }

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

    public String createJsonLines(List<Object> objects) {
        addObjects(objects);
        return stringify();
    }

    public void addObject(Object object) {
        if (lines.size() > 2) {
            String last = lines.get(lines.size() - 2);
            last = last + ",";
            lines.set(lines.size() -2, last);
        }
        lines.add(lines.size()-1, createJsonLine(object));
    }

    public void addObjects(List<Object> objects) {
        lines = new LinkedList<String>();
        lines.add("[");
        lines.add("]");
        for (Object object: objects) {
            addObject(object);
        }
    }

    public void writeToFile(List<Object> objects) {
        addObjects(objects);
        writeToFile();
    }

    public void writeToFile(List<Object> objects, String filename) {
        addObjects(objects);
        writeToFile(filename);
    }

}