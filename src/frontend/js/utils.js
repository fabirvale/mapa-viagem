//========Preencher contexto da viagem no modal de veículo e motorista================
function preencherInfoViagem(idElemento, tipo) {
  const info = document.getElementById(idElemento);

  if (!viagemSelecionada || !info) return;

  info.innerHTML = `
    <strong>${viagemSelecionada.cidadeOrigem} → ${viagemSelecionada.cidadeDestino}</strong><br>
     Data: ${formatarData(viagemSelecionada.dataViagem)}
     Hora: ${viagemSelecionada.horaPrevista || '-'}<br>
     ${tipo === 'veiculo' 
        ? `Motorista: ${viagemSelecionada.motoristaNome || 'Não definido'}` 
        : `Veículo: ${viagemSelecionada.veiculoModelo || 'Não definido'}`}
   `;
}

function formatarData(data) {
  if (!data) return '-';
  var partes = data.split('-');
  if (partes.length === 3) {
    return partes[2] + '/' + partes[1] + '/' + partes[0];
  }
  return data;
}

function badgeStatus(status) {
  if (!status) return '-';
  var classe = 'badge ';
  var s = status.toUpperCase();
  if (s === 'AGENDADA') classe += 'badge-agendada';
  else if (s === 'INICIADA') classe += 'badge-iniciada';
  else if (s === 'FINALIZADA') classe += 'badge-finalizada';
  return '<span class="' + classe + '">' + s + '</span>';
}

