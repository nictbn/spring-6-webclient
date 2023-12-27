package com.example.spring6webclient.client;

import com.example.spring6webclient.model.BeerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient beerClient;

    @Test
    void listBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeer().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void listBeerMap() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerMap().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerJson() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerJsonNode().subscribe(jsonNode -> {
            System.out.println(jsonNode.toPrettyString());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerDto() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerDtos().subscribe(dto -> {
            System.out.println(dto.getBeerName());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerById() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerDtos()
                .flatMap(dto -> beerClient.getBeerById(dto.getId()))
                .subscribe((byIdDto -> {
                    System.out.println(byIdDto.getBeerName());
                    atomicBoolean.set(true);
                }));
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerByBeerStyle() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.getBeerByBeerStyle("Pale Ale")
                .subscribe((dto -> {
                    System.out.println(dto);
                    atomicBoolean.set(true);
                }));
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testCreateBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        BeerDto newDto = BeerDto.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle("IPA")
                .quantityOnHand(500)
                .upc("123245")
                .build();
        beerClient.createBeer(newDto)
                .subscribe(dto -> {
                    System.out.println(dto);
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testUpdateBeer() {
        final String name = "New Name";
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerDtos()
                .next()
                .doOnNext(beerDto -> beerDto.setBeerName(name))
                .flatMap(dto -> beerClient.updateBeer(dto))
                .subscribe(byIdDto -> {
                    System.out.println(byIdDto);
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }
}