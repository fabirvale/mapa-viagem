package com.fabiana.mapa_viagem;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

@SpringBootApplication
public class MapaViagemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapaViagemApplication.class, args);
    }

    @Bean
    CommandLineRunner testViagem(ViagemRepository viagemRepository) {
        return args -> {

            Viagem viagem1 = new Viagem(LocalDate.now(), "Pouso Alegre", "Belo Horizonte");
            Viagem viagem2 = new Viagem(LocalDate.now(), "Santa Rita do Sapucai", "Pouso Alegre");

            viagemRepository.save(viagem1);
            viagemRepository.save(viagem2);

            System.out.println("Total de viagens: " + viagemRepository.count());
        };
    }
}
