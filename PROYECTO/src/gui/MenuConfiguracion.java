package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class MenuConfiguracion extends JFrame {

	private static final long serialVersionUID = 1L;
	public int volumenActualMusica;
	public int volumenActualSonido;
	//sliders utilizados para ajustar el sonido
	public JSlider sliderVolumenMusica = new JSlider(0,100,volumenActualMusica);
	public JSlider sliderVolumenSonido = new JSlider(0,100,volumenActualSonido);
	
	private Color colorFondo = new Color(206, 249, 249);
	
	public MenuConfiguracion() {
		//tamaño, metodo de cerrado y sitio de aparición de ventana
		setSize(300, 400);
		setTitle("Opciones");
		setLocationRelativeTo(null);
		
		//panel de Opciones principal
		JPanel panelOpciones = new JPanel();
		panelOpciones.setBackground(colorFondo);
		//Gridlayout principal
		panelOpciones.setLayout(new GridLayout(4,1));
		//El texto de "Opciones" que sale en grande 
		JLabel textoOpciones = new JLabel();
		textoOpciones.setText("Opciones");
		textoOpciones.setFont(new Font("Arial",Font.BOLD, 24));
		textoOpciones.setHorizontalAlignment(JLabel.CENTER);
		//lo añado al grid principal
		panelOpciones.add(textoOpciones);
		//creo un panel que tiene un grid en vertical 
		JPanel panelSliderMusica = new JPanel();
		panelSliderMusica.setLayout(new GridLayout(2,1));
		panelSliderMusica.setBackground(colorFondo);
		// en el grid PanelSliderSonido meto otro panel con grid
		// en el cual estan los numeros que indican el min y max volumen
		JPanel panelSliderMusicaArriba = new JPanel(new GridLayout(1,3));
		panelSliderMusicaArriba.setBackground(colorFondo);
		//creo los labels 0 y 100
		JLabel volACeroMusica = new JLabel("0");
		volACeroMusica.setFont(new Font("Arial", Font.BOLD, 10));
		volACeroMusica.setVerticalAlignment(JLabel.BOTTOM);
		
		
		JLabel volMusica = new JLabel("Música");
		volMusica.setHorizontalAlignment(JLabel.CENTER);
		volMusica.setFont(new Font("Arial",Font.BOLD,16));
		
		JLabel volACienMusica = new JLabel("100");
		volACienMusica.setFont(new Font("Arial", Font.BOLD, 10));
		volACienMusica.setHorizontalAlignment(JLabel.RIGHT);
		volACienMusica.setVerticalAlignment(JLabel.BOTTOM);
		
		//añado todo lo creado
		panelSliderMusicaArriba.add(volACeroMusica);
		panelSliderMusicaArriba.add(volMusica);
		panelSliderMusicaArriba.add(volACienMusica);
		
		//configuro el SliderVolumen
		sliderVolumenMusica.setBackground(colorFondo);
		panelSliderMusica.add(panelSliderMusicaArriba);
		panelSliderMusica.add(sliderVolumenMusica);
		

		panelOpciones.add(panelSliderMusica);
		
		

				

		//creo un panel que tiene un grid en vertical 
		JPanel panelSliderSonido = new JPanel();
		panelSliderSonido.setLayout(new GridLayout(2,1));
		panelSliderSonido.setBackground(colorFondo);
		// en el grid PanelSliderSonido meto otro panel con grid
		// en el cual estan los numeros que indican el min y max volumen
		JPanel panelSliderSonidoArriba = new JPanel(new GridLayout(1,3));
		panelSliderSonidoArriba.setBackground(colorFondo);
		//creo los labels 0 y 100
		JLabel volACeroSonido = new JLabel("0");
		volACeroSonido.setFont(new Font("Arial", Font.BOLD, 10));
		volACeroSonido.setVerticalAlignment(JLabel.BOTTOM);
				
				
		JLabel volSonido = new JLabel("Sonido");
		volSonido.setHorizontalAlignment(JLabel.CENTER);
		volSonido.setFont(new Font("Arial",Font.BOLD,16));
				
		JLabel volACienSonido = new JLabel("100");
		volACienSonido.setFont(new Font("Arial", Font.BOLD, 10));
		volACienSonido.setHorizontalAlignment(JLabel.RIGHT);
		volACienSonido.setVerticalAlignment(JLabel.BOTTOM);
				
		//añado todo lo creado
		panelSliderSonidoArriba.add(volACeroSonido);
		panelSliderSonidoArriba.add(volSonido);
		panelSliderSonidoArriba.add(volACienSonido);
				
		//configuro el SliderVolumen	
		sliderVolumenSonido.setBackground(colorFondo);
		panelSliderSonido.add(panelSliderSonidoArriba);
		panelSliderSonido.add(sliderVolumenSonido);
				
		panelOpciones.add(panelSliderSonido);
		
		
		JButton botonSalir = new JButton("Salir");
		botonSalir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(MenuConfiguracion.this,"Quieres guardar los cambios?","Confirmar",JOptionPane.YES_NO_OPTION);
				if (response ==JOptionPane.YES_OPTION) {
					guardarCambios();
					dispose();
				}
			}
		});
		panelOpciones.add(botonSalir);
		//Añado el grid Principal completo
		add(panelOpciones);
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int response = JOptionPane.showConfirmDialog(MenuConfiguracion.this,"Quieres guardar los cambios?","Confirmar",JOptionPane.YES_NO_OPTION);
				if (response ==JOptionPane.YES_OPTION) {
					guardarCambios();
					dispose();
				}	
			}		
		});
		
		setResizable(true);
		setVisible(true);
	}
	
	public void guardarCambios() {
		volumenActualSonido=sliderVolumenSonido.getValue();
		volumenActualMusica= sliderVolumenMusica.getValue();
	}
	
}
