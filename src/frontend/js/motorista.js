//==========Adicionar motorista============
function carregarMotoristas() {
 let selectMotorista = document.getElementById('motoristaId');

  fetch(API + '/motoristas')
    .then(res => res.json())
    .then(motoristas => {

      selectMotorista.innerHTML =
        '<option value="">Escolha o motorista</option>';

      motoristas.forEach(function(motorista) {

        let option =
          document.createElement('option');

        option.value = motorista.id;
        option.textContent = motorista.nome;

        selectMotorista.appendChild(option);
      });
    })
    .catch(function(err) {

      console.error(err);

      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar motoristas."
      });
    });
}

//Expõe a função no window para que possa ser chamada a partir do HTML ou de outros scripts
window.carregarMotoristas = carregarMotoristas;
       