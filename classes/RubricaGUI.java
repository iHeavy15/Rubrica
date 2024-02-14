package classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

public class RubricaGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Vector<Persona> rubrica;
    private DefaultTableModel tableModel;
    private JTable table;
    private final String FOLDER_PATH = "informazioni";

    public RubricaGUI() {
        super("Rubrica Telefonica");
        rubrica = new Vector<>();

        // Creazione e configurazione della tabella
        String[] columnNames = {"Nome", "Cognome", "Telefono"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Carica i dati dalla cartella dopo l'inizializzazione della tabella
        caricaDatiDaCartella();

        // Creazione della barra degli strumenti
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // Impedisci alla barra degli strumenti di essere trascinata

        // Aggiunta dei bottoni alla barra degli strumenti
        JButton nuovoButton = new JButton("Nuovo");
        JButton modificaButton = new JButton("Modifica");
        JButton eliminaButton = new JButton("Elimina");

        toolBar.add(nuovoButton);
        toolBar.add(modificaButton);
        toolBar.add(eliminaButton);

        add(toolBar, BorderLayout.NORTH); // Aggiunta della barra degli strumenti in cima alla finestra

        // Aggiunta dei listener ai bottoni
        nuovoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apriEditorPersona(null);
            }
        });

        modificaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    apriEditorPersona(rubrica.get(selectedRow));
                } else {
                    JOptionPane.showMessageDialog(RubricaGUI.this, "Seleziona una persona da modificare", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eliminaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Persona persona = rubrica.get(selectedRow);
                    int result = JOptionPane.showConfirmDialog(RubricaGUI.this, "Eliminare la persona " + persona.getNome() + " " + persona.getCognome() + "?", "Conferma", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        rubrica.remove(selectedRow);
                        tableModel.removeRow(selectedRow);
                        eliminaFilePersona(persona);
                    }
                } else {
                    JOptionPane.showMessageDialog(RubricaGUI.this, "Seleziona una persona da eliminare", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
    }

    private void apriEditorPersona(Persona persona) {
        EditorPersona editorPersona = new EditorPersona(this, persona);
        editorPersona.setVisible(true);
    }

    public void aggiungiPersona(Persona persona) {
        rubrica.add(persona);
        Object[] rowData = {persona.getNome(), persona.getCognome(), persona.getTelefono()};
        tableModel.addRow(rowData);
        salvaPersonaSuFile(persona);
    }

    public void modificaPersona(int index, Persona persona) {
        Persona personaPrecedente = rubrica.get(index);
        rubrica.set(index, persona);
        tableModel.setValueAt(persona.getNome(), index, 0);
        tableModel.setValueAt(persona.getCognome(), index, 1);
        tableModel.setValueAt(persona.getTelefono(), index, 2);
        salvaPersonaSuFile(persona);

        // Elimina il file associato al contatto precedente
        eliminaFilePersona(personaPrecedente);
    }

    private void caricaDatiDaCartella() {
        File folder = new File(FOLDER_PATH);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(file));
                            String line = reader.readLine();
                            if (line != null) {
                                String[] parts = line.split(";");
                                if (parts.length == 5) {
                                    String nome = parts[0];
                                    String cognome = parts[1];
                                    String telefono = parts[2];
                                    String indirizzo = parts[3];
                                    int eta = Integer.parseInt(parts[4]);
                                    Persona persona = new Persona(nome, cognome, telefono, indirizzo, eta);
                                    rubrica.add(persona);
                                    Object[] rowData = {nome, cognome, telefono};
                                    tableModel.addRow(rowData);
                                }
                            }
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void salvaPersonaSuFile(Persona persona) {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String nomeFile = FOLDER_PATH + "/" + persona.getNome() + "-" + persona.getCognome() + "-" + persona.getTelefono() + ".txt";
        try {
            FileWriter writer = new FileWriter(nomeFile);
            writer.write(persona.getNome() + ";" + persona.getCognome() + ";" + persona.getTelefono() + ";" + persona.getIndirizzo() + ";" + persona.getEta() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminaFilePersona(Persona persona) {
        String nomeFile = FOLDER_PATH + "/" + persona.getNome() + "-" + persona.getCognome() + "-" + persona.getTelefono() + ".txt";
        File file = new File(nomeFile);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RubricaGUI();
            }
        });
    }
}

