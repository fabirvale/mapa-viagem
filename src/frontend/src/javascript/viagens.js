var API = 'http://localhost:8080';

// Elementos
var corpoTabela = document.getElementById('corpoTabela');
var painelDetalhe = document.getElementById('details-panel');
var viagemSelecionada = null;

// ========== LISTAGEM ==========
function carregarViagens() {
   console.log('Carregando viagens...');
  fetch(API + '/viagens')
    .then(function (res) { return res.json(); })
    .then(function (viagens) {

      if (!viagens || viagens.length === 0) {
        corpoTabela.innerHTML = '<tr><td colspan="6" class="vazio">Nenhuma viagem encontrada.</td></tr>';
        return;
      }

      corpoTabela.innerHTML = '';

      viagens.forEach(function (v) {
        var tr = document.createElement('tr');

        // guarda o ID na linha
        tr.dataset.id = v.id;

        //hover com destaque + seleção
        tr.addEventListener('click', function () {

          document.querySelectorAll('#corpoTabela tr')
            .forEach(l => l.classList.remove('ativa'));

          tr.classList.add('ativa');
          viagemSelecionada = v;
          
          //quando a viagem não foi selecionada, mostra detalhes e  carrega agendamentos da primeira linha 
          mostrarDetalhe(viagemSelecionada, false);
          carregarAgendamentosDaViagem();
        });
      
        tr.innerHTML =
          '<td>' + (v.cidadeOrigem || '-') + '</td>' +
          '<td>' + (v.cidadeDestino || '-') + '</td>' +
          '<td>' + formatarData(v.dataViagem) + '</td>' +
          '<td>' + (v.horaPrevista || '-') + '</td>' +
          '<td>' + badgeStatus(v.status) + '</td>' +
          '<td><i class="fa-solid fa-ellipsis-vertical"></i></td>';

        corpoTabela.appendChild(tr);
      });

      //manter seleção após recarregar
      if (viagens.length > 0) {

        const selecionada = viagens.find(v => v.id === viagemSelecionada?.id);

        viagemSelecionada = selecionada || viagens[0];

        mostrarDetalhe(viagemSelecionada);
        carregarAgendamentosDaViagem();

        const linhas = document.querySelectorAll('#corpoTabela tr');

        linhas.forEach((linha) => {
          if (linha.dataset.id == viagemSelecionada.id) {
            linha.classList.add('ativa');
          }
        });
      }

    })
    .catch(function (err) {
      console.error('Erro ao carregar viagens:', err);
      corpoTabela.innerHTML = '<tr><td colspan="6" class="vazio">Erro ao carregar viagens.</td></tr>';
    });
}

// ========== Mostrar detalhesda viagem no painel lateral ==========

function mostrarDetalhe(v, scroll = true) {
  viagemSelecionada = v;

  atualizarBadgePainel(v.status);
  document.getElementById('origemText').textContent =  v.cidadeOrigem || '-';
  document.getElementById('destinoText').textContent = v.cidadeDestino || '-';
  document.getElementById('destinoText').textContent = v.cidadeDestino || '-';
  document.getElementById('dataText').textContent = formatarData(v.dataViagem);
  document.getElementById('horaText').textContent = v.horaPrevista || '-';
  document.getElementById('motoristaText').textContent = v.motoristaNome || 'Não atribuído';
  document.getElementById('veiculoText').textContent = v.veiculoModelo || 'Não atribuído';
  document.getElementById('obsText').textContent = v.observacao || '-';
 
}


// Evita que o navegador tente restaurar a posição de rolagem ao voltar para a página
if ('scrollRestoration' in history) {
  history.scrollRestoration = 'manual';
}

// Garante que a rolagem seja para o topo após o carregamento da página
document.addEventListener('DOMContentLoaded', function () {
  setTimeout(function() {
    window.scrollTo(0, 0);
  }, 50);
});

//carrega a tabela de viagens ao carregar a página
carregarViagens();