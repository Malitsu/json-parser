package fi.tuni.tiko.objectorientedprogramming.JSONparser;

import org.hibernate.*;
import org.hibernate.cfg.*;

import java.lang.reflect.Field;
import java.util.List;

public class App {
	public static void main(String[] args) {

		Parser parser = new Parser();
		JavaObject jo = new JavaObject("juice", "5 liters");
		System.out.println(parser.parseToJson(jo));

	}
}
