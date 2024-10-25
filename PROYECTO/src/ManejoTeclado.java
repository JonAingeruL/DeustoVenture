import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Esta clase se va a utilizar para leer los inputs del teclado, basicamente que si tocas algo del teclado en el juego pase algo o no
//Por ejemplo si pulsas la letra w el personaje suba para arriba, o que si pulsas la flecha haga lo mismo

public class ManejoTeclado implements KeyListener{ //KeyListener es la interfaz necesaria para recibir los eventos de teclado, tambien se llaman keystrokes
	
	//Despuesd de la asignacion de botones creamos unas variables booleanas para saber si estan siendo pulsados o no
	public boolean arribaPulsado, abajoPulsado, izquierdaPulsado, derechaPulsado, shiftPulsado;

	//Estos tres metodos se generan automaticamente cunado implementas en KeyListener
	@Override
	public void keyTyped(KeyEvent e) {	//Este no lo necesitamos
	}

	@Override
	public void keyPressed(KeyEvent e) { //Cuando estas pulsando una tecla
		
		int code = e.getKeyCode(); //Esto devuelve un numero de la tecla que fue presionada
		//Basicamente cada tecla del teclado tiene asociado un numero, por ejemplo el ENTER es el numero 10, por lo que si pulsas el ENTER code tendra el valor de 10
		
		if (code == KeyEvent.VK_W) { //Esto quiere decir que si el usuario pulsa la letra W haga lo que pone en el if
			arribaPulsado = true;
		}
		if (code == KeyEvent.VK_S) { //Esto quiere decir que si el usuario pulsa la letra S haga lo que pone en el if
			abajoPulsado = true;
		}
		if (code == KeyEvent.VK_A) { //Esto quiere decir que si el usuario pulsa la letra A haga lo que pone en el if
			izquierdaPulsado = true;
		}
		if (code == KeyEvent.VK_D) { //Esto quiere decir que si el usuario pulsa la letra D haga lo que pone en el if
			derechaPulsado = true;
		}
		if (code == KeyEvent.VK_SHIFT) { //Esto quiere decir que si el usuario pulsa la tecla SHIFT haga lo que pone en el if
			shiftPulsado = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) { //Cuando no estas pulsando una tecla
		
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_W) { //Esto quiere decir que si el usuario no pulsa la letra W haga lo que pone en el if
			arribaPulsado = false;
		}
		if (code == KeyEvent.VK_S) { //Esto quiere decir que si el usuario no pulsa la letra A haga lo que pone en el if
			abajoPulsado = false;
		}
		if (code == KeyEvent.VK_A) { //Esto quiere decir que si el usuario no pulsa la letra S haga lo que pone en el if
			izquierdaPulsado = false;
		}
		if (code == KeyEvent.VK_D) { //Esto quiere decir que si el usuario no pulsa la letra D haga lo que pone en el if
			derechaPulsado = false;
		}
		if (code == KeyEvent.VK_SHIFT) { //Esto quiere decir que si el usuario no pulsa la tecla SHIFT haga lo que pone en el if
			shiftPulsado = false;
		}
		
	} 

	
}
