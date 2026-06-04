package com.psayago.pruebaApp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psayago.pruebaApp.model.Greeting;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class AppRestController {

	static Logger logger = LogManager.getLogger(AppRestController.class);
	ObjectMapper objectMapper = new ObjectMapper();
	//@Value("${java.net.URL}")//leo variable de app
	//private String url;
	
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
	@CrossOrigin(origins = {"http://localhost:3000", "https://my-marketplace-app-nine.vercel.app"})
	@GetMapping(path = "/getJson" )
	public ResponseEntity<JsonNode> leerJson() throws IOException {
		//String url = System.getenv("JAVA_HOME"); leo variables de entorno
		//String url = System.getProperty("user.dir"); leo variables internas de JAVA
		String directorioActual = System.getProperty("user.dir");
		System.out.println("Directorio de trabajo: " + directorioActual);
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println("\n### Ejecuto comandos varios - INICIO ### ");
		//System.out.println(url);
		String rutaClase = AppRestController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		leerArchivo();
		//verComando();
		System.out.println("rutaClase: " + rutaClase);
		System.out.println("### Ejecuto comandos varios - FIN ### \n");
        //File archivoJson = new File("app"+File.separator +"src"+File.separator + "main" + File.separator + "resources"+ File.separator +"static"+File.separator+url);
        //File archivoJson = new File("src/main/resources/static/"+url);//para windows
		File archivoJson = new File("object.json");
		System.out.println("Ejecuto el codigo");
        System.out.println(archivoJson);
        Optional<JsonNode> json ;

        // Convierte el archivo JSON a un objeto Java
		json = Optional.ofNullable(objectMapper.readTree(archivoJson));
		JsonNode jsonNode = json.orElseThrow();
        System.out.println(jsonNode.toPrettyString());
		return ResponseEntity.ok().body(jsonNode);
	}

	public void leerArchivo() {
		// La ruta comienza desde la raíz de tus recursos (ej. src/main/resources/)
		String rutaArchivo = "/object.json";

		try (InputStream inputStream = getClass().getResourceAsStream(rutaArchivo)) {
			if (inputStream != null) {
				String contenido = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
						.lines().collect(Collectors.joining("\n"));
				System.out.println(contenido);
			} else {
				System.out.println("Archivo no encontrado en el classpath.");
			}
		} catch (Exception e) {
			logger.error("Error al leer el archivo", e);
		}
	}

	public static void verComando() {
			// Construimos el comando como una lista (más seguro y evita problemas con espacios)
			List<String> comando = new ArrayList<>();
			comando.add("/bin/bash");
			comando.add("-c");
			comando.add("ls");
			comando.add("ls -la /var/log"); // Reemplaza con tu comando

			ProcessBuilder pb = new ProcessBuilder(comando);

			// Combina la salida de error estándar (stderr) con la salida estándar (stdout)
			pb.redirectErrorStream(true);

			try {
				// Ejecutamos el comando
				Process proceso = pb.start();

				// Leemos la salida del comando
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(proceso.getInputStream()))) {

					String linea;
					while ((linea = reader.readLine()) != null) {
						logger.info(linea);
					}
				}

				// Obtenemos el código de salida (0 = éxito, distinto de 0 = error)
				int exitCode = proceso.waitFor();
				logger.info("\nProceso finalizado con código: " + exitCode);

			} catch (IOException | InterruptedException e) {
				logger.error("Error al ejecutar el comando", e);
			}
	}

}