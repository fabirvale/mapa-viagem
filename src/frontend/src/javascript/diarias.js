var API = 'http://localhost:8080';   // use o mesmo valor do viagens.js

var selectMotorista = document.getElementById('filtroMotoristaDiarias');
var inputMes = document.getElementById('filtroMesDiarias');

document.addEventListener('DOMContentLoaded', function () {
    inputMes.value = new Date().toISOString().substring(0, 7);   // mês atual (AAAA-MM)
    carregarMotoristas();
    document.getElementById('btnConsultarDiarias').addEventListener('click', consultarDiarias);
});

async function carregarMotoristas() {
    try {
        const res = await fetch(API + '/motoristas');   // ajuste se o endpoint for outro
        if (!res.ok) throw new Error('Erro ' + res.status);
        const lista = await res.json();

        selectMotorista.innerHTML = '<option value="">Selecione o motorista</option>';
        lista.forEach(function (m) {
            var opt = document.createElement('option');
            opt.value = m.id;
            opt.textContent = m.nome;
            selectMotorista.appendChild(opt);
        });
    } catch (err) {
        console.error('Erro ao carregar motoristas:', err);
        Swal.fire({ icon: 'error', title: 'Erro', text: 'Erro ao carregar os motoristas.' });
    }
}

async function consultarDiarias() {
    var motoristaId = selectMotorista.value;
    var mes = inputMes.value;   // "2026-10"

    if (!motoristaId || !mes) {
        Swal.fire({ icon: 'warning', title: 'Atenção', text: 'Escolha o motorista e o mês.' });
        return;
    }

    var partes = mes.split('-');
    var ano = parseInt(partes[0]);
    var m = parseInt(partes[1]);
    var ultimoDia = new Date(ano, m, 0).getDate();

    var inicio = mes + '-01';
    var fim = mes + '-' + String(ultimoDia).padStart(2, '0');

    var btn = document.getElementById('btnConsultarDiarias');
    btn.disabled = true;

    try {
        const res = await fetch(API + '/viagens/motoristas/' + motoristaId +
            '/resumo?inicio=' + inicio + '&fim=' + fim);
        if (!res.ok) {
            var mensagem = 'Erro ao consultar as diárias.';
            try { mensagem = (await res.json()).message || mensagem; } catch (e) {}
            throw new Error(mensagem);
        }
        mostrarResumoDiarias(await res.json());
    } catch (err) {
        console.error('Erro ao consultar diárias:', err);
        Swal.fire({ icon: 'error', title: 'Erro', text: err.message });
    } finally {
        btn.disabled = false;
    }
}

function mostrarResumoDiarias(r) {

    // aviso de viagens pendentes
    var aviso = document.getElementById('avisoPendentes');
    if (r.viagensPendentes > 0) {
        aviso.textContent = r.viagensPendentes === 1
            ? 'Há 1 viagem deste motorista no período ainda não finalizada. O total considera só as finalizadas.'
            : 'Há ' + r.viagensPendentes + ' viagens deste motorista no período ainda não finalizadas. O total considera só as finalizadas.';
        aviso.style.display = 'block';
    } else {
        aviso.style.display = 'none';
    }

    // cards
    document.getElementById('kpiQtdViagens').textContent = r.viagens.length;
    document.getElementById('kpiPeriodo').textContent =
        formatarData(r.inicio) + ' a ' + formatarData(r.fim);
    document.getElementById('kpiTotalReceber').textContent = formatarMoeda(r.totalAReceber);
    document.getElementById('kpiMotorista').textContent = r.motoristaNome;

    // tabela
    var corpo = document.getElementById('corpoTabelaDiarias');
    corpo.innerHTML = '';

    if (r.viagens.length === 0) {
        var vazia = document.createElement('tr');
        var td = document.createElement('td');
        td.colSpan = 7;
        td.className = 'vazio';
        td.textContent = 'Nenhuma viagem finalizada neste período.';
        vazia.appendChild(td);
        corpo.appendChild(vazia);
    } else {
        r.viagens.forEach(function (v) {
            var tr = document.createElement('tr');
            [formatarData(v.data),
             v.rota, 
             formatarDataHoraCurta(v.saida),
             formatarDataHoraCurta(v.retorno),
             v.kmPercorridos != null ? v.kmPercorridos + ' km' : '-',
             rotuloTipoDiaria(v.tipoDiaria), formatarMoeda(v.valorDiaria)]
                .forEach(function (texto) {
                    var c = document.createElement('td');
                    c.textContent = texto;
                    tr.appendChild(c);
                });
            corpo.appendChild(tr);
        });
    }

    document.getElementById('rodapeTotal').textContent = formatarMoeda(r.totalAReceber);

    document.getElementById('msgInicialDiarias').style.display = 'none';
    document.getElementById('resultadoDiarias').style.display = 'block';
}