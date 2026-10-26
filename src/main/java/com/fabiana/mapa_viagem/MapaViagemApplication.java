package com.fabiana.mapa_viagem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fabiana.mapa_viagem.enums.StatusAgendamento;
import com.fabiana.mapa_viagem.enums.StatusViagem;
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
import com.fabiana.mapa_viagem.repository.MotivoCancelamentoRepository;
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
    		AgendamentoRepository agendamentoRepository, TipoEspecialidadeRepository tipoEspecialidadeRepository, MotivoCancelamentoRepository motivoCancelamentoRepository) {
        return args -> {
        	
        	//Veiculo
        	
        	Veiculo v1 = new Veiculo("FAB2111", "Gol", 4);
            Veiculo v2 = new Veiculo("EUC1407", "VAN", 8);
            veiculoRepository.saveAll(Arrays.asList(v1, v2));
            System.out.println("Total de veiculos: " + veiculoRepository.count());
            
            //Motorista
            Motorista m1 = new Motorista("Joao Silva",LocalDate.of(1967, 2, 27),"033.044.012-52", "Rua Joao de Camrgo,151", "35 982003030");      
            Motorista m2 = new Motorista("Fabio Souza",LocalDate.of(1970, 4, 8),"023.014.052-22", "Cleia Sodre,20", "35 982304130");
            motoristaRepository.saveAll(Arrays.asList(m1, m2));
            System.out.println("Total de Motorista: " + motoristaRepository.count());
            
        
            
            // Pacientes para os testes de fechamento
            
            Paciente p1 = new Paciente("Alex Green",LocalDate.of(1957, 2, 10),"034.044.012-52", "Rua Arlindo Vieira, 20", "35 982105030","xxxxx");      
            Paciente p2 = new Paciente("Maria Brown",LocalDate.of(1957, 11, 6),"024.014.052-21", "Rua Parma,180", "35 982391130","yyyy");
            Paciente p3 = new Paciente("Zoe Blue",LocalDate.of(2024, 12, 27),"084.029.077-21", "Rua Parma,180", "35 998886149","zzzzz");
            Paciente p4 = new Paciente("Lola Black",LocalDate.of(1999, 12, 27),"081.023.077-20", "Rua Nova,180", "35 998815149","ddddd");
            pacienteRepository.saveAll(Arrays.asList(p1, p2,p3,p4));
            
            
            Paciente pacienteTeste6h = new Paciente(
                    "Carlos Oliveira",
                    LocalDate.of(1960, 3, 12),
                    "100.000.000-01",
                    "Rua das Flores, 10",
                    "35 999110001",
                    "teste6h"
            );

            Paciente pacienteTeste8h = new Paciente(
                    "Ana Martins",
                    LocalDate.of(1965, 5, 20),
                    "100.000.000-02",
                    "Rua das Acácias, 25",
                    "35 999110002",
                    "teste8h"
            );

            Paciente pacienteTeste9h = new Paciente(
                    "Pedro Santos",
                    LocalDate.of(1970, 7, 8),
                    "100.000.000-03",
                    "Rua do Sol, 30",
                    "35 999110003",
                    "teste9h"
            );

            Paciente pacienteTeste300km = new Paciente(
                    "Mariana Costa",
                    LocalDate.of(1975, 9, 15),
                    "100.000.000-04",
                    "Rua da Serra, 40",
                    "35 999110004",
                    "teste300km"
            );

            Paciente pacienteTestePernoite = new Paciente(
                    "Roberto Almeida",
                    LocalDate.of(1980, 11, 22),
                    "100.000.000-05",
                    "Rua Central, 50",
                    "35 999110005",
                    "testePernoite"
            );

            pacienteRepository.saveAll(Arrays.asList(
                    pacienteTeste6h,
                    pacienteTeste8h,
                    pacienteTeste9h,
                    pacienteTeste300km,
                    pacienteTestePernoite
            ));
            
            System.out.println("Total de Paciente: " + pacienteRepository.count());
            
            //Acompanhante
            Acompanhante a1 = new Acompanhante("Apolinario Silva",LocalDate.of(1978, 7, 14),"035.044.012-52", "Rua Parma,180", "35 982105890","zzzzz");      
            Acompanhante a2 = new Acompanhante("Zara Santos",LocalDate.of(1976, 11, 21),"040.027.061-25", "Rua Arlindo Vieira, Morada Do Sol, 20", "35 991062181","ffff");
            acompanhanteRepository.saveAll(Arrays.asList(a1, a2));
            System.out.println("Total de Acompanhante: " + acompanhanteRepository.count());
            
            
            //Hospital
            Hospital hospital1 = new Hospital("Clinica Sul Mineira","Praça Dr. Jose Braz","151", null, "Morro Chic","37541000","Santa Rita do Sapucai", "3534212020", TipoEstabelecimento.CLINICA);  
            Hospital hospital2 = new Hospital("Hospital Regional Sul de Minas","Av. Rui Barbosa","158", null, "Centro","37002140 ","Pouso Alegre", "3537212160", TipoEstabelecimento.HOSPITAL); 
            hospitalRepository.saveAll(Arrays.asList(hospital1, hospital2));
            System.out.println("Total de hospitais: " + hospitalRepository.count());
            
            TipoEspecialidade tp = new TipoEspecialidade("Teste");
            tipoEspecialidadeRepository.save(tp);
            
            // Viagens para testar o fechamento e o cálculo da diária
            LocalDate hoje = LocalDate.now();
           
            Viagem viagem1 = new Viagem(LocalDate.of(2026, 10, 30),"Pacientes para exames de sangue", "Santa Rita do Sapucaí", "Santa Rita do Sapucaí","MG", "MG", LocalTime.of(13, 00), null, null, StatusViagem.AGENDADA);
            Viagem viagem2 = new Viagem(LocalDate.now(), "Pacientes para exames de vista", "Santa Rita do Sapucaí", "Pouso Alegre", "MG", "MG",LocalTime.of(15, 30), m2, v2, StatusViagem.AGENDADA);
            Viagem viagem3 = new Viagem(LocalDate.of(2026, 10, 30),"Pacientes para exames", "Santa Rita do Sapucaí", "Pouso Alegre", "MG", "MG",LocalTime.of(15, 00), null, null, StatusViagem.AGENDADA);
           
            Viagem viagemTeste6h = new Viagem(hoje, "Teste fechamento - 6 horas", "Santa Rita do Sapucaí", "Pouso Alegre", "MG", "MG", LocalTime.of(8, 0), m1, v1, StatusViagem.AGENDADA);
            Viagem viagemTeste8h = new Viagem(
                    hoje,
                    "Teste fechamento - 8 horas",
                    "Santa Rita do Sapucaí",
                    "Pouso Alegre",
                    "MG",
                    "MG",
                    LocalTime.of(8, 0),
                    m1,
                    v1,
                    StatusViagem.AGENDADA
            );
            Viagem viagemTeste9h = new Viagem(
                    hoje,
                    "Teste fechamento - 9 horas",
                    "Santa Rita do Sapucaí",
                    "Pouso Alegre",
                    "MG",
                    "MG",
                    LocalTime.of(8, 0),
                    m2,
                    v2,
                    StatusViagem.AGENDADA
            );

            Viagem viagemTeste300km = new Viagem(
                    hoje,
                    "Teste fechamento - 300 km",
                    "Santa Rita do Sapucaí",
                    "Pouso Alegre",
                    "MG",
                    "MG",
                    LocalTime.of(8, 0),
                    m2,
                    v2,
                    StatusViagem.AGENDADA
            );

            Viagem viagemTestePernoite = new Viagem(
                    hoje,
                    "Teste fechamento - pernoite",
                    "Santa Rita do Sapucaí",
                    "Pouso Alegre",
                    "MG",
                    "MG",
                    LocalTime.of(8, 0),
                    m1,
                    v1,
                    StatusViagem.AGENDADA
            );

            viagemRepository.saveAll(Arrays.asList(
                    viagemTeste6h,
                    viagemTeste8h,
                    viagemTeste9h,
                    viagemTeste300km,
                    viagemTestePernoite
            ));

            
            
            viagemRepository.save(viagem1);
            viagemRepository.save(viagem2);
            viagemRepository.save(viagem3);
            System.out.println("Total de viagens: " + viagemRepository.count());
            
            //Agendamento
                      
            Agendamento agenda1 = new Agendamento(p1, a1, hospital1, tp, viagem1, LocalDate.of(2026,9 , 30),LocalTime.of(14, 30), TipoCompromisso.SUS, false, false, false, false, null, true, true, StatusAgendamento.AGENDADO);         	 
            Agendamento agenda2 = new Agendamento(p2, a2, hospital1, tp, viagem2, LocalDate.now(),LocalTime.of(16, 30), TipoCompromisso.SUS, false, false, false, false, null, true, true, StatusAgendamento.AGENDADO);
            Agendamento agenda3 = new Agendamento(p3, a2, hospital2, tp, viagem3, LocalDate.of(2026, 9, 30),LocalTime.of(16, 30), TipoCompromisso.SUS, false, false, false, false, null, true, true, StatusAgendamento.AGENDADO); 
            agendamentoRepository.saveAll(Arrays.asList(agenda1, agenda2, agenda3));
            
            Agendamento agendaTeste6h = new Agendamento(
                    pacienteTeste6h, a1, hospital1, tp, viagemTeste6h,
                    hoje, LocalTime.of(10, 0),
                    TipoCompromisso.SUS,
                    false, false, false, false, null,
                    true, true, StatusAgendamento.AGENDADO
            );

            // Agendamentos vinculados às viagens de teste
            Agendamento agendaTeste8h = new Agendamento(
                    pacienteTeste8h, a2, hospital1, tp, viagemTeste8h,
                    hoje, LocalTime.of(10, 0),
                    TipoCompromisso.SUS,
                    false, false, false, false, null,
                    true, true, StatusAgendamento.AGENDADO
            );

            Agendamento agendaTeste9h = new Agendamento(
                    pacienteTeste9h, a1, hospital2, tp, viagemTeste9h,
                    hoje, LocalTime.of(10, 0),
                    TipoCompromisso.SUS,
                    false, false, false, false, null,
                    true, true, StatusAgendamento.AGENDADO
            );

            Agendamento agendaTeste300km = new Agendamento(
                    pacienteTeste300km, a2, hospital2, tp, viagemTeste300km,
                    hoje, LocalTime.of(9, 0),
                    TipoCompromisso.SUS,
                    false, false, false, false, null,
                    true, true, StatusAgendamento.AGENDADO
            );

            Agendamento agendaTestePernoite = new Agendamento(
                    pacienteTestePernoite, a1, hospital2, tp, viagemTestePernoite,
                    hoje,  LocalTime.of(23, 0),
                    TipoCompromisso.SUS,
                    false, false, false, false, null,
                    true, true, StatusAgendamento.AGENDADO
            );

            agendamentoRepository.saveAll(Arrays.asList(
                    agendaTeste6h,
                    agendaTeste8h,
                    agendaTeste9h,
                    agendaTeste300km,
                    agendaTestePernoite
            ));

            
            System.out.println("Total de agendamento: " + agendamentoRepository.count());
           
        
        };
    }
}
