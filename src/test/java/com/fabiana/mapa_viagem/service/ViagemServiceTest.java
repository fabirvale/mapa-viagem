package com.fabiana.mapa_viagem.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fabiana.mapa_viagem.dto.FecharViagemDTO;
import com.fabiana.mapa_viagem.dto.ViagemDTO;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.OcorrenciaDuranteViagemRepository;
import com.fabiana.mapa_viagem.repository.ViagemRepository;
import com.fabiana.mapa_viagem.service.ViagemService;

@ExtendWith(MockitoExtension.class)
public class ViagemServiceTest {
	
	  @InjectMocks
	  private ViagemService viagemService;

	  @Mock
	  private ViagemRepository viagemRepository;

	  @Mock
	  private OcorrenciaDuranteViagemRepository ocorrenciaRepository;
	  
	  @Test
	  void deveFecharViagemComSucesso() {

	      Long viagemId = 1L;

	      Viagem viagem = new Viagem();
	      viagem.setDataViagem(LocalDate.now());
	      viagem.setHoraPrevista(LocalTime.of(8, 0));

	      when(viagemRepository.findById(viagemId))
	              .thenReturn(Optional.of(viagem));

	      ViagemDTO viagemDTO = new ViagemDTO();
	      viagemDTO.setDataRetorno(LocalDate.now());
	      viagemDTO.setHoraChegada(LocalTime.of(10, 0));
	      viagemDTO.setKmInicial(100);
	      viagemDTO.setKmFinal(200);

	      FecharViagemDTO pagamentoDTO = new FecharViagemDTO();
	      pagamentoDTO.setDataHoraSaida(LocalDateTime.now().minusHours(5));
	      pagamentoDTO.setDataHoraAlmoco(LocalDateTime.now().minusHours(2));
	      pagamentoDTO.setDataHoraRetorno(LocalDateTime.now());

	      viagemService.fecharViagem(viagemId, viagemDTO, pagamentoDTO);

	      verify(viagemRepository).save(any(Viagem.class));
	  }
	  
	  @Test
	  void deveLancarExcecaoQuandoViagemNaoExiste() {

	      Long viagemId = 1L;

	      when(viagemRepository.findById(viagemId))
	              .thenReturn(Optional.empty());

	      ViagemDTO viagemDTO = new ViagemDTO();
	      FecharViagemDTO pagamentoDTO = new FecharViagemDTO();

	      assertThrows(RecursoNaoEncontradoException.class, () -> {
	          viagemService.fecharViagem(viagemId, viagemDTO, pagamentoDTO);
	      });
	  }
	  
	  @Test
	  void deveLancarErroQuandoKmFinalMenorQueInicial() {

	      Long viagemId = 1L;

	      Viagem viagem = new Viagem();
	      viagem.setDataViagem(LocalDate.now());
	      viagem.setHoraPrevista(LocalTime.of(8, 0));

	      when(viagemRepository.findById(viagemId))
	              .thenReturn(Optional.of(viagem));

	      ViagemDTO viagemDTO = new ViagemDTO();
	      viagemDTO.setDataRetorno(LocalDate.now());
	      viagemDTO.setHoraChegada(LocalTime.of(10, 0));
	      viagemDTO.setKmInicial(200);
	      viagemDTO.setKmFinal(100); // erro aqui

	      FecharViagemDTO pagamentoDTO = new FecharViagemDTO();
	      pagamentoDTO.setDataHoraSaida(LocalDateTime.now().minusHours(5));
	      pagamentoDTO.setDataHoraAlmoco(LocalDateTime.now().minusHours(2));
	      pagamentoDTO.setDataHoraRetorno(LocalDateTime.now());

	      assertThrows(RegraNegocioException.class, () -> {
	          viagemService.fecharViagem(viagemId, viagemDTO, pagamentoDTO);
	      });
	  }
	  
	  @Test
	  void deveLancarErroQuandoDataChegadaAntesInicio() {

	      Long viagemId = 1L;

	      Viagem viagem = new Viagem();
	      viagem.setDataViagem(LocalDate.now());
	      viagem.setHoraPrevista(LocalTime.of(10, 0));

	      when(viagemRepository.findById(viagemId))
	              .thenReturn(Optional.of(viagem));

	      ViagemDTO viagemDTO = new ViagemDTO();
	      viagemDTO.setDataRetorno(LocalDate.now());
	      viagemDTO.setHoraChegada(LocalTime.of(8, 0)); // antes da saída 
	      viagemDTO.setKmInicial(100);
	      viagemDTO.setKmFinal(200);

	      FecharViagemDTO pagamentoDTO = new FecharViagemDTO();
	      pagamentoDTO.setDataHoraSaida(LocalDateTime.now().minusHours(5));
	      pagamentoDTO.setDataHoraAlmoco(LocalDateTime.now().minusHours(2));
	      pagamentoDTO.setDataHoraRetorno(LocalDateTime.now());

	      assertThrows(RegraNegocioException.class, () -> {
	          viagemService.fecharViagem(viagemId, viagemDTO, pagamentoDTO);
	      });
	  }
}
