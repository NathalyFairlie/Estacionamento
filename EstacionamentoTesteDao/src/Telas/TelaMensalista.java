/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Telas;

import Conexao.Conexao;
import Dao.DaoDesconto;
import Dao.DaoMensalista;
import Dao.DaoPagamentos;
import Dao.DaoValores;
import estacionamento1.Desconto;
import estacionamento1.Mensalista;
import estacionamento1.Pagamentos;
import estacionamento1.Valores;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TelaMensalista extends javax.swing.JFrame {

    private TelaPagamentos telaPagamentos;
    private TelaVeiculo telaVeiculo;
    private DaoDesconto daoDesconto = new DaoDesconto();
    private final Conexao conexao;
    private DaoValores daoValores = new DaoValores();
    private final DaoMensalista daoMensalista;
    private final String placa;
    private final LocalDate data = LocalDate.now();
    private String nomeDescontoSelecionado;

    public TelaMensalista(String placa) {
        initComponents();
        daoMensalista = new DaoMensalista();
        this.placa = placa;
        this.conexao = new Conexao();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = data.format(formatter);

        txtPlacaM.setText(placa);
        txtInicioVigencia.setText(dataFormatada);

        // Initialize with values from the listaMensalista
        Mensalista mensalista = getMensalistaFromList(placa);
        if (mensalista != null) {
            if (mensalista != null) {
                txtInicioVigencia.setText(mensalista.getInicioContrato().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            // Check if mesesContratados is not null and set txtMeses accordingly
            if (mensalista != null) {
                txtMeses.setText(String.valueOf(mensalista.getMesesContratados()));
            }

            // Check if fimContrato is not null and set txtFimVigencia accordingly
            if (mensalista.getFimContrato() != null) {
                txtFimVigencia.setText(mensalista.getFimContrato().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                txtValorTotal.setText(String.valueOf(mensalista.getValorTotal()));
                txtValorAPagar.setText(String.valueOf(mensalista.getValorComDesconto()));
            }

        }
    }

    private Mensalista getMensalistaFromList(String placa) {
        List<Mensalista> listaMensalistas = daoMensalista.listaMensalista();
        for (Mensalista mensalista : listaMensalistas) {
            if (mensalista.getPlaca().equalsIgnoreCase(placa)) {
                return mensalista;
            }
        }
        return null;
    }

    public void atualizarInformacoesMensalista(int mesesContratados, LocalDate fimContrato) {
        // Atualize as informações na tela conforme necessário
        txtMeses.setText(String.valueOf(mesesContratados));
        txtFimVigencia.setText(fimContrato.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

    }

    private double obterValorMensal() {
        // Obter a lista existente de valores
        List<Valores> listaValores = daoValores.listaValores();

        // Supondo que você está interessado na primeira linha da lista
        if (!listaValores.isEmpty()) {
            Valores valor = listaValores.get(0); // Pode ser ajustado conforme a lógica desejada

            // Retorna o valor da coluna mensal
            return valor.getMensal();
        }

        return 0.0; // Retorna 0 se a lista de valores estiver vazia
    }

    private double obterDescontoSelecionado() {
        // Certifique-se de que o ComboBox foi inicializado antes de chamar este método
        if (ComboBoxDesc == null) {
            // System.err.println("ComboBox não inicializado corretamente.");
            return 0.0;
        }

        String nomeDescontoSelecionado = (String) ComboBoxDesc.getSelectedItem();
        //System.out.println("Desconto selecionado: " + nomeDescontoSelecionado);

        // Verifica se a opção selecionada é "Não possui desconto"
        if ("Não possui desconto".equalsIgnoreCase(nomeDescontoSelecionado)) {
            return 0.0;
        }

        // Iterar sobre a lista de descontos
        List<Desconto> listaDescontos = daoDesconto.listaDesconto();
        for (Desconto desconto : listaDescontos) {
            String nomeDescontoLista = desconto.getNomeDesconto();

            // Tratar o caractere especial "~"
            nomeDescontoLista = nomeDescontoLista.replace("~", "Texto Desejado");

            // Comparar o item selecionado com o nome do desconto
            if (nomeDescontoLista.equalsIgnoreCase(nomeDescontoSelecionado)) {
                return desconto.getDesconto();

            }
        }

        return 0.0; // Retorna 0 se nenhum desconto foi encontrado
    }

    private String obterNomeDescontoSelecionado() {
        // Certifique-se de que o ComboBox foi inicializado antes de chamar este método
        if (ComboBoxDesc == null) {
            //System.err.println("ComboBox não inicializado corretamente.");
            return "erro";
        }

        String nomeDescontoSelecionado = (String) ComboBoxDesc.getSelectedItem();
        //System.out.println("Desconto selecionado: " + nomeDescontoSelecionado);

        // Verifica se a opção selecionada é "Não possui desconto"
        if ("Não possui desconto".equalsIgnoreCase(nomeDescontoSelecionado)) {
            return "Não possui desconto";
        }

        // Iterar sobre a lista de descontos
        List<Desconto> listaDescontos = daoDesconto.listaDesconto();
        for (Desconto desconto : listaDescontos) {
            String nomeDescontoLista = desconto.getNomeDesconto();

            // Tratar o caractere especial "~"
            nomeDescontoLista = nomeDescontoLista.replace("~", "Texto Desejado");

            // Comparar o item selecionado com o nome do desconto
            if (nomeDescontoLista.equalsIgnoreCase(nomeDescontoSelecionado)) {
                return nomeDescontoSelecionado;

            }
        }

        return "Não possui desconto";
    }

    private int validarMeses(String meses) {
        int mesesContratados = Integer.parseInt(meses);

        // Check if the entered value is a positive integer greater than 1
        if (mesesContratados <= 0) {
            throw new NumberFormatException("O número de meses deve ser um inteiro positivo maior que 0.");
        }

        return mesesContratados;
    }

    private double calcularValorComDesconto(double valorTotal, double percentualDesconto) {
        return valorTotal - (valorTotal * percentualDesconto / 100);
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
        lblPlacaM = new javax.swing.JLabel();
        txtPlacaM = new javax.swing.JTextField();
        txtMeses = new javax.swing.JTextField();
        lblMeses = new javax.swing.JLabel();
        txtInicioVigencia = new javax.swing.JTextField();
        lblInicioVigencia = new javax.swing.JLabel();
        txtFimVigencia = new javax.swing.JTextField();
        lblFimVigencia = new javax.swing.JLabel();
        btnContinuar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        ComboBoxDesc = new javax.swing.JComboBox<>();
        txtValorTotal = new javax.swing.JTextField();
        lblValorAPagar = new javax.swing.JLabel();
        btnCalcular = new javax.swing.JButton();
        lblValorAPagar1 = new javax.swing.JLabel();
        lblValorAPagar2 = new javax.swing.JLabel();
        txtValorAPagar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        pnlCabecalho.setBackground(new java.awt.Color(153, 0, 153));
        pnlCabecalho.setDoubleBuffered(false);

        lblCabecalho.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCabecalho.setForeground(new java.awt.Color(255, 255, 255));
        lblCabecalho.setText("Mensalista");

        javax.swing.GroupLayout pnlCabecalhoLayout = new javax.swing.GroupLayout(pnlCabecalho);
        pnlCabecalho.setLayout(pnlCabecalhoLayout);
        pnlCabecalhoLayout.setHorizontalGroup(
            pnlCabecalhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCabecalhoLayout.createSequentialGroup()
                .addGap(174, 174, 174)
                .addComponent(lblCabecalho)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCabecalhoLayout.setVerticalGroup(
            pnlCabecalhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCabecalhoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblCabecalho)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        lblPlacaM.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPlacaM.setLabelFor(txtPlacaM);
        lblPlacaM.setText("Placa:");

        txtPlacaM.setEditable(false);
        txtPlacaM.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPlacaM.setToolTipText("Placa do carro que quer se tornar mensalista");
        txtPlacaM.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtPlacaM.setEnabled(false);
        txtPlacaM.setName(""); // NOI18N

        txtMeses.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMeses.setToolTipText("Insira valor inteiro de quantos meses serão contratados");
        txtMeses.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        lblMeses.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMeses.setLabelFor(txtMeses);
        lblMeses.setText("Meses:");

        txtInicioVigencia.setEditable(false);
        txtInicioVigencia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInicioVigencia.setToolTipText("Inicio da vigencia do contrato mensal");
        txtInicioVigencia.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtInicioVigencia.setEnabled(false);

        lblInicioVigencia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblInicioVigencia.setLabelFor(txtInicioVigencia);
        lblInicioVigencia.setText("Início:");

        txtFimVigencia.setEditable(false);
        txtFimVigencia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtFimVigencia.setToolTipText("Fim da vigencia do contrato mensal calculado automaticamente");
        txtFimVigencia.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtFimVigencia.setEnabled(false);

        lblFimVigencia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblFimVigencia.setLabelFor(txtFimVigencia);
        lblFimVigencia.setText("Fim:");
        lblFimVigencia.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnContinuar.setBackground(new java.awt.Color(153, 0, 153));
        btnContinuar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnContinuar.setForeground(new java.awt.Color(255, 255, 255));
        btnContinuar.setText("Confirmar Pagamento");
        btnContinuar.setToolTipText("Clique aqui para confirmar o pagamento");
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
        ComboBoxDesc.setToolTipText("Selecione Desconto  a ser aplicado caso possua");
        ComboBoxDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxDescActionPerformed(evt);
            }
        });

        txtValorTotal.setEditable(false);
        txtValorTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtValorTotal.setToolTipText("Valor total a ser pago sem desconto");
        txtValorTotal.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtValorTotal.setEnabled(false);

        lblValorAPagar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblValorAPagar.setLabelFor(ComboBoxDesc);
        lblValorAPagar.setText("Desconto:");
        lblValorAPagar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnCalcular.setBackground(new java.awt.Color(153, 0, 153));
        btnCalcular.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        btnCalcular.setForeground(new java.awt.Color(255, 255, 255));
        btnCalcular.setText("Calcular");
        btnCalcular.setToolTipText("Clique aqui para calcular o valor a ser pago ");
        btnCalcular.setBorder(null);
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        lblValorAPagar1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblValorAPagar1.setLabelFor(txtValorTotal);
        lblValorAPagar1.setText("Valor Total:");
        lblValorAPagar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lblValorAPagar2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblValorAPagar2.setLabelFor(txtValorAPagar);
        lblValorAPagar2.setText("Valor a Pagar:");
        lblValorAPagar2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txtValorAPagar.setEditable(false);
        txtValorAPagar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtValorAPagar.setToolTipText("Valor a ser Pago após verificação de desconto");
        txtValorAPagar.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtValorAPagar.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCabecalho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPlacaM)
                            .addComponent(lblInicioVigencia))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtInicioVigencia, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                    .addComponent(txtPlacaM))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblMeses)
                                    .addComponent(lblFimVigencia))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFimVigencia)
                                    .addComponent(txtMeses)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnContinuar))))
                    .addComponent(btnVoltar)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblValorAPagar1)
                            .addComponent(lblValorAPagar)
                            .addComponent(lblValorAPagar2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtValorAPagar, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ComboBoxDesc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnlCabecalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPlacaM)
                    .addComponent(txtPlacaM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMeses)
                    .addComponent(txtMeses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFimVigencia)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblInicioVigencia)
                        .addComponent(txtInicioVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtFimVigencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblValorAPagar2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblValorAPagar1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ComboBoxDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblValorAPagar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValorAPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnVoltar)
                    .addComponent(btnContinuar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinuarActionPerformed

        try {
            String placa = txtPlacaM.getText();
            LocalDate data = LocalDate.now();
            double valorPago = Double.parseDouble(txtValorAPagar.getText());

            Pagamentos pagamento = new Pagamentos(placa, data, valorPago);

            // Use DaoPagamentos para salvar o pagamento no banco de dados
            DaoPagamentos daoPagamento = new DaoPagamentos();
            daoPagamento.addPagamento(pagamento);

            // Adiciona o mensalista apenas quando o botão "Continuar" for clicado
            int mesesContratados = validarMeses(txtMeses.getText());
            double valorTotal = Double.parseDouble(txtValorTotal.getText());
            double descontoSelecionado = obterDescontoSelecionado();
            double valorComDesconto = Double.parseDouble(txtValorAPagar.getText());
            String nomeDescontoSelecionado = obterNomeDescontoSelecionado();
            Mensalista novoMensalista = new Mensalista(placa, LocalDate.now(), mesesContratados);
            novoMensalista.setFimContrato(novoMensalista.calcularDataFinalVigenciaMensalista());
            novoMensalista.setValorTotal(valorTotal);
            novoMensalista.setNomeDesconto(nomeDescontoSelecionado);
            novoMensalista.setPercentualDesconto(descontoSelecionado);
            novoMensalista.setValorComDesconto(valorComDesconto);

            daoMensalista.addMensalista(novoMensalista);

            JOptionPane.showMessageDialog(this, "Pagamento salvo com sucesso! \n "
                    + "Voltando para página anterior");
            this.dispose();
            TelaVeiculo telaVeiculo = new TelaVeiculo((JFrame) SwingUtilities.getWindowAncestor(this));
            telaVeiculo.setVisible(true);

        } catch (DateTimeParseException | NumberFormatException e) {
            // Tratar exceções, se necessário
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnContinuarActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed

        this.dispose();
        TelaVeiculo telaVeiculo = new TelaVeiculo((JFrame) SwingUtilities.getWindowAncestor(this));
        telaVeiculo.setVisible(true);
        ComboBoxDesc.setEnabled(true);
        txtMeses.setEnabled(true);

    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        try {
            String placa = txtPlacaM.getText();
            LocalDate dataInicio = LocalDate.parse(txtInicioVigencia.getText());
            int mesesContratados = validarMeses(txtMeses.getText());

            double valorMensal = obterValorMensal();
            double valorTotal = valorMensal * mesesContratados;
            double descontoSelecionado = obterDescontoSelecionado();
            double valorComDesconto = calcularValorComDesconto(valorTotal, descontoSelecionado);
            String nomeDescontoSelecionado = obterNomeDescontoSelecionado();

            txtFimVigencia.setText(dataInicio.plusMonths(mesesContratados).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            txtValorTotal.setText(String.valueOf(valorTotal));
            ComboBoxDesc.setSelectedItem(nomeDescontoSelecionado);
            txtValorAPagar.setText(String.valueOf(valorComDesconto));

            JOptionPane.showMessageDialog(this, "Cadastro de mensalista salvo com sucesso!");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Preencha o campo 'Inicio Vigência' corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preencha o campo 'Meses' corretamente com números inteiros positivos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnCalcularActionPerformed

    private void handleComboBoxSelection() {
        String nomeDescontoSelecionado = (String) ComboBoxDesc.getSelectedItem();
        double valorTotal = Double.parseDouble(txtValorTotal.getText());
        double descontoSelecionado = obterDescontoSelecionado();
        double valorComDesconto = calcularValorComDesconto(valorTotal, descontoSelecionado);
        txtValorAPagar.setText(String.valueOf(valorComDesconto));
    }

    private void ComboBoxDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxDescActionPerformed
        handleComboBoxSelection();
    }//GEN-LAST:event_ComboBoxDescActionPerformed
// Método para calcular o valor com desconto

    public void ocultarBtnCalcular() {
        btnCalcular.setVisible(false);
    }

    public void ocultarBtnContinuar() {
        btnContinuar.setVisible(false);
    }

    public void ocultarBtnVoltar() {
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

    public void setTxtMesesDisable() {
        txtMeses.setEnabled(false);
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

    public JLabel getLblFimVigencia() {
        return lblFimVigencia;
    }

    public void setLblFimVigencia(JLabel lblFimVigencia) {
        this.lblFimVigencia = lblFimVigencia;
    }

    public JLabel getLblInicioVigencia() {
        return lblInicioVigencia;
    }

    public void setLblInicioVigencia(JLabel lblInicioVigencia) {
        this.lblInicioVigencia = lblInicioVigencia;
    }

    public JLabel getLblMeses() {
        return lblMeses;
    }

    public void setLblMeses(JLabel lblMeses) {
        this.lblMeses = lblMeses;
    }

    public JLabel getLblPlacaM() {
        return lblPlacaM;
    }

    public void setLblPlacaM(JLabel lblPlacaM) {
        this.lblPlacaM = lblPlacaM;
    }

    public JLabel getLblValorAPagar() {
        return lblValorAPagar;
    }

    public void setLblValorAPagar(JLabel lblValorAPagar) {
        this.lblValorAPagar = lblValorAPagar;
    }

    public JLabel getLblValorAPagar1() {
        return lblValorAPagar1;
    }

    public void setLblValorAPagar1(JLabel lblValorAPagar1) {
        this.lblValorAPagar1 = lblValorAPagar1;
    }

    public JLabel getLblValorAPagar2() {
        return lblValorAPagar2;
    }

    public void setLblValorAPagar2(JLabel lblValorAPagar2) {
        this.lblValorAPagar2 = lblValorAPagar2;
    }

    public JPanel getPnlCabecalho() {
        return pnlCabecalho;
    }

    public void setPnlCabecalho(JPanel pnlCabecalho) {
        this.pnlCabecalho = pnlCabecalho;
    }

    public JTextField getTxtFimVigencia() {
        return txtFimVigencia;
    }

    public void setTxtFimVigencia(JTextField txtFimVigencia) {
        this.txtFimVigencia = txtFimVigencia;
    }

    public JTextField getTxtInicioVigencia() {
        return txtInicioVigencia;
    }

    public void setTxtInicioVigencia(JTextField txtInicioVigencia) {
        this.txtInicioVigencia = txtInicioVigencia;
    }

    public JTextField getTxtMeses() {
        return txtMeses;
    }

    public void setTxtMeses(JTextField txtMeses) {
        this.txtMeses = txtMeses;
    }

    public JTextField getTxtPlacaM() {
        return txtPlacaM;
    }

    public void setTxtPlacaM(JTextField txtPlacaM) {
        this.txtPlacaM = txtPlacaM;
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
            java.util.logging.Logger.getLogger(TelaCadastroUsuarioSistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroUsuarioSistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroUsuarioSistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroUsuarioSistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JLabel lblFimVigencia;
    private javax.swing.JLabel lblInicioVigencia;
    private javax.swing.JLabel lblMeses;
    private javax.swing.JLabel lblPlacaM;
    private javax.swing.JLabel lblValorAPagar;
    private javax.swing.JLabel lblValorAPagar1;
    private javax.swing.JLabel lblValorAPagar2;
    private javax.swing.JPanel pnlCabecalho;
    private javax.swing.JTextField txtFimVigencia;
    private javax.swing.JTextField txtInicioVigencia;
    private javax.swing.JTextField txtMeses;
    private javax.swing.JTextField txtPlacaM;
    private javax.swing.JTextField txtValorAPagar;
    private javax.swing.JTextField txtValorTotal;
    // End of variables declaration//GEN-END:variables
}
