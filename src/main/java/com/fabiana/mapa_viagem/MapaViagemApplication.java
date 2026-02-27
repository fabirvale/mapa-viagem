package com.fabiana.mapa_viagem;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fabiana.mapa_viagem.model.Motorista;
import com.fabiana.mapa_viagem.model.Veiculo;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.MotoristaRepository;
import com.fabiana.mapa_viagem.repository.VeiculoRepository;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

@SpringBootApplication
public class MapaViagemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapaViagemApplication.class, args);
    }

    @Bean
    CommandLineRunner testViagem(ViagemRepository viagemRepository, VeiculoRepository veiculoRepository, MotoristaRepository motoristaRepository) {
        return args -> {

            Viagem viagem1 = new Viagem(LocalDate.now(),"Pacientes para exames de sangue", "Pouso Alegre", "Belo Horizonte");
            Viagem viagem2 = new Viagem(LocalDate.now(), "Pacientes para exames de vista", "Santa Rita do Sapucai", "Pouso Alegre");
            viagemRepository.save(viagem1);
            viagemRepository.save(viagem2);
            System.out.println("Total de viagens: " + viagemRepository.count());
            
            Veiculo v1 = new Veiculo("FAB2111", "Gol", 4);
            Veiculo v2 = new Veiculo("EUC1407", "VAN", 8);
            veiculoRepository.saveAll(Arrays.asList(v1, v2));
            System.out.println("Total de veiculos: " + veiculoRepository.count());
            
            Motorista m1 = new Motorista("Joao Silva",LocalDate.of(1967, 2, 27),"033.044.012-52", "Rua Joao de Camrgo,151", "35 982003030");      
            Motorista m2 = new Motorista("Fabio Souza",LocalDate.of(1970, 4, 8),"023.014.052-22", "Cleia Sodre,20", "35 982304130");
            motoristaRepository.saveAll(Arrays.asList(m1, m2));
            System.out.println("Total de Motorista: " + motoristaRepository.count());
            
            
 
        };
    }
}
