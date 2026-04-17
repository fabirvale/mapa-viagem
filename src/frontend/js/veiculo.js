//=======Adicionar Veiculo============
function adicionarVeiculo() {
  let modalVeiculoOverlay = document.getElementById('modalVeiculoOverlay');
  let selectVeiculo = document.getElementById('selectVeiculo');
  
  if (!viagemSelecionada) {
    alert("Selecione uma viagem primeiro");
    return;
  }
  
  fetch(API + '/veiculos')
    .then(function(res) {
      return res.json();
    })
    .then(function(veiculos) {
      selectVeiculo.innerHTML = '<option value="">Escolha o veículo</option>';

      veiculos.forEach(function(v) {
        let option = document.createElement('option');
        option.value = v.id;
        option.textContent = `${v.modelo} - ${v.placa}`;
        selectVeiculo.appendChild(option);
      });

      // se já existe veículo associado, seleciona automaticamente
      if (viagemSelecionada.veiculoId) {
        selectVeiculo.value = viagemSelecionada.veiculoId;
      }

      modalVeiculoOverlay.style.display = 'flex';
     preencherInfoViagem('infoViagemVeiculo', 'veiculo');
    })
    .catch(function(err) {
      console.error('Erro ao carregar veículos:', err);
       Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar veículos."
      });
    });
}

//==========Salvar veículo============
function salvarModalVeiculo() {
 
  if (!viagemSelecionada) {
    Swal.fire({
      icon: "warning",
      title: "Atenção",
      text: "Selecione uma viagem primeiro"
    });
    return;
  }
  const mensagemSucesso = viagemSelecionada.veiculoId ? "Veículo alterado com sucesso" : "Veículo adicionado com sucesso";

  let selectVeiculo = document.getElementById('selectVeiculo'); 
  const veiculoId = selectVeiculo.value
   if (!veiculoId) {
    alert("Selecione um veículo");
    return;
  }

  fetch(`http://localhost:8080/viagens/${viagemSelecionada.id}/veiculo`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ veiculoId: Number(veiculoId) })
  })
  .then(res => {
    //if (!res.ok) throw new Error("Erro ao adicionar veiculo");
    //return res.json();
    if (!res.ok) throw new Error("Erro na API");
    return res.text(); 
  })
  .then(() => {
    return fetch(`http://localhost:8080/viagens/${viagemSelecionada.id}`);
  })
  .then(res => res.json())
  .then(viagemAtualizada => {
    viagemSelecionada = viagemAtualizada; 
    mostrarDetalhe(viagemSelecionada);
    carregarViagens();
    fecharModalVeiculo();

    //SweetAlert2
    Swal.fire({
    icon: "success",
    title: "Sucesso",
    text: mensagemSucesso
});
  })
  .catch(err => {
    console.error(err);

    //SweetAlert2
    Swal.fire({
    icon: "error",
    title: "Erro",
    text: "Erro ao adicionar veículo"
});
  });

}
//========Fechar modal veiculo================
function fecharModalVeiculo() {
   let modalVeiculoOverlay = document.getElementById('modalVeiculoOverlay');
   modalVeiculoOverlay.style.display = 'none';
}