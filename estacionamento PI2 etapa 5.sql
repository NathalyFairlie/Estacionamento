CREATE SCHEMA IF NOT EXISTS `estacionamento` ;

USE `estacionamento`;

CREATE TABLE IF NOT EXISTS `cadastro_usuariosistema` (
  `id_funcionario` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(200) NULL,
  `cargo` VARCHAR(200) NULL,
  `login` VARCHAR(200) NULL,
  `senha` VARCHAR(200) NULL,
  PRIMARY KEY (`id_funcionario`)
);

CREATE TABLE veiculo (
    Placa VARCHAR(20) PRIMARY KEY,
    Modelo VARCHAR(100),
    Marca VARCHAR(100)
);

CREATE TABLE avulso (
    Placa VARCHAR(20) PRIMARY KEY,
    Data DATE,
    Entrada TIME,
    Saida TIME,
    HorasEstacionadas INT,
    ValorTotal DOUBLE,
    NomeDesconto VARCHAR(100),
    PercentualDesconto DOUBLE,
    ValorComDesconto DOUBLE
);
create table Pagamentos (
	 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     Placa VARCHAR(20), 
     Data DATE,
     ValorPago Double
);
CREATE TABLE mensalista (
    Placa VARCHAR(20) PRIMARY KEY,
    MesesContratados INT,
    InicioContrato DATE,
    FimContrato DATE,
    ValorTotal DOUBLE,
    NomeDesconto VARCHAR(100),
    PercentualDesconto DOUBLE,
    ValorComDesconto DOUBLE
    
);

CREATE TABLE Desconto (
    nomeDesconto VARCHAR(100) PRIMARY KEY,
    desconto DOUBLE
);
CREATE TABLE valores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hora1 DOUBLE,
    hora2 DOUBLE,
    hora3 DOUBLE,
    hora6 DOUBLE,
    hora12 DOUBLE,
    diaria DOUBLE,
    mensal DOUBLE);