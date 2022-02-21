package chatTCP;

import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Cliente {

	public static void main(String[] args) {
		VentanaCliente Vclient = new VentanaCliente();
		Vclient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class VentanaCliente extends JFrame {
	public VentanaCliente() {
		setTitle("Cliente");
		setBounds(700, 300, 700, 1000);

		LaminaCliente Lclient = new LaminaCliente();
		add(Lclient);

		setVisible(true);

	}
}

class LaminaCliente extends JPanel implements Runnable {
	private JLabel texto = new JLabel("Cliente");
	private JTextField Campo1 = new JTextField(20);
	private JButton enviarBut = new JButton("Enviar");
	private JTextArea Chat = new JTextArea(20, 30);

	public LaminaCliente() {
		add(texto);
		add(Campo1);

		EnviaTexto miEvento = new EnviaTexto();
		enviarBut.addActionListener(miEvento);

		add(enviarBut);

		add(Chat);

		Thread HiloRecep = new Thread(this);
		HiloRecep.start();

	}

	private class EnviaTexto implements ActionListener {
		//Socket emisor de mensajes
		private static final String IP = "localhost";
		private static final int PUERTOServer = 9999;

		public void actionPerformed(ActionEvent e) {
			try {
				Socket SocketEnvio = new Socket(IP, PUERTOServer);
				DataOutputStream flujoSalida = new DataOutputStream(SocketEnvio.getOutputStream());
				flujoSalida.writeUTF(Campo1.getText());
				flujoSalida.close();

			} catch (UnknownHostException Ue) {
				System.out.println(Ue.getMessage());
				Ue.printStackTrace();
			} catch (IOException IOe) {
				System.out.println(IOe.getMessage());
				IOe.printStackTrace();
			}

		}

	}

	public void run() {
		final int PUERTORecep = 9090;
		try {
			ServerSocket ServerCliente = new ServerSocket(PUERTORecep);
			Socket cliente;
			
			while (true) {
				cliente = ServerCliente.accept();
				DataInputStream flujoEntrada = new DataInputStream(cliente.getInputStream());
				String Mensaje = flujoEntrada.readUTF();
				Chat.append(Mensaje);
				cliente.close();
			}
			

		} catch (IOException IOe) {
			System.out.println(IOe.getMessage());
		}

	}
}
