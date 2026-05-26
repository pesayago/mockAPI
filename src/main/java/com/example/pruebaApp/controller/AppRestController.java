package com.example.pruebaApp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pruebaApp.model.Greeting;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@RestController
public class AppRestController {
	
	private static final String URL = null;
	ObjectMapper objectMapper = new ObjectMapper();
	@Value("${java.net.URL}")
	private String url;
	
	@GetMapping(path = "/getBar" )
	public ResponseEntity<JsonNode> getAllBares(){
//		try {
//			Thread.sleep(700);
//		} catch (InterruptedException e) {
//			e.printStackTrace();	
//		}
		Greeting greeting = new Greeting(0, "Hello Ibis");
		
		JsonNode json = objectMapper.valueToTree(greeting);
		
		return ResponseEntity.ok().body(json);		
	}
	
	@GetMapping(path = "/getJson" )
	public ResponseEntity<JsonNode> leerJson() throws IOException {
		//String url = System.getenv("JAVA_HOME"); leo variables de entorno
		//String url = System.getProperty("user.dir"); leo variables de internas de JAVA
		String directorioActual = System.getProperty("user.dir");
		System.out.println("Directorio de trabajo: " + directorioActual);
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println("Ejecuto el codigo");
		System.out.println(url);
        File archivoJson = new File("main\\resources\\static\\"+url);
        //File archivoJson = new File("app/main/resources/static/"+url); para windows
        Optional<JsonNode>  json = null;

        // Convierte el archivo JSON a un objeto Java
		json = Optional.ofNullable(objectMapper.readTree(archivoJson));
		JsonNode jsonNode = json.orElseThrow();
        System.out.println(jsonNode.toPrettyString());
		return ResponseEntity.ok().body(jsonNode);
	}

}


