# Plano de Testes Funcionais

## Projeto

Mapa de Viagem

## Objetivo

Este documento descreve os cenários de testes funcionais do sistema **Mapa de Viagem**.

Seu objetivo é validar as funcionalidades implementadas e garantir que
as regras de negócio estejam sendo respeitadas antes da disponibilização
de uma nova versão.

------------------------------------------------------------------------

## Premissas

Este plano de testes considera a versão atual da aplicação e sua
evolução futura.

Está previsto para versões posteriores:

-   Autenticação com Spring Security;
-   Controle de acesso por perfis de usuário;
-   Auditoria das operações realizadas.

Quando essas funcionalidades forem implementadas, este documento será
atualizado.

------------------------------------------------------------------------

# Módulo: Viagem

## Funcionalidades

-   Cadastrar viagem
-   Editar viagem
-   Duplicar viagem
-   Iniciar viagem
-   Finalizar viagem (em desenvolvimento)
-   Cancelar viagem
-   Excluir viagem

------------------------------------------------------------------------

# Funcionalidade: Cadastrar Viagem

## Regras de Negócio

### RNV001

É permitido cadastrar viagens cuja cidade de origem seja igual à cidade
de destino.

------------------------------------------------------------------------

### RNV002

Toda nova viagem deve ser cadastrada com status **AGENDADA**.

------------------------------------------------------------------------

### RNV003

Caso já exista uma viagem com a mesma cidade de origem, cidade de
destino, data da viagem, horário previsto e status, o sistema deverá
solicitar confirmação antes de concluir o cadastro.

------------------------------------------------------------------------

### RNV004

Caso o usuário confirme o cadastro da viagem duplicada, o sistema deverá
permitir o cadastramento normalmente.

------------------------------------------------------------------------

### RNV005

É permitido cadastrar uma viagem sem motorista.

------------------------------------------------------------------------

### RNV006

É permitido cadastrar uma viagem sem veículo.

------------------------------------------------------------------------

## Cenários de Teste

### CTV001 -- Cadastrar viagem válida

**Objetivo**

Verificar se o usuário consegue cadastrar uma viagem válida.

**Pré-condições**

-   Aplicação em execução.
-   Usuário autenticado (quando Spring Security estiver implementado).

**Passos**

1.  Acessar o Dashboard.
2.  Clicar em **Nova Viagem**.
3.  Informar os campos obrigatórios.
4.  Clicar em **Cadastrar**.

**Resultado esperado**

-   Viagem cadastrada com sucesso.
-   Status inicial AGENDADA.
-   Viagem exibida na listagem.

------------------------------------------------------------------------

### CTV002 -- Cadastrar viagem sem motorista

**Objetivo**

Verificar se o sistema permite cadastrar uma viagem sem motorista.

**Pré-condições**

Aplicação em execução.

**Passos**

1.  Abrir Nova Viagem.
2.  Informar os dados obrigatórios.
3.  Não selecionar motorista.
4.  Salvar.

**Resultado esperado**

Viagem cadastrada normalmente.

------------------------------------------------------------------------

### CTV003 -- Cadastrar viagem sem veículo

**Objetivo**

Verificar se o sistema permite cadastrar uma viagem sem veículo.

**Pré-condições**

Aplicação em execução.

**Passos**

1.  Abrir Nova Viagem.
2.  Informar os dados obrigatórios.
3.  Não selecionar veículo.
4.  Salvar.

**Resultado esperado**

Viagem cadastrada normalmente.

------------------------------------------------------------------------

### CTV004 -- Cadastrar viagem duplicada

**Objetivo**

Validar o tratamento de duplicidade.

**Pré-condições**

Existir uma viagem com mesma origem, destino, data, horário e status.

**Passos**

1.  Cadastrar nova viagem com os mesmos dados.
2.  Clicar em Cadastrar.

**Resultado esperado**

O sistema solicita confirmação da duplicidade.

------------------------------------------------------------------------

### CTV005 -- Confirmar cadastro de viagem duplicada

**Objetivo**

Verificar se o cadastro é permitido após confirmação.

**Pré-condições**

Executar o cenário CTV004.

**Passos**

1.  Confirmar a duplicidade.

**Resultado esperado**

Viagem cadastrada normalmente.

------------------------------------------------------------------------

# Funcionalidade: Editar Viagem

## Regras de Negócio

### RNV007

É permitido editar somente viagens com status **AGENDADA**.

------------------------------------------------------------------------

### RNV008

É permitido alterar durante a edição:

-   Observação;
-   Cidade de origem;
-   Cidade de destino;
-   Data da viagem;
-   Horário previsto;
-   Motorista;
-   Veículo.

------------------------------------------------------------------------

### RNV009

Não é permitido alterar manualmente o status da viagem durante a edição.
A alteração deverá ocorrer apenas pelas funcionalidades específicas do
sistema.

------------------------------------------------------------------------

### RNV010 — Alteração da data da viagem

É permitido alterar a data de uma viagem com status AGENDADA, desde que, quando existirem agendamentos vinculados, o usuário confirme também a alteração da data desses agendamentos.

------------------------------------------------------------------------

### RNV011 — Alteração da data dos agendamentos

Ao alterar a data de uma viagem que possua agendamentos vinculados, o sistema deverá solicitar confirmação ao usuário para alterar também a data dos agendamentos.

Se confirmado: a nova data será aplicada à viagem e aos agendamentos vinculados.
Se não confirmado: a alteração será cancelada e a viagem permanecerá com a data original.

------------------------------------------------------------------------

### RNV012

Enquanto a viagem estiver com status **AGENDADA**, o usuário poderá alterar ou remover o motorista e o veículo vinculados à viagem.

A viagem poderá permanecer sem motorista ou veículo enquanto estiver agendada.

A obrigatoriedade de motorista e veículo será verificada somente no momento de iniciar a viagem.

---

## Cenários de Teste

### CTV006 -- Editar viagem válida

**Objetivo**

Verificar se uma viagem AGENDADA pode ser editada.

**Pré-condições**

Existir uma viagem AGENDADA.

**Passos**

1.  Selecionar a viagem.
2.  Clicar em Editar.
3.  Alterar um ou mais campos permitidos.
4.  Salvar.

**Resultado esperado**

A viagem deverá ser atualizada com sucesso.

------------------------------------------------------------------------

### CTV007 -- Editar viagem EM_ANDAMENTO

**Objetivo**

Verificar que viagens em andamento não podem ser editadas.

**Pré-condições**

Existir uma viagem EM_ANDAMENTO.

**Passos**

1.  Selecionar a viagem.
2.  Tentar editar.

**Resultado esperado**

O sistema deverá impedir a edição.

------------------------------------------------------------------------

### CTV008 -- Editar viagem FINALIZADA

**Objetivo**

Verificar que viagens finalizadas não podem ser editadas.

**Pré-condições**

Existir uma viagem FINALIZADA.

**Passos**

1.  Selecionar a viagem.
2.  Tentar editar.

**Resultado esperado**

O sistema deverá impedir a edição.

------------------------------------------------------------------------

### CTV009 -- Alterar data sem agendamentos

**Objetivo**

Verificar a alteração da data quando não existem agendamentos.

**Pré-condições**

Viagem sem agendamentos vinculados.

**Passos**

1.  Editar a viagem.
2.  Alterar a data.
3.  Salvar.

**Resultado esperado**

A data deverá ser alterada.

------------------------------------------------------------------------

### CTV010 -- Alterar data com agendamentos

**Objetivo**

Verificar o comportamento da alteração da data de uma viagem futura com agendamentos vinculados.

**Pré-condições**

Viagem com agendamentos vinculados.Existir uma viagem com status AGENDADA e data futura.
Existir pelo menos um agendamento vinculado à viagem.
A data do agendamento ser igual à data atual da viagem.

**Passos**

1. Editar a viagem.
2. Alterar a data da viagem para uma nova data futura.
3. Salvar a alteração.
4. Na mensagem de confirmação, selecionar Sim, autorizando a alteração da data dos agendamentos.

**Resultado esperado**

Alterar a data da viagem para a nova data.
Alterar a data dos agendamentos vinculados para a mesma nova data.
Manter os agendamentos vinculados à viagem.
Informar que a alteração foi realizada com sucesso.

##CTV011 — Recusar alteração da data dos agendamentos

**Objetivo**

Verificar o comportamento quando o usuário não autoriza a alteração dos agendamentos vinculados.

**Pré-condições**

Existir uma viagem com status AGENDADA e data futura.
Existir pelo menos um agendamento vinculado à viagem.

**Passos**

1. Editar a viagem.
2. Alterar a data da viagem para uma nova data futura.
3. Salvar a alteração.
4. Na mensagem de confirmação, selecionar Não.

**Resultado esperado**

O sistema deverá cancelar a alteração, mantendo:

a data original da viagem;
a data original dos agendamentos;
os agendamentos vinculados à viagem.


## CTV012 - Remover motorista ou veículo de viagem agendada

**Objetivo**

Verificar se o usuário consegue remover o motorista ou veículo de uma viagem com status **AGENDADA**.

**Pré-condições**

- Existir uma viagem com status **AGENDADA**.
- A viagem possuir motorista e veículo vinculados.

**Passos**

1. Selecionar a viagem.
2. Clicar em **Editar**.
3. Selecionar a opção **Escolher motorista**.
4. Selecionar a opção **Escolher veículo**, se necessário.
5. Clicar em **Salvar**.

**Resultado esperado**

- A viagem deverá ser atualizada com sucesso.
- O motorista e/ou veículo removido deverá deixar de estar vinculado à viagem.
- A viagem deverá permanecer com status **AGENDADA**.
- A viagem poderá permanecer sem motorista ou veículo enquanto estiver agendada.

---

**Resultado:** ✅ Aprovado

------------------------------------------------------------------------

# Funcionalidade: Duplicar Viagem

## Regras de Negócio

### RNV013

A funcionalidade de duplicação de viagem deverá seguir as mesmas regras definidas para o cadastro de viagem (RNV001 a RNV006).

---

### RNV014

Ao duplicar uma viagem, o sistema deverá abrir um novo cadastro preenchido com os dados da viagem selecionada, permitindo ao usuário alterar as informações antes da confirmação do cadastro.


# Cenários de Teste

## CTV013 - Duplicar viagem

### Objetivo

Verificar se o sistema permite criar uma nova viagem utilizando os dados de uma viagem existente.

### Pré-condições

- Aplicação em execução.
- Existir uma viagem cadastrada.

### Passos

1. Selecionar uma viagem.
2. Clicar em **Duplicar**.
3. Verificar se o formulário foi aberto com os dados preenchidos.
4. Alterar, se desejar, as informações da nova viagem.
5. Clicar em **Cadastrar**.

### Resultado esperado

- O formulário deverá ser aberto preenchido com os dados da viagem original.
- O usuário poderá alterar qualquer informação permitida.
- O sistema deverá cadastrar uma nova viagem com um novo identificador (ID).
- A viagem original deverá permanecer inalterada.

## CTV014 - Duplicar viagem sem motorista

### Objetivo

Verificar se o sistema permite duplicar uma viagem sem motorista.

### Pré-condições

Existir uma viagem cadastrada sem motorista.

### Passos

1. Selecionar a viagem.
2. Clicar em **Duplicar**.
3. Confirmar a operação.

### Resultado esperado

A nova viagem deverá ser cadastrada normalmente.

---

# Funcionalidade: Iniciar Viagem

## Regras de Negócio

### RNV017

É permitido iniciar somente viagens com status **AGENDADA**.

---

### RNV018

Não é permitido iniciar uma viagem que não possua agendamentos vinculados.

---

### RNV019

Não é permitido iniciar uma viagem sem motorista vinculado.

---

### RNV020

Não é permitido iniciar uma viagem sem veículo vinculado.

---

### RNV021

Ao iniciar uma viagem, o sistema deverá alterar seu status para **EM_ANDAMENTO**.

### RNV022

Não é permitido iniciar uma viagem cuja data seja posterior à data atual.

---

# Cenários de Teste

## CTV018 - Iniciar viagem válida

### Objetivo

Verificar se o sistema permite iniciar uma viagem com status **AGENDADA**.

### Pré-condições

- Aplicação em execução.
- Existir uma viagem com status **AGENDADA**.
- A viagem possuir pelo menos um agendamento vinculado.
- A viagem possuir motorista vinculado.
- A viagem possuir veículo vinculado.

### Passos

1. Selecionar uma viagem com status **AGENDADA**.
2. Clicar em **Iniciar Viagem**.

### Resultado esperado

- O sistema deverá alterar o status da viagem para **EM_ANDAMENTO**.
- A viagem deverá permanecer disponível na listagem com o novo status.

---

## CTV019 - Iniciar viagem sem agendamento

### Objetivo

Verificar se o sistema impede iniciar uma viagem sem agendamentos vinculados.

### Pré-condições

- Existir uma viagem com status **AGENDADA**.
- A viagem não possuir agendamentos vinculados.

### Passos

1. Selecionar a viagem.
2. Clicar em **Iniciar Viagem**.

### Resultado esperado

O sistema deverá impedir o início da viagem e apresentar a mensagem informando que não é possível iniciar uma viagem sem agendamentos.

---

## CTV020 - Iniciar viagem sem motorista

### Objetivo

Verificar se o sistema impede iniciar uma viagem sem motorista vinculado.

### Pré-condições

- Existir uma viagem com status **AGENDADA**.
- A viagem possuir agendamento.
- A viagem não possuir motorista.

### Passos

1. Selecionar a viagem.
2. Clicar em **Iniciar Viagem**.

### Resultado esperado

O sistema deverá impedir o início da viagem e apresentar a mensagem informando que não é possível iniciar uma viagem sem motorista.

---

## CTV021 - Iniciar viagem sem veículo

### Objetivo

Verificar se o sistema impede iniciar uma viagem sem veículo vinculado.

### Pré-condições

- Existir uma viagem com status **AGENDADA**.
- A viagem possuir agendamento.
- A viagem possuir motorista.
- A viagem não possuir veículo.

### Passos

1. Selecionar a viagem.
2. Clicar em **Iniciar Viagem**.

### Resultado esperado

O sistema deverá impedir o início da viagem e apresentar a mensagem informando que não é possível iniciar uma viagem sem veículo.

---

## CTV022 - Iniciar viagem com status diferente de AGENDADA

### Objetivo

Verificar se o sistema impede iniciar viagens com status diferente de **AGENDADA**.

### Pré-condições

Existir uma viagem com status:

- EM_ANDAMENTO; ou
- CANCELADA; ou
- FINALIZADA (quando implementada).

### Passos

1. Selecionar a viagem.
2. Clicar em **Iniciar Viagem**.

### Resultado esperado

O sistema deverá impedir o início da viagem e apresentar a mensagem informando que somente viagens com status **AGENDADA** podem ser iniciadas.


### CTV023 - Tentar iniciar viagem com data futura

**Objetivo**

Verificar se o sistema impede o início de uma viagem com data futura.

**Pré-condições**

- Existir uma viagem com status **AGENDADA**.
- A data da viagem ser posterior à data atual.
- A viagem possuir agendamento, motorista e veículo.

**Passos**

1. Selecionar uma viagem com data futura.
2. Clicar em **Iniciar Viagem**.
3. Confirmar a operação.

**Resultado esperado**

O sistema deverá impedir o início da viagem e informar que não é possível iniciar uma viagem com data futura.

# Funcionalidade: Cancelar Viagem

## Regras de Negócio

### RNV022

É permitido cancelar somente viagens com status **AGENDADA**.

---

### RNV023

Ao cancelar uma viagem, o sistema deverá alterar seu status para **CANCELADA**.

---

### RNV024

É permitido registrar uma observação referente ao motivo do cancelamento da viagem.

# Cenários de Teste

## CTV023 - Cancelar viagem válida

### Objetivo

Verificar se o sistema permite cancelar uma viagem com status **AGENDADA**.

### Pré-condições

- Aplicação em execução.
- Existir uma viagem com status **AGENDADA**.

### Passos

1. Selecionar uma viagem com status **AGENDADA**.
2. Clicar em **Cancelar Viagem**.
3. Informar uma observação (opcional).
4. Confirmar o cancelamento.

### Resultado esperado

- O sistema deverá alterar o status da viagem para **CANCELADA**.
- A observação informada deverá ser gravada na viagem.
- A viagem deverá permanecer na listagem com o novo status.

---

## CTV024 - Cancelar viagem EM_ANDAMENTO

### Objetivo

Verificar se o sistema impede o cancelamento de uma viagem com status **EM_ANDAMENTO**.

### Pré-condições

Existir uma viagem com status **EM_ANDAMENTO**.

### Passos

1. Selecionar a viagem.
2. Clicar em **Cancelar Viagem**.

### Resultado esperado

O sistema deverá impedir o cancelamento e apresentar a mensagem informando que somente viagens com status **AGENDADA** podem ser canceladas.

---

## CTV025 - Cancelar viagem CANCELADA

### Objetivo

Verificar se o sistema impede o cancelamento de uma viagem já cancelada.

### Pré-condições

Existir uma viagem com status **CANCELADA**.

### Passos

1. Selecionar a viagem.
2. Clicar em **Cancelar Viagem**.

### Resultado esperado

O sistema deverá impedir o cancelamento e apresentar a mensagem informando que somente viagens com status **AGENDADA** podem ser canceladas.

---

## CTV026 - Cancelar viagem FINALIZADA

### Objetivo

Verificar se o sistema impede o cancelamento de uma viagem com status **FINALIZADA**.

### Pré-condições

Existir uma viagem com status **FINALIZADA** (quando esta funcionalidade estiver implementada).

### Passos

1. Selecionar a viagem.
2. Clicar em **Cancelar Viagem**.

### Resultado esperado

O sistema deverá impedir o cancelamento e apresentar a mensagem informando que somente viagens com status **AGENDADA** podem ser canceladas.


# Funcionalidade: Excluir Viagem

## Regras de Negócio

### RNV025

É permitido excluir somente viagens com status **AGENDADA**.

---

### RNV026

Não é permitido excluir viagens que possuam agendamentos vinculados.

---

### RNV027

Após a exclusão, a viagem deverá ser removida definitivamente do sistema.

# Cenários de Teste

## CTV027 - Excluir viagem válida

### Objetivo

Verificar se o sistema permite excluir uma viagem com status **AGENDADA**.

### Pré-condições

- Aplicação em execução.
- Existir uma viagem com status **AGENDADA**.
- A viagem não possuir agendamentos vinculados.

### Passos

1. Selecionar uma viagem com status **AGENDADA**.
2. Clicar em **Excluir**.
3. Confirmar a exclusão.

### Resultado esperado

- A viagem deverá ser removida do sistema.
- A viagem não deverá mais ser exibida na listagem.

---

## CTV028 - Excluir viagem com agendamentos vinculados

### Objetivo

Verificar se o sistema impede a exclusão de uma viagem que possua agendamentos vinculados.

### Pré-condições

- Existir uma viagem com status **AGENDADA**.
- A viagem possuir pelo menos um agendamento vinculado.

### Passos

1. Selecionar a viagem.
2. Clicar em **Excluir**.
3. Confirmar a exclusão.

### Resultado esperado

O sistema deverá impedir a exclusão e apresentar a mensagem informando que não é possível excluir a viagem porque existem agendamentos vinculados.

---

## CTV029 - Excluir viagem EM_ANDAMENTO

### Objetivo

Verificar se o sistema impede a exclusão de uma viagem com status **EM_ANDAMENTO**.

### Pré-condições

Existir uma viagem com status **EM_ANDAMENTO**.

### Passos

1. Selecionar a viagem.
2. Clicar em **Excluir**.

### Resultado esperado

O sistema deverá impedir a exclusão e apresentar a mensagem informando que somente viagens com status **AGENDADA** podem ser excluídas.

---

## CTV030 - Excluir viagem CANCELADA

### Objetivo

Verificar se o sistema impede a exclusão de uma viagem com status **CANCELADA**.

### Pré-condições

Existir uma viagem com status **CANCELADA**.

### Passos

1. Selecionar a viagem.
2. Clicar em **Excluir**.

### Resultado esperado

O sistema deverá impedir a exclusão e apresentar a mensagem informando que somente viagens com status **AGENDADA** podem ser excluídas.

---

## CTV031 - Excluir viagem FINALIZADA

### Objetivo

Verificar se o sistema impede a exclusão de uma viagem com status **FINALIZADA**.

### Pré-condições

Existir uma viagem com status **FINALIZADA** (quando esta funcionalidade estiver implementada).

### Passos

1. Selecionar a viagem.
2. Clicar em **Excluir**.

### Resultado esperado

O sistema deverá impedir a exclusão e apresentar a mensagem informando que somente viagens com status **AGENDADA** podem ser excluídas.


# Módulo: Agendamento

## Funcionalidades

- Cadastrar agendamento
- Editar agendamento
- Cancelar agendamento
- Excluir agendamento
- Exibir status do agendamento

------------------------------------------------------------------------

# Funcionalidade: Cadastrar Agendamento

## Regras de Negócio

### RNA001

Todo agendamento deve estar associado a uma viagem no momento do
cadastro.

------------------------------------------------------------------------

### RNA002

A data do agendamento deve ser igual à data da viagem associada.

------------------------------------------------------------------------

### RNA003

O horário do agendamento deve respeitar as regras de horário definidas
para a viagem.

------------------------------------------------------------------------

### RNA004

O hospital selecionado deve pertencer à cidade de destino da viagem.

------------------------------------------------------------------------

### RNA005

Todo novo agendamento deve ser cadastrado com status `AGENDADO`.

------------------------------------------------------------------------

## Cenários de Teste

### CTA001 -- Cadastrar agendamento válido

**Objetivo**

Verificar se o usuário consegue cadastrar um agendamento válido.

**Pré-condições**

- Aplicação em execução.
- Existir uma viagem válida.
- Existir paciente cadastrado.
- Existir hospital compatível com a cidade destino.

**Passos**

1. Acessar o Dashboard.
2. Selecionar uma viagem.
3. Clicar em **Novo Agendamento**.
4. Selecionar o paciente.
5. Selecionar o hospital.
6. Informar os dados obrigatórios.
7. Clicar em **Salvar**.

**Resultado esperado**

- Agendamento cadastrado com sucesso.
- Agendamento associado à viagem selecionada.
- Status inicial `AGENDADO`.
- Agendamento exibido no painel da viagem.

------------------------------------------------------------------------

### CTA002 -- Cadastrar agendamento com data diferente da viagem

**Objetivo**

Verificar se o sistema impede o cadastro de um agendamento cuja data
seja diferente da data da viagem.

**Pré-condições**

Existir uma viagem com data definida.

**Passos**

1. Selecionar uma viagem.
2. Abrir **Novo Agendamento**.
3. Informar uma data diferente da data da viagem.
4. Informar os demais dados obrigatórios.
5. Clicar em **Salvar**.

**Resultado esperado**

O sistema deverá impedir o cadastro e informar que a data do agendamento
deve ser igual à data da viagem.

------------------------------------------------------------------------

### CTA003 -- Cadastrar agendamento com horário inválido

**Objetivo**

Verificar se o sistema impede o cadastro de um agendamento com horário
incompatível com a viagem.

**Pré-condições**

Existir uma viagem com horário definido.

**Passos**

1. Selecionar uma viagem.
2. Abrir **Novo Agendamento**.
3. Informar horário fora da regra permitida.
4. Informar os demais dados obrigatórios.
5. Clicar em **Salvar**.

**Resultado esperado**

O sistema deverá impedir o cadastro e informar o motivo da
inconsistência de horário.

------------------------------------------------------------------------

### CTA004 -- Cadastrar agendamento com hospital incompatível

**Objetivo**

Verificar se o sistema impede o cadastro quando o hospital não pertence
à cidade destino da viagem.

**Pré-condições**

- Existir uma viagem com cidade destino definida.
- Existir hospital cadastrado em outra cidade.

**Passos**

1. Selecionar a viagem.
2. Abrir **Novo Agendamento**.
3. Selecionar hospital de cidade diferente da cidade destino.
4. Informar os demais dados obrigatórios.
5. Clicar em **Salvar**.

**Resultado esperado**

O sistema deverá impedir o cadastro e informar que o hospital não
pertence à cidade destino da viagem.

------------------------------------------------------------------------

# Funcionalidade: Editar Agendamento

## Regras de Negócio

### RNA006

É permitido editar somente agendamentos com status `AGENDADO`.

------------------------------------------------------------------------

### RNA007

A alteração da data do agendamento deve continuar respeitando a data
da viagem associada.

------------------------------------------------------------------------

### RNA008

A alteração do horário do agendamento deve continuar respeitando as
regras de horário da viagem.

------------------------------------------------------------------------

### RNA009

A alteração do hospital deve continuar respeitando a cidade destino
da viagem.

------------------------------------------------------------------------

### RNA010

Um agendamento com status `CANCELADO` não pode ser editado.

------------------------------------------------------------------------

## Cenários de Teste

### CTA005 -- Editar agendamento válido

**Objetivo**

Verificar se um agendamento com status `AGENDADO` pode ser editado.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Selecionar a viagem.
2. Selecionar o agendamento.
3. Clicar em **Editar**.
4. Alterar um ou mais campos permitidos.
5. Salvar.

**Resultado esperado**

O agendamento deverá ser atualizado com sucesso.

------------------------------------------------------------------------

### CTA006 -- Alterar paciente

**Objetivo**

Verificar se o paciente pode ser alterado.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Selecionar outro paciente.
3. Salvar.

**Resultado esperado**

O agendamento deverá apresentar o novo paciente.

------------------------------------------------------------------------

### CTA007 -- Alterar acompanhante

**Objetivo**

Verificar se o acompanhante pode ser alterado.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Selecionar outro acompanhante.
3. Salvar.

**Resultado esperado**

O novo acompanhante deverá ser associado ao agendamento.

------------------------------------------------------------------------

### CTA008 -- Alterar hospital válido

**Objetivo**

Verificar se o hospital pode ser alterado para outro hospital pertencente
à cidade destino.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Selecionar outro hospital da cidade destino.
3. Salvar.

**Resultado esperado**

A alteração deverá ser realizada com sucesso.

------------------------------------------------------------------------

### CTA009 -- Alterar hospital para cidade incompatível

**Objetivo**

Verificar se a regra de hospital × cidade destino continua sendo aplicada
durante a edição.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Selecionar hospital de outra cidade.
3. Salvar.

**Resultado esperado**

O sistema deverá impedir a alteração.

------------------------------------------------------------------------

### CTA010 -- Alterar data

**Objetivo**

Verificar se a alteração da data respeita a data da viagem.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Alterar a data.
3. Salvar.

**Resultado esperado**

O sistema deverá permitir somente uma data compatível com a viagem.

------------------------------------------------------------------------

### CTA011 -- Alterar horário

**Objetivo**

Verificar se a alteração do horário continua respeitando a regra da
viagem.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Alterar o horário.
3. Salvar.

**Resultado esperado**

O sistema deverá permitir somente horário compatível com a viagem.

------------------------------------------------------------------------

### CTA012 -- Alterar tipo de compromisso

**Objetivo**

Verificar se o tipo de compromisso pode ser alterado.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Alterar o tipo de compromisso.
3. Salvar.

**Resultado esperado**

O novo tipo de compromisso deverá ser salvo corretamente.

------------------------------------------------------------------------

### CTA013 -- Alterar especialidade

**Objetivo**

Verificar se a especialidade pode ser alterada.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Alterar a especialidade.
3. Salvar.

**Resultado esperado**

A nova especialidade deverá ser salva corretamente.

------------------------------------------------------------------------

### CTA014 -- Alterar necessidades especiais

**Objetivo**

Verificar a alteração das necessidades especiais do paciente.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Alterar os campos de necessidades especiais.
3. Salvar.

**Resultado esperado**

Os novos valores deverão ser persistidos corretamente.


------------------------------------------------------------------------

### CTA015 -- Tentar editar agendamento cancelado

**Objetivo**

Verificar se um agendamento cancelado não pode ser editado.

**Pré-condições**

Existir um agendamento com status `CANCELADO`.

**Passos**

1. Selecionar o agendamento cancelado.
2. Tentar acessar a opção **Editar**.

**Resultado esperado**

A opção de edição não deverá estar disponível ou o sistema deverá
impedir a operação.

------------------------------------------------------------------------

# Funcionalidade: Excluir Agendamento

## Regras de Negócio

### RNA011

É permitido excluir agendamentos que estejam aptos para exclusão de
acordo com o fluxo atual do sistema.

------------------------------------------------------------------------

### RNA012

Um agendamento com status `CANCELADO` não pode ser excluído.

------------------------------------------------------------------------

## Cenários de Teste

### CTA016 -- Excluir agendamento

**Objetivo**

Verificar se um agendamento pode ser excluído.

**Pré-condições**

Existir um agendamento que não esteja cancelado.

**Passos**

1. Selecionar o agendamento.
2. Clicar em **Excluir**.
3. Confirmar a exclusão.

**Resultado esperado**

- O agendamento deverá ser excluído.
- O agendamento não deverá mais aparecer na lista.
- A quantidade de agendamentos da viagem deverá ser atualizada.

------------------------------------------------------------------------

### CTA017 -- Cancelar exclusão do agendamento

**Objetivo**

Verificar se o usuário pode desistir da exclusão.

**Pré-condições**

Existir um agendamento que não esteja cancelado.

**Passos**

1. Selecionar o agendamento.
2. Clicar em **Excluir**.
3. Cancelar a operação na confirmação.

**Resultado esperado**

O agendamento deverá permanecer inalterado.

------------------------------------------------------------------------

### CTA018 -- Tentar excluir agendamento cancelado

**Objetivo**

Verificar se um agendamento cancelado não pode ser excluído.

**Pré-condições**

Existir um agendamento com status `CANCELADO`.

**Passos**

1. Selecionar o agendamento cancelado.
2. Tentar acessar a opção **Excluir**.

**Resultado esperado**

A opção de exclusão não deverá estar disponível ou o sistema deverá
impedir a operação.

------------------------------------------------------------------------

# Funcionalidade: Cancelar Agendamento

## Regras de Negócio

### RNA013

O cancelamento deve alterar o status do agendamento para `CANCELADO`.

------------------------------------------------------------------------

### RNA014

O cancelamento não deve excluir o registro do agendamento do banco de
dados.

------------------------------------------------------------------------

### RNA015

O cancelamento deve registrar o motivo informado pelo usuário.

------------------------------------------------------------------------

### RNA016

Um agendamento que já esteja com status `CANCELADO` não deve ser
cancelado novamente.

------------------------------------------------------------------------

## Cenários de Teste

### CTA019 -- Cancelar agendamento

**Objetivo**

Verificar se um agendamento `AGENDADO` pode ser cancelado.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Selecionar o agendamento.
2. Clicar em **Cancelar Agendamento**.
3. Informar o motivo do cancelamento.
4. Confirmar a operação.

**Resultado esperado**

- O cancelamento deverá ser realizado com sucesso.
- O status deverá ser alterado para `CANCELADO`.
- O motivo deverá ser registrado.
- O registro deverá permanecer existente no sistema.

------------------------------------------------------------------------

### CTA020 -- Desistir do cancelamento

**Objetivo**

Verificar se o usuário pode desistir do cancelamento.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Abrir o modal de cancelamento.
2. Informar o motivo, se necessário.
3. Fechar o modal ou cancelar a operação.

**Resultado esperado**

O agendamento deverá permanecer com status `AGENDADO` e sem alteração
nos seus dados.

------------------------------------------------------------------------

### CTA021 -- Tentar cancelar agendamento já cancelado

**Objetivo**

Verificar o comportamento quando o usuário tenta cancelar novamente um
agendamento já cancelado.

**Pré-condições**

Existir um agendamento com status `CANCELADO`.

**Passos**

1. Selecionar o agendamento.
2. Tentar executar novamente o cancelamento.

**Resultado esperado**

O sistema deverá impedir uma nova operação de cancelamento ou informar
que o agendamento já está cancelado.

------------------------------------------------------------------------

# Funcionalidade: Exibir Status do Agendamento

## Regras de Negócio

### RNA017

O status do agendamento deve ser apresentado ao usuário no painel de
detalhes da viagem.

------------------------------------------------------------------------

### RNA018

O status apresentado deve corresponder ao status atual do agendamento.

------------------------------------------------------------------------

## Cenários de Teste

### CTA022 -- Exibir agendamento com status AGENDADO

**Objetivo**

Verificar se o usuário consegue identificar que o agendamento está
agendado.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Selecionar a viagem.
2. Visualizar o painel **Agendamentos desta viagem**.

**Resultado esperado**

O card do agendamento deverá apresentar claramente o status
**AGENDADO**.

------------------------------------------------------------------------

### CTA023 -- Exibir agendamento com status CANCELADO

**Objetivo**

Verificar se o usuário consegue identificar que o agendamento foi
cancelado.

**Pré-condições**

Existir um agendamento com status `CANCELADO`.

**Passos**

1. Selecionar a viagem.
2. Visualizar o painel **Agendamentos desta viagem**.

**Resultado esperado**

O card do agendamento deverá apresentar claramente o status
**CANCELADO**.

------------------------------------------------------------------------

### CTA024 -- Atualizar status no painel após cancelamento

**Objetivo**

Verificar se o painel é atualizado após o cancelamento do agendamento.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Selecionar o agendamento.
2. Cancelar o agendamento.
3. Retornar ao painel da viagem.

**Resultado esperado**

O card deverá apresentar o status **CANCELADO**.

------------------------------------------------------------------------

### CTA025 -- Exibir horário do agendamento

**Objetivo**

Verificar se o horário do agendamento é apresentado corretamente no
card.

**Pré-condições**

Existir um agendamento cadastrado.

**Passos**

1. Selecionar a viagem.
2. Visualizar o painel **Agendamentos desta viagem**.

**Resultado esperado**

O horário deverá ser apresentado de forma clara, separado visualmente
das demais informações do agendamento.

------------------------------------------------------------------------

# Funcionalidade: Atualização do Painel após Operações

## Cenários de Teste

### CTA026 -- Atualizar painel após cadastro

**Objetivo**

Verificar se o novo agendamento aparece no painel após o cadastro.

**Pré-condições**

Existir uma viagem válida.

**Passos**

1. Cadastrar um novo agendamento.
2. Fechar o modal.
3. Visualizar o painel da viagem.

**Resultado esperado**

O novo agendamento deverá aparecer no painel da viagem e o contador de
agendamentos deverá ser atualizado.

------------------------------------------------------------------------

### CTA027 -- Atualizar painel após edição

**Objetivo**

Verificar se os dados alterados aparecem corretamente no painel após a
edição.

**Pré-condições**

Existir um agendamento com status `AGENDADO`.

**Passos**

1. Editar o agendamento.
2. Alterar um dado.
3. Salvar.
4. Visualizar o painel da viagem.

**Resultado esperado**

O painel deverá apresentar os dados atualizados do agendamento.

------------------------------------------------------------------------

### CTA028 -- Atualizar painel após exclusão

**Objetivo**

Verificar se o agendamento excluído deixa de aparecer no painel.

**Pré-condições**

Existir um agendamento apto para exclusão.

**Passos**

1. Excluir o agendamento.
2. Visualizar o painel da viagem.

**Resultado esperado**

- O agendamento não deverá mais aparecer.
- O contador de agendamentos deverá ser atualizado.

