package energy.bar;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

class BotaoPersonalizado extends JButton {

    private Color corPadrao;
    private Color corSelecionada;

    public BotaoPersonalizado(String texto, Color corPadrao, Color corSelecionada, Font fonte) {
        super(texto);
        this.corPadrao = corPadrao;
        this.corSelecionada = corSelecionada;
        setFont(fonte);
        setBackground(corPadrao);
        setBorderPainted(false);
        setFocusPainted(false);
        setVisible(true);
    }

    public void selecionar() {
        setBackground(corSelecionada);
    }

    public void desmarcar() {
        setBackground(corPadrao);
    }
}
