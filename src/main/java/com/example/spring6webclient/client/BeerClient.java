package com.example.spring6webclient.client;

import com.example.spring6webclient.model.BeerDto;
import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface BeerClient {
    Flux<String> listBeer();
    Flux<Map> listBeerMap();

    Flux<JsonNode> listBeerJsonNode();

    Flux<BeerDto> listBeerDtos();

    Mono<BeerDto> getBeerById(String id);

    Flux<BeerDto> getBeerByBeerStyle(String beerStyle);

    Mono<BeerDto> createBeer(BeerDto newDto);

    Mono<BeerDto> updateBeer(BeerDto dto);

    Mono<BeerDto> patchBeer(BeerDto dto);

    Mono<Void> deleteBeer(BeerDto dto);
}
