package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Parser {
    List<String> lines;
    List<Item> items;
    String filename;

    public Parser() {
        lines = new LinkedList<>();
        lines.add("{");
        lines.add("}");
        filename = "save.json";
    }

    public void addItem(Item item) {
        String newLine = "  \"" +item.getTag() +"\": \"" +item.getProperty() +"\",";
        lines.add(lines.size()-1, newLine);
    }

    public void writeToFile() {
        try {
            Path file = Paths.get(filename);
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch(Exception e) { e.printStackTrace(); }
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

    public void readFromFile() {
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch(Exception e) { e.printStackTrace(); }
        items = lines.stream()
                .filter(line -> !"{".equals(line))
                .filter(line -> !"}".equals(line))
                .map(line -> line.replace("  \"", ""))
                .map(line -> line.replace("\",", ""))
                .map(line -> line.split("\":"))
                .map(arr -> new Item(arr[0], arr[1]))
                .collect(Collectors.toList());
    }

}