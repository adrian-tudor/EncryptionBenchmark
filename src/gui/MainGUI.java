package gui;

import attack.HashCollision;
import attack.ParallelBruteForceAES;
import attack.ParallelDictionaryAttackAES;
import utils.TimerUtil;
import org.jfree.chart.ChartPanel;
import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private final ResultsTableModel tableModel = new ResultsTableModel();
    private final TestRunner testRunner = new TestRunner(tableModel);
    private final JTextField threadsField = new JTextField("4", 5);
    private final JTextField knownTextField = new JTextField("Lorem", 10);
    private final JTextField maxKeyLengthField = new JTextField("3", 3);

    public MainGUI() {
        setTitle("Encryption Benchmark");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTable resultsTable = new JTable(tableModel);

        // Criptare/Hashing Buttons
        JButton runCryptoBtn = new JButton("Teste Criptare (Simetric/Asimetric)");
        JButton runHashingBtn = new JButton("Teste Hashing");
        JButton saveResultsBtn = new JButton("Salveaza Rezultate");
        JButton chartBtn = new JButton("Vezi Grafic");

        runCryptoBtn.addActionListener(e -> runCryptoTests());
        runHashingBtn.addActionListener(e -> runHashingTests());
        saveResultsBtn.addActionListener(e -> saveResults());
        chartBtn.addActionListener(e -> showChart());

        // Atacuri Buttons
        JButton dictionaryAttackBtn = new JButton("Dictionary Attack AES");
        JButton bruteForceAttackBtn = new JButton("Brute-Force Attack AES");
        JButton hashCollisionBtn = new JButton("Simuleaza Coliziune Hash");

        dictionaryAttackBtn.addActionListener(e -> runDictionaryAttack());
        bruteForceAttackBtn.addActionListener(e -> runBruteForceAttack());
        hashCollisionBtn.addActionListener(e -> runHashCollision());

        JPanel attackPanel = new JPanel(new FlowLayout());
        attackPanel.setBorder(BorderFactory.createTitledBorder("Atacuri"));
        attackPanel.add(new JLabel("Threads:"));
        attackPanel.add(threadsField);
        attackPanel.add(new JLabel("Text Cunoscut:"));
        attackPanel.add(knownTextField);
        attackPanel.add(new JLabel("Max Lungime Parola:"));
        attackPanel.add(maxKeyLengthField);
        attackPanel.add(dictionaryAttackBtn);
        attackPanel.add(bruteForceAttackBtn);
        attackPanel.add(hashCollisionBtn);

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Teste"));
        controlPanel.add(runCryptoBtn);
        controlPanel.add(runHashingBtn);
        controlPanel.add(saveResultsBtn);
        controlPanel.add(chartBtn);

        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        add(attackPanel, BorderLayout.SOUTH);
    }

    private void runCryptoTests() {
        try {
            testRunner.runCryptoTests();
            JOptionPane.showMessageDialog(this, "Teste de criptare finalizate!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare: " + e.getMessage());
        }
    }

    private void runHashingTests() {
        try {
            testRunner.runHashingTests();
            JOptionPane.showMessageDialog(this, "Teste de hashing finalizate!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare: " + e.getMessage());
        }
    }

    private void runDictionaryAttack() {
        try {
            int threadCount = Integer.parseInt(threadsField.getText());
            String knownText = knownTextField.getText();
            if (knownText.isBlank()) throw new IllegalArgumentException("Text cunoscut lipsa");

            TimerUtil timer = new TimerUtil("Dictionary Attack AES");
            timer.start();
            boolean found = ParallelDictionaryAttackAES.attack("resources/encrypted_aes.txt", "resources/dictionary.txt", knownText, threadCount);
            double time = timer.stopAndGet();
            tableModel.addResult("AES", found ? "Dictionary Attack (Found)" : "Dictionary Attack (Not Found)", time);
            JOptionPane.showMessageDialog(this, found ? "Parola gasita" : "Parola NU a fost gasita");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la Dictionary Attack: " + e.getMessage());
        }
    }

    private void runBruteForceAttack() {
        try {
            int threadCount = Integer.parseInt(threadsField.getText());
            int maxLength = Integer.parseInt(maxKeyLengthField.getText());
            String knownText = knownTextField.getText();

            TimerUtil timer = new TimerUtil("Brute-force AES");
            timer.start();
            boolean found = ParallelBruteForceAES.attack("resources/encrypted_aes.txt", knownText, maxLength, threadCount);
            double time = timer.stopAndGet();
            tableModel.addResult("AES", found ? "Brute-force (Found)" : "Brute-force (Not Found)", time);
            JOptionPane.showMessageDialog(this, found ? "Parola gasita" : "Parola NU a fost gasita");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la Brute-force: " + e.getMessage());
        }
    }

    private void runHashCollision() {
        try {
            TimerUtil timer = new TimerUtil("Simulare Coliziune Hash MD5");
            timer.start();
            boolean found = HashCollision.simulateHashCollisions("MD5", 100000);
            double time = timer.stopAndGet();
            tableModel.addResult("MD5", found ? "Coliziune gasita" : "Nicio coliziune", time);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare simulare coliziune: " + e.getMessage());
        }
    }

    private void saveResults() {
        try {
            FileManager.saveResults("resources/results.csv", tableModel.getData());
            JOptionPane.showMessageDialog(this, "Rezultatele au fost salvate!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la salvare!");
        }
    }

    private void showChart() {
        JFrame chartFrame = new JFrame("Grafic Performanta");
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ChartGenerator chartGen = new ChartGenerator();
        chartFrame.setContentPane(chartGen.createBarChart(tableModel.getData()));
        chartFrame.setSize(800, 600);
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
