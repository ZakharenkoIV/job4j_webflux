package ru.job4j.weather.reactive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.job4j.weather.reactive.model.Weather;
import ru.job4j.weather.reactive.service.WeatherService;

import java.time.Duration;

@RestController
public class WeatherControl {

    private final WeatherService weathers;

    public WeatherControl(WeatherService weathers) {
        this.weathers = weathers;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        Flux<Weather> data = weathers.all();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weathers.findById(id);
    }

    @GetMapping(value = "/hottest")
    public Mono<Weather> getHottest() {
        return weathers.findHottest();
    }

    @GetMapping(value = "/cityGreatThen/{temperature}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> getCityGreatThen(@PathVariable Integer temperature) {
        return weathers.findByGreatThen(temperature);
    }
}