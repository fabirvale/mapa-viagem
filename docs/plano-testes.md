# Plano de Testes Funcionais

## Projeto

Mapa de Viagem

## Objetivo

Este documento descreve os cenários de testes funcionais do sistema Mapa de Viagem.

Seu objetivo é validar as funcionalidades implementadas e garantir que as regras de negócio estejam sendo respeitadas antes da disponibilização de uma nova versão.

---

## Premissas

Este plano de testes considera a versão atual da aplicação e sua evolução futura.

Está previsto para versões posteriores:

- Autenticação com Spring Security;
- Controle de acesso por perfis de usuário;
- Auditoria das operações realizadas.

Quando essas funcionalidades forem implementadas, este documento será atualizado.

---

# Módulo: Viagem

## Funcionalidades

- Cadastrar viagem
- Editar viagem
- Duplicar viagem
- Iniciar viagem
- Finalizar viagem (em desenvolvimento)
- Cancelar viagem
- Excluir viagem

---

# Funcionalidade: Cadastrar Viagem

## Regras de Negócio

### RNV001

É permitido cadastrar viagens cuja cidade de origem seja igual à cidade de destino.

---

### RNV002

Toda nova viagem deve ser cadastrada com status **AGENDADA**.

---

### RNV003

Caso já exista uma viagem com a mesma cidade de origem, cidade de destino, data da viagem, horário previsto e status, o sistema deverá solicitar confirmação antes de concluir o cadastro.

---

### RNV004

Caso o usuário confirme o cadastro da viagem duplicada, o sistema deverá permitir o cadastramento normalmente.

---

### RNV005

É permitido cadastrar uma viagem sem motorista.

---

### RNV006

É permitido cadastrar uma viagem sem veículo.

---

# Cenários de Teste

## CTV001 - Cadastrar viagem válida

### Objetivo

Verificar se um usuário autorizado consegue cadastrar uma viagem válida.

### Pré-condições

- Aplicação em execução.
- Usuário autenticado (quando Spring Security estiver implementado).

### Passos

1. Realizar login.
2. Acessar o Dashboard.
3. Clicar em **Nova Viagem**.
4. Informar todos os campos obrigatórios.
5. Clicar em **Cadastrar**.

### Resultado esperado

- A viagem deve ser cadastrada com sucesso.
- O status inicial deve ser **AGENDADA**.
- A viagem deve aparecer na listagem.

---

## CTV002 - Cadastrar viagem sem motorista

### Objetivo

Verificar se o sistema permite cadastrar uma viagem sem motorista.

### Pré-condições

Aplicação em execução.

### Passos

1. Abrir o cadastro de viagem.
2. Informar todos os campos obrigatórios.
3. Não selecionar motorista.
4. Salvar.

### Resultado esperado

A viagem deve ser cadastrada normalmente.

---

## CTV003 - Cadastrar viagem sem veículo

### Objetivo

Verificar se o sistema permite cadastrar uma viagem sem veículo.

### Pré-condições

Aplicação em execução.

### Passos

1. Abrir o cadastro de viagem.
2. Informar todos os campos obrigatórios.
3. Não selecionar veículo.
4. Salvar.

### Resultado esperado

A viagem deve ser cadastrada normalmente.

---

## CTV004 - Cadastrar viagem duplicada

### Objetivo

Verificar o tratamento de viagens duplicadas.

### Pré-condições

Existir uma viagem cadastrada com:

- mesma origem;
- mesmo destino;
- mesma data;
- mesmo horário;
- mesmo status.

### Passos

1. Cadastrar uma nova viagem com os mesmos dados.
2. Clicar em **Cadastrar**.

### Resultado esperado

O sistema deverá exibir uma mensagem informando que já existe uma viagem cadastrada e solicitar confirmação do usuário.

---

## CTV005 - Confirmar cadastro de viagem duplicada

### Objetivo

Verificar se o sistema permite cadastrar uma viagem duplicada após confirmação do usuário.

### Pré-condições

Executar o cenário CTV004.

### Passos

1. Confirmar a duplicidade.

### Resultado esperado

A viagem deverá ser cadastrada normalmente.