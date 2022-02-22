package chatTCP;

import javax.swing.*;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Servidor {

	public static void main(String[] args) {
		VentanaServidor Vserver = new VentanaServidor();
		Vserver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class VentanaServidor extends JFrame implements Runnable {
	JTextArea AreaTexto = new JTextArea();

	public VentanaServidor() {
		setTitle("Servidor");
		setBounds(700, 300, 1000, 700);

		JPanel miLamina = new JPanel();
		miLamina.setLayout(new BorderLayout());

		miLamina.add(AreaTexto, BorderLayout.CENTER);
		add(miLamina);

		setVisible(true);

		Thread HiloRecep = new Thread(this);
		HiloRecep.start();

	}

	public void run() {
		

		try {
			ServerSocket servidor = new ServerSocket(9999);
			while (true) {

				Socket miSocket = servidor.accept();
				DataInputStream flujoEntrada = new DataInputStream(miSocket.getInputStream());
				String Mensaje = flujoEntrada.readUTF();
				AreaTexto.append("Mensaje recibido" + Mensaje+ "\n");
				miSocket.close();
				
				LocalDate FechaActual = LocalDate.now();
				LocalTime HoraActual = LocalTime.now();
				
				Mensaje = this.CambiarCadena(Mensaje);
				
				
				Socket SocketEnvio = new Socket("localhost", 9090);
				DataOutputStream flujoSalida = new DataOutputStream(SocketEnvio.getOutputStream());
				int randomNum = (int) Math.floor(Math.random()*(3000-1+1)+1);
				flujoSalida.writeUTF("Número aleatorio: " + randomNum + "\nFecha: " + FechaActual + "\nHora: " + HoraActual+ "\n"+ Mensaje + "\n");
				
				flujoSalida.close();
				SocketEnvio.close();
			}
			
			
			
			

		} catch (IOException IOe) {
			System.out.println(IOe.getMessage());
			IOe.printStackTrace();
		}

	}
	
	public String CambiarCadena(String cadena) {
		String CadenaCambiada = cadena.toLowerCase();
		CadenaCambiada = CadenaCambiada.replace(' ', ';');
		return CadenaCambiada;
	}
}
