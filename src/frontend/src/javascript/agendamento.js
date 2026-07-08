// ========== CARREGAR AGENDAMENTOS DA VIAGEM ==========
function carregarAgendamentosDaViagem() {

  if (!viagemSelecionada) return;

  fetch(API + '/agendamentos/viagem/' + viagemSelecionada.id)
    .then(function(res) {
      console.log('Status:', res.status);
      return res.json();
    })
    .then(function(data) {

      var agendamentos = Array.isArray(data)
        ? data
        : (data.conteudo || data.agendamentos || []);

      renderizarAgendamentos(agendamentos);
    })
    .catch(function(err) {
      console.error('Erro ao carregar agendamentos:', err);

      document.querySelector('.appointments-list').innerHTML =
        '<p class="vazio">Erro ao carregar agendamentos.</p>';

      document.getElementById('appointmentsCount').textContent = 0;
    });
}

function renderizarAgendamentos(agendamentos) {
  var lista = document.querySelector('.appointments-list');
  var contador = document.getElementById('appointmentsCount');
  var link = document.querySelector('.view-all-link');

  if (!Array.isArray(agendamentos) || agendamentos.length === 0) {
    lista.innerHTML = '<p class="vazio">Nenhum agendamento encontrado.</p>';
    contador.textContent = 0;
    link.style.display = 'none';
    return;
  }
  // atualiza contador total
  contador.textContent = agendamentos.length;

  // limpa lista
  lista.innerHTML = '';

  // mostra no máximo 2
  var limite = Math.min(agendamentos.length, 2);

  for (var i = 0; i < limite; i++) {

    var a = agendamentos[i];

    lista.innerHTML +=
      '<div class="appointment-card">' +

        '<div class="appointment-top">' +
          '<h5>' + (a.pacienteNome || '-') + '</h5>' +
          '<span class="appointment-time">' +
            (a.horarioAtendimento || '-') +
          '</span>' +
          '<button class="appointment-menu">' +
            '<i class="fa-solid fa-ellipsis-vertical"></i>' +
          '</button>' +
        '</div>' +

        '<p class="appointment-hospital">' +
          (a.hospitalNome || '-') +
        '</p>' +

        '<small class="appointment-specialty">' +
          (a.especialidade || '-') +
        '</small>' +

      '</div>';
  }

  // mostra link "ver todos" se tiver mais de 2
  if (agendamentos.length > 2) {
    link.style.display = 'block';
  } else {
    link.style.display = 'none';
  }
}