//=======Adicionar Veiculo============
function carregarVeiculos() {
  let selectVeiculo = document.getElementById('veiculoId');
   
  return fetch(API + '/veiculos')
    .then(function(res) {
      return res.json();
    })
    .then(function(veiculos) {
      selectVeiculo.innerHTML = '<option value="">Escolha o veículo</option>';

      veiculos.forEach(function(v) {
        let option = document.createElement('option');
        option.value = v.id;
        option.textContent = `${v.modelo} - ${v.placa} - (${v.capacidadePassageiros} lugares)`;
        selectVeiculo.appendChild(option);
      });
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
//Expõe a função no window para que possa ser chamada a partir do HTML ou de outros scripts
window.carregarVeiculos = carregarVeiculos;