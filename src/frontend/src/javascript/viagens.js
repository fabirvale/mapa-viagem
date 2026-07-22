var API = 'http://localhost:8080';

// Elementos
var corpoTabela = document.getElementById('corpoTabela');
var painelDetalhe = document.getElementById('details-panel');
var btnNovaViagem = document.getElementById('btnNovaViagem');
var formCancelarViagem = document.getElementById('formCancelarViagem');
var modalOverlay = document.getElementById("modalOverlay");
var modalCancelarOverlay = document.getElementById("modalCancelarOverlay");
var formViagem = document.getElementById("formViagem");
var btnFecharModal = document.getElementById("btnFecharModal");
var viagemSelecionada = null;
var viagemIdParaCancelar = null;
var modoModal = null; // "novo", "editar" ou "duplicar"

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
          '<td class="acoes">' +
            '<button class="btn-acoes" onclick="toggleMenu(this)">' +
                '<i class="fa-solid fa-ellipsis-vertical"></i>' +
            '</button>' +

            '<div class="menu-acoes">' +
                '<button onclick="editarViagem(' + v.id + ')">' +
                    '<i class="fa-solid fa-pen"></i> Editar' +
                '</button>' +
        
                '<button onclick="duplicarViagem(' + v.id + ')">' +
                    '<i class="fa-solid fa-play"></i> Duplicar' +
                '</button>' +

                '<button onclick="excluirViagem(' + v.id + ')">' +
                    '<i class="fa-solid fa-trash"></i> Excluir' +
                '</button>' +

                '<button onclick="cancelarViagem(' + v.id + ', \'' + v.status + '\')">' +
                    '<i class="fa-solid fa-calendar-xmark"></i> Cancelar' +
                '</button>' +
           '</div>' +
        '</td>';
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

// ========== MENU DE AÇÕES - abrir/fechar ==========
function toggleMenu(botao) {

    const menu = botao.nextElementSibling;
    const estaAberto = menu.style.display === "block";

    // fecha todos os menus
    document.querySelectorAll(".menu-acoes").forEach(m => {
        m.style.display = "none";
    });

    if (estaAberto) return; // clicou de novo no mesmo botão -> só fecha

    // calcula posição do botão na tela
    const rect = botao.getBoundingClientRect();

    menu.style.top = (rect.bottom + 4) + "px";
    menu.style.left = (rect.right - 180) + "px"; // 180 = largura do menu
    menu.style.display = "block";
}

//========= FECHAR MENU DE AÇÕES AO CLICAR FORA ==========
document.addEventListener("click", function(e){

    if(!e.target.closest(".acoes")){
        document.querySelectorAll(".menu-acoes")
            .forEach(menu => menu.style.display = "none");
    }

});

//========= FECHAR MENU DE AÇÕES AO ROLAR A PÁGINA =========
document.addEventListener("scroll", function(){
    document.querySelectorAll(".menu-acoes")
        .forEach(menu => menu.style.display = "none");
}, true); // true = captura scroll em qualquer container, não só na window

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

// ========== MODAL - CRIAR VIAGEM ==========

if (btnNovaViagem) {
     
    document.getElementById("tituloModal").textContent = "Nova Viagem";
    document.getElementById("btnSalvarViagem").textContent = "Cadastrar";
    modoModal = "novo";

    btnNovaViagem.addEventListener('click', async function () {
    viagemSelecionada = null
    
    // limpa formulário (ESSENCIAL)
    formViagem.reset();

    modalOverlay.style.display = 'flex';
    carregarMotoristas();
    carregarVeiculos();
    await carregarEstados();
    await carregarCidades();
    await carregarStatus();
   
  });
}

// ========== MODAL - FECHAR ==========
if (btnFecharModal) {
  btnFecharModal.addEventListener('click', function () {
    fecharModal(modalOverlay);
  });
}

if (btnFecharModalCancelar) {
    btnFecharModalCancelar.addEventListener('click', function () {
        fecharModal(modalCancelarOverlay);
    });
}

function fecharModal(modal) {
    modal.style.display = 'none';
}

//========== MODAL(CADASTRO/EDIÇÃO) CANCELAR OPERAÇÃO==========
function fecharModalViagem() {
    formViagem.reset();
    viagemSelecionada = null;
    fecharModal(modalOverlay);
}

//========== MODAL - CANCELAR VIAGEM  ==========
function fecharModalCancelarViagem() {
    formViagem.reset();
    viagemSelecionada = null;
     fecharModal(modalCancelarOverlay);
}

//========== MODAL - FECHAR COM ESC ==========
document.addEventListener("keydown", function (e) {

    const modal = document.getElementById("modalOverlay");

    if (e.key === "Escape" && modal.style.display === "flex") {
        fecharModalViagem();
    }

});

//========== MODAL - SALVAR VIAGEM ==========
async function salvarViagem(dados, url, method) {
    const res = await fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
    });

    if (!res.ok) {
        let errorMessage = "Erro ao salvar viagem";

        try {
            const err = await res.json();
            errorMessage = err.message || errorMessage;
        } catch {
            try {
                errorMessage = await res.text();
            } catch {}
        }

        throw new Error(errorMessage);
    }

    return res;
}

//=========== MODAL - METODO POST OU PUT VIAGEM ====
formViagem.addEventListener('submit', async function (e) {

    e.preventDefault();

    var dados = {
        observacao: document.getElementById('observacao').value,
        estadoOrigem: document.getElementById('estadoOrigem').value,
        cidadeOrigem: document.getElementById('cidadeOrigem').value,
        cidadeDestino: document.getElementById('cidadeDestino').value,
        estadoDestino: document.getElementById('estadoDestino').value,
        dataViagem: document.getElementById('dataViagem').value,
        horaPrevista: document.getElementById('horaPrevista').value,
        motoristaId: document.getElementById('motoristaId').value,
        veiculoId: document.getElementById('veiculoId').value,
        status: document.getElementById('statusViagem').value,
        ignorarDuplicidade: false
    };

    let url = API + '/viagens';
    let method = 'POST';

    if (viagemSelecionada) {
        url = API + '/viagens/' + viagemSelecionada;
        method = 'PUT';
    }
    else {
         delete dados.id //garantia extra no CREATE
        }

    try {
        console.log("Enviando dados para a API:", dados, "URL:", url, "Method:", method);
        await salvarViagem(dados, url, method);

    } catch (err) {

        if (err.message === "Já existe uma viagem com esses dados.") {

            const resposta = await Swal.fire({
                icon: "warning",
                title: "Viagem duplicada",
                text: "Já existe uma viagem com a mesma origem, destino, data, horário e status. Deseja criar outra viagem mesmo assim?",
                showCancelButton: true,
                confirmButtonText: "Sim",
                cancelButtonText: "Não"
            });

            if (resposta.isConfirmed) {

                dados.ignorarDuplicidade = true;

                await salvarViagem(dados, url, method);

            } else {
                return;
            }

        } else {

            Swal.fire({
                icon: "error",
                title: "Erro",
                text: err.message
            });

            return;
        }
    }

    formViagem.reset();
    modalOverlay.style.display = 'none';
    viagemSelecionada = null;

    carregarViagens();

    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });

    let mensagem = "";

    if (modoModal === "novo") {
        mensagem = "Viagem cadastrada com sucesso!";
    } else if (modoModal === "editar") {
        mensagem = "Viagem atualizada com sucesso!";
    } else {
        mensagem = "Viagem duplicada com sucesso!";
    }

    Swal.fire({
        icon: "success",
        title: "Sucesso",
        text: mensagem
    });

});



//======= Carregar estados no select do formulário ========
async function carregarEstados() {
    try {
        const response = await fetch(
            "https://servicodados.ibge.gov.br/api/v1/localidades/estados"
        );

        const estados = await response.json();

        estados.sort((a, b) => a.nome.localeCompare(b.nome));

        estados.forEach(estado => {
            const option = document.createElement("option");

            option.value = estado.sigla;
            option.textContent = estado.nome;

            //estadoOrigem.appendChild(option);
            estadoOrigem.appendChild(option.cloneNode(true));
            estadoDestino.appendChild(option);
        });

    } catch (error) {
        console.error("Erro ao carregar estados:", error);
    }
}

//======= Carregar cidades =============
estadoOrigem.addEventListener("change", function () {

    const siglaEstado = estadoOrigem.value;

    if (!siglaEstado) {
        cidadeOrigem.innerHTML =
            '<option value="">Selecione uma cidade</option>';
        return;
    }

    carregarCidades(siglaEstado, cidadeOrigem);
});

estadoDestino.addEventListener("change", function () {

    const siglaEstado = estadoDestino.value;

    if (!siglaEstado) {
        cidadeDestino.innerHTML =
            '<option value="">Selecione uma cidade</option>';
        return;
    }

    carregarCidades(siglaEstado, cidadeDestino);
});

async function carregarCidades(siglaEstado, selectCidade) {

    try {

        selectCidade.innerHTML =
               '<option value="">Selecione uma cidade</option>';

        const response = await fetch(
            `https://servicodados.ibge.gov.br/api/v1/localidades/estados/${siglaEstado}/municipios`
        );

        const cidades = await response.json();

        cidades.forEach(cidade => {

            const option = document.createElement("option");

            option.value = cidade.nome;
            option.textContent = cidade.nome;

            selectCidade.appendChild(option);
        });

    } catch (error) {
        console.error("Erro ao carregar cidades:", error);
    }
}

//======= Carregar status no select do formulário ========
 async function carregarStatus() {

    const res = await fetch(API + "/viagens/status");
    const status = await res.json();

    const select = document.getElementById("statusViagem");
    select.innerHTML = "";

    status.forEach(s => {
        const option = document.createElement("option");
        option.value = s;
        option.textContent = s.replace("_", " ");
        select.appendChild(option);
    });
    select.disabled = true; // desabilita o select após carregar os status
}


//========== MODAL - CARREGAR VIAGEM NO FORMULÁRIO PARA EDITAR/DUPLICAR ==========
async function carregarViagemNoModal(id, modo) {

    // Fecha qualquer menu aberto
    document.querySelectorAll(".menu-acoes").forEach(menu => {
        menu.style.display = "none";
    });
    
     const res = await fetch(API + '/viagens/' + id);
     const v = await res.json();

     // Regra de negócio para edição
     if (modo === "editar" && v.status !== "AGENDADA") {

        Swal.fire({
            icon: "warning",
            title: "Viagem não pode ser editada",
            text: "Somente viagens com status AGENDADA podem ser editadas."
        });

        return;
     }
      
      
      document.getElementById('observacao').value = v.observacao || '';

      await carregarEstados();
      document.getElementById('estadoOrigem').value = v.estadoOrigem || '';
      document.getElementById('estadoDestino').value = v.estadoDestino || '';
     
      await carregarCidades(v.estadoOrigem, document.getElementById('cidadeOrigem'));
      await carregarCidades(v.estadoDestino, document.getElementById('cidadeDestino'));
      document.getElementById('cidadeOrigem').value = v.cidadeOrigem || '';
      document.getElementById('cidadeDestino').value = v.cidadeDestino || '';
  
      await carregarMotoristas();
      await carregarVeiculos();
      document.getElementById('motoristaId').value = v.motoristaId || '';
      document.getElementById('veiculoId').value = v.veiculoId || '';

      document.getElementById('dataViagem').value = v.dataViagem || '';
      document.getElementById('horaPrevista').value = v.horaPrevista || '';
      
      await carregarStatus();
      document.getElementById('statusViagem').value = v.status || '';
      document.getElementById('statusViagem').disabled = false; 

      if (modo === "editar") {
        document.getElementById("tituloModal").textContent = "Editar Viagem";
        document.getElementById("btnSalvarViagem").textContent = "Salvar Alterações";
        viagemSelecionada = v.id;
      } 
      else {
        document.getElementById("tituloModal").textContent = "Duplicar Viagem";
        document.getElementById("btnSalvarViagem").textContent = "Duplicar";
        viagemSelecionada = null; // força um POST
      }
      modalOverlay.style.display = 'flex';
  
}

async function editarViagem(id) {
    modoModal = "editar";
    await carregarViagemNoModal(id, "editar");
}

async function duplicarViagem(id) {
    modoModal = "duplicar";
   await carregarViagemNoModal(id, "duplicar");
}

async function excluirViagem(id) {

  // fecha o menu de ações, se estiver aberto
  document.querySelectorAll(".menu-acoes").forEach(menu => {
      menu.style.display = "none";
  });


  const confirmacao = await Swal.fire({
    icon: "warning",
    title: "Excluir viagem?",
    text: "Essa ação não pode ser desfeita.",
    showCancelButton: true,
    confirmButtonText: "Excluir",
    cancelButtonText: "Cancelar"
  });

  if (!confirmacao.isConfirmed) return;

  const  url = API + '/viagens/' + id;

  try {
        const res = await fetch(url, { method: 'DELETE' });

       if (!res.ok) {
        let errorMessage = 'Erro ao excluir viagem';

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
        text: "Viagem excluída com sucesso"
      });

    carregarViagens(); // atualiza a tabela

  } catch (err) {
    console.error(err);

    Swal.fire({
      icon: "error",
      title: "Erro",
      text: err.message || "Erro ao excluir viagem"
    });
  }
}

//========== MODAL - CANCELAR VIAGEM ==========
 function cancelarViagem(id, status) {

    if (status !== 'AGENDADA') {

        Swal.fire({
            icon: 'warning',
            title: 'Não é possível cancelar',
            text: 'Somente viagens agendadas podem ser canceladas.'
        });

        return;
    }
    
    // fecha o menu de ações, se estiver aberto para não aparecer sobre o modal
    document.querySelectorAll(".menu-acoes").forEach(menu => {
       menu.style.display = "none";
    });

    viagemIdParaCancelar = id;
   
    formCancelarViagem.reset();
    document.getElementById("motivoCancelamento").focus();
    modalCancelarOverlay.style.display = 'flex';

}   

// ========== MODAL - CANCELAR VIAGEM ==========

if (formCancelarViagem) {

    console.log("FORM ENCONTRADO:", formCancelarViagem);

    formCancelarViagem.addEventListener('submit', async function (e) {

        console.log("SUBMIT FOI DISPARADO");

        e.preventDefault();

        const dados = {
            observacao: document.getElementById('motivoCancelamento').value
        };

        const url = API + '/viagens/' + viagemIdParaCancelar + '/cancelar';

        console.log("ID DA VIAGEM:", viagemIdParaCancelar);
        console.log("URL:", url);
        console.log("DADOS:", dados);

        try {

            const res = await fetch(url, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dados)
            });

            console.log("STATUS DA RESPOSTA:", res.status);

            if (!res.ok) {

                let errorMessage = "Erro ao cancelar viagem";

                try {
                    const err = await res.json();
                    errorMessage = err.message || errorMessage;

                } catch {
                    try {
                        errorMessage = await res.text();
                    } catch {}
                }

                throw new Error(errorMessage);
            }

            fecharModal(modalCancelarOverlay);

            formCancelarViagem.reset();

            carregarViagens();

            Swal.fire({
                icon: "success",
                title: "Sucesso",
                text: "Viagem cancelada com sucesso!"
            });

        } catch (err) {

            console.error("ERRO AO CANCELAR:", err);

            Swal.fire({
                icon: "error",
                title: "Erro",
                text: err.message || "Erro ao cancelar viagem"
            });
        }
    });
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
