package entidades;

public class NPC {
		private int x, y; //posicion del NPC
		private String [] dialogos; //frases que dicen los NPCs
		
		public NPC(int x, int y, String[] dialogos) {
			this.x = x;
			this.y = y;
			this.dialogos = dialogos;
		}
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public String[] getDialogos() {
			return dialogos;
		}
		
		//DE MOMENTO LO SACO POR PANTALLA
		//mas adelante tengo que cambiarlo a que se muestre por pantalla, es para comprobar si funciona
		public String hablar() {
			StringBuilder textoTodo = new StringBuilder();// uso String builder para Concatenar múltiples cadenas
			for(String linea: dialogos) {
				System.out.println(linea);
				textoTodo.append(linea).append(" ");
			}
			return textoTodo.toString().trim(); //devuelve el texto como una cadena
			
		}
		

}
