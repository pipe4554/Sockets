package otros;

import javax.swing.*;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Servidor {
	/**
	 * Clase Main donde se incia el servidor
	 * 
	 */
	public static void main(String[] args) {
		VentanaServidor Vserver = new VentanaServidor();// Instancia de la ventana Servidor
		// Con la siguiente operacion, el programa se finaliza al cerrar la ventana de
		// este
		Vserver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

/**
 * Ventana del Servidor con sus caracteristicas e inicializacion
 */
class VentanaServidor extends JFrame implements Runnable {
	JTextArea AreaTexto = new JTextArea();// Area de texto donde apareceran los mensajes del cliente
	/*
	 * constructor de la venta del servidor
	 */

	public VentanaServidor() {
		setTitle("Servidor");// Nombre de la ventana
		setBounds(700, 300, 1000, 700);// Coordenadas y dimensiones de la ventana al iniciarse

		JPanel miLamina = new JPanel();// Lamina para anadir los elementos
		miLamina.setLayout(new BorderLayout());// Diseno de la lamina

		miLamina.add(AreaTexto, BorderLayout.CENTER);// Se anade el area de texto a la lamina y se centra
		add(miLamina);// Se anade la lamina a la ventana

		setVisible(true);// Se indica que la ventana debe ser visible

		// Crea una instancia Thread de la clase LaminaCliente gracias al polimorfismo
		// la cual crea un servidor para recibir mensaje del servidor
		Thread HiloRecep = new Thread(this);//
		HiloRecep.start();// Inicia el hilo

	}

	public void run() {

		try {
			// Inicio del socket servidor
			ServerSocket servidor = new ServerSocket(9999);
			while (true) {
				// Acepta la conexion con el socket servidor para crear un socket de entrada de
				// datos
				Socket miSocket = servidor.accept();
				// Crea un flujo de entrada para recibir datos del cliente
				DataInputStream flujoEntrada = new DataInputStream(miSocket.getInputStream());
				String Mensaje = flujoEntrada.readUTF();// Lee el mensaje enviado desde el cliente
				AreaTexto.append("Mensaje recibido" + Mensaje + "\n");// Muestra el mensaje en la ventana del servidor
				miSocket.close();// Cierra el socket de entrada de datos

				Mensaje = this.CambiarCadena(Mensaje);// Formatea el mensaje y anade mas elementos
				// Crea un socket para el envio de datos
				Socket SocketEnvio = new Socket("localhost", 9090);
				// Crea un flujo de salida de datos
				DataOutputStream flujoSalida = new DataOutputStream(SocketEnvio.getOutputStream());
				// Escribe el mensaje en el flujo de salida
				flujoSalida.writeUTF(Mensaje);

				flujoSalida.close();// cierra el flujo de salida de datos
				SocketEnvio.close();// cierra el socket de envio de datos
			}

		} catch (IOException IOe) {
			System.out.println(IOe.getMessage());
			IOe.printStackTrace();
		}

	}

	/**
	 * Metodo para formatear la cadena y anadir un numero aleatorio, la fecha y la
	 * hora actual
	 */
	public String CambiarCadena(String cadena) {
		LocalDate FechaActual = LocalDate.now();
		LocalTime HoraActual = LocalTime.now();
		int randomNum = (int) Math.floor(Math.random() * (3000 - 1 + 1) + 1);

		String CadenaCambiada = cadena.toLowerCase();
		CadenaCambiada = CadenaCambiada.replace(' ', ';');

		CadenaCambiada = "Número aleatorio: " + randomNum + "\nFecha: " + FechaActual + "\nHora: " + HoraActual + "\n"
				+ CadenaCambiada + "\n";
		return CadenaCambiada;
	}
}
