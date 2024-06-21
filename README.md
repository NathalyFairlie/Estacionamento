Programa para Estacionamentos baseado na placa do veículo

Status do Projeto
  Em desenvolvimento

Tecnologias Utilizadas
  •	Linguagem de Programação: Java
  •	Banco de Dados: MySQL

Time de Desenvolvedores
  •	Nathaly Fairlie Pearson Freitas

Objetivo do Software
  O software visa proporcionar um controle eficiente das entradas e saídas de veículos em estacionamentos, utilizando a identificação pela placa do veículo. Ele suporta dois tipos de contratação de serviço:
    •	Mensalista: Para clientes com contrato de vigência ativa, permitindo a entrada e saída livre durante o período contratado.
    •	Avulso: Para clientes que pagam por hora, calculando o valor a ser pago com base no tempo de permanência.

Funcionalidades do Sistema
1.	Identificação de Veículos:
    o	Manualmente pela placa (com possível futura integração de reconhecimento de placa por AI).
    o	Autodetecção de veículos cadastrados e seu tipo de contratação (Mensalista ou Avulso).
2.	Gerenciamento de Mensalistas:
    o	Permissão de entrada e saída durante o período contratado.
    o	Bloqueio de acesso após o vencimento do contrato.
3.	Gestão de Veículos Avulsos:
    o	Cálculo do tempo de permanência no estacionamento.
    o	Cálculo automático do valor a ser pago com base na tabela de preços definida pelo gerente.
    o	Restrição de entrada e saída a uma vez por sessão de estacionamento.
4.	Administração de Estacionamento:
    o	Inserção de Descontos.
    o	Gerenciamento da Tabela de Preços.
    o	Registro e controle de pagamentos efetuados, acessível apenas aos gerentes do estacionamento.
