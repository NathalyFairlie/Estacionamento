/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Telas;

import Conexao.Conexao;
import Dao.DaoAvulso;
import Dao.DaoDesconto;
import Dao.DaoPagamentos;
import Dao.DaoValores;
import estacionamento1.Avulso;
import estacionamento1.Desconto;
import estacionamento1.Pagamentos;
import estacionamento1.Valores;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaAvulso extends JFrame {

    private Conexao conexao;
    private TelaPagamentos telaPagamentos;
    private TelaVeiculo telaVeiculo;
    private final DaoDesconto daoDesconto = new DaoDesconto();
    private final DaoValores daoValores = new DaoValores();
    private final DaoAvulso daoAvulso;
    private final String placa;
    private final LocalDateTime entrada = LocalDateTime.now();
    private final LocalDateTime saida = LocalDateTime.now();
    private final LocalDateTime data = LocalDateTime.now();
    private String nomeDescontoSelecionado;
    private Boolean pesquisa = false;

    public TelaAvulso(String placa, String source) {
        initComponents();
        daoAvulso = new DaoAvulso();
        this.placa = placa;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = data.format(formatter);

        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("HH:mm:ss");
        String entradaFormatada = data.format(formatterEntrada);

        DateTimeFormatter formatterSaida = DateTimeFormatter.ofPattern("HH:mm:ss");
        String saidaFormatada = data.format(formatterSaida);

        lblData.setText(dataFormatada);
        txtPlacaA.setText(placa);

        Avulso avulso = getAvulsoFromList(placa);
        // Check the source of the call
        if ("btnAvulso".equals(source)) {
            txtHoraEntrada.setText(entrada.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            txtHoraSaida.setText(null);
        } else if ("btnPesquisar".equals(source)) {
            try {
                LocalTime entrada = avulso.getEntrada();
                LocalTime saida = avulso.getSaida();

                if (entrada != null) {
                    txtHoraEntrada.setText(entrada.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                }

                if (saida != null) {
                    txtHoraSaida.setText(saida.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                    //txtHoraSaida.setText(String.valueOf(avulso.getSaida()));

                    if (avulso.getHorasEstacionadas() == 0.0) {
                        int permanencia = calcularPermanencia(entrada);
                        txtPermanencia.setText(String.valueOf(permanencia));
                    } else {
                        txtPermanencia.setText(String.valueOf(avulso.getHorasEstacionadas()));

                        double valorTotal = obterValorPermanencia();
                        txtValorTotal.setText(String.valueOf(valorTotal));

                        if (avulso.getValorComDesconto() != 0.0) {
                            txtValorAPagar.setText(String.valueOf(avulso.getValorComDesconto()));

                        }
                    }
                } else {
                    txtHoraSaida.setText(saidaFormatada);
                    int permanencia = calcularPermanencia(entrada);
                    txtPermanencia.setText(String.valueOf(permanencia));
                    double valorTotal = obterValorPermanencia();
                    txtValorTotal.setText(String.valueOf(valorTotal));

                }
            } catch (NullPointerException e) {
                txtHoraSaida.setText(null);
                txtPermanencia.setText(null);
            }
        } else {
            try {
                if (txtHoraSaida != null && avulso.getSaida() != null) {
                    txtHoraSaida.setText(avulso.getSaida().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                } else {
                    txtHoraSaida.setText(null);
                }
            } catch (NullPointerException e) {
                txtHoraSaida.setText(null);
            }
        }
    }

    private Avulso getAvulsoFromList(String placa) {
        List<Avulso> listaAvulsos = daoAvulso.listaAvulso();
        for (Avulso avulso : listaAvulsos) {
            if (avulso.getPlaca().equalsIgnoreCase(placa)) {
                return avulso;
            }
        }
        return null;
    }

    private double calcularValorComDesconto(double valorTotal, double percentualDesconto) {
        return valorTotal - (valorTotal * percentualDesconto / 100);
    }

    private int calcularPermanencia(LocalTime entrada) {
        int horasArredondadas = 0;

        try {
            LocalTime now = LocalTime.now();

            // Se a entrada for após o momento atual, subtraia um dia para evitar valores negativos
            if (entrada.isAfter(now)) {
                entrada = entrada.minusHours(24); // Subtrai 24 horas para evitar valores negativos
            }

            // Calcula a diferença de tempo em minutos
            long minutosPermanencia = Duration.between(entrada, now).toMinutes();

            // Verifica se houve mudança nos segundos e arredonda para a próxima hora, se necessário
            if (entrada.getSecond() != now.getSecond()) {
                minutosPermanencia += 60 - now.getMinute(); // Adiciona os minutos restantes até a próxima hora
            }

            // Arredonda para cima para a hora mais próxima
            horasArredondadas = (int) Math.ceil(minutosPermanencia / 60.0);

        } catch (DateTimeException e) {
            JOptionPane.showMessageDialog(this, "Erro ao calcular a permanência. Certifique-se de que a entrada está no formato correto.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao calcular a permanência: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return horasArredondadas;
    }

    private double obterValorPermanencia() {
        List<Valores> listaValores = daoValores.listaValores();
        int permanencia = Integer.parseInt(txtPermanencia.getText());

        if (!listaValores.isEmpty()) {
            Valores valor = listaValores.get(0);

            switch (permanencia) {
                case 1:
                    return valor.getHora1();
                case 2:
                    return valor.getHora2();
                case 3:
                    return valor.getHora3();
                case 6:
                    return valor.getHora6();
                case 12:
                    return valor.getHora12();
                default:
                    return valor.getDiaria();
            }
        }

        return 0.0;
    }

    private double obterDescontoSelecionado() {
        if (ComboBoxDesc == null) {
            //System.err.println("ComboBox não inicializado corretamente.");
            return 0.0;
        }

        String nomeDescontoSelecionado = (String) ComboBoxDesc.getSelectedItem();

        if ("Não possui desconto".equalsIgnoreCase(nomeDescontoSelecionado)) {
            return 0.0;
        }

        List<Desconto> listaDescontos = daoDesconto.listaDesconto();
        for (Desconto desconto : listaDescontos) {
            String nomeDescontoLista = desconto.getNomeDesconto();
            nomeDescontoLista = nomeDescontoLista.replace("~", "Texto Desejado");

            if (nomeDescontoLista.equalsIgnoreCase(nomeDescontoSelecionado)) {
                return desconto.getDesconto();
            }
        }

        return 0.0;
    }

    private String obterNomeDescontoSelecionado() {
        if (ComboBoxDesc == null) {
            //System.err.println("ComboBox não inicializado corretamente.");
            return "erro";
        }

        String nomeDescontoSelecionado = (String) ComboBoxDesc.getSelectedItem();

        if ("Não possui desconto".equalsIgnoreCase(nomeDescontoSelecionado)) {
            return "Não possui desconto";
        }

        List<Desconto> listaDescontos = daoDesconto.listaDesconto();
        for (Desconto desconto : listaDescontos) {
            String nomeDescontoLista = desconto.getNomeDesconto();
            nomeDescontoLista = nomeDescontoLista.replace("~", "Texto Desejado");

            if (nomeDescontoLista.equalsIgnoreCase(nomeDescontoSelecionado)) {
                return nomeDescontoSelecionado;
            }
        }

        return "Não possui desconto";
    }

    private void handleComboBoxSelection() {
        nomeDescontoSelecionado = (String) ComboBoxDesc.getSelectedItem();
        //System.out.println("Desconto selecionado: " + nomeDescontoSelecionado);
        String selectedDiscount = (String) ComboBoxDesc.getSelectedItem();

        List<Desconto> listaDescontos = daoDesconto.listaDesconto();
        for (Desconto desconto : listaDescontos) {
            String nomeDescontoLista = desconto.getNomeDesconto();
            nomeDescontoLista = nomeDescontoLista.replace("~", "Texto Desejado");

            if (selectedDiscount.equals(nomeDescontoLista)) {
                //System.out.println(nomeDescontoLista + " selecionado");
                break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnlCabecalho = new javax.swing.JPanel();
        lblCabecalho = new javax.swing.JLabel();
        lblPlacaA = new javax.swing.JLabel();
        txtPlacaA = new javax.swing.JTextField();
        txtPermanencia = new javax.swing.JTextField();
        lblPermanencia = new javax.swing.JLabel();
        txtHoraEntrada = new javax.swing.JTextField();
        lblHoraEntrada = new javax.swing.JLabel();
        txtHoraSaida = new javax.swing.JTextField();
        lblHoraSaida = new javax.swing.JLabel();
        btnContinuar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        ComboBoxDesc = new javax.swing.JComboBox<>();
        txtValorTotal = new javax.swing.JTextField();
        lblValorTotal = new javax.swing.JLabel();
        lblValorAPagar = new javax.swing.JLabel();
        txtValorAPagar = new javax.swing.JTextField();
        btnCalcular = new javax.swing.JButton();
        lblDesconto = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlCabecalho.setBackground(new java.awt.Color(153, 0, 153));
        pnlCabecalho.setDoubleBuffered(false);

        lblCabecalho.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCabecalho.setForeground(new java.awt.Color(255, 255, 255));
        lblCabecalho.setText("Avulso");

        javax.swing.GroupLayout pnlCabecalhoLayout = new javax.swing.GroupLayout(pnlCabecalho);
        pnlCabecalho.setLayout(pnlCabecalhoLayout);
        pnlCabecalhoLayout.setHorizontalGroup(
            pnlCabecalhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCabecalhoLayout.createSequentialGroup()
                .addGap(202, 202, 202)
                .addComponent(lblCabecalho)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCabecalhoLayout.setVerticalGroup(
            pnlCabecalhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCabecalhoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblCabecalho)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        lblPlacaA.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPlacaA.setLabelFor(txtPlacaA);
        lblPlacaA.setText("Placa:");

        txtPlacaA.setEditable(false);
        txtPlacaA.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPlacaA.setToolTipText("Placa do carro que quer se tornar mensalista");
        txtPlacaA.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtPlacaA.setEnabled(false);
        txtPlacaA.setName(""); // NOI18N

        txtPermanencia.setEditable(false);
        txtPermanencia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPermanencia.setToolTipText("Permanencia em horas do veiculo no estabelecimento");
        txtPermanencia.setActionCommand("<Not Set>");
        txtPermanencia.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtPermanencia.setEnabled(false);

        lblPermanencia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblPermanencia.setLabelFor(txtPermanencia);
        lblPermanencia.setText("Permanencia:");

        txtHoraEntrada.setEditable(false);
        txtHoraEntrada.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtHoraEntrada.setToolTipText("Inicio da vigencia do contrato mensal");
        txtHoraEntrada.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtHoraEntrada.setEnabled(false);

        lblHoraEntrada.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblHoraEntrada.setLabelFor(txtHoraEntrada);
        lblHoraEntrada.setText("Entrada:");

        txtHoraSaida.setEditable(false);
        txtHoraSaida.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtHoraSaida.setToolTipText("Hora da saida do veículo");
        txtHoraSaida.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtHoraSaida.setEnabled(false);

        lblHoraSaida.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblHoraSaida.setLabelFor(txtHoraSaida);
        lblHoraSaida.setText("Saída:");
        lblHoraSaida.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnContinuar.setBackground(new java.awt.Color(153, 0, 153));
        btnContinuar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnContinuar.setForeground(new java.awt.Color(255, 255, 255));
        btnContinuar.setText("Confirmar Pagamento");
        btnContinuar.setToolTipText("Clique para confirmar o pagamento");
        btnContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinuarActionPerformed(evt);
            }
        });

        btnVoltar.setBackground(new java.awt.Color(204, 102, 255));
        btnVoltar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnVoltar.setForeground(new java.awt.Color(255, 255, 255));
        btnVoltar.setText("Voltar");
        btnVoltar.setToolTipText("Clique aqui para voltar para tela anerior");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        ComboBoxDesc.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();

        // Adicionar a opção "Não possui desconto" como a primeira linha
        comboBoxModel.addElement("Não possui desconto");

        // Populate the ComboBoxModel with partner names and corresponding discounts
        List<Desconto> descontoList = daoDesconto.listaDesconto();
        for (Desconto desconto : descontoList) {
            // Utilizar a mesma codificação para garantir consistência
            String item = new String(desconto.getNomeDesconto().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1);
            comboBoxModel.addElement(item);
        }
        ComboBoxDesc.setModel(comboBoxModel);
        ComboBoxDesc.setToolTipText("Selecione desconto a ser aplicado ");
        ComboBoxDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxDescActionPerformed(evt);
            }
        });

        txtValorTotal.setEditable(false);
        txtValorTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtValorTotal.setToolTipText("Valor total sem desconto");
        txtValorTotal.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtValorTotal.setEnabled(false);

        lblValorTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblValorTotal.setLabelFor(txtHoraSaida);
        lblValorTotal.setText("Valor Total:");
        lblValorTotal.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lblValorAPagar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblValorAPagar.setLabelFor(txtHoraSaida);
        lblValorAPagar.setText("Valor a Pagar:");
        lblValorAPagar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txtValorAPagar.setEditable(false);
        txtValorAPagar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtValorAPagar.setToolTipText("Valor final com desconto caso o veiculo possua desconto");
        txtValorAPagar.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtValorAPagar.setEnabled(false);

        btnCalcular.setBackground(new java.awt.Color(153, 0, 153));
        btnCalcular.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCalcular.setForeground(new java.awt.Color(255, 255, 255));
        btnCalcular.setText("Calcular");
        btnCalcular.setToolTipText("Clique para calcular o valor final a ser pago pelo cliente");
        btnCalcular.setBorder(null);
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        lblDesconto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDesconto.setLabelFor(ComboBoxDesc);
        lblDesconto.setText("Desconto?");

        lblData.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblData.setText("19/01/2024");
        lblData.setToolTipText("Data em que o veiculo deu entrada no estacionamento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCabecalho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnVoltar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnContinuar)
                        .addGap(14, 14, 14))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblHoraEntrada)
                                    .addComponent(lblPlacaA))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPlacaA)
                                    .addComponent(txtHoraEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblPermanencia)
                                    .addComponent(lblHoraSaida))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPermanencia, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                    .addComponent(txtHoraSaida)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblValorTotal)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblDesconto)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ComboBoxDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblValorAPagar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtValorAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblData)
                .addGap(183, 183, 183))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnlCabecalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(lblData)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPlacaA)
                            .addComponent(txtPlacaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPermanencia)
                            .addComponent(txtPermanencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHoraEntrada)
                            .addComponent(txtHoraEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHoraSaida)
                            .addComponent(txtHoraSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblValorTotal)
                    .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDesconto)
                    .addComponent(ComboBoxDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblValorAPagar)
                    .addComponent(txtValorAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(btnVoltar))
                    .addComponent(btnContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        try {
            String placa = txtPlacaA.getText();

            // Calcula a permanência
            try {
                LocalTime entrada = LocalTime.parse(txtHoraEntrada.getText());

                // Calcula o valor total e o valor com desconto
                double valorTotal = obterValorPermanencia();
                double percentualDesconto = obterDescontoSelecionado();
                String nomeDescontoSelecionado = obterNomeDescontoSelecionado();
                ComboBoxDesc.setSelectedItem(nomeDescontoSelecionado);

                double descontoSelecionado = obterDescontoSelecionado();
                double valorComDesconto = calcularValorComDesconto(valorTotal, descontoSelecionado);
                txtValorAPagar.setText(String.valueOf(valorComDesconto));

                List<Avulso> listaAvulso = daoAvulso.listaAvulso();

                // Flag para verificar se a entrada foi encontrada na lista
                boolean encontrado = false;

                // Verifica se uma entrada para a placa já existe na lista
                for (Avulso avulso : listaAvulso) {
                    if (avulso.getPlaca().equalsIgnoreCase(placa)) {
                        encontrado = true;

                        // Atualiza as informações na instância de Avulso
                        avulso.setSaida(LocalTime.parse(txtHoraSaida.getText(), DateTimeFormatter.ofPattern("HH:mm:ss")));
                        avulso.setHorasEstacionadas(Integer.parseInt(txtPermanencia.getText()));
                        avulso.setValorTotal(valorTotal);
                        avulso.setNomeDesconto(nomeDescontoSelecionado);
                        avulso.setPercentualDesconto(percentualDesconto);
                        avulso.setValorComDesconto(valorComDesconto);

                        // Atualiza no banco de dados
                        daoAvulso.updateAvulso(avulso);

                        JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");

                        // Saia do loop após encontrar e atualizar a entrada
                        break;
                    }
                }

                // Se não foi encontrada uma entrada para a placa fornecida, exiba uma mensagem
                if (!encontrado) {
                    JOptionPane.showMessageDialog(this, "Não foi encontrada uma entrada para a placa fornecida.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Formato de hora inválido. Certifique-se de que está no formato hh:mm:ss.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Erro ao parsear a data/hora.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao converter um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnCalcularActionPerformed

    private void ComboBoxDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxDescActionPerformed
        handleComboBoxSelection();
    }//GEN-LAST:event_ComboBoxDescActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        this.dispose();
        TelaVeiculo telaVeiculo = new TelaVeiculo((JFrame) SwingUtilities.getWindowAncestor(this));
        telaVeiculo.setVisible(true);
        ComboBoxDesc.setEnabled(true);


    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinuarActionPerformed
 try {
                String placa = txtPlacaA.getText();
                LocalDate data = LocalDate.now();

                double valorPago = Double.parseDouble(txtValorAPagar.getText());

                Pagamentos pagamento = new Pagamentos(placa, data, valorPago);

                // Use SavePagamentosMensalista to save the information
                DaoPagamentos daoPagamento = new DaoPagamentos();
                daoPagamento.addPagamento(pagamento);

                
            JOptionPane.showMessageDialog(this, "Pagamento salvo com sucesso! \n "
                    + "Voltando para página anterior");
            this.dispose();
        TelaVeiculo telaVeiculo = new TelaVeiculo((JFrame) SwingUtilities.getWindowAncestor(this));
        telaVeiculo.setVisible(true);

            } catch (DateTimeParseException e) {

            }
 
    }//GEN-LAST:event_btnContinuarActionPerformed
    public void ocultarBtnVoltar() {
        btnVoltar.setVisible(false);
    }

    public void ocultarBtnContinuar() {
        btnContinuar.setVisible(false);
    }

    public void setComboBoxDisable() {
        ComboBoxDesc.setEnabled(false); // Desativa a JComboBox
    }

    public void setComboBoxDesc(JComboBox<String> ComboBoxDesc) {
        this.ComboBoxDesc = ComboBoxDesc;
    }

    public void setComboBoxDesc(String nomeDesconto) {
        ComboBoxDesc.setSelectedItem(nomeDesconto);
    }

    public String getNomeDescontoSelecionado() {
        return nomeDescontoSelecionado;
    }

    public void setNomeDescontoSelecionado(String nomeDescontoSelecionado) {
        this.nomeDescontoSelecionado = nomeDescontoSelecionado;
    }

    public JComboBox<String> getComboBoxDesc() {
        return ComboBoxDesc;
    }

    public JButton getBtnCalcular() {
        return btnCalcular;
    }

    public void setBtnCalcular(JButton btnCalcular) {
        this.btnCalcular = btnCalcular;
    }

    public JButton getBtnContinuar() {
        return btnContinuar;
    }

    public void setBtnContinuar(JButton btnContinuar) {
        this.btnContinuar = btnContinuar;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }

    public void setBtnVoltar(JButton btnVoltar) {
        this.btnVoltar = btnVoltar;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public void setjPanel1(JPanel jPanel1) {
        this.jPanel1 = jPanel1;
    }

    public JLabel getLblCabecalho() {
        return lblCabecalho;
    }

    public void setLblCabecalho(JLabel lblCabecalho) {
        this.lblCabecalho = lblCabecalho;
    }

    public JLabel getLblData() {
        return lblData;
    }

    public void setLblData(JLabel lblData) {
        this.lblData = lblData;
    }

    public JLabel getLblDesconto() {
        return lblDesconto;
    }

    public void setLblDesconto(JLabel lblDesconto) {
        this.lblDesconto = lblDesconto;
    }

    public JLabel getLblHoraEntrada() {
        return lblHoraEntrada;
    }

    public void setLblHoraEntrada(JLabel lblHoraEntrada) {
        this.lblHoraEntrada = lblHoraEntrada;
    }

    public JLabel getLblHoraSaida() {
        return lblHoraSaida;
    }

    public void setLblHoraSaida(JLabel lblHoraSaida) {
        this.lblHoraSaida = lblHoraSaida;
    }

    public JLabel getLblPermanencia() {
        return lblPermanencia;
    }

    public void setLblPermanencia(JLabel lblPermanencia) {
        this.lblPermanencia = lblPermanencia;
    }

    public JLabel getLblPlacaA() {
        return lblPlacaA;
    }

    public void setLblPlacaA(JLabel lblPlacaA) {
        this.lblPlacaA = lblPlacaA;
    }

    public JLabel getLblValorAPagar() {
        return lblValorAPagar;
    }

    public void setLblValorAPagar(JLabel lblValorAPagar) {
        this.lblValorAPagar = lblValorAPagar;
    }

    public JLabel getLblValorTotal() {
        return lblValorTotal;
    }

    public void setLblValorTotal(JLabel lblValorTotal) {
        this.lblValorTotal = lblValorTotal;
    }

    public JPanel getPnlCabecalho() {
        return pnlCabecalho;
    }

    public void setPnlCabecalho(JPanel pnlCabecalho) {
        this.pnlCabecalho = pnlCabecalho;
    }

    public JTextField getTxtHoraEntrada() {
        return txtHoraEntrada;
    }

    public void setTxtHoraEntrada(JTextField txtHoraEntrada) {
        this.txtHoraEntrada = txtHoraEntrada;
    }

    public JTextField getTxtHoraSaida() {
        return txtHoraSaida;
    }

    public void setTxtHoraSaida(JTextField txtHoraSaida) {
        this.txtHoraSaida = txtHoraSaida;
    }

    public JTextField getTxtPermanencia() {
        return txtPermanencia;
    }

    public void setTxtPermanencia(JTextField txtPermanencia) {
        this.txtPermanencia = txtPermanencia;
    }

    public JTextField getTxtPlacaA() {
        return txtPlacaA;
    }

    public void setTxtPlacaA(JTextField txtPlacaA) {
        this.txtPlacaA = txtPlacaA;
    }

    public JTextField getTxtValorAPagar() {
        return txtValorAPagar;
    }

    public void setTxtValorAPagar(JTextField txtValorAPagar) {
        this.txtValorAPagar = txtValorAPagar;
    }

    public JTextField getTxtValorTotal() {
        return txtValorTotal;
    }

    public void setTxtValorTotal(JTextField txtValorTotal) {
        this.txtValorTotal = txtValorTotal;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroUsuarioSistema.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroUsuarioSistema.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroUsuarioSistema.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroUsuarioSistema.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastroUsuarioSistema().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboBoxDesc;
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnContinuar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCabecalho;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblDesconto;
    private javax.swing.JLabel lblHoraEntrada;
    private javax.swing.JLabel lblHoraSaida;
    private javax.swing.JLabel lblPermanencia;
    private javax.swing.JLabel lblPlacaA;
    private javax.swing.JLabel lblValorAPagar;
    private javax.swing.JLabel lblValorTotal;
    private javax.swing.JPanel pnlCabecalho;
    private javax.swing.JTextField txtHoraEntrada;
    private javax.swing.JTextField txtHoraSaida;
    private javax.swing.JTextField txtPermanencia;
    private javax.swing.JTextField txtPlacaA;
    private javax.swing.JTextField txtValorAPagar;
    private javax.swing.JTextField txtValorTotal;
    // End of variables declaration//GEN-END:variables
}
