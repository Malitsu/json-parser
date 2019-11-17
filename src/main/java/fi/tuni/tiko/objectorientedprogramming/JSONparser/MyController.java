package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.*;

// This class acts as a controller.
// Usually when using @Controller, you will use also @RequestMapping
@RestController
public class MyController {

    @Autowired
	MyDatabaseConnection connection;

    // When HTTP GET, POST, PUT or OTHER request happens
    // to http://localhost:8080/greeting
    // invoke this method
    @RequestMapping("/greeting")
    // The return value will be the HTTP Body
    @ResponseBody
    public String greeting() {
        return "Hello World";
    }

    @RequestMapping("/circle")
    public Circle getCircle() {
          Circle c = new Circle();
          c.setRadius(4);
          return c;
    }

    @RequestMapping("/circles")
    public Iterable<Circle> getCircles() {
        Iterable<Circle> results = connection.findAll();
          return results;
    }
}