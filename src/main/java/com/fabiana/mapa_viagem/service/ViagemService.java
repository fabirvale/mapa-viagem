package com.fabiana.mapa_viagem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabiana.mapa_viagem.dto.FecharViagemDTO;
import com.fabiana.mapa_viagem.dto.FecharViagemResponseDTO;
import com.fabiana.mapa_viagem.dto.LinhaResumoDTO;
import com.fabiana.mapa_viagem.dto.ResumoMotoristaDTO;
import com.fabiana.mapa_viagem.dto.ViagemDTO;
import com.fabiana.mapa_viagem.enums.StatusViagem;
import com.fabiana.mapa_viagem.exception.AgendamentosVinculadosException;
import com.fabiana.mapa_viagem.exception.RecursoNaoEncontradoException;
import com.fabiana.mapa_viagem.exception.RegraNegocioException;
import com.fabiana.mapa_viagem.model.Agendamento;
import com.fabiana.mapa_viagem.model.Motorista;
import com.fabiana.mapa_viagem.model.OcorrenciaDuranteViagem;
import com.fabiana.mapa_viagem.model.PagamentoDiaria;
import com.fabiana.mapa_viagem.model.Veiculo;
import com.fabiana.mapa_viagem.model.Viagem;
import com.fabiana.mapa_viagem.repository.AgendamentoRepository;
import com.fabiana.mapa_viagem.repository.MotoristaRepository;
import com.fabiana.mapa_viagem.repository.VeiculoRepository;
import com.fabiana.mapa_viagem.repository.ViagemRepository;

//registrado como componente
@Service
public class ViagemService {

	@Autowired
	private ViagemRepository viagemRepository;

	@Autowired
	private AgendamentoRepository agendamentoRepository;

	@Autowired
	private MotoristaRepository motoristaRepository;

	@Autowired
	private VeiculoRepository veiculoRepository;

	public List<ViagemDTO> findAll() {
		List<Viagem> list = viagemRepository.findAll();
		List<ViagemDTO> listDto = new ArrayList<>();

		for (Viagem v : list) {
			ViagemDTO dto = new ViagemDTO(v);
			listDto.add(dto);
		}
		return listDto;
	}

	public ViagemDTO findById(Long id) {
		Viagem entity = buscarViagemOuFalhar(id);
		return new ViagemDTO(entity);
	}

	private Viagem buscarViagemOuFalhar(Long id) {
		return viagemRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));
	}

	public ViagemDTO insert(ViagemDTO dto) {

		boolean viagemExistente = viagemRepository.existsByCidadeOrigemAndCidadeDestinoAndDataViagemAndHoraPrevista(
				dto.getCidadeOrigem(), dto.getCidadeDestino(), dto.getDataViagem(), dto.getHoraPrevista(),
				dto.getStatus());

		if (viagemExistente && !dto.isIgnorarDuplicidade()) {
			throw new RegraNegocioException("Já existe uma viagem com esses dados.");
		}

		Viagem entity = fromDTO(dto);
		entity.setStatus(StatusViagem.AGENDADA);
		entity = viagemRepository.save(entity);
		return new ViagemDTO(entity);

	}

	public void delete(Long id) {
		Viagem viagem = buscarViagemOuFalhar(id);

		if (viagem.getStatus() != StatusViagem.AGENDADA) {
			throw new RegraNegocioException("Somente viagens agendadas podem ser excluídas.");
		}

		boolean existeAgendamento = agendamentoRepository.existsByViagemId(id);
		if (existeAgendamento) {
			throw new RegraNegocioException("Não é possível excluir a viagem pois existem agendamentos vinculados.");
		}
		viagemRepository.delete(viagem);
	}

	// Update
	@Transactional
	public void update(Long id, ViagemDTO dto) {

		Viagem viagem = buscarViagemOuFalhar(id);

		if (viagem.getStatus() != StatusViagem.AGENDADA) {
			throw new RegraNegocioException("Somente viagens com status AGENDADA podem ser editadas.");
		}

		if (dto.getObservacao() != null) {
			viagem.setObservacao(dto.getObservacao());
		}

		if (dto.getStatus() != null && dto.getStatus() != viagem.getStatus()) {

			validarAlteracaoStatus(viagem, dto.getStatus());

			viagem.setStatus(dto.getStatus());
		}

		if (dto.getCidadeOrigem() != null) {
			viagem.setCidadeOrigem(dto.getCidadeOrigem());
		}

		if (dto.getCidadeDestino() != null) {
			viagem.setCidadeDestino(dto.getCidadeDestino());
		}

		if (dto.getHoraPrevista() != null) {

			if (!dto.getHoraPrevista().equals(viagem.getHoraPrevista())) {

				validarHoraPrevistaComAgendamentos(viagem, dto.getHoraPrevista());

			}

			viagem.setHoraPrevista(dto.getHoraPrevista());
		}

		if (dto.getMotoristaId() != null) {
			Motorista motorista = motoristaRepository.findById(dto.getMotoristaId())
					.orElseThrow(() -> new RegraNegocioException("Motorista não encontrado"));

			viagem.setMotorista(motorista);
		} else {
			viagem.setMotorista(null);
		}

		if (dto.getVeiculoId() != null) {
			Veiculo veiculo = veiculoRepository.findById(dto.getVeiculoId())
					.orElseThrow(() -> new RegraNegocioException("Veículo não encontrado"));

			viagem.setVeiculo(veiculo);
		} else {
			viagem.setVeiculo(null);
		}

		if (dto.getDataViagem() != null) {

			if (!dto.getDataViagem().equals(viagem.getDataViagem())) {

				processarAlteracaoDataViagem(id, viagem, dto.getDataViagem(),
						dto.isConfirmarAlteracaoDataAgendamentos());

			} else {
				viagem.atualizarDataViagem(dto.getDataViagem());
			}
		}

	}

	// cancelar viagem
	public void cancelarViagem(Long id, ViagemDTO dto) {
		Viagem viagem = buscarViagemOuFalhar(id);

		if (viagem.getStatus() != StatusViagem.AGENDADA) {
			throw new RegraNegocioException("Somente viagens agendadas podem ser canceladas.");
		}
		viagem.setStatus(StatusViagem.CANCELADA);
		viagem.setObservacao(dto.getObservacao());
		viagemRepository.save(viagem);
	}

	// Validar alteração do campos status
	private void validarAlteracaoStatus(Viagem viagem, StatusViagem novoStatus) {

		if (viagem.getStatus() == StatusViagem.AGENDADA && novoStatus == StatusViagem.EM_ANDAMENTO) {

			boolean existeAgendamento = agendamentoRepository.existsByViagemId(viagem.getId());

			if (!existeAgendamento) {
				throw new RegraNegocioException("Não é possível iniciar a viagem sem agendamento.");
			}

			if (viagem.getMotorista() == null) {
				throw new RegraNegocioException("Não é possível iniciar a viagem sem motorista.");
			}

			if (viagem.getVeiculo() == null) {
				throw new RegraNegocioException("Não é possível iniciar a viagem sem veículo.");
			}
		}
	}

	// Validar alteração da hora prevista da viagem com agendamentos vinculados
	private void validarHoraPrevistaComAgendamentos(Viagem viagem, LocalTime novaHora) {

		List<Agendamento> agendamentos = agendamentoRepository.findByViagemId(viagem.getId());

		for (Agendamento agendamento : agendamentos) {

			if (!novaHora.isBefore(agendamento.getHorarioAtendimento())) {

				throw new RegraNegocioException("Não é possível alterar a hora da viagem. "
						+ "A hora prevista deve ser anterior ao horário dos agendamentos vinculados.");
			}
		}
	}

	// Validar alteração da data da viagem com agendamentos
	private void processarAlteracaoDataViagem(Long viagemId, Viagem viagem, LocalDate novaData,
			boolean confirmarAlteracaoDataAgendamentos) {

		List<Agendamento> agendamentos = agendamentoRepository.findByViagemId(viagemId);

		if (!agendamentos.isEmpty() && !confirmarAlteracaoDataAgendamentos) {
			throw new AgendamentosVinculadosException(agendamentos.size());
		}

		viagem.atualizarDataViagem(novaData);

		if (!agendamentos.isEmpty()) {
			for (Agendamento agendamento : agendamentos) {
				agendamento.setDataAtendimento(novaData);
			}

			agendamentoRepository.saveAll(agendamentos);
		}
	}

	private Viagem fromDTO(ViagemDTO objDto) {

		Motorista motorista = null;
		Veiculo veiculo = null;

		if (objDto.getMotoristaId() != null) {
			motorista = motoristaRepository.findById(objDto.getMotoristaId())
					.orElseThrow(() -> new RegraNegocioException("Motorista não encontrado."));
		}

		if (objDto.getVeiculoId() != null) {
			veiculo = veiculoRepository.findById(objDto.getVeiculoId())
					.orElseThrow(() -> new RegraNegocioException("Veículo não encontrado."));
		}
		return new Viagem(objDto.getDataViagem(), objDto.getObservacao(), objDto.getCidadeOrigem(),
				objDto.getCidadeDestino(), objDto.getEstadoOrigem(), objDto.getEstadoDestino(),
				objDto.getHoraPrevista(), motorista, veiculo, objDto.getStatus());
	}

	// Alterar o status da viagem
	public void iniciarViagem(Long id) {
		Viagem viagem = buscarViagemOuFalhar(id);
		if (viagem.getStatus() != StatusViagem.AGENDADA) {
			throw new RegraNegocioException("Somente viagens agendadas podem ser iniciadas.");
		}

		if (viagem.getDataViagem().isAfter(LocalDate.now())) {
			throw new RegraNegocioException("Não é possível iniciar uma viagem com data futura.");
		}

		validarAlteracaoStatus(viagem, StatusViagem.EM_ANDAMENTO);

		// verifica se existe agendamento, motorista, veiculo para ser iniciada
		viagem.setStatus(StatusViagem.EM_ANDAMENTO);

		viagemRepository.save(viagem);
	}

	// Filtros dashboard
	public List<ViagemDTO> buscarComFiltros(String busca, LocalDate dataInicial, LocalDate dataFinal,
			StatusViagem status, Long motoristaId) {

		// normaliza string vazia para null, evitando filtro "fantasma"
		String buscaTratada = (busca != null && busca.isBlank()) ? null : busca;

		List<Viagem> viagens = viagemRepository.buscarComFiltros(buscaTratada, dataInicial, dataFinal, status,
				motoristaId);

		return viagens.stream().map(ViagemDTO::new).toList();
	}
	
	// Fechar Viagem
	@Transactional
	public FecharViagemResponseDTO fecharViagem(Long viagemId, ViagemDTO dto, FecharViagemDTO fecharViagemDto) {

		if (dto == null || fecharViagemDto == null) {
			throw new RegraNegocioException("Dados de viagem e de pagamento são obrigatórios");
		}

		Viagem viagem = viagemRepository.findById(viagemId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));

		if (viagem.getStatus() == StatusViagem.CANCELADA) {
			throw new RegraNegocioException("Não é possível finalizar uma viagem cancelada");
		}

		if (viagem.getMotorista() == null) {
			throw new RegraNegocioException("Atribua um motorista à viagem antes de finalizá-la");
		}

		if (dto.getDataRetorno() == null || dto.getHoraChegada() == null || dto.getOdometroInicial() == null
				|| dto.getOdometroFinal() == null) {
			throw new RegraNegocioException("Data de retorno, hora de chegada e odômetros são obrigatórios");
		}

		LocalDateTime inicio = LocalDateTime.of(viagem.getDataViagem(), viagem.getHoraPrevista());
		LocalDateTime chegada = LocalDateTime.of(dto.getDataRetorno(), dto.getHoraChegada());
		LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));   

		if (inicio.isAfter(agora)) {                                               
		    throw new RegraNegocioException("A viagem ainda não ocorreu. Finalize depois do horário de saída");
		}

		if (chegada.isBefore(inicio)) {
		    throw new RegraNegocioException("A chegada não pode ser antes do início da viagem");
		}

		if (chegada.isAfter(agora)) {                                              
		    throw new RegraNegocioException("A chegada não pode estar no futuro. Finalize a viagem depois que ela ocorrer");
		}
			

		if (dto.getOdometroFinal() <= dto.getOdometroInicial()) {
			throw new RegraNegocioException("Km final deve ser maior que o km inicial");
		}

		// saída e retorno da diária: usa o enviado; se não vier, deriva da viagem
		LocalDateTime saida = fecharViagemDto.getDataHoraSaida() != null
				? fecharViagemDto.getDataHoraSaida() : inicio;

		LocalDateTime retorno = fecharViagemDto.getDataHoraRetorno() != null
				? fecharViagemDto.getDataHoraRetorno() : chegada;

		if (!retorno.isAfter(saida)) {
			throw new RegraNegocioException("O retorno deve ser depois da saída");
		}

		// almoço: opcional (viagens curtas não têm), mas se informado deve ser consistente
		LocalDateTime almocoInicio = fecharViagemDto.getDataHoraAlmocoInicio();
		LocalDateTime almocoFim = fecharViagemDto.getDataHoraAlmocoFim();

		if ((almocoInicio == null) != (almocoFim == null)) {
		    throw new RegraNegocioException("Informe o início e o fim do almoço, ou deixe os dois em branco");
		}

		if (almocoInicio != null) {
		    if (!almocoFim.isAfter(almocoInicio)) {
		        throw new RegraNegocioException("O fim do almoço deve ser depois do início");
		    }
		    if (almocoInicio.isBefore(saida) || almocoFim.isAfter(retorno)) {
		        throw new RegraNegocioException("O almoço deve estar dentro do período da viagem");
		    }
		}
		
		// atualização da viagem
		viagem.setDataRetorno(dto.getDataRetorno());
		viagem.setHoraChegada(dto.getHoraChegada());
		viagem.setOdometroInicial(dto.getOdometroInicial());
		viagem.setOdometroFinal(dto.getOdometroFinal());
		viagem.setStatus(StatusViagem.FINALIZADA);

		// reaproveita o pagamento existente (modo editar fechamento)
		PagamentoDiaria pagamento = viagem.getPagamentoDiaria() != null ? viagem.getPagamentoDiaria()
				: new PagamentoDiaria();

		pagamento.setDataHoraSaida(saida);
		pagamento.setDataHoraAlmocoInicio(almocoInicio);
		pagamento.setDataHoraAlmocoFim(almocoFim);
		pagamento.setLocalAlmoco(fecharViagemDto.getLocalAlmoco());
		pagamento.setDataHoraRetorno(retorno);

		int kmPercorridos = dto.getOdometroFinal() - dto.getOdometroInicial();
		pagamento.definirTipoDiaria(kmPercorridos);

		if (pagamento.getTipoDiaria() == null) {
			throw new RegraNegocioException("Não foi possível definir o tipo de diária para o período informado");
		}

		pagamento.setValorDiaria(pagamento.getTipoDiaria().getValor());
		viagem.setPagamentoDiaria(pagamento);

		viagemRepository.save(viagem);

		return new FecharViagemResponseDTO(viagem.getId(), viagem.getMotorista().getNome(), viagem.getDataRetorno(),
				viagem.getHoraChegada(), kmPercorridos, pagamento.getTipoDiaria().name(), pagamento.getValorDiaria(),
				calcularTotalOcorrencias(viagem));
	}

	// Checar se a viagem foi finalizada
	public boolean viagemFinalizada(Viagem viagem) {
		return viagem.getDataRetorno() != null && viagem.getHoraChegada() != null && viagem.getOdometroFinal() != null
				&& viagem.getPagamentoDiaria() != null;
	}

	// Calcular o total da ocorrencia
	private BigDecimal calcularTotalOcorrencias(Viagem viagem) {

		BigDecimal total = BigDecimal.ZERO;

		List<OcorrenciaDuranteViagem> ocorrencias = viagem.getOcorrencias();

		if (ocorrencias == null || ocorrencias.isEmpty()) {
			return BigDecimal.ZERO;
		}

		for (OcorrenciaDuranteViagem o : ocorrencias) {
			total = total.add(o.calcularValor());
		}

		return total;
	}
	
	//Report com total da diária do motorista
	@Transactional(readOnly = true)
	public ResumoMotoristaDTO resumoMotorista(Long motoristaId, LocalDate inicio, LocalDate fim) {

	    if (inicio == null || fim == null || fim.isBefore(inicio)) {
	        throw new RegraNegocioException("Período inválido");
	    }
	    
	  
	    Motorista motorista = motoristaRepository.findById(motoristaId)
	            .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado"));

	    List<Viagem> viagens = viagemRepository
	            .buscarFinalizadasDoMotorista(motoristaId, StatusViagem.FINALIZADA, inicio, fim);

	    List<LinhaResumoDTO> linhas = viagens.stream()
	            .filter(v -> v.getPagamentoDiaria() != null)
	            .map(v -> {
	                PagamentoDiaria p = v.getPagamentoDiaria();
	                Integer km = (v.getOdometroInicial() != null && v.getOdometroFinal() != null)
	                        ? v.getOdometroFinal() - v.getOdometroInicial()
	                        : null;
	                return new LinhaResumoDTO(
	                        v.getId(),
	                        v.getDataViagem(),
	                        v.getCidadeOrigem() + " → " + v.getCidadeDestino(),
	                        p.getDataHoraSaida(),
	                        p.getDataHoraRetorno(),
	                        km,
	                        p.getTipoDiaria().name(),
	                        p.getValorDiaria());
	            }).toList();


	    BigDecimal totalAReceber = linhas.stream().map(LinhaResumoDTO::getValorDiaria).reduce(BigDecimal.ZERO, BigDecimal::add);

	    long pendentes = viagemRepository.countByMotoristaIdAndStatusNotInAndDataViagemBetween(
	            motoristaId,
	            List.of(StatusViagem.FINALIZADA, StatusViagem.CANCELADA),
	            inicio, fim);

	    return new ResumoMotoristaDTO(motorista.getNome(), inicio, fim, linhas, pendentes, totalAReceber);
	}
	
	@Transactional
	public void reabrirViagem(Long viagemId) {
	    Viagem viagem = viagemRepository.findById(viagemId)
	            .orElseThrow(() -> new RecursoNaoEncontradoException("Viagem não encontrada"));

	    if (viagem.getStatus() != StatusViagem.FINALIZADA) {
	        throw new RegraNegocioException("Só é possível reabrir uma viagem finalizada");
	    }

	    viagem.setStatus(StatusViagem.EM_ANDAMENTO);
	    viagemRepository.save(viagem);
	}


}
