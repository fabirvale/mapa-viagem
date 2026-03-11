package com.fabiana.mapa_viagem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fabiana.mapa_viagem.enums.TipoEstabelecimento;
import com.fabiana.mapa_viagem.model.Acompanhante;
import com.fabiana.mapa_viagem.model.Agendamento;
import com.fabiana.mapa_viagem.model.Hospital;
import com.fabiana.mapa_viagem.model.Motorista;
import com.fabiana.mapa_viagem.model.Paciente;
import com.fabiana.mapa_viagem.model.Veiculo;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.AcompanhanteRepository;
import com.fabiana.mapa_viagem.repository.AgendamentoRepository;
import com.fabiana.mapa_viagem.repository.HospitalRepository;
import com.fabiana.mapa_viagem.repository.MotoristaRepository;
import com.fabiana.mapa_viagem.repository.PacienteRepository;
import com.fabiana.mapa_viagem.repository.VeiculoRepository;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

@SpringBootApplication
public class MapaViagemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapaViagemApplication.class, args);
    }

    @Bean
    CommandLineRunner testViagem(ViagemRepository viagemRepository, VeiculoRepository veiculoRepository,
    		MotoristaRepository motoristaRepository, PacienteRepository pacienteRepository, 
    		AcompanhanteRepository acompanhanteRepository, HospitalRepository hospitalRepository,
    		AgendamentoRepository agendamentoRepository) {
        return args -> {
        	
        	Veiculo v1 = new Veiculo("FAB2111", "Gol", 4);
            Veiculo v2 = new Veiculo("EUC1407", "VAN", 8);
            veiculoRepository.saveAll(Arrays.asList(v1, v2));
            System.out.println("Total de veiculos: " + veiculoRepository.count());
            
            Motorista m1 = new Motorista("Joao Silva",LocalDate.of(1967, 2, 27),"033.044.012-52", "Rua Joao de Camrgo,151", "35 982003030");      
            Motorista m2 = new Motorista("Fabio Souza",LocalDate.of(1970, 4, 8),"023.014.052-22", "Cleia Sodre,20", "35 982304130");
            motoristaRepository.saveAll(Arrays.asList(m1, m2));
            System.out.println("Total de Motorista: " + motoristaRepository.count());
            

            Viagem viagem1 = new Viagem(LocalDate.of(2026, 3, 30),"Pacientes para exames de sangue", "Santa Rita do Sapucai", "Santa Rita do Sapucai",LocalTime.of(10, 00), null, null);
            Viagem viagem2 = new Viagem(LocalDate.now(), "Pacientes para exames de vista", "Santa Rita do Sapucai", "Pouso Alegre",LocalTime.of(9, 30), m2, v2);
            Viagem viagem3 = new Viagem(LocalDate.of(2026, 3, 30),"Pacientes para exames", "Santa Rita do Sapucai", "Pouso Alegre",LocalTime.of(9, 00), null, null);
            viagemRepository.save(viagem1);
            viagemRepository.save(viagem2);
            viagemRepository.save(viagem3);
            System.out.println("Total de viagens: " + viagemRepository.count());
            
            
            Paciente p1 = new Paciente("Luiz Carlos Ribeiro do Vale",LocalDate.of(1957, 2, 10),"034.044.012-52", "Rua Erasmo Cabral,157", "35 982105030","xxxxx");      
            Paciente p2 = new Paciente("Maria de Fátima Ribeiro do Vale",LocalDate.of(1957, 11, 6),"024.014.052-21", "Rua Erasmo Cabral,157", "35 982391130","yyyy");
            Paciente p3 = new Paciente("Zoe Ribeiro do Vale",LocalDate.of(2024, 12, 27),"084.029.077-21", "Rua Erasmo Cabral,157", "35 998886149","zzzzz");
            pacienteRepository.saveAll(Arrays.asList(p1, p2,p3));
            System.out.println("Total de Paciente: " + pacienteRepository.count());
            
            Acompanhante a1 = new Acompanhante("Euclides de Oliveira Ribeiro do Vale",LocalDate.of(1978, 7, 14),"035.044.012-52", "Rua Erasmo Cabral,157", "35 982105890","zzzzz");      
            Acompanhante a2 = new Acompanhante("Fabiana Ap. Ribeiro do Vale",LocalDate.of(1976, 11, 21),"028.072.016-52", "Rua Ricjard Conrard de Menezes de Alckimin,126, Morada Do Sol, apto-304", "11 981062080","ffff");
            acompanhanteRepository.saveAll(Arrays.asList(a1, a2));
            System.out.println("Total de Acompanhante: " + acompanhanteRepository.count());
            
            Hospital hospital1 = new Hospital("Clinica Sul Mineira","Praça Dr. Jose Braz","151", null, "Morro Chic","37541000","Santa Rita do Sapucai", "3534212020", TipoEstabelecimento.CLINICA);  
            Hospital hospital2 = new Hospital("Hospital Regional Sul de Minas","Av. Rui Barbosa","158", null, "Centro","37002140 ","Pouso Alegre", "3537212160", TipoEstabelecimento.HOSPITAL); 
            hospitalRepository.saveAll(Arrays.asList(hospital1, hospital2));
            System.out.println("Total de hospitais: " + hospitalRepository.count());
            
            Agendamento agenda1 = new Agendamento(p1, a1, hospital1, viagem1, LocalDate.now(),LocalTime.of(14, 30));         	 
            Agendamento agenda2 = new Agendamento(p2, a2, hospital1, viagem2, LocalDate.now(),LocalTime.of(14, 30));
            Agendamento agenda3 = new Agendamento(p3, a2, hospital2, viagem3, LocalDate.of(2026, 3, 30),LocalTime.of(14, 30)); 
            agendamentoRepository.saveAll(Arrays.asList(agenda1, agenda2, agenda3));
            System.out.println("Total de agendamento: " + agendamentoRepository.count());
           
        
        };
    }
}
