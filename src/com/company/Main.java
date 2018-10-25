package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    Socket socket;
    JTextField message;
    PrintWriter writer;
    JFrame mainFrame;
    JButton buttonWyslij;

    public static void main(String[] args) {
        new Main().doDziela();
    }

    private void doDziela() {

        mainFrame = new JFrame("Smieszny klient");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 90);

        Box buttonBox = new Box(BoxLayout.LINE_AXIS);
        Box messageBox = new Box(BoxLayout.LINE_AXIS);

        buttonWyslij = new JButton("Wyslij");

        message = new JTextField(20);

        buttonBox.add(buttonWyslij);
        messageBox.add(message);

        mainFrame.getContentPane().add(BorderLayout.CENTER, messageBox);
        mainFrame.getContentPane().add(BorderLayout.EAST, buttonBox);

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
            writer.println(message.getText());
            writer.flush();
            message.setText("");
            message.requestFocus();
        });

        konfigurujPolaczenieSieciowe();
    }

    private void konfigurujPolaczenieSieciowe() {
        try {
            socket = new Socket("127.0.0.1", 4242);
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("Connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
