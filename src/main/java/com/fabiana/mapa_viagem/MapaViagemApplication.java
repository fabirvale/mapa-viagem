package com.fabiana.mapa_viagem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fabiana.mapa_viagem.enums.TipoCompromisso;
import com.fabiana.mapa_viagem.enums.TipoEstabelecimento;
import com.fabiana.mapa_viagem.model.Acompanhante;
import com.fabiana.mapa_viagem.model.Agendamento;
import com.fabiana.mapa_viagem.model.Hospital;
import com.fabiana.mapa_viagem.model.Motorista;
import com.fabiana.mapa_viagem.model.Paciente;
import com.fabiana.mapa_viagem.model.TipoEspecialidade;
import com.fabiana.mapa_viagem.model.Veiculo;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.AcompanhanteRepository;
import com.fabiana.mapa_viagem.repository.AgendamentoRepository;
import com.fabiana.mapa_viagem.repository.HospitalRepository;
import com.fabiana.mapa_viagem.repository.MotoristaRepository;
import com.fabiana.mapa_viagem.repository.PacienteRepository;
import com.fabiana.mapa_viagem.repository.TipoEspecialidadeRepository;
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
    		AgendamentoRepository agendamentoRepository, TipoEspecialidadeRepository tipoEspecialidadeRepository) {
        return args -> {
        	
        	Veiculo v1 = new Veiculo("FAB2111", "Gol", 4);
            Veiculo v2 = new Veiculo("EUC1407", "VAN", 8);
            veiculoRepository.saveAll(Arrays.asList(v1, v2));
            System.out.println("Total de veiculos: " + veiculoRepository.count());
            
            Motorista m1 = new Motorista("Joao Silva",LocalDate.of(1967, 2, 27),"033.044.012-52", "Rua Joao de Camrgo,151", "35 982003030");      
            Motorista m2 = new Motorista("Fabio Souza",LocalDate.of(1970, 4, 8),"023.014.052-22", "Cleia Sodre,20", "35 982304130");
            motoristaRepository.saveAll(Arrays.asList(m1, m2));
            System.out.println("Total de Motorista: " + motoristaRepository.count());
            

            Viagem viagem1 = new Viagem(LocalDate.of(2026, 6, 30),"Pacientes para exames de sangue", "Santa Rita do Sapucaí", "Santa Rita do Sapucaí","MG", "MG", LocalTime.of(13, 00), null, null);
            Viagem viagem2 = new Viagem(LocalDate.now(), "Pacientes para exames de vista", "Santa Rita do Sapucaí", "Pouso Alegre", "MG", "MG",LocalTime.of(15, 30), m2, v2);
            Viagem viagem3 = new Viagem(LocalDate.of(2026, 6, 30),"Pacientes para exames", "Santa Rita do Sapucaí", "Pouso Alegre", "MG", "MG",LocalTime.of(15, 00), null, null);
            viagemRepository.save(viagem1);
            viagemRepository.save(viagem2);
            viagemRepository.save(viagem3);
            System.out.println("Total de viagens: " + viagemRepository.count());
            
            
            Paciente p1 = new Paciente("Alex Green",LocalDate.of(1957, 2, 10),"034.044.012-52", "Rua Arlindo Vieira, 20", "35 982105030","xxxxx");      
            Paciente p2 = new Paciente("Maria Brown",LocalDate.of(1957, 11, 6),"024.014.052-21", "Rua Parma,180", "35 982391130","yyyy");
            Paciente p3 = new Paciente("Zoe Blue",LocalDate.of(2024, 12, 27),"084.029.077-21", "Rua Parma,180", "35 998886149","zzzzz");
            pacienteRepository.saveAll(Arrays.asList(p1, p2,p3));
            System.out.println("Total de Paciente: " + pacienteRepository.count());
            
            Acompanhante a1 = new Acompanhante("Apolinario Silva",LocalDate.of(1978, 7, 14),"035.044.012-52", "Rua Parma,180", "35 982105890","zzzzz");      
            Acompanhante a2 = new Acompanhante("Zara Santos",LocalDate.of(1976, 11, 21),"040.027.061-25", "Rua Arlindo Vieira, Morada Do Sol, 20", "35 991062181","ffff");
            acompanhanteRepository.saveAll(Arrays.asList(a1, a2));
            System.out.println("Total de Acompanhante: " + acompanhanteRepository.count());
            
            Hospital hospital1 = new Hospital("Clinica Sul Mineira","Praça Dr. Jose Braz","151", null, "Morro Chic","37541000","Santa Rita do Sapucai", "3534212020", TipoEstabelecimento.CLINICA);  
            Hospital hospital2 = new Hospital("Hospital Regional Sul de Minas","Av. Rui Barbosa","158", null, "Centro","37002140 ","Pouso Alegre", "3537212160", TipoEstabelecimento.HOSPITAL); 
            hospitalRepository.saveAll(Arrays.asList(hospital1, hospital2));
            System.out.println("Total de hospitais: " + hospitalRepository.count());
            
            TipoEspecialidade tp = new TipoEspecialidade("Teste");
            tipoEspecialidadeRepository.save(tp);
            
                      
            Agendamento agenda1 = new Agendamento(p1, a1, hospital1, tp, viagem1, LocalDate.of(2026,6 , 30),LocalTime.of(14, 30), TipoCompromisso.SUS, false, false, false, false, null, true, true);         	 
            Agendamento agenda2 = new Agendamento(p2, a2, hospital1, tp, viagem2, LocalDate.now(),LocalTime.of(14, 30), TipoCompromisso.SUS, false, false, false, false, null, true, true);
            Agendamento agenda3 = new Agendamento(p3, a2, hospital2, tp, viagem3, LocalDate.of(2026, 6, 30),LocalTime.of(14, 30), TipoCompromisso.SUS, false, false, false, false, null, true, true); 
            agendamentoRepository.saveAll(Arrays.asList(agenda1, agenda2, agenda3));
            System.out.println("Total de agendamento: " + agendamentoRepository.count());
           
        
        };
    }
}
