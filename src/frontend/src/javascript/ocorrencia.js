var ocorrenciasCarregadas = []; // cache da lista completa da viagem atual
var ocorrenciaEditandoId = null;
const formOcorrencia = document.getElementById('formOcorrencia');

formOcorrencia.addEventListener('submit', function(event) {
  event.preventDefault(); // evita o reload da página
  salvarOcorrencia();    // função que fará o POST
});

async function carregarOcorrenciasDaViagem() {
  
    if (!viagemSelecionada) return;

    try {
        const res = await fetch(API + '/ocorrencias/viagens/' + viagemSelecionada.id);
        const data = await res.json();
        ocorrenciasCarregadas = Array.isArray(data) ? data : [];
        atualizarResumoOcorrencias(ocorrenciasCarregadas);
    } catch (err) {
        console.error('Erro ao carregar ocorrências:', err);
    }
}

function atualizarResumoOcorrencias(ocorrencias) {
    var total = ocorrencias.reduce(function(soma, o) {
        return soma + (o.valor || 0);
    }, 0);

    document.getElementById('ocorrenciasCount').textContent = ocorrencias.length;
    document.getElementById('ocorrenciasTotalValor').textContent =
        total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    var link = document.getElementById('linkVerTodasOcorrencias');
    link.style.display = ocorrencias.length > 0 ? 'block' : 'none';    
}

// ========== MODAL - LISTA COMPLETA ==========
function abrirModalListaOcorrencias() {

    const modal = document.getElementById('modalListaOcorrenciasOverlay');

    if (!modal) {
        console.error('Modal de lista de ocorrências não encontrado.');
        return;
    }

    document.getElementById('listaOcorrenciasSubtitulo').textContent =
        (viagemSelecionada.cidadeOrigem || '-') + ' → ' + (viagemSelecionada.cidadeDestino || '-') +
        ' · ' + formatarData(viagemSelecionada.dataViagem) +
        ' · ' + (viagemSelecionada.horaPrevista ? viagemSelecionada.horaPrevista.substring(0, 5) : '-');

    modal.style.display = 'flex';
    renderizarTabelaOcorrencias(ocorrenciasCarregadas);
}

function fecharModalListaOcorrencias() {
    document.getElementById('modalListaOcorrenciasOverlay').style.display = 'none';
}

function renderizarTabelaOcorrencias(ocorrencias) {
    var corpo = document.getElementById('corpoTabelaOcorrencias');
    var totalSpan = document.getElementById('totalOcorrenciasLista');

    if (!Array.isArray(ocorrencias) || ocorrencias.length === 0) {
        corpo.innerHTML = '<tr><td colspan="5" class="vazio">Nenhuma ocorrência encontrada.</td></tr>';
        totalSpan.textContent = 'Total: R$ 0,00';
        return;
    }

    corpo.innerHTML = '';
    var total = 0;

    ocorrencias.forEach(function(o) {

        var tipoLabel = o.unidade !== undefined ? 'Despesa' : 'Multa';
        total += (o.valor || 0);

        corpo.innerHTML +=
            '<tr>' +
                '<td>' + tipoLabel + '</td>' +
                '<td>' + (o.descricao || '-') + '</td>' +
                '<td>' + formatarData(o.data) + '</td>' +
                '<td>' + (o.valor || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }) + '</td>' +
                '<td class="acoes">' +
                    '<button class="btn-acoes" onclick="toggleMenu(this)">' +
                        '<i class="fa-solid fa-ellipsis-vertical"></i>' +
                    '</button>' +
                    '<div class="menu-acoes">' +
                        '<button onclick="editarOcorrencia(' + o.id + ')"><i class="fa-solid fa-pen"></i> Editar</button>' +
                        '<button onclick="excluirOcorrencia(' + o.id + ')"><i class="fa-solid fa-trash"></i> Excluir</button>' +
                    '</div>' +
                '</td>' +
            '</tr>';
    });

    totalSpan.textContent = 'Total: ' + total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function abrirModalOcorrencia() {

    if (!viagemSelecionada) {
        Swal.fire({
            icon: "warning",
            title: "Nenhuma viagem selecionada",
            text: "Selecione uma viagem primeiro."
        });
        return;
    }

     if (viagemSelecionada.status == "AGENDADA" || viagemSelecionada.status == "CANCELADA") {

        Swal.fire({
            icon: "warning",
            title: "Operação não permitida.",
            text: "Não é possível criar ocorrencias para uma viagem que está agendada ou cancelada."
        });

        return;
    }

    // reset do formulário para o modo "criar nova"
    ocorrenciaEditandoId = null;
    formOcorrencia.reset();

    // volta o radio para Despesa como padrão, e mostra os campos certos
    document.querySelector('input[name="tipoOcorrencia"][value="DESPESA"]').checked = true;
    document.getElementById('camposDespesa').style.display = 'block';
    document.getElementById('camposMulta').style.display = 'none';

    document.getElementById('tituloModalOcorrencia').textContent = 'Nova Ocorrência';
  
    const hoje = new Date();
    const ano = hoje.getFullYear();
    const mes = String(hoje.getMonth() + 1).padStart(2, "0");
    const dia = String(hoje.getDate()).padStart(2, "0");
    document.getElementById("dataOcorrencia").value = `${ano}-${mes}-${dia}`;

    document.getElementById('modalOcorrenciaOverlay').style.display = 'flex';
}

function fecharModalOcorrencia() {
    document.getElementById('modalOcorrenciaOverlay').style.display = 'none';
}

//============Função Salvar Ocorrência=============

async function salvarOcorrencia() {

    if (!viagemSelecionada) {
        Swal.fire({
            icon: "error",
            title: "Erro",
            text: "Nenhuma viagem selecionada."
        });
        return;
    }

    if (viagemSelecionada.status == "AGENDADA" || viagemSelecionada.status == "CANCELADA") {

        Swal.fire({
            icon: "warning",
            title: "Operação não permitida.",
            text: "Não é possível criar ocorrencias para uma viagem que está agendada ou cancelada."
        });

        return;
    }

    var tipo = document.querySelector('input[name="tipoOcorrencia"]:checked').value;

    var dadosBase = {
        id: ocorrenciaEditandoId,
        viagemId: viagemSelecionada.id,
        descricao: document.getElementById('txtDescricaoOcorrencia').value,
        data: document.getElementById('dataOcorrencia').value,
        valor: parseFloat(document.getElementById('valorOcorrencia').value)
    };

    var dados;

    if (tipo === 'DESPESA') {
        dados = Object.assign({}, dadosBase, {
            unidade: document.getElementById('selectUnidadeDespesa').value,
            quantidade: parseInt(document.getElementById('quantidadeDespesa').value) || null,
            documentoFiscal: document.getElementById('documentoFiscalDespesa').value,
            km: parseInt(document.getElementById('kmDespesa').value) || 0
        });
    } else {
        dados = Object.assign({}, dadosBase, {
            autoInfracao: document.getElementById('autoInfracaoMulta').value,
            horario: document.getElementById('horarioMulta').value,
            municipio: document.getElementById('municipioMulta').value,
            descricaoMulta: document.getElementById('descricaoMultaTexto').value
        });
    }

    console.log('Dados a enviar:', dados);

    const url = ocorrenciaEditandoId ? API + '/ocorrencias/' + ocorrenciaEditandoId : API + '/ocorrencias';
    const metodo = ocorrenciaEditandoId ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method: metodo,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });

        if (!res.ok) {
            let errorMessage = 'Erro ao salvar ocorrência';
            try {
                const err = await res.json();
                errorMessage = err.message || errorMessage;
            } catch {}
            throw new Error(errorMessage);
        }

        const foiEdicao = !!ocorrenciaEditandoId;

        formOcorrencia.reset();
        document.getElementById('modalOcorrenciaOverlay').style.display = 'none';

        carregarOcorrenciasDaViagem();

        Swal.fire({
            icon: "success",
            title: "Sucesso",
            text: foiEdicao ? "Ocorrência atualizada com sucesso!" : "Ocorrência criada com sucesso!"
        });

    } catch (err) {
        console.error('Erro ao salvar ocorrência:', err);
        Swal.fire({
            icon: "error",
            title: "Erro",
            text: err.message || "Erro ao salvar ocorrência."
        });
    }
}

//============Função Editar Ocorrência=============
async function editarOcorrencia(id) {

    if (viagemSelecionada.status == "AGENDADA" || viagemSelecionada.status == "CANCELADA") {

        Swal.fire({
            icon: "warning",
            title: "Operação não permitida.",
            text: "Não é possível editar ocorrencias para uma viagem que está agendada ou cancelada."
        });

        return;
    }

    ocorrenciaEditandoId = id;

    try {
        const res = await fetch(API + '/ocorrencias/' + id);
        const o = await res.json();

        // campos comuns
        document.getElementById('txtDescricaoOcorrencia').value = o.descricao || '';
        document.getElementById('dataOcorrencia').value = o.data || '';
        document.getElementById('valorOcorrencia').value = o.valor || '';

        // detecta o tipo e marca o radio certo
        var ehDespesa = o.unidade !== undefined;

        document.querySelector('input[name="tipoOcorrencia"][value="' + (ehDespesa ? 'DESPESA' : 'MULTA') + '"]').checked = true;

        if (ehDespesa) {
            document.getElementById('camposDespesa').style.display = 'block';
            document.getElementById('camposMulta').style.display = 'none';

            document.getElementById('selectUnidadeDespesa').value = o.unidade || '';
            document.getElementById('quantidadeDespesa').value = o.quantidade || '';
            document.getElementById('documentoFiscalDespesa').value = o.documentoFiscal || '';
            document.getElementById('kmDespesa').value = o.km || '';
        } else {
            document.getElementById('camposDespesa').style.display = 'none';
            document.getElementById('camposMulta').style.display = 'block';

            document.getElementById('autoInfracaoMulta').value = o.autoInfracao || '';
            document.getElementById('horarioMulta').value = o.horario || '';
            document.getElementById('municipioMulta').value = o.municipio || '';
            document.getElementById('descricaoMultaTexto').value = o.descricaoMulta || '';
        }

        document.getElementById('tituloModalOcorrencia').textContent = 'Editar Ocorrência';
        document.getElementById('modalOcorrenciaOverlay').style.display = 'flex';

    } catch (err) {
        console.error('Erro ao carregar ocorrência:', err);
        Swal.fire({
            icon: "error",
            title: "Erro",
            text: "Erro ao carregar dados da ocorrência."
        });
    }
}

//============Função Excluir Ocorrência=============
async function excluirOcorrencia(id) {

     if (viagemSelecionada.status !== "EM_ANDAMENTO") {

        Swal.fire({
            icon: "warning",
            title: "Operação não permitida.",
            text: "Não é possível excluir uma ocorrência neste momento. A exclusão só é permitida enquanto a viagem está em andamento."
        });

        return;
    }


    const confirmacao = await Swal.fire({
        icon: "warning",
        title: "Excluir ocorrência?",
        text: "Essa ação não pode ser desfeita.",
        showCancelButton: true,
        confirmButtonText: "Excluir",
        cancelButtonText: "Cancelar"
    });

    if (!confirmacao.isConfirmed) return;

    try {
        const res = await fetch(API + '/ocorrencias/' + id, { method: 'DELETE' });

        if (!res.ok) {
            let errorMessage = 'Erro ao excluir ocorrência';
            try {
                const err = await res.json();
                errorMessage = err.message || errorMessage;
            } catch {}
            throw new Error(errorMessage);
        }

        Swal.fire({
            icon: "success",
            title: "Sucesso",
            text: "Ocorrência excluída com sucesso"
        });

        await carregarOcorrenciasDaViagem();

        // se o modal de lista estiver aberto, atualiza a tabela nele também
        const modalLista = document.getElementById('modalListaOcorrenciasOverlay');
        if (modalLista.style.display === 'flex') {
            renderizarTabelaOcorrencias(ocorrenciasCarregadas);
        }

    } catch (err) {
        console.error('Erro ao excluir ocorrência:', err);
        Swal.fire({
            icon: "error",
            title: "Erro",
            text: err.message || "Erro ao excluir ocorrência."
        });
    }
}

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('input[name="tipoOcorrencia"]').forEach(function(radio) {
        radio.addEventListener('change', function() {
            var camposDespesa = document.getElementById('camposDespesa');
            var camposMulta = document.getElementById('camposMulta');

            if (this.value === 'DESPESA') {
                camposDespesa.style.display = 'block';
                camposMulta.style.display = 'none';
            } else {
                camposDespesa.style.display = 'none';
                camposMulta.style.display = 'block';
            }
        });
    });
});