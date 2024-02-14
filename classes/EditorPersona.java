package classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorPersona extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField telefonoField;
    private JTextField indirizzoField;
    private JTextField etaField;

    public EditorPersona(RubricaGUI parent, Persona persona) {
        super("Editor Persona");

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);
        panel.add(new JLabel("Cognome:"));
        cognomeField = new JTextField();
        panel.add(cognomeField);
        panel.add(new JLabel("Telefono:"));
        telefonoField = new JTextField();
        panel.add(telefonoField);
        panel.add(new JLabel("Indirizzo:"));
        indirizzoField = new JTextField();
        panel.add(indirizzoField);
        panel.add(new JLabel("Età:"));
        etaField = new JTextField();
        panel.add(etaField);

        JButton salvaButton = new JButton("Salva");
        JButton annullaButton = new JButton("Annulla");
        panel.add(salvaButton);
        panel.add(annullaButton);

        salvaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String cognome = cognomeField.getText();
                String telefono = telefonoField.getText();
                String indirizzo = indirizzoField.getText();
                int eta = Integer.parseInt(etaField.getText());

                if (nome.isEmpty() || cognome.isEmpty() || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(EditorPersona.this, "Inserisci tutti i campi obbligatori (Nome, Cognome, Telefono)", "Errore", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (persona == null) {
                        parent.aggiungiPersona(new Persona(nome, cognome, telefono, indirizzo, eta));
                    } else {
                        parent.modificaPersona(parent.rubrica.indexOf(persona), new Persona(nome, cognome, telefono, indirizzo, eta));
                    }
                    dispose();
                }
            }
        });

        annullaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        if (persona != null) {
            nomeField.setText(persona.getNome());
            cognomeField.setText(persona.getCognome());
            telefonoField.setText(persona.getTelefono());
            indirizzoField.setText(persona.getIndirizzo());
            etaField.setText(Integer.toString(persona.getEta()));
        }

        add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }
}

