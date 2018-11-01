package com.company;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    Socket socket;
    JTextField message;
    JTextArea odebraneWiadomosci;
    PrintWriter writer;
    JFrame mainFrame;
    JButton buttonWyslij;
    BufferedReader czytelnik;
    String nickName;

    public static void main(String[] args) {
        new Main().doDziela();
    }

    private void doDziela() {

        mainFrame = new JFrame("Smieszny klient");
        JPanel panelGlowny = new JPanel();
        odebraneWiadomosci = new JTextArea(15, 50);
        odebraneWiadomosci.setLineWrap(true);
        odebraneWiadomosci.setWrapStyleWord(true);
        odebraneWiadomosci.setEditable(false);

        JScrollPane przewijanie = new JScrollPane(odebraneWiadomosci);
        DefaultCaret caret = (DefaultCaret) odebraneWiadomosci.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        przewijanie.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        przewijanie.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(530, 350);

        Box buttonBox = new Box(BoxLayout.LINE_AXIS);
        Box messageBox = new Box(BoxLayout.LINE_AXIS);

        buttonWyslij = new JButton("Wyslij");

        message = new JTextField(20);

        buttonBox.add(buttonWyslij);
        messageBox.add(message);

        mainFrame.getContentPane().add(BorderLayout.CENTER, panelGlowny);
        //mainFrame.getContentPane().add(BorderLayout.EAST, buttonBox);

        do {
            nickName = JOptionPane.showInputDialog("Enter your nickname: ");
        } while (nickName == null);

        JLabel nickLabel = new JLabel(nickName);

        mainFrame.setVisible(true);

        buttonWyslij.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                buttonWyslij.setBackground(Color.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonWyslij.setBackground(new JButton().getBackground());
            }
        });
        buttonWyslij.addActionListener(a->{
            writer.println(nickLabel.getText() + ": " + message.getText());
            writer.flush();
            message.setText("");
            message.requestFocus();
        });

        panelGlowny.add(nickLabel);
        panelGlowny.add(przewijanie);
        panelGlowny.add(message);
        panelGlowny.add(buttonWyslij);


        konfigurujPolaczenieSieciowe();

        Thread thread = new Thread(() -> {
            String wiadom;

            try {
                while ((wiadom = czytelnik.readLine()) != null) {
                    System.out.println("Odczytano " + wiadom);
                    odebraneWiadomosci.append(wiadom + "\n");

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();



    }

    private void konfigurujPolaczenieSieciowe() {
        try {
            socket = new Socket("127.0.0.1", 5000);
            writer = new PrintWriter(socket.getOutputStream());
            InputStreamReader czytelnikStrm = new InputStreamReader(socket.getInputStream());
            czytelnik = new BufferedReader(czytelnikStrm);
            System.out.println("Connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
