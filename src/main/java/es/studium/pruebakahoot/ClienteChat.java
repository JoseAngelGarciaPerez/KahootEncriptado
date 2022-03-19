package es.studium.pruebakahoot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;


public class ClienteChat extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	Socket socket;
	ObjectInputStream fentradaPregunta;
	DataInputStream fentrada;
	DataOutputStream fsalida;
	//ObjectOutputStream fsalida;
	String nombre;
	boolean repetir = true;
	JLabel lbl_pregunta=new JLabel("PREGUNTA");
	int contador;

	
	ButtonGroup grupo=new ButtonGroup();
	JRadioButton respuesta1=new JRadioButton("texto1");
	JRadioButton respuesta2=new JRadioButton("texto2");
	JRadioButton respuesta3=new JRadioButton("texto3");
	JRadioButton respuesta4=new JRadioButton("texto4");

	

	JTextArea area=new JTextArea();

	JButton btn_enviar= new JButton("Enviar");

	Preguntas  pregunta;

	public ClienteChat(Socket socket, String nombre) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchProviderException {
		// Prepara la pantalla. Se recibe el socket creado y el nombre del cliente
		super(" Conexi�n del cliente chat: " + nombre);
		setTitle("Cliente 1");
		setBounds(100, 100, 500, 600);
		//JPanel
		JPanel panel= new JPanel();		
		panel.setLayout(null);	

		lbl_pregunta.setBounds(20, 1, 500, 100);
		panel.add(lbl_pregunta);

		respuesta1.setBounds(50, 100, 500, 50);
		grupo.add(respuesta1);
		respuesta2.setBounds(50, 150, 500, 50);
		grupo.add(respuesta2);
		respuesta3.setBounds(50, 200, 500, 50);
		grupo.add(respuesta3);
		respuesta4.setBounds(50, 250, 500, 50);
		grupo.add(respuesta4);

		panel.add(respuesta1);
		panel.add(respuesta2);
		panel.add(respuesta3);
		panel.add(respuesta4);

		area.setBounds(20, 350, 300, 200);
		panel.add(area);

		btn_enviar.setBounds(350, 380, 100, 150);
		panel.add(btn_enviar);

		add(panel);

		//Fin JPanel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		btn_enviar.addActionListener(this);
		this.socket = socket;
		this.nombre = nombre;
		
		System.out.println("ESTE ES EL NOMBRE = " + nombre);
		//Se crean los flujos de entrada y salida.
		//En el flujo de salida se escribe un mensaje
		//indicando que el cliente se ha unido al Chat.
		//El HiloServidor recibe este mensaje y
		//lo reenv�a a todos los clientes conectados
		try {
			fentrada = new DataInputStream(socket.getInputStream());
			fsalida = new DataOutputStream(socket.getOutputStream());
			//fsalida = new ObjectOutputStream(socket.getOutputStream());
			String texto = encriptarNombreCliente(nombre);
			//NombreCliente nc = new NombreCliente(nombre);
			//nc.setNombre(encriptarNombreCliente(nc));
			//fsalida.writeObject(nc);
			fsalida.writeUTF(texto);
		} catch (IOException ex) {
			System.out.println("Error de E/S");
			ex.printStackTrace();
			System.exit(0);
		}
	}

	//El m�todo main es el que lanza el cliente,
	//para ello en primer lugar se solicita el nombre o nick del
	//cliente, una vez especificado el nombre
	//se crea la conexi�n al servidor y se crear la pantalla del Chat(ClientChat)
	//lanzando su ejecuci�n (ejecutar()).
	public static void main(String[] args) throws Exception {
		int puerto = 44444;
		String nombre = JOptionPane.showInputDialog("Introduce tu nombre o nick:");
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", puerto);
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Imposible conectar con el servidor \n" + ex.getMessage(),
					"<<Mensaje de Error:1>>", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		if (!nombre.trim().equals("")) {
			ClienteChat cliente = new ClienteChat(socket, nombre);
			cliente.setBounds(0, 0, 500, 600);
			cliente.setVisible(true);
			cliente.ejecutar();
		} else {
			System.out.println("El nombre est� vac�o...");
		}
	}

	// Cuando se pulsa el bot�n Enviar,
	// el mensaje introducido se env�a al servidor por el flujo de salida
	public void actionPerformed(ActionEvent e)
 {
		if (e.getSource() == btn_enviar) {
			String texto=area.getText();
			if(respuesta1.isSelected())
			{
				grupo.clearSelection();
				int correcta = comprobarPregunta(respuesta1.getText(), pregunta.getCorrecta());
				System.out.println(correcta + "Cliente Seleccionada");
				try {
					EnviarResultado(correcta);
				} catch (InvalidKeyException e1) {e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {e1.printStackTrace();
				} catch (BadPaddingException e1) {e1.printStackTrace();
				} catch (InvalidKeySpecException e1) {e1.printStackTrace();
				} catch (NoSuchProviderException e1) {e1.printStackTrace();
				}
			}
			else if(respuesta2.isSelected())
			{
			
				respuesta2.setSelected(false);
				int correcta = comprobarPregunta(respuesta2.getText(), pregunta.getCorrecta());
				System.out.println(correcta+ "Cliente Seleccionada");
				try {
					EnviarResultado(correcta);
				} catch (InvalidKeyException e1) {e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {e1.printStackTrace();
				} catch (BadPaddingException e1) {e1.printStackTrace();
				} catch (InvalidKeySpecException e1) {e1.printStackTrace();
				} catch (NoSuchProviderException e1) {e1.printStackTrace();
				}
			}
			else if(respuesta3.isSelected())
			{
				respuesta3.setSelected(false);
				int correcta = comprobarPregunta(respuesta3.getText(), pregunta.getCorrecta());
				System.out.println(correcta+ "Cliente Seleccionada");
				try {
					EnviarResultado(correcta);
				} catch (InvalidKeyException e1) {e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {e1.printStackTrace();
				} catch (BadPaddingException e1) {e1.printStackTrace();
				} catch (InvalidKeySpecException e1) {e1.printStackTrace();
				} catch (NoSuchProviderException e1) {e1.printStackTrace();
				}
			}
			else if(respuesta4.isSelected())
			{
				respuesta4.setSelected(false);
				int correcta = comprobarPregunta(respuesta4.getText(), pregunta.getCorrecta());
				System.out.println(correcta+ "Cliente Seleccionada");
				try {
					EnviarResultado(correcta);
				} catch (InvalidKeyException e1) {e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {e1.printStackTrace();
				} catch (BadPaddingException e1) {e1.printStackTrace();
				} catch (InvalidKeySpecException e1) {e1.printStackTrace();
				} catch (NoSuchProviderException e1) {e1.printStackTrace();
				}
			}
			grupo.clearSelection();
			try {
				area.setText("");
				fsalida.writeUTF(texto);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		// Si se pulsa el bot�n Salir,
		// se env�a un mensaje indicando que el cliente abandona el chat
		// y tambi�n se env�a un * para indicar
		// al servidor que el cliente se ha cerrado
		/*	else if (e.getSource() == btn_enviar) {
			String texto = "SERVIDOR> Abandona el chat... " + nombre;
			try {
				fsalida.writeUTF(texto);
				fsalida.writeUTF("*");
				repetir = false;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}*/
	}

	// Dentro del m�todo ejecutar(), el cliente lee lo que el
	// hilo le manda (mensajes del Chat) y lo muestra en el textarea.
	// Esto se ejecuta en un bucle del que solo se sale
	// en el momento que el cliente pulse el bot�n Salir
	// y se modifique la variable repetir
	public void ejecutar() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		while (repetir) {
			try {
				
				rellenar();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Imposible conectar con el servidor \n" + ex.getMessage(),
						"<<Mensaje de Error:2>>", JOptionPane.ERROR_MESSAGE);
				repetir = false;
			}
		}
		try {
			socket.close();
			System.out.println("Ganador desde cliente: " + desencriptarPregunta(pregunta.getCorrecta()));
			this.setVisible(false);
			DialogoGanadores dialogo = new DialogoGanadores();
			dialogo.lblMensaje.setText("HA GANADO "+ desencriptarPregunta(pregunta.getCorrecta()));
			//System.exit(0);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void rellenar() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		try 
		{
			System.out.println("Leyendo objeto");
			fentradaPregunta = new ObjectInputStream(socket.getInputStream());
			pregunta = (Preguntas) fentradaPregunta.readObject();
			
			if(pregunta.getEnunciado().equals("*"))
			{
				repetir = false;
			}else {
				ordenAleatorioPregunta(pregunta);
			}
			
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Clase no encontrada");
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException");
		}
	}
	private void EnviarResultado(int resultado) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchProviderException {
		//for (int i = 0; i < ServidorChat.CONEXIONES; i++) {
			//Socket socket = ServidorChat.tabla[i];
			try {
				DataOutputStream fsalida = new DataOutputStream(socket.getOutputStream());
				fsalida.writeUTF(encriptarResultado(resultado));
				//String resultadoString = Integer.toString(resultado);
				//encriptarNombre(resultadoString);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	private void ordenAleatorioPregunta(Preguntas pregunta) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException {
		int n = (int) (Math.random() * (4 - 0)) + 0;
		
		String enunciado, correcta, incorrecta1, incorrecta2, incorrecta3;
		String[] preguntaSplit = desencriptarPregunta(pregunta.getEnunciado()).split("/");
		
		enunciado = preguntaSplit[0];
		correcta = preguntaSplit[1];
		incorrecta1 = preguntaSplit[2];
		incorrecta2 = preguntaSplit[3];
		incorrecta3 = preguntaSplit[4];
		
		pregunta.setEnunciado(enunciado);
		pregunta.setCorrecta(correcta);
		pregunta.setIncorrecta1(incorrecta1);
		pregunta.setIncorrecta2(incorrecta2);
		pregunta.setIncorrecta3(incorrecta3);
		
		
		System.out.println(enunciado);
	//	System.out.println(desencriptarPregunta(pregunta.getEnunciado()));
		switch(n) 
		{
			case 0:
				lbl_pregunta.setText(pregunta.getEnunciado());
				respuesta1.setText(pregunta.getCorrecta());
				respuesta2.setText(pregunta.getIncorrecta1());
				respuesta3.setText(pregunta.getIncorrecta2());
				respuesta4.setText(pregunta.getIncorrecta3());
				break;
			case 1:
				lbl_pregunta.setText(pregunta.getEnunciado());
				respuesta2.setText(pregunta.getCorrecta());
				respuesta1.setText(pregunta.getIncorrecta1());
				respuesta3.setText(pregunta.getIncorrecta2());
				respuesta4.setText(pregunta.getIncorrecta3());
				break;

			case 2:
				lbl_pregunta.setText(pregunta.getEnunciado());
				respuesta3.setText(pregunta.getCorrecta());
				respuesta1.setText(pregunta.getIncorrecta1());
				respuesta2.setText(pregunta.getIncorrecta2());
				respuesta4.setText(pregunta.getIncorrecta3());
				break;

			case 3:
				lbl_pregunta.setText(pregunta.getEnunciado());
				respuesta4.setText(pregunta.getCorrecta());
				respuesta1.setText(pregunta.getIncorrecta1());
				respuesta2.setText(pregunta.getIncorrecta2());
				respuesta3.setText(pregunta.getIncorrecta3());
				break;
		}
	}
	private int comprobarPregunta(String respuesta, String correcta) 
	{
		int correcto = 0;
		if(respuesta.equals(correcta)) 
		{
			correcto = 1;
		}
		return correcto;
	}
	
	//Encripta el nombre
	public String encriptarNombre(String nombre) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchProviderException, IOException 
	{
		String nombreEncriptado = "";
		
		RSA rsa = new RSA();
		rsa.genKeyPair(2048);
		
		rsa.saveToDiskPrivateKey("rsa.pri");
		rsa.saveToDiskPublicKey("rsa.pub");
		
		nombreEncriptado = rsa.Encrypt(nombre);
		
		System.out.println(nombreEncriptado);
		
		return nombreEncriptado;
	}
	
	public String encriptarResultado(int resultado) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException, NoSuchProviderException 
	{
		
		String resEncriptado = "";
		
		RSA rsa = new RSA();
		rsa.genKeyPair(2048);
		
		rsa.saveToDiskPrivateKey("rsa.pri");
		rsa.saveToDiskPublicKey("rsa.pub");
		
		resEncriptado = rsa.Encrypt(resultado+"");
		
		return resEncriptado;
	}
	
	public String desencriptarPregunta(String pregunta) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException 
	{
		String desencriptado="";
		
		RSA rsa = new RSA();
		rsa.genKeyPair(2048);
		
		rsa.openFromDiskPrivateKey("rsa.pri");
		rsa.openFromDiskPublicKey("rsa.pub");
		
		desencriptado = rsa.Decrypt(pregunta);
		
		return desencriptado;
	}
	
	public String encriptarNombreCliente(String nombre) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException, NoSuchProviderException 
	{
		String nombreEncriptado = "";
		String nombreSinEncriptar = nombre;

		
		RSA rsa = new RSA();
		rsa.genKeyPair(2048);
		
		rsa.saveToDiskPrivateKey("rsa.pri");
		rsa.saveToDiskPublicKey("rsa.pub");
		
		nombreEncriptado = rsa.Encrypt(nombreSinEncriptar);
		
		System.out.println(nombreEncriptado);
		
		return nombreEncriptado;
	}
}
