package com.myapp;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

public class Event {
	private static final JsonGeneratorFactory JS_FACTORY;
	static {
		Map<String, Object> properties = new HashMap<String, Object>(1);
	    properties.put(JsonGenerator.PRETTY_PRINTING, true);
	    JS_FACTORY = Json.createGeneratorFactory(properties);
	}

	
	private Date date;
	private int amount;
	
	public static Event buildNextRandomically(){
		Random rand = new Random();
		Event e = new Event();
		e.amount = rand.nextInt(100000);
		e.date = new Date();
		return e;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String toJson() {        
        StringWriter writter = new StringWriter();
        JsonGenerator gen = JS_FACTORY.createGenerator(writter);

        gen.writeStartObject()              // {
	    	.write("date", date.getTime() ) //    "date":35423462625654,
	    	.write("amount", amount)        //    "name":16545,
	    .writeEnd()                         // }
	    .close();
        
        return writter.toString();
	}

	@Override
	public String toString() {
		return "Event [date=" + date + ", amount=" + amount + "]";
	}
}
