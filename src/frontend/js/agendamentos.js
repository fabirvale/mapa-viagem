// Elementos do modal de agendamento
var selectPaciente = document.getElementById('selectPaciente');
var selectAcompanhante = document.getElementById('selectAcompanhante');
var selectHospital = document.getElementById('selectHospital');
var modalAgendamentoOverlay = document.getElementById('modalAgendamentoOverlay');
const formAgendamento = document.getElementById('formAgendamento');

// Armazenar hospitais para consulta posterior
var hospitaisCarregados = [];

function abrirModalAgendamento() {
    if (!viagemSelecionada) {
      alert("Selecione uma viagem primeiro");
      return;
    }
    
    // Preencher informações da viagem
    var infoDiv = document.getElementById('infoViagemAgendamento');
    if (infoDiv) {
      infoDiv.innerHTML = `
        <strong>${viagemSelecionada.cidadeOrigem} → ${viagemSelecionada.cidadeDestino}</strong><br>
        Data: ${formatarData(viagemSelecionada.dataViagem)}<br>
        Hora: ${viagemSelecionada.horaPrevista || '-'}
      `;
    }
    
    // Carregando os selects
    carregarPacientes();
    carregarAcompanhantes();
    carregarHospitais();
    
    modalAgendamentoOverlay.style.display = 'flex';
}

function carregarPacientes() {
  fetch(API + '/pacientes')
    .then(function(res) {
      return res.json();
    })
    .then(function(pacientes) {
      selectPaciente.innerHTML = '<option value="">Selecione um paciente</option>';

      pacientes.forEach(function(p) {
        let option = document.createElement('option');
        option.value = p.id;
        option.textContent = p.nome;
        selectPaciente.appendChild(option);
      });
    })
    .catch(function(err) {
      console.error('Erro ao carregar pacientes:', err);
      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar pacientes."
      });
    });
}

function carregarAcompanhantes() {
  fetch(API + '/acompanhantes')
    .then(function(res) {
      return res.json();
    })
    .then(function(acompanhantes) {
      selectAcompanhante.innerHTML = '<option value="">Selecione um acompanhante</option>';

      acompanhantes.forEach(function(a) {
        let option = document.createElement('option');
        option.value = a.id;
        option.textContent = a.nome;
        selectAcompanhante.appendChild(option);
      });
    })
    .catch(function(err) {
      console.error('Erro ao carregar acompanhantes:', err);
      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar acompanhantes."
      });
    });
}

function carregarHospitais() {
  fetch(API + '/hospitais')
    .then(function(res) {
      return res.json();
    })
    .then(function(hospitais) {
      hospitaisCarregados = hospitais;
      selectHospital.innerHTML = '<option value="">Selecione um hospital</option>';

      hospitais.forEach(function(h) {
        let option = document.createElement('option');
        option.value = h.id;
        option.textContent = h.nome;
        selectHospital.appendChild(option);
      });
    })
    .catch(function(err) {
      console.error('Erro ao carregar hospitais:', err);
      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar hospitais."
      });
    });
}

function fecharModalAgendamento() {
   modalAgendamentoOverlay.style.display = 'none';
}

function atualizarDadosHospital() {
  var hospitalDetalhes = document.getElementById('hospitalDetalhes');
  if (!hospitalDetalhes) return;

  var hospitalId = Number(selectHospital.value);
  
  if (!hospitalId) {
    hospitalDetalhes.innerHTML = '';
    return;
  }

  var hospital = hospitaisCarregados.find(function(h) {
    return h.id === hospitalId;
  });

  if (hospital) {
    hospitalDetalhes.innerHTML = `
      <p><strong>Endereço:</strong> ${hospital.endereco}, ${hospital.numero}</p>
      <p><strong>Bairro:</strong> ${hospital.bairro}</p>
      <p><strong>CEP:</strong> ${hospital.cep}</p>
      <p><strong>Cidade:</strong> ${hospital.cidade}</p>
      <p><strong>Telefone:</strong> ${hospital.telefone || '-'}</p>
    `;
  } else {
    hospitalDetalhes.innerHTML = '';
  }
}

// ========== CARREGAR AGENDAMENTOS DA VIAGEM ==========

function carregarAgendamentosDaViagem() {
  if (!viagemSelecionada) return;

  fetch(API + '/agendamentos/viagem/' + viagemSelecionada.id)
    .then(function(res) {
      console.log('Status:', res.status);
      return res.json();
    })
    .then(function(data) {
      console.log('Dados recebidos:', data);
      // Se a resposta for um objeto com propriedade "conteudo" ou similar
      var agendamentos = Array.isArray(data) ? data : (data.conteudo || data.agendamentos || []);
      renderizarAgendamentos(agendamentos);
    })
    .catch(function(err) {
      console.error('Erro ao carregar agendamentos:', err);
      document.getElementById('listaAgendamentos').innerHTML = '<p class="vazio">Erro ao carregar agendamentos.</p>';
    });
}

function renderizarAgendamentos(agendamentos) {
  var listaDiv = document.getElementById('listaAgendamentos');
  
  // Validar se agendamentos é um array
  if (!Array.isArray(agendamentos) || agendamentos.length === 0) {
    listaDiv.innerHTML = '<p class="vazio">Nenhum agendamento encontrado.</p>';
    return;
  }

  var html = `
    <table class="tabela-agendamentos">
      <thead>
        <tr>
          <th>Paciente</th>
          <th>Acompanhante</th>
          <th>Hospital</th>
          <th>Data</th>
          <th>Hora</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
  `;

  agendamentos.forEach(function(agendamento) {
    html += `
      <tr>
        <td>${agendamento.pacienteNome || '-'}</td>
        <td>${agendamento.acompanhanteNome || '-'}</td>
        <td>${agendamento.hospitalNome || '-'}</td>
        <td>${formatarData(agendamento.dataAtendimento)}</td>
        <td>${agendamento.horarioAtendimento || '-'}</td>
        <td>
          <button class="btn btn-secondary btn-sm" onclick="editarAgendamento(${agendamento.id})">Editar</button>
          <button class="btn btn-danger btn-sm" onclick="cancelarAgendamento(${agendamento.id})">Cancelar</button>
        </td>
      </tr>
    `;
  });

  html += `
      </tbody>
    </table>
  `;

  listaDiv.innerHTML = html;
}

function editarAgendamento(id) {
  // TODO: Implementar edição
  alert('Funcionalidade de edição em desenvolvimento');
}

function cancelarAgendamento(id) {
  // TODO: Implementar cancelamento
  alert('Funcionalidade de cancelamento em desenvolvimento');
}

formAgendamento.addEventListener('submit', function(event) {
  event.preventDefault(); // evita o reload da página
  salvarAgendamento();    // função que fará o POST
});

function salvarAgendamento() {

    if (!viagemSelecionada) {
        Swal.fire({
            icon: "error",
            title: "Erro",
            text: "Nenhuma viagem selecionada."
        });
        return;
    }
    var dados = {
      pacienteId: selectPaciente.value,
      acompanhanteId: selectAcompanhante.value,
      hospitalId: selectHospital.value,
      viagemId: viagemSelecionada.id,
      dataAtendimento: document.getElementById('dataAtendimento').value,
      horarioAtendimento: document.getElementById('horaAtendimento').value
    };

    fetch(API + '/agendamentos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(dados)
    })
      .then(function (res) {
        console.log('Response status:', res.status);
        if (!res.ok) {
          // Tenta ler a mensagem do backend
          return res.json().catch(function() {
            // Se não conseguir fazer parse, usa mensagem genérica
            throw new Error('Erro ' + res.status + ' ao criar agendamento');
          }).then(function(errData) {
            console.log('Dados de erro do backend:', errData); // Log do erro
            let mensagem = errData.message || errData.error || 'Erro ao criar agendamento';
            
            // Se houver array de erros, extrai as mensagens
            if (errData.errors && Array.isArray(errData.errors)) {
              mensagem = errData.errors.map(function(e) { 
                return e.message; 
              }).join('; ');
            }
            
            throw new Error(mensagem);
          });
        }
        return res.json();
      })
      .then(function(data) {
        console.log('Agendamento criado com sucesso:', data);
        formAgendamento.reset();
        modalAgendamentoOverlay.style.display = 'none';
        carregarAgendamentosDaViagem();
        Swal.fire({
          icon: "success",
          title: "Sucesso",
          text: "Agendamento criado com sucesso!"
        });
      })
      .catch(function (err) {
        console.error('Erro detalhado:', err);
        Swal.fire({
          icon: "error",
          title: "Erro",
          text: err.message || "Erro desconhecido ao criar agendamento",
          confirmButtonText: "OK"
        }).then(function() {
          // Fecha o modal quando o usuário clicar em OK
          modalAgendamentoOverlay.style.display = 'none';
        });
      });
  };
