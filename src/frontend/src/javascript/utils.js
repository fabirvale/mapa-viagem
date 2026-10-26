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
  else if (s === 'EM_ANDAMENTO') classe += 'badge-emandamento';
  else if (s === 'FINALIZADA') classe += 'badge-finalizada';
  else if (s === 'CANCELADA') classe += 'badge-cancelada'; 
  return '<span class="' + classe + '">' + s + '</span>';
}


function atualizarBadgePainel(status) {
  var badge = document.getElementById('statusBadge');
  if (!status) {
    badge.textContent = '-';
    badge.className = 'badge';
    return;
  }
  
  var s = status.toUpperCase();
  badge.textContent = s;
  var classe = 'badge ';

  if (s === 'AGENDADA') classe += 'badge-agendada';
  else if (s === 'EM_ANDAMENTO') classe += 'badge-emandamento';
  else if (s === 'CANCELADA') classe += 'badge-cancelada'; 
  else if (s === 'FINALIZADA') classe += 'badge-finalizada';

  badge.className = classe;
}

function atualizarStatusAgendamento(status) {
  var s = (status || '').toLowerCase();
  if (s.includes('cancel')) return 'status-cancelado';
  if (s.includes('conclu')) return 'status-concluido';
  if (s.includes('pendente')) return 'status-pendente';
  return 'status-agendado';
}
function formatarMoeda(valor) {
    return (valor || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

// "COMPLETA_COM_PERNOITE" -> "Completa com pernoite"
function rotuloTipoDiaria(tipo) {
    if (!tipo) return '-';
    var t = tipo.replace(/_/g, ' ').toLowerCase();
    return t.charAt(0).toUpperCase() + t.slice(1);
}

// "2026-10-13T08:00" -> "13/10 08:00"
function formatarDataHoraCurta(iso) {
    if (!iso) return '-';
    var p = iso.split('T');
    var d = p[0].split('-');
    return d[2] + '/' + d[1] + ' ' + p[1].substring(0, 5);
}
