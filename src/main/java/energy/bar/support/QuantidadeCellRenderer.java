
package energy.bar.support;

import java.awt.Color;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;

public class QuantidadeCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Chamando o método da superclasse
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Verificando o valor da quantidade
        if (value instanceof Integer) {
            int quantidade = (Integer) value;
            if (quantidade <= 3) {
                c.setBackground(Color.RED); // Quantidade <= 3 - Vermelho
            } else if (quantidade >= 4 && quantidade <= 5) {
                c.setBackground(Color.WHITE); // Quantidade entre 4 e 5 - Branco
            } else if (quantidade >= 6) {
                c.setBackground(Color.GREEN); // Quantidade >= 6 - Verde
            }
        }

        return c;
    }
}
