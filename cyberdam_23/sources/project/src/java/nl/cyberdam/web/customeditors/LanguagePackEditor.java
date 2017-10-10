package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.multilanguage.LanguagePack;
import nl.cyberdam.multilanguage.MultiLanguageSource;

public class LanguagePackEditor extends PropertyEditorSupport {
    private MultiLanguageSource languageSource;

    public LanguagePackEditor(MultiLanguageSource languageSource) {
        this.languageSource = languageSource;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof LanguagePack) {
            return ((LanguagePack) this.getValue()).getId().toString();
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(languageSource.getLanguagePack(new Long(text)));
        } else {
            setValue(null);
        }
    }
}