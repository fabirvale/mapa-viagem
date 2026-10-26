var modalFinalizarViagemOverlay = document.getElementById('modalFinalizarViagemOverlay');
var formFinalizarViagem = document.getElementById('formFinalizarViagem');
var viagemFechamento = null;            // viagem que está sendo finalizada
var retornoEditadoManualmente = false;  // controla o preenchimento automático do retorno da diária

async function abrirModalFinalizarViagem(id) {

    // fecha o menu de ações para não ficar sobre o modal
    document.querySelectorAll(".menu-acoes").forEach(m => m.style.display = "none");

    let v;
    try {
        const res = await fetch(API + '/viagens/' + id);
        if (!res.ok) throw new Error('Erro ' + res.status);
        v = await res.json();
    } catch (err) {
        console.error('Erro ao buscar viagem:', err);
        Swal.fire({ icon: "error", title: "Erro", text: "Erro ao carregar os dados da viagem." });
        return;
    }

    if (v.status === "CANCELADA") {
        Swal.fire({
            icon: "warning",
            title: "Operação não permitida.",
            text: "Não é possível finalizar uma viagem cancelada."
        });
        return;
    }

    if (!v.motoristaId) {
        Swal.fire({
            icon: "warning",
            title: "Motorista não atribuído",
            text: "Atribua um motorista à viagem antes de finalizá-la."
        });
        return;
    }
    
    viagemFechamento = v;

    // não deixa finalizar viagem cuja saída ainda não aconteceu (edição de fechamento continua liberada)
    if (!v.pagamentoDiaria) {
        var inicioPrevisto = new Date(v.dataViagem + 'T' + v.horaPrevista.substring(0, 5));
        if (inicioPrevisto > new Date()) {
            Swal.fire({
                icon: 'warning',
                title: 'Viagem ainda não ocorreu',
                text: 'Só é possível finalizar a viagem depois do horário de saída (' +
                    formatarData(v.dataViagem) + ' ' + v.horaPrevista.substring(0, 5) + ').'
            });
            return;
        }
    }

    // contexto da viagem
    document.getElementById('fechamentoRota').textContent = (v.cidadeOrigem || '-') + ' → ' + (v.cidadeDestino || '-');
    document.getElementById('fechamentoData').textContent = formatarData(v.dataViagem);
    document.getElementById('fechamentoMotorista').textContent = v.motoristaNome || '-';
    document.getElementById('fechamentoVeiculo').textContent = v.veiculoModelo || '-';

    // reset: sempre abre na tela do formulário
    formFinalizarViagem.reset();
    formFinalizarViagem.style.display = 'block';
    document.getElementById('resumoFechamento').style.display = 'none';

    var jaFechada = !!v.pagamentoDiaria;
    var finalizada = v.status === 'FINALIZADA'; // decide os textos

    if (jaFechada) {
        // modo EDITAR: preenche com o que já está gravado
        document.getElementById('tituloModalFinalizar').textContent  = finalizada ? 'Editar Fechamento' : 'Finalizar Viagem';
        document.getElementById('btnConfirmarFinalizar').textContent = finalizada ? 'Salvar fechamento' : 'Calcular e finalizar';

        document.getElementById('fechamentoDataRetorno').value = v.dataRetorno || '';
        document.getElementById('fechamentoHoraChegada').value = v.horaChegada || '';
        document.getElementById('fechamentoKmInicial').value = v.odometroInicial ?? '';
        document.getElementById('fechamentoKmFinal').value = v.odometroFinal ?? '';

        var p = v.pagamentoDiaria;
        document.getElementById('fechamentoInicial').value = p.dataHoraAlmocoInicio ? p.dataHoraAlmocoInicio.substring(0, 16) : '';
        document.getElementById('fechamentoFinal').value = p.dataHoraAlmocoFim ? p.dataHoraAlmocoFim.substring(0, 16) : '';
        document.getElementById('fechamentoLocal').value = p.localAlmoco || '';
       

        retornoEditadoManualmente = true;   // não sobrescreve o que já está gravado
    } else {
        // modo CRIAR: campos vazios, com sugestões editáveis
        document.getElementById('tituloModalFinalizar').textContent = 'Finalizar Viagem';
        document.getElementById('btnConfirmarFinalizar').textContent = 'Calcular e finalizar';
        document.getElementById('fechamentoDataRetorno').value = v.dataViagem || '';
        
        retornoEditadoManualmente = false;
    }

    atualizarKmPercorridos();
    modalFinalizarViagemOverlay.style.display = 'flex';
}

formFinalizarViagem.addEventListener('submit', function (event) {
    event.preventDefault();   // evita o reload da página
    salvarFechamento();
});

async function salvarFechamento() {

    var dataRetorno   = document.getElementById('fechamentoDataRetorno').value;
    var horaChegada   = document.getElementById('fechamentoHoraChegada').value;
    var kmInicial     = parseInt(document.getElementById('fechamentoKmInicial').value);
    var kmFinal       = parseInt(document.getElementById('fechamentoKmFinal').value);
    var inicioAlmoco  = document.getElementById('fechamentoInicial').value;
    var fimAlmoco     = document.getElementById('fechamentoFinal').value;
    var localAlmoco   = document.getElementById('fechamentoLocal').value;
    
    // ---- validações no front (o backend repete todas) ----
    var inicioViagem = viagemFechamento.dataViagem + 'T' + viagemFechamento.horaPrevista.substring(0, 5);
    var chegada = dataRetorno + 'T' + horaChegada;
    var agora         = new Date();
    var chegadaDate   = new Date(chegada);   // "2026-10-22T17:00"
    
    if (chegadaDate > agora) {
        avisoFechamento('A chegada não pode estar no futuro. Finalize a viagem depois que ela ocorrer.');
        return;
    }

    if (chegada < inicioViagem) {
        avisoFechamento('A chegada não pode ser antes do início da viagem.');
        return;
    }
    if (kmFinal <= kmInicial) {
        avisoFechamento('O KM final deve ser maior que o KM inicial.');
        return;
    }
    // almoço é opcional, mas se informado precisa estar completo e em ordem
    if (!!inicioAlmoco !== !!fimAlmoco) {
        avisoFechamento('Informe o início e o fim do almoço, ou deixe os dois em branco.');
        return;
    }
    if (inicioAlmoco && fimAlmoco <= inicioAlmoco) {
        avisoFechamento('O retorno do almoço deve ser depois da saída.');
        return;
    }

    // ---- JSON enxuto: só o que o fechamento usa ----
    var payload = {
        viagem: {
            dataRetorno: dataRetorno,
            horaChegada: horaChegada,
            odometroInicial: kmInicial,
            odometroFinal: kmFinal
        },
        pagamento: {
            dataHoraAlmocoInicio: inicioAlmoco || null,
            dataHoraAlmocoFim: fimAlmoco || null,
            localAlmoco: localAlmoco || null
        }
    };

    var btn = document.getElementById('btnConfirmarFinalizar');
    btn.disabled = true;   // evita clique duplo

    try {
        const res = await fetch(API + '/viagens/' + viagemFechamento.id + '/fechar', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            var mensagem = 'Erro ao finalizar a viagem.';
            try {
                const err = await res.json();
                mensagem = err.message || mensagem;
            } catch (e) {}
            throw new Error(mensagem);
        }

        const r = await res.json();

        mostrarResumoFechamento(r, inicioViagem, chegada, inicioAlmoco, fimAlmoco);

        // atualiza a tela mantendo os filtros ativos (fluxo de fim de mês)
        buscarViagens();
        carregarIndicadores();

    } catch (err) {
        console.error('Erro ao finalizar viagem:', err);
        Swal.fire({ icon: 'error', title: 'Erro', text: err.message || 'Erro ao finalizar a viagem.' });
    } finally {
        btn.disabled = false;
    }
}

function avisoFechamento(texto) {
    Swal.fire({ icon: 'warning', title: 'Atenção', text: texto });
}

// ========== TELA 2: resumo do cálculo ==========
function mostrarResumoFechamento(r, inicioViagem, chegada, horaAlmocoInicio, horaAlmocoFim) {

    document.getElementById('tituloModalFinalizar').textContent = 'Viagem finalizada';

    document.getElementById('resumoMotorista').textContent = r.motoristaNome || '-';
    document.getElementById('resumoPeriodo').textContent =  
        formatarDataHoraCurta(inicioViagem) + ' → ' + formatarDataHoraCurta(chegada);
    document.getElementById('resumoHoraAlmoco').textContent =
        (horaAlmocoInicio && horaAlmocoFim)
        ? formatarDataHoraCurta(horaAlmocoInicio) + '  →  ' + formatarDataHoraCurta(horaAlmocoFim)
        : 'Sem almoço';
    document.getElementById('resumoKm').textContent = r.kmPercorridos + ' km';
    document.getElementById('resumoTipoDiaria').textContent = rotuloTipoDiaria(r.tipoDiaria);
    document.getElementById('resumoValorDiaria').textContent = formatarMoeda(r.valorDiaria);
    document.getElementById('resumoOcorrencias').textContent = formatarMoeda(r.totalOcorrencias);

    formFinalizarViagem.style.display = 'none';
    document.getElementById('resumoOcorrencias').textContent =
    r.totalOcorrencias > 0 ? formatarMoeda(r.totalOcorrencias) : 'Nenhuma ocorrência';
    document.getElementById('resumoFechamento').style.display = 'block';
}


function fecharModalFinalizarViagem() {
    modalFinalizarViagemOverlay.style.display = 'none';
}

// ========== KM percorridos, calculado ao vivo ==========
function atualizarKmPercorridos() {
    var ini = parseInt(document.getElementById('fechamentoKmInicial').value);
    var fim = parseInt(document.getElementById('fechamentoKmFinal').value);
    var campo = document.getElementById('fechamentoKmPercorridos');

    if (isNaN(ini) || isNaN(fim)) {
        campo.value = '';
        campo.style.color = '';
    } else if (fim <= ini) {
        campo.value = 'KM final inválido';
        campo.style.color = '#dc2626';
    } else {
        campo.value = (fim - ini) + ' km';
        campo.style.color = '';
    }
}

// ========== Retorno da diária acompanha a chegada (até o operador editar) ==========
function sincronizarRetornoDiaria() {
    if (retornoEditadoManualmente) return;

    var data = document.getElementById('fechamentoDataRetorno').value;
    var hora = document.getElementById('fechamentoHoraChegada').value;

   /* if (data && hora) {
        document.getElementById('fechamentoRetorno').value = data + 'T' + hora.substring(0, 5);
    }*/
}

document.addEventListener('DOMContentLoaded', function () {

    document.getElementById('fechamentoKmInicial').addEventListener('input', atualizarKmPercorridos);
    document.getElementById('fechamentoKmFinal').addEventListener('input', atualizarKmPercorridos);

    document.getElementById('fechamentoDataRetorno').addEventListener('change', sincronizarRetornoDiaria);
    document.getElementById('fechamentoHoraChegada').addEventListener('change', sincronizarRetornoDiaria);
  
});