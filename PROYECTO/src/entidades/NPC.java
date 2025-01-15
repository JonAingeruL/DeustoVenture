package entidades;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

import main.GamePanel;

public class NPC {
	GamePanel gp;
    private int posX;
    private int posY;
    private String frase;
    private Random r = new Random();
    private int numImagen = r.nextInt(1,6);


    public NPC(int posX, int posY, String frase, GamePanel gp) {
        this.posX = posX;
        this.posY = posY;
        this.frase = frase;
        this.gp =gp;
    }


    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public String getFrase() {
        return frase;
    }

    
    public void dibujarNpc(Graphics2D g2) {
		if (this instanceof NPCcomerciante) {
			Image i = new ImageIcon("resources/texturas/npc/comerciante.png").getImage();
			g2.drawImage(i, posX, posY, gp.tamañoBaldosa, gp.tamañoBaldosa, null);
		} else {
			Image i = new ImageIcon("resources/texturas/npc/personaje"+numImagen+".png").getImage();
			g2.drawImage(i, posX, posY, gp.tamañoBaldosa, gp.tamañoBaldosa, null);
		}
}
    
    public String invertirFraseRecursiva(String frase) {
        if (frase == null || frase.length() <= 1) {
            return frase;
        }
        // Primer carácter se mueve al final, y se llama recursivamente con el resto
        return invertirFraseRecursiva(frase.substring(1)) + frase.charAt(0);
    }

}
