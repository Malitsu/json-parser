package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Parser<T, P> {
    List<String> lines;
    List<Item> items;
    String filename;

    public Parser() {
        reset();
        filename = "save.json";
    }

    public Item createItem(T first, P second) {
        Item item = new Item();
        item.setTag(first.toString());
        item.setProperty(second.toString());
        return item;
    }

    public List<Item> createItems(HashMap<T, P> map) {
        LinkedList<Item> list = new LinkedList<>();
        for (T object: map.keySet()) {
            Item item = new Item();
            item.setTag(object.toString());
            item.setProperty(map.get(object).toString());
            list.add(item);
        }
        return list;
    }

    public String createJSONLine(Item item) {
        String line = "  \"" +item.getTag() +"\": \"" +item.getProperty() +"\"";
        return line;
    }

    public List<String> createJSONLines(HashMap<T, P> map) {
        LinkedList<String> list = new LinkedList<>();
        for (T object: map.keySet()) {
            String line = "  \"" +object.toString() +"\": \"" +map.get(object).toString() +"\"";
            list.add(line);
        }
        return list;
    }

    public List<String> createJSONLines(List<Item> items) {
        LinkedList<String> list = new LinkedList<>();
        for(Item item: items) {
            list.add(createJSONLine(item));
        }
        return list;
    }

    public HashMap<String, String> convertToHashMap(List<Item> items) {
        HashMap<String, String> map = new HashMap<>();
        for(Item item: items) {
            String first = item.getTag();
            String second = item.getProperty();
            map.put(first, second);
        }
        return map;
    }

    public void addItem(Item item) {
        if (lines.size() > 2) {
            String last = lines.get(lines.size() - 2);
            last = last + ",";
            lines.set(lines.size() -2, last);
        }
        lines.add(lines.size()-1, createJSONLine(item));
    }

    public void addAll(HashMap<T, P> map) {
        lines = createJSONLines(map);
        lines.add(0, "{");
        lines.add(lines.size(), "}");
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
        this.filename = filename;
        writeToFile();
    }

    public void writeToFile(List<Item> items) {
        addAllItems(items);
        writeToFile();
    }

    public void writeToFile(List<Item> items, String filename) {
        addAllItems(items);
        writeToFile(filename);
    }

    public void writeToFile(HashMap<T, P> map) {
        addAll(map);
        writeToFile();
    }

    public void writeToFile(HashMap<T, P> map, String filename) {
        addAll(map);
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
        this.filename = filename;
        readFromFile();
    }

    public void reset() {
        lines = new LinkedList<>();
        lines.add("{");
        lines.add("}");
        items = new LinkedList<>();
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
        this.filename = filename;
        return fileIntoLines();
    }

    public String convertToJson(List<Item> items) {
        addAllItems(items);
        return stringify();
    }

    public String convertToJson(HashMap<T, P> map) {
        addAll(map);
        return stringify();
    }

    public String stringify() {
        String answer = "";
        for (String line: lines) {
            answer = answer + line +"\n";
        }
        return answer;
    }

    public Item jsonToItem(String json) {
        String[] arr = json.replace("{", "")
                    .replace("}", "")
                    .replace("\n", "")
                    .replace("\"", "")
                    .replace(",", "")
                    .trim()
                    .split(":  ");
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

}