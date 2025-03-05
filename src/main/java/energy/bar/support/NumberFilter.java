package energy.bar.support;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberFilter extends DocumentFilter {

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
        if (str.matches("\\d+")) { // Aceita apenas dígitos
            super.insertString(fb, offs, str, a);
        }
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.matches("\\d*")) { // Aceita apenas dígitos ou vazio
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
