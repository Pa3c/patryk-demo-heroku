package com.patryk.demo.heroku;

import io.netty.util.internal.MathUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class DemoHerokuApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoHerokuApplication.class, args);
	}

}

@RestController
class DemoEndpoint {

	@GetMapping(path = "/hello")
	public String helloDemo(@RequestParam(name = "name", required = false) String name) {

		return "Hello "+ ( name == null ? "Idioto" : name );
	}


	@GetMapping(path = "/hello/hooman",produces = MediaType.APPLICATION_JSON_VALUE)
	public Hooman helloPrzystojniaku(@RequestParam(name = "name", required = false) String name) {
		final var randomGenerator = new Random();
		return new Hooman(( name == null ? "Idiota" : name ), randomGenerator.nextDouble(100, 180), randomGenerator.nextDouble(100, 180));
	}

	@GetMapping(path = "/hello/hooman/reactive",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<Hooman> helloReaktywnyPrzystojniaku(@RequestParam(name = "name", required = false) String name) {
		final var randomGenerator = new Random();
		final var hoooman = new Hooman(( name == null ? "Idiota" : name ), randomGenerator.nextDouble(100, 180), randomGenerator.nextDouble(100, 180));
		return Mono.justOrEmpty(hoooman);
	}

	@GetMapping(path = "/hello/harem/reactive",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Hooman> helloReaktywnyHarem(@RequestParam(name = "name", required = false) String name) {
		final var randomGenerator = new Random();

		final List<Hooman> hoomans = new ArrayList<>();
			for(int i = 0; i<=10; i++){
				hoomans.add(new Hooman(( name == null ? "Derp" : name ), randomGenerator.nextDouble(100, 180), randomGenerator.nextDouble(100, 180)));
			}

			return Flux.fromIterable(hoomans).delayElements(Duration.ofSeconds(1));
	}
}

record Hooman(String name, double height, double weight) {}