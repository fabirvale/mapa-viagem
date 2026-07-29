// Elementos do modal de agendamento
var selectPaciente = document.getElementById('selectPaciente');
var selectAcompanhante = document.getElementById('selectAcompanhante');
var selectHospital = document.getElementById('selectHospital');
var selecttipoCompromisso = document.getElementById('selecttipoCompromisso');
var selectespecialidade = document.getElementById('selectespecialidade');
var modalAgendamentoOverlay = document.getElementById('modalAgendamentoOverlay');
const formAgendamento = document.getElementById('formAgendamento');
let agendamentoEditandoId = null;

// Armazenar hospitais, pacientes para consulta posterior
var hospitaisCarregados = [];
var pacientesCarregados = [];

//Função para abrir o modal de agendamento, preenchendo os selects e informações da viagem
function abrirModalAgendamento() {
    console.log('Abrindo modal de agendamento para a viagem:', viagemSelecionada);
    if (!viagemSelecionada) {
      alert("Selecione uma viagem primeiro");
      return;
    }
     if (viagemJaPassouDoHorario()) {
        Swal.fire({
            icon: "warning",
            title: "Horário da viagem já passou",
            text: "Não é possível criar ou alterar agendamentos para esta viagem."
        });

        return;
    }
    
    // A viagem precisa estar agendada para permitir novos agendamentos
    if (viagemSelecionada.status !== "AGENDADA") {

        Swal.fire({
            icon: "warning",
            title: "Agendamento não permitido",
            text: "Não é possível criar agendamentos para uma viagem que já foi iniciada ou finalizada."
        });

        return;
    }
    
    // Preencher informações da viagem
   document.getElementById('viagemOrigem').textContent  = viagemSelecionada.cidadeOrigem || '-';
   document.getElementById('viagemDestino').textContent = viagemSelecionada.cidadeDestino || '-';
   document.getElementById('viagemData').textContent    = formatarData(viagemSelecionada.dataViagem);
   document.getElementById('viagemHora').textContent    = viagemSelecionada.horaPrevista || '-';
   document.getElementById('dataAtendimento').value     = viagemSelecionada.dataViagem;
    // Carregando os selects
    carregarPacientes();
    carregarAcompanhantes();
    carregarHospitais();
    carregartipoCompromisso();
    carregartipoEspecialidade();
    
    modalAgendamentoOverlay.style.display = 'flex';
}

function viagemJaPassouDoHorario() {
    const agora = new Date();
    const dataHoraViagem = new Date(viagemSelecionada.dataViagem + 'T' + viagemSelecionada.horaPrevista);
    return agora > dataHoraViagem;
}
 //carega os pacientes/acompanhantes/hospitais/tipoCompromisso/tipoEspecialidade para o select do modal de agendamento
async function carregarPacientes() {
  try {
        const res = await fetch(API + '/pacientes');
        const pacientes = await res.json(); 
        pacientesCarregados = pacientes;
        selectPaciente.innerHTML = '<option value="">Selecione um paciente</option>';
        pacientes.forEach(function(p) {
              let option = document.createElement('option');
              option.value = p.id;
              option.textContent = p.nome;
              selectPaciente.appendChild(option);
            });
      }
      catch(err) {
          console.error('Erro ao carregar pacientes:', err);
          Swal.fire({
            icon: "error",
            title: "Erro",
            text: "Erro ao carregar pacientes."
          });
    }
}

async function carregarAcompanhantes() {
  try {
    const res = await fetch(API + '/acompanhantes');
    const acompanhantes = await res.json();

    selectAcompanhante.innerHTML =
      '<option value="">Selecione um acompanhante</option>';

    acompanhantes.forEach(function(a) {
      let option = document.createElement('option');
      option.value = a.id;
      option.textContent = a.nome;
      selectAcompanhante.appendChild(option);
    });

  } catch (err) {
    console.error('Erro ao carregar acompanhantes:', err);
    Swal.fire({
      icon: "error",
      title: "Erro",
      text: "Erro ao carregar acompanhantes."
    });
  }
}

async function carregarHospitais() {
  try{
       const res = await fetch(API + '/hospitais');
       const hospitais = await res.json();
       hospitaisCarregados = hospitais;
       selectHospital.innerHTML = '<option value="">Selecione um hospital</option>';
       hospitais.forEach(function(h) {
            let option = document.createElement('option');
            option.value = h.id;
            option.textContent = h.nome;
            selectHospital.appendChild(option);
          });  
      }
      catch(err) {
          console.error('Erro ao carregar hospitais:', err);
          Swal.fire({
            icon: "error",
            title: "Erro",
            text: "Erro ao carregar hospitais."
          });
    };
}

async function carregartipoCompromisso() {
  try {
     const res = await fetch(API + '/tipocompromisso');
     const tipoCompromisso = await res.json();

      selecttipoCompromisso.innerHTML = '';

     tipoCompromisso.forEach(function(tc) {
        let option = document.createElement('option');
        option.value = tc;
        option.textContent = tc;
        selecttipoCompromisso.appendChild(option);
     });
      selecttipoCompromisso.value = 'SUS'; // define o padrão depois de popular
    }
    catch(err) {
      console.error('Erro ao carregar tipo de compromisso:', err);
      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar tipo de compromisso."
      });
    };
 
}

async function carregartipoEspecialidade() {
  try{
      const res = await fetch(API + '/especialidades')
      const tipoEspecialidade = await res.json();

      tipoEspecialidade.forEach(function(te) {
            let option = document.createElement('option');
            option.value = te.id;
            option.textContent = te.especialidade;
            selectespecialidade.appendChild(option);
       });
          
    }
    catch(err) {
      console.error('Erro ao carregar tipo de especialidade:', err);
      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar tipo de especialidade."
      });
    };
 
}

// ======== Fecha o modal de agendamento ==============
function fecharModalAgendamento() {
   modalAgendamentoOverlay.style.display = 'none';
}

// Atualiza os dados do hospital selecionado no modal
function atualizarDadosHospital() {
  var hospitalId = Number(selectHospital.value);

  if (!hospitalId) {
    document.getElementById('hospitalEndereco').textContent = '';
    document.getElementById('hospitalBairro').textContent = '';
    document.getElementById('hospitalCidade').textContent = '';
    document.getElementById('hospitalTelefone').textContent = '';
    return;
  }

  var hospital = hospitaisCarregados.find(function(h) {
    return h.id === hospitalId;
  });

  if (hospital) {
    document.getElementById('hospitalEndereco').textContent =
      hospital.endereco + ', ' + hospital.numero;

    document.getElementById('hospitalBairro').textContent =
      hospital.bairro;

    document.getElementById('hospitalCidade').textContent =
      hospital.cidade;

    document.getElementById('hospitalTelefone').textContent =
      hospital.telefone || '-';
  }
}

// Atualiza os dados do paciente selecionado no modal
function atualizarDadosPaciente() {
  console.log('Paciente selecionado:', selectPaciente.value);
  var pacienteId = Number(selectPaciente.value);

  if (!pacienteId) {
    document.getElementById('txttelefone').value = '';
    document.getElementById('txtenderecoPaciente').value = '';
    return;
  }
  
  var paciente = pacientesCarregados.find(function(p) {
    return p.id === pacienteId;
  });

  if (paciente) {
    document.getElementById('txtenderecoPaciente').value = paciente.endereco;
    document.getElementById('txttelefone').value = paciente.telefone;
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
          '<div class="appointment-actions">' +
            '<button class="appointment-menu" ' +
              'onclick="abrirMenuAgendamento(event, ' + a.id + ')">' +
              '<i class="fa-solid fa-ellipsis-vertical"></i>' +
            '</button>' +
            '<div class="menu-acoes menu-agendamento" id="menu-agendamento-' + a.id + '">' +
              '<button onclick="editarAgendamento(' + a.id + ')">' +
                '<i class="fa-solid fa-pen"></i> Editar' +
              '</button>' +
              '<button onclick="excluirAgendamento(' + a.id + ')">' +
                '<i class="fa-solid fa-trash"></i> Excluir' +
              '</button>' +
            '</div>' +
           '</div>' +
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

function abrirMenuAgendamento(event, id) {
    event.stopPropagation();
    var menu = document.getElementById('menu-agendamento-' + id);

    if (!menu) return;

    var botao = event.currentTarget;
    var rect = botao.getBoundingClientRect();

    menu.style.top = (rect.bottom + 5) + 'px';
    menu.style.left = (rect.right - 180) + 'px';

    menu.style.display =
    menu.style.display === 'block' ? 'none' : 'block';
}

async function editarAgendamento(id) {
     agendamentoEditandoId = id;

     if (viagemJaPassouDoHorario()) {
        Swal.fire({
            icon: "warning",
            title: "Horário da viagem já passou",
            text: "Não é possível criar ou alterar agendamentos para esta viagem."
        });

        return;
    }

     const res = await fetch(API + '/agendamentos/' + id);
     const a = await res.json();

      await carregarPacientes();
      document.getElementById('selectPaciente').value = a.pacienteId || '';
      atualizarDadosPaciente();

      await carregarAcompanhantes();
      document.getElementById('selectAcompanhante').value = a.acompanhanteId || '';

      document.getElementById('checkboxida').checked = a.ida || '';
      document.getElementById('checkboxvolta').checked = a.volta || '';

      await carregarHospitais();
      document.getElementById('selectHospital').value = a.hospitalId || '';
      atualizarDadosHospital();
     
       // Preencher informações da viagem
      document.getElementById('viagemOrigem').textContent  = viagemSelecionada.cidadeOrigem || '-';
      document.getElementById('viagemDestino').textContent = viagemSelecionada.cidadeDestino || '-';
      document.getElementById('viagemData').textContent    = formatarData(viagemSelecionada.dataViagem);
      document.getElementById('viagemHora').textContent    = viagemSelecionada.horaPrevista || '-';

      document.getElementById('dataAtendimento').value  = a.dataAtendimento;
      document.getElementById('horaAtendimento').value  = a.horarioAtendimento || '-';

      await carregartipoCompromisso();
      document.getElementById('selecttipoCompromisso').value = a.tipoCompromisso || '';

      await carregartipoEspecialidade();
      document.getElementById('selectespecialidade').value = a.tipoEspecialidade_Id || '';

      document.getElementById('checkboxoxigenio').checked = a.oxigenio || '';
      document.getElementById('checkboxcadeirante').checked = a.cadeirante || '';
      document.getElementById('checkboxmaca').checked = a.maca || '';
      document.getElementById('checkboxoutrosCuidados').checked = a.outrosCuidados || '';
      
      document.getElementById('txtobservacao').value = a.observacao || '';
     
      modalAgendamentoOverlay.style.display = 'flex';
  
}

function cancelarAgendamento(id) {
  // TODO: Implementar cancelamento
  alert('Funcionalidade de cancelamento em desenvolvimento');
}

formAgendamento.addEventListener('submit', function(event) {
  event.preventDefault(); // evita o reload da página
  salvarAgendamento();    // função que fará o POST
});

// ========== SALVAR AGENDAMENTO ==========


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
      id: agendamentoEditandoId,
      pacienteId: selectPaciente.value,
      acompanhanteId: selectAcompanhante.value,
      hospitalId: selectHospital.value,
      viagemId: viagemSelecionada.id,
      dataAtendimento: document.getElementById('dataAtendimento').value,
      horarioAtendimento: document.getElementById('horaAtendimento').value,
      ida : document.getElementById('checkboxida').checked,
      volta : document.getElementById('checkboxvolta').checked,
      tipoCompromisso: selecttipoCompromisso.value,
      tipoEspecialidade_Id: selectespecialidade.value,
      observacao: document.getElementById('txtobservacao').value,
      cadeirante: document.getElementById('checkboxcadeirante').checked,
      maca: document.getElementById('checkboxmaca').checked,
      oxigenio: document.getElementById('checkboxoxigenio').checked,
      outrosCuidados: document.getElementById('checkboxoutrosCuidados').checked
    };
    console.log('Dados a serem enviados para o backend:', dados);
    const url = agendamentoEditandoId ? API + '/agendamentos/' + agendamentoEditandoId : API + '/agendamentos';
    const metodo = agendamentoEditandoId ? 'PUT' : 'POST';
    
    fetch(url, {
      method: metodo,
      headers: {
          'Content-Type': 'application/json'
      },
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
        return res;
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
        })
      });
  };

  async function excluirAgendamento(id) {
    // fecha o menu agendamento, se estiver aberto
    document.querySelectorAll(".menu-agendamento").forEach(menu => {
        menu.style.display = "none";
    });

    if (viagemSelecionada.status !== "AGENDADA") {
      Swal.fire({
          icon: "warning",
          title: "Não é possível excluir",
          text: "Somente agendamentos de viagens agendadas podem ser excluídos."
      });
      return;
    }
   
    const confirmacao = await Swal.fire({
    icon: "warning",
    title: "Excluir agendamento?",
    text: "Essa ação não pode ser desfeita.",
    showCancelButton: true,
    confirmButtonText: "Excluir",
    cancelButtonText: "Cancelar"
  });

    if (!confirmacao.isConfirmed) return;

     const  url = API + '/agendamentos/' + id;

  try {
        const res = await fetch(url, { method: 'DELETE' });

       if (!res.ok) {
        let errorMessage = 'Erro ao excluir agendamento';

          try {
            const err = await res.json();
            errorMessage = err.message || errorMessage;
          } catch (e) {
            try {
              const text = await res.text();
              errorMessage = text || errorMessage;
            } catch (e2) {}
          }

        throw new Error(errorMessage);
      }

      Swal.fire({
        icon: "success",
        title: "Sucesso",
        text: "Agendamento excluída com sucesso"
      });

     carregarAgendamentosDaViagem();

  } catch (err) {
    console.error(err);

    Swal.fire({
      icon: "error",
      title: "Erro",
      text: err.message || "Erro ao excluir viagem"
    });
  }

 }

  