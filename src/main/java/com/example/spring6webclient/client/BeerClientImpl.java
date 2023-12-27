package com.example.spring6webclient.client;

import com.example.spring6webclient.model.BeerDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class BeerClientImpl implements BeerClient {
    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final WebClient webClient;

    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri(BEER_PATH).retrieve().bodyToFlux(String.class);
    }

    @Override
    public Flux<Map> listBeerMap() {
        return webClient.get().uri(BEER_PATH).retrieve().bodyToFlux(Map.class);
    }

    @Override
    public Flux<JsonNode> listBeerJsonNode() {
        return webClient.get().uri(BEER_PATH).retrieve().bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<BeerDto> listBeerDtos() {
        return webClient.get().uri(BEER_PATH).retrieve().bodyToFlux(BeerDto.class);
    }

    @Override
    public Mono<BeerDto> getBeerById(String id) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(id))
                .retrieve()
                .bodyToMono(BeerDto.class);
    }

    @Override
    public Flux<BeerDto> getBeerByBeerStyle(String beerStyle) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path(BEER_PATH)
                        .queryParam("beerStyle", beerStyle).build())
                .retrieve()
                .bodyToFlux(BeerDto.class);
    }

    @Override
    public Mono<BeerDto> createBeer(BeerDto newDto) {
        return webClient.post()
                .uri(BEER_PATH)
                .body(Mono.just(newDto), BeerDto.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> Mono.just(voidResponseEntity.getHeaders().get("Location").get(0)))
                .map(path -> path.split("/")[path.split("/").length - 1])
                .flatMap(this::getBeerById);
    }

    @Override
    public Mono<BeerDto> updateBeer(BeerDto dto) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(dto.getId()))
                .body(Mono.just(dto), BeerDto.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(dto.getId()));
    }
}
