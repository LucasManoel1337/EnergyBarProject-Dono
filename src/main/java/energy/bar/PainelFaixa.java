package energy.bar;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class PainelFaixa extends JPanel {

    public PainelFaixa() {
        setBackground(new Color(32, 3, 3)); // Marrom escuro
        setPreferredSize(new Dimension(220, 600)); // Largura e altura fixas
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Margens de 10px nos quatro lados
    }
}
