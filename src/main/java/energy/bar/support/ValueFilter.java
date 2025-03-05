package energy.bar.support;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class ValueFilter extends DocumentFilter {

    private NumberFormat formatter;

    public ValueFilter() {
        formatter = new DecimalFormat("#,##0.00");
        DecimalFormatSymbols symbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.'); // Define o ponto como separador decimal
        ((DecimalFormat) formatter).setDecimalFormatSymbols(symbols);
    }

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
        String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
        String novoTexto = textoAtual.substring(0, offs) + str + textoAtual.substring(offs);

        novoTexto = novoTexto.replaceAll("[^0-9]", ""); // Remove caracteres não numéricos

        if (novoTexto.length() > 0) {
            try {
                double valor = Double.parseDouble(novoTexto) / 100; // Divide por 100 para ajustar as casas decimais
                String valorFormatado = formatter.format(valor);
                fb.replace(0, fb.getDocument().getLength(), valorFormatado, a);
            } catch (NumberFormatException ex) {
                // Lida com erros de formatação, se necessário
            }
        } else {
            fb.replace(0, fb.getDocument().getLength(), "", a); // Limpa o campo se o texto for vazio
        }
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
        String novoTexto = textoAtual.substring(0, offset) + text + textoAtual.substring(offset + length);

        novoTexto = novoTexto.replaceAll("[^0-9]", ""); // Remove caracteres não numéricos

        if (novoTexto.length() > 0) {
            try {
                double valor = Double.parseDouble(novoTexto) / 100; // Divide por 100 para ajustar as casas decimais
                String valorFormatado = formatter.format(valor);
                fb.replace(0, fb.getDocument().getLength(), valorFormatado, attrs);
            } catch (NumberFormatException ex) {
                // Lida com erros de formatação, se necessário
            }
        } else {
            fb.replace(0, fb.getDocument().getLength(), "", attrs); // Limpa o campo se o texto for vazio
        }
    }
}