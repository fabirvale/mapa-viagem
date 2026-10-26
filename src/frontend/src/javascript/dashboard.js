var API = 'http://localhost:8080';   // mesmo valor do viagens.js

var inputMes = document.getElementById('filtroMesDashboard');
var graficos = {};
var PALETA = ['#2563eb', '#16a34a', '#f59e0b', '#9333ea', '#dc2626', '#0891b2', '#db2777', '#65a30d'];

document.addEventListener('DOMContentLoaded', function () {
    var d = new Date();
    inputMes.value = d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0');

    inputMes.addEventListener('change', carregarGraficos);

    carregarKpis();
    carregarGraficos();
});

// ========== utilitários ==========
async function buscar(caminho, mes) {
    var url = API + '/dashboard/' + caminho + (mes ? '?mes=' + mes : '');
    var res = await fetch(url);
    if (!res.ok) throw new Error('Erro ' + res.status + ' em ' + caminho);
    return res.json();
}

// "2026-10" -> "10/2026"
function rotuloMes(ym) {
    var p = ym.split('-');
    return p[1] + '/' + p[0];
}

function desenhar(id, config, temDados) {
    if (graficos[id]) {
        graficos[id].destroy();
        delete graficos[id];
    }
    var canvas = document.getElementById(id);
    var vazio = document.getElementById(id + 'Vazio');

    canvas.style.display = temDados ? 'block' : 'none';
    vazio.style.display = temDados ? 'none' : 'flex';
    if (!temDados) return;

    graficos[id] = new Chart(canvas, config);
}

function tooltipMoeda() {
    return { callbacks: { label: function (ctx) { return ' ' + formatarMoeda(Number(ctx.raw)); } } };
}

function configBarras(rotulos, datasets, moeda, horizontal) {
    var eixoValor = { beginAtZero: true, ticks: moeda ? {} : { precision: 0 } };
    var scales = horizontal ? { x: eixoValor } : { y: eixoValor };
    return {
        type: 'bar',
        data: { labels: rotulos, datasets: datasets },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            indexAxis: horizontal ? 'y' : 'x',
            scales: scales,
            plugins: {
                legend: { display: datasets.length > 1 },
                tooltip: moeda ? tooltipMoeda() : {}
            }
        }
    };
}

function configPizza(rotulos, valores, moeda) {
    return {
        type: 'doughnut',
        data: {
            labels: rotulos,
            datasets: [{ data: valores, backgroundColor: PALETA }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'right' },
                tooltip: moeda ? tooltipMoeda() : {}
            }
        }
    };
}

// ========== KPIs (endpoint já existente) ==========
async function carregarKpis() {
    try {
        var res = await fetch(API + '/dashboard/resumo');
        if (!res.ok) throw new Error('Erro ' + res.status);
        var d = await res.json();

        document.getElementById('kpiViagensAgendadas').textContent = d.viagensAgendadas;
        document.getElementById('kpiViagensHoje').textContent = d.viagensHoje;
        document.getElementById('kpiPacientesAgendados').textContent = d.pacientesAgendadosHoje;
        document.getElementById('kpiViagensFinalizadas').textContent = d.viagensFinalizadasMes;
    } catch (err) {
        console.error('Erro ao carregar KPIs:', err);
    }
}

// ========== gráficos ==========
async function carregarGraficos() {
    var mes = inputMes.value;
    if (!mes) return;

    var tarefas = [
        graficoViagensPorMes, graficoViagensPorStatus,
        graficoDiariasPorMotorista, graficoTiposDiaria,
        graficoCancelPorMotivo, graficoCancelPorMes,
        graficoCustoVeiculo, graficoMultasMotorista,
        graficoEspecialidade
    ];

    // cada gráfico falha sozinho, sem derrubar os outros
    await Promise.all(tarefas.map(function (fn) {
        return fn(mes).catch(function (err) {
            console.error('Erro no gráfico ' + fn.name + ':', err);
        });
    }));
}

async function graficoViagensPorMes(mes) {
    var dados = await buscar('viagens-por-mes', mes);
    var temDados = dados.some(function (d) { return d.finalizadas > 0 || d.canceladas > 0; });

    desenhar('grafViagensPorMes', configBarras(
        dados.map(function (d) { return rotuloMes(d.mes); }),
        [
            { label: 'Finalizadas', data: dados.map(function (d) { return d.finalizadas; }), backgroundColor: '#16a34a' },
            { label: 'Canceladas', data: dados.map(function (d) { return d.canceladas; }), backgroundColor: '#dc2626' }
        ], false, false), temDados);
}

async function graficoViagensPorStatus(mes) {
    var dados = await buscar('viagens-por-status', mes);
    desenhar('grafViagensPorStatus', configPizza(
        dados.map(function (d) { return rotuloTipoDiaria(d.rotulo); }),
        dados.map(function (d) { return d.valor; }), false), dados.length > 0);
}

async function graficoDiariasPorMotorista(mes) {
    var dados = await buscar('diarias-por-motorista', mes);
    desenhar('grafDiariasPorMotorista', configBarras(
        dados.map(function (d) { return d.rotulo; }),
        [{ label: 'Diárias (R$)', data: dados.map(function (d) { return d.valor; }), backgroundColor: '#2563eb' }],
        true, false), dados.length > 0);
}

async function graficoTiposDiaria(mes) {
    var dados = await buscar('tipos-de-diaria', mes);
    desenhar('grafTiposDiaria', configPizza(
        dados.map(function (d) { return rotuloTipoDiaria(d.rotulo); }),
        dados.map(function (d) { return d.valor; }), false), dados.length > 0);
}

async function graficoCancelPorMotivo(mes) {
    var dados = await buscar('cancelamentos-por-motivo', mes);
    desenhar('grafCancelPorMotivo', configPizza(
        dados.map(function (d) { return d.rotulo; }),
        dados.map(function (d) { return d.valor; }), false), dados.length > 0);
}

async function graficoCancelPorMes(mes) {
    var dados = await buscar('cancelamentos-por-mes', mes);
    var temDados = dados.some(function (d) { return d.valor > 0; });

    desenhar('grafCancelPorMes', {
        type: 'line',
        data: {
            labels: dados.map(function (d) { return rotuloMes(d.rotulo); }),
            datasets: [{
                label: 'Cancelamentos',
                data: dados.map(function (d) { return d.valor; }),
                borderColor: '#dc2626',
                backgroundColor: 'rgba(220, 38, 38, 0.15)',
                fill: true,
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: { y: { beginAtZero: true, ticks: { precision: 0 } } },
            plugins: { legend: { display: false } }
        }
    }, temDados);
}

async function graficoCustoVeiculo(mes) {
    var dados = await buscar('custo-ocorrencias-por-veiculo', mes);
    desenhar('grafCustoVeiculo', configPizza(
        dados.map(function (d) { return d.rotulo; }),
        dados.map(function (d) { return d.valor; }), true), dados.length > 0);
}

async function graficoMultasMotorista(mes) {
    var dados = await buscar('multas-por-motorista', mes);
    desenhar('grafMultasMotorista', configBarras(
        dados.map(function (d) { return d.rotulo; }),
        [{ label: 'Multas (R$)', data: dados.map(function (d) { return d.valor; }), backgroundColor: '#f59e0b' }],
        true, false), dados.length > 0);
}

async function graficoEspecialidade(mes) {
    var dados = await buscar('atendimentos-por-especialidade', mes);
    desenhar('grafEspecialidade', configBarras(
        dados.map(function (d) { return d.rotulo; }),
        [{ label: 'Atendimentos', data: dados.map(function (d) { return d.valor; }), backgroundColor: '#9333ea' }],
        false, true), dados.length > 0);
}