package com.example.ventas_bodega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class VentasBodegaApplication {

	public static void main(String[] args) {
		// Fija la zona horaria de la JVM sin depender del reloj del SO/host donde se
		// despliegue (local, Docker, nube): LocalDateTime.now() se usa en todo el código
		// para timestamps y debe reflejar siempre la hora de Perú.
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
		SpringApplication.run(VentasBodegaApplication.class, args);
	}

}
