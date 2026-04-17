var API = 'http://localhost:8080';

// Elementos
var corpoTabela = document.getElementById('corpoTabela');
var btnNovaViagem = document.getElementById('btnNovaViagem');
var modalOverlay = document.getElementById('modalOverlay');
var btnFecharModal = document.getElementById('btnFecharModal');
var formViagem = document.getElementById('formViagem');
var painelDetalhe = document.getElementById('painelDetalhe');
var detalheConteudo = document.getElementById('detalheConteudo');
var btnFecharVeiculo = document.getElementById('btnFecharVeiculo')

var viagemSelecionada = null;

// ========== LISTAGEM ==========

function carregarViagens() {
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

          mostrarDetalhe(viagemSelecionada, false);
        });
      
        tr.innerHTML =
          '<td>' + (v.cidadeOrigem || '-') + '</td>' +
          '<td>' + (v.cidadeDestino || '-') + '</td>' +
          '<td>' + formatarData(v.dataViagem) + '</td>' +
          '<td>' + (v.horaPrevista || '-') + '</td>' +
          '<td>' + badgeStatus(v.status) + '</td>' +
          '<td>' + botoesAcao(v) + '</td>';

        corpoTabela.appendChild(tr);
      });

      //manter seleção após recarregar
      if (viagens.length > 0) {

        const selecionada = viagens.find(v => v.id === viagemSelecionada?.id);

        viagemSelecionada = selecionada || viagens[0];

        mostrarDetalhe(viagemSelecionada);

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

function botoesAcao(v) {
  if (!v.status) return '';
  var s = v.status.toUpperCase();

  if (s === 'AGENDADA') {
   return `
    <button class="btn btn-editar" onclick="event.stopPropagation(); editarViagem(${v.id})">Editar</button>
    <button class="btn btn-iniciar" onclick="event.stopPropagation(); iniciarViagem(${v.id})">Iniciar</button>
  `;
  }
  if (s === 'INICIADA') {
    return '<button class="btn btn-finalizar" onclick="event.stopPropagation(); finalizarViagem(' + v.id + ')">Finalizar</button>';
  }
  return '<button class="btn" disabled>Finalizada</button>';
}

// ========== AÇÕES ==========

function iniciarViagem(id) {
  fetch(API + '/viagens/' + id + '/iniciar', { method: 'PATCH' })
    .then(function () { carregarViagens(); painelDetalhe.style.display = 'none'; })
    .catch(function (err) { alert('Erro ao iniciar viagem'); console.error(err); });
}

function finalizarViagem(id) {
  fetch(API + '/viagens/' + id + '/finalizar', { method: 'PATCH' })
    .then(function () { carregarViagens(); painelDetalhe.style.display = 'none'; })
    .catch(function (err) { alert('Erro ao finalizar viagem'); console.error(err); });
}

// ========== DETALHE ==========

function mostrarDetalhe(v, scroll = true) {
  painelDetalhe.style.display = 'block';
  viagemSelecionada = v;

  var motorista = v.motoristaNome || 'Não atribuído';
  var veiculo = v.veiculoModelo || 'Não atribuído';
  var kmInicial = v.kmInicial != null ? v.kmInicial : 'Não registrado';
  var btnMotorista = document.getElementById("btnMotorista");
  var btnVeiculo = document.getElementById('btnVeiculo')

  detalheConteudo.innerHTML =
    '<div class="detalhe-grid">' +
      '<p><strong>Rota:</strong> ' + (v.cidadeOrigem || '-') + ' → ' + (v.cidadeDestino || '-') + '</p>' +
      '<p><strong>Status:</strong> ' + badgeStatus(v.status) + '</p>' +
      '<p><strong>Data:</strong> ' + formatarData(v.dataViagem) + '</p>' +
      '<p><strong>Hora Prevista:</strong> ' + (v.horaPrevista || '-') + '</p>' +
      '<p><strong>KM Inicial:</strong> ' + kmInicial + '</p>' +
      '<p><strong>Motorista:</strong> ' + motorista + '</p>' +
      '<p><strong>Veículo:</strong> ' + veiculo + '</p>' +
    '</div>';
    //trocar o label do botão
    if (btnMotorista) {
        btnMotorista.textContent = v.motoristaNome
        ? "✏️ Alterar Motorista"
        : "+ Adicionar Motorista";
    }
     if (btnVeiculo) {
        btnVeiculo.textContent = v.veiculoModelo
        ? "✏️ Alterar Veículo"
        : "+ Adicionar Veículo";
    }
   if (scroll) {
    painelDetalhe.scrollIntoView({ behavior: 'smooth' });
   }

   // Se a aba "Agendamentos" estiver ativa, recarregar agendamentos
   var abaAgendamentos = document.querySelector('.aba.ativa');
   if (abaAgendamentos && abaAgendamentos.textContent === 'Agendamentos') {
     carregarAgendamentosDaViagem();
   }
}
// ========== MODAL - CRIAR VIAGEM ==========

if (btnNovaViagem) {
    btnNovaViagem.addEventListener('click', function () {
    viagemSelecionada = null
    
    // limpa formulário (ESSENCIAL)
    formViagem.reset();

    modalOverlay.style.display = 'flex';
  });
}

if (btnFecharModal) {
  btnFecharModal.addEventListener('click', function () {
    modalOverlay.style.display = 'none';
  });
}

if (modalOverlay) {
  modalOverlay.addEventListener('click', function (e) {
    if (e.target === modalOverlay) {
      modalOverlay.style.display = 'none';
    }
  });
}


//Criar ou editar viagem
if (formViagem) {
  formViagem.addEventListener('submit', async function (e) {
    e.preventDefault()

    var dados = {
      descricao: document.getElementById('descricao').value,
      cidadeOrigem: document.getElementById('cidadeOrigem').value,
      cidadeDestino: document.getElementById('cidadeDestino').value,
      dataViagem: document.getElementById('dataViagem').value,
      horaPrevista: document.getElementById('horaPrevista').value
    };

    let url = API + '/viagens'
    let method = 'POST'

    if (viagemSelecionada && viagemSelecionada.id) {
      url = API + '/viagens/' + viagemSelecionada.id
      method = 'PUT'
    }
    else {
      delete dados.id //garantia extra no CREATE
    }


    try {
      const res = await fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
      });

      if (!res.ok) {
        let errorMessage = 'Erro ao salvar viagem'

        try {
          const err = await res.json();
          errorMessage = err.message || errorMessage || errorMessage
        } catch (e) {
          try {
            const text = await res.text();
            errorMessage = text || errorMessage
          } catch (e2) {}
        }

        throw new Error(errorMessage)
      }

      // sucesso
      await res.json()

      formViagem.reset()
      modalOverlay.style.display = 'none'
      viagemSelecionada = null

      carregarViagens()

      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });

      Swal.fire({
        icon: "success",
        title: "Sucesso",
        text: method === 'POST'
          ? "Viagem criada com sucesso!"
          : "Viagem atualizada com sucesso!"
      });

    } catch (err) {
      console.error(err);

      Swal.fire({
        icon: "error",
        title: "Erro",
        text: err.message || "Erro ao salvar viagem"
      });
    }
  });
}

/*function editarViagem(id) {
  fetch(API + '/viagens/' + id)
    .then(res => res.json())
    .then(v => {

      viagemSelecionada = v;

      document.getElementById('descricao').value = v.descricao || '';
      document.getElementById('cidadeOrigem').value = v.cidadeOrigem || '';
      document.getElementById('cidadeDestino').value = v.cidadeDestino || '';
      document.getElementById('dataViagem').value = v.dataViagem || '';
      document.getElementById('horaPrevista').value = v.horaPrevista || '';

      modalOverlay.style.display = 'flex';
    });
}*/

// ========== INIT ==========
if ('scrollRestoration' in history) {
  history.scrollRestoration = 'manual';
}
document.addEventListener('DOMContentLoaded', function () {
  setTimeout(function() {
    window.scrollTo(0, 0);
  }, 50);
});
carregarViagens();
 

function mostrarAba(aba, event) {
  // Esconder todas as abas de conteúdo
  document.querySelectorAll('.conteudo-aba').forEach(div => div.style.display = 'none');
  
  // Remover classe ativa de todas as abas
  document.querySelectorAll('.aba').forEach(btn => btn.classList.remove('ativa'));
  
  // Mostrar aba selecionada
  const conteudoId = 'conteudo' + aba.charAt(0).toUpperCase() + aba.slice(1);
  document.getElementById(conteudoId).style.display = 'block';
  
  // Adicionar classe ativa ao botão clicado
  event.target.classList.add('ativa');
  
  // Se for a aba de agendamentos, carregar os agendamentos
  if (aba === 'agendamentos') {
    carregarAgendamentosDaViagem();
  }
}