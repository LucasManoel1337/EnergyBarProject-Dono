package energy.bar.support;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class IdFilter extends DocumentFilter {

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
        if (str.matches("\\d+")) { // Aceita apenas dígitos
            String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());

            // Remove os zeros à esquerda
            textoAtual = textoAtual.replaceFirst("^0+(?!$)", "");

            // Adiciona o novo dígito
            String novoTexto = textoAtual.substring(0, offs) + str + textoAtual.substring(offs);

            // Formata com 3 dígitos
            try {
                int idNumerico = Integer.parseInt(novoTexto);
                String idFormatado = String.format("%03d", idNumerico);
                fb.replace(0, fb.getDocument().getLength(), idFormatado, a);
            } catch (NumberFormatException ex) {
                // Lida com IDs não numéricos, se necessário
            }
        }
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.matches("\\d*")) { // Aceita apenas dígitos ou vazio
            String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String novoTexto = textoAtual.substring(0, offset) + text + textoAtual.substring(offset + length);

            // Remove os zeros à esquerda
            novoTexto = novoTexto.replaceFirst("^0+(?!$)", "");

            // Formata com 3 dígitos (se não for vazio)
            try {
                if (!novoTexto.isEmpty() && novoTexto.length() <= 3) {
                    int idNumerico = Integer.parseInt(novoTexto);
                    String idFormatado = String.format("%03d", idNumerico);
                    fb.replace(0, fb.getDocument().getLength(), idFormatado, attrs);
                } else if (novoTexto.isEmpty()) {
                    fb.replace(0, fb.getDocument().getLength(), "", attrs); // Limpa o campo se o texto for vazio
                }
            } catch (NumberFormatException ex) {
                // Lida com IDs não numéricos, se necessário
            }
        }
    }
}
