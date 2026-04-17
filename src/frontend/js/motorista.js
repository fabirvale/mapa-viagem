//==========Adicionar motorista============
function adicionarMotorista() {
  let modalMotoristaOverlay = document.getElementById('modalMotoristaOverlay');
  let selectMotorista = document.getElementById('selectMotorista');
  
  if (!viagemSelecionada) {
    alert("Selecione uma viagem primeiro");
    return;
  }
  
  fetch(API + '/motoristas')
    .then(function(res) {
      return res.json();
    })
    .then(function(motoristas) {
      selectMotorista.innerHTML = '<option value="">Escolha o motorista</option>';

      motoristas.forEach(function(v) {
        let option = document.createElement('option');
        option.value = v.id;
        option.textContent = `${v.nome}`;
        selectMotorista.appendChild(option);
      });

      // se já existe motorista associado, seleciona automaticamente
      if (viagemSelecionada.motoristaId) {
        selectMotorista.value = viagemSelecionada.motoristaId;
      }

      modalMotoristaOverlay.style.display = 'flex';
      preencherInfoViagem('infoViagemMotorista');
    })
    .catch(function(err) {
      console.error('Erro ao carregar mtoristas:', err);
       Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar motoristas."
      });
    });
}

//===============Salvar motorista=======================
function salvarModalMotorista() {
 
  if (!viagemSelecionada) {
    Swal.fire({
      icon: "warning",
      title: "Atenção",
      text: "Selecione uma viagem primeiro"
    });
    return;
  }
  const mensagemSucesso = viagemSelecionada.motoristaId ? "Motorista alterado com sucesso" : "Motorista adicionado com sucesso";

  let selectMotorista = document.getElementById('selectMotorista'); 
  const motoristaId = selectMotorista.value
   if (!motoristaId) {
    alert("Selecione um veículo");
    return;
  }

  fetch(`http://localhost:8080/viagens/${viagemSelecionada.id}/motorista`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ motoristaId: Number(motoristaId) })
  })
  .then(res => {
    //if (!res.ok) throw new Error("Erro ao adicionar motorista");
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
    fecharModalMotorista();

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
function fecharModalMotorista() {
   let modalMotoristaOverlay = document.getElementById('modalMotoristaOverlay');
   modalMotoristaOverlay.style.display = 'none';
}