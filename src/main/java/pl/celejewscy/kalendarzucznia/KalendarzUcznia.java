package pl.celejewscy.kalendarzucznia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class KalendarzUcznia extends JFrame {

    public static void main(String[] args) {
        KalendarzUcznia naszaAplikacja = new KalendarzUcznia();
        naszaAplikacja.setVisible(true);
    }

    private JTextField data = new JTextField();
    private JTextArea zadanie = new JTextArea();
    private JTextArea lista = new JTextArea();
    private JButton przycisk = new JButton("Dodaj zadanie");

    public KalendarzUcznia() {
        this.setSize(700, 400);
        this.setLocation(400,400);

        this.setLayout(new GridBagLayout());
        this.add(new JLabel("Data:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
        this.add(data, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

        this.add(new JLabel("Zadanie:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
        this.add(zadanie, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 1, 1));

        this.add(new JLabel("Lista"), new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
        this.add(lista, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 1, 1));

        this.add(przycisk, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        ActionListener dodawaczka = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Class.forName("com.mysql.jdbc.Driver");

                    Connection polaczenie = DriverManager.getConnection("jdbc:mysql://192.168.0.33/kalendarz_ucznia", "kalucz", "kacper");
                    Statement polecenie = polaczenie.createStatement();
                    polecenie.execute("insert into zadania (id, data, zadanie) values (null, '" + data.getText() + "', '" + zadanie.getText() + "');");
                    polaczenie.close();
                    data.setText("");
                    zadanie.setText("");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };

        KeyListener obslugiwaczKlawiszyPolaData = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    zadanie.grabFocus();
                }
            }
        };

        KeyListener obslugiwaczKlawiszyPolaZadanie = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_F && e.getModifiers() == 2) {
                    System.out.println("Teraz zapisz");
                }
            }
        };

        data.addKeyListener(obslugiwaczKlawiszyPolaData);
        zadanie.addKeyListener(obslugiwaczKlawiszyPolaZadanie);

        przycisk.addActionListener(dodawaczka);

        try {

            String zadania = "";

            Class.forName("com.mysql.jdbc.Driver");

            Connection polaczenie = DriverManager.getConnection("jdbc:mysql://192.168.0.33/kalendarz_ucznia", "kalucz", "kacper");
            Statement polecenie = polaczenie.createStatement();
            ResultSet resultSet = polecenie.executeQuery("select * from zadania");
            while (resultSet.next()) {
                String data = resultSet.getString(2);
                String zadanie = resultSet.getString(3);
                zadania += "  " + data + " " + zadanie.replaceAll("\n", " ") + "\n";
                zadania += "-----------------------------\n";
            }

            lista.setText(zadania);

            polaczenie.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }


        zadanie.setBackground(Color.yellow);

    }
}
