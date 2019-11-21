package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.*;

@SpringBootApplication
public class Parser implements CommandLineRunner {

	@Autowired
	MyDatabaseConnection connection;

	public static void main(String[] args) {
		SpringApplication.run(Parser.class, args);

	}

	@Transactional //
	public void run(String[] args) throws Exception {

		Item item = new Item("apple juice","5 litres");

		connection.save(item);
		connection.findAll()
					.forEach(System.out::println);
	}

}
