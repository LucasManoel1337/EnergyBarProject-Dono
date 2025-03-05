package energy.bar;

import energy.bar.support.LabelEnergyBar;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class TelaInicio extends JPanel {

    LabelEnergyBar labelEnergyBar = new LabelEnergyBar();

    public TelaInicio() {
        setLayout(new BorderLayout());

        // Criando e adicionando a label EnergyBar
        JLabel energyBarLabel = labelEnergyBar.criarLabelEnergyBar();
        add(energyBarLabel);

        JLabel label = new JLabel("Tela Inicio", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.CENTER);
    }
}
