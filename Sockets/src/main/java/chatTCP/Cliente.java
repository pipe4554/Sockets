package chatTCP;

import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Cliente {
	/**
	* Clase Main desde donde se incia la ventana del cliente
	*/
	public static void main(String[] args) {
		VentanaCliente Vclient = new VentanaCliente();//Instancia de la ventana cliente 
		//Con la siguiente operacion, el programa se finaliza al cerrar la ventana de este
		Vclient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
/**
* Ventana del cliente cons sus caracteristicas e inicializacion
*/
class VentanaCliente extends JFrame {
	public VentanaCliente() {
		setTitle("Cliente");//Nombre de la ventana
		setBounds(700, 300, 700, 1000);//Coordenadas y dimensiones de la ventana al iniciarse
		
		//Capa de la ventana que muestra los datos del cliente y sus funciones
		LaminaCliente Lclient = new LaminaCliente();
		add(Lclient);//Se Añade la capa a la ventana

		setVisible(true);//Se indica que la ventana debe ser visible

	}
}
/**
* Lamina del cliente que funciona para mostrar las caracteristicas y funcionalidades de este,
* implementa las interfaz Runnable para lanzar hilos que serviran para la conexion con el servidor
*/
class LaminaCliente extends JPanel implements Runnable {
	private JLabel texto = new JLabel("Cliente"); //Texto que aparece al lado del cuadro de escritura
	private JTextField Campo1 = new JTextField(20);//Area de escritura del cliente
	private JButton enviarBut = new JButton("Enviar");//Boton que sirve para enviar el mensaje
	private JTextArea Chat = new JTextArea(20, 30);//Area de texto que mostrara los datos del servidor
	
	public LaminaCliente() {//Constructor 
		add(texto);//Anade el texto "cliente"
		add(Campo1);//Anade el campo de escritura

		EnviaTexto miEvento = new EnviaTexto();//Crea la instancia de un ActionListener
		enviarBut.addActionListener(miEvento);//Ahora el boton de enviar tiene asociado ese ActionListener

		add(enviarBut);//Anade el boton de "Enviar"

		add(Chat);//Anade el Area de texto de datos del servidor
		//Crea una instancia Thread de la clase LaminaCliente gracias al polimorfismo la cual crea un servidor para recibir mensaje del servidor
		Thread HiloRecep = new Thread(this);
		HiloRecep.start();//Inicia el hilo

	}
	/**
	* Clase asociada al ActionListener que se activa al pulsar el boton.
	* Crea una conexion con el servidor que le envia el mensaje del Campo1(Area de texto)
	*/
	private class EnviaTexto implements ActionListener {
		//-----------------------------------Socket emisor de mensajes
		private static final String IP = "localhost"; //Direccion IP del Sockect
		private static final int PUERTOServer = 9999;//Puerto que se utiliza para la conexion
		//Metodo de ActionListener que se ejecuta al pulsar el boton "Enviar"
		public void actionPerformed(ActionEvent e) {
			try {
				Socket SocketEnvio = new Socket(IP, PUERTOServer);//Se crea la conexion con la IP y el puerto de esta
				//Se crea un flujo de salida de datos para intercambiar mensajes con el servidor
				DataOutputStream flujoSalida = new DataOutputStream(SocketEnvio.getOutputStream());
				//Escribe la informacion en el flujo a partir de lo escrito en el area de texto
				flujoSalida.writeUTF(Campo1.getText());
				flujoSalida.close();//cierra el flujo de salida de datos

			} catch (UnknownHostException Ue) {//Error por no encontrar un servidor 
				System.out.println(Ue.getMessage());
				Ue.printStackTrace();
			} catch (IOException IOe) {//Error en caso de problemas con la conexion
				System.out.println(IOe.getMessage());
				IOe.printStackTrace();
			}

		}

	}
	/**
	* Con este metodo crea un sockect que funciona como "servidor" y que solo recibe datos del Servidor 
	*/
	public void run() {
		final int PUERTORecep = 9090;//Puerto por el cual recibe datos
		try {	
			//"Servidor" del cliente que recibe los datos del Servidor
			ServerSocket ServerCliente = new ServerSocket(PUERTORecep);
			Socket cliente;//Crea una conexion para recibir los datos
			
			while (true) {//Esta zona del cliente esta constantemente recibiendo los datos del servidor
				cliente = ServerCliente.accept();//Acepta la conexion del cliente dentro de su servidor e inicializa
				//Flujo de entrada de datos el cual recibe el mensaje del Servidor
				DataInputStream flujoEntrada = new DataInputStream(cliente.getInputStream());
				String Mensaje = flujoEntrada.readUTF();//Lee el mensaje recibido y lo almacena en un String
				Chat.append(Mensaje);//El mensaje se anade al chat para mostrarlo
				cliente.close();//cierra la conexion
			}
			

		} catch (IOException IOe) {//Error de conexion
			System.out.println(IOe.getMessage());
		}

	}
}
