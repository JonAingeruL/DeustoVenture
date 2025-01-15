package entidades;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;

public class NPC {
	GamePanel gp;
    private int posX;
    private int posY;
    private String frase;


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
		g2.setColor(new Color(0,0,0));

		g2.fillRect(posX, posY, gp.tamañoBaldosa, gp.tamañoBaldosa);

}
    
    public String invertirFraseRecursiva(String frase) {
        if (frase == null || frase.length() <= 1) {
            return frase;
        }
        // Primer carácter se mueve al final, y se llama recursivamente con el resto
        return invertirFraseRecursiva(frase.substring(1)) + frase.charAt(0);
    }

}
