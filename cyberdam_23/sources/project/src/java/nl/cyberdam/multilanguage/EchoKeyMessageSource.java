package nl.cyberdam.multilanguage;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.support.AbstractMessageSource;

/**
 * This message source just echos the key you put into it. Use this as a 
 * fallback in case no message was found. It also useful to see the keys
 * that are used.
 */
public class EchoKeyMessageSource extends AbstractMessageSource {

	@Override
	protected MessageFormat resolveCode(String code, Locale arg1) {
		return new MessageFormat(code);
	}

}
