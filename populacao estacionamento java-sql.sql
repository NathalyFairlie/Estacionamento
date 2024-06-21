/*isso é o que eu posso deixar previamente salvo nas tabelas pelas as funçoes que meu programa faz
pois dependo da inserção de dados pelo usuario para que o programa funcione de acordo regras de negocio*/


-- Populating cadastro_usuariosistema table
INSERT INTO cadastro_usuariosistema (nome, cargo, login, senha) 
VALUES 
('João Silva', 'Gerente', '2', '2'),
('Maria Santos', 'Atendente', '1', '1');


-- Populating veiculo table
INSERT INTO veiculo (Placa, Modelo, Marca) 
VALUES 
('AAA1111', 'Corolla', 'Toyota'),
('BBB1111', 'City', 'Honda'),
('CCC1111', 'Civic', 'Honda'),
('DDD1111', 'Gol', 'Volkswagen');


-- Populating mensalista table com vigencia vencida
INSERT INTO mensalista (Placa, MesesContratados, InicioContrato, FimContrato, ValorTotal, NomeDesconto, PercentualDesconto, ValorComDesconto) 
VALUES 
('ABC1111', 1, '2024-01-01', '2024-02-01', 300.00,'Não possui desconto', 0, 300.00);


-- Populating Desconto table
INSERT INTO Desconto (nomeDesconto, desconto) 
VALUES 
('Desconto Estudante', 10.00),
('Desconto Idoso', 15.00),
('Desconto Fidelidade', 5.00);

INSERT INTO valores (hora1, hora2, hora3, hora6, hora12, diaria, mensal) 
VALUES (1.00, 2.00, 3.00, 6.00, 12.00, 24.00, 300.00);