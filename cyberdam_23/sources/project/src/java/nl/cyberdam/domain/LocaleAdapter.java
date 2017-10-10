package nl.cyberdam.domain;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.util.StringUtils;

public class LocaleAdapter  extends XmlAdapter<String,Locale> {
	public LocaleAdapter() {
		// TODO Auto-generated constructor stub
	}
    public Locale unmarshal(String val) throws Exception {
        return StringUtils.parseLocaleString(val);
    }
    public String marshal(Locale val) throws Exception {
        return val.toString();
    }
}