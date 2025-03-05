package energy.bar.support;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class IdVerifier extends InputVerifier {

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        JTextField textField = (JTextField) input;
        String text = textField.getText();
        try {
            Integer.parseInt(text); // Tenta converter para inteiro
            return true; // Aceita se for numérico
        } catch (NumberFormatException e) {
            return false; // Rejeita se não for numérico
        }
    }

    @Override
    public boolean verify(JComponent input) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
