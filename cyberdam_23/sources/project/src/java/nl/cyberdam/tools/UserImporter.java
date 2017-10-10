package nl.cyberdam.tools;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import nl.cyberdam.domain.Group;
import nl.cyberdam.domain.User;
import nl.cyberdam.multilanguage.MultiLanguageSource;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.MailService;
import nl.cyberdam.service.PasswordGenerator;
import nl.cyberdam.service.UserManager;
import nl.cyberdam.util.GameUtil;

import org.springframework.mail.SimpleMailMessage;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

/**
 * Utility class to parse a csv file:
 * 
 * A CSV file with:
 * first record: a list of field names 
 * each following record: data for one of the new users 
 * the field names are:
 * 
 * 0 given name (required) 
 * 1 family name (required) 
 * 2 login name (required) 
 * 3 Game session master (Y/N, default: N) 
 * 4 Game manifest composer (Y/N, default: N) 
 * 5 Game Author (Y/N, default: N) 
 * 6 Playground author (Y/N, default: N) 
 * 7 LCMS administrator (Y/N, default: N) 
 * 8 LMS administrator (Y/N, default: N)
 * 9 VLE administrator (Y/N, default: N)
 * 10 groups (a list of group names the user belongs to separated by "|", may be empty)
 * 11 dialogue language, default "nl"
 * 12 e-mail address (required)
 * 
 */
public class UserImporter {
	
	private MailService mailService;
	private GameManager gameManager;
	private MultiLanguageSource messageSource;
	public static final boolean NO_ADMIN_FLAGS = true;
	public static final boolean WITH_ADMIN_FLAGS = false;
	/**
	 * returns a User object for a line
	 */
	public void addUsers(Reader r, Writer w, UserManager userManager, PasswordGenerator passwordGenerator, 
			boolean noAdminFlags) throws IOException {
		
		CsvReader reader = new CsvReader(r);
		CsvWriter writer = new CsvWriter(w, ',');
		String [] nextLine;
		final int fieldCount = noAdminFlags ? 4 : 13;
		Group defaultGroup = null;
		if (noAdminFlags)
		{
		    defaultGroup = new Group ();
		    User currentUser = GameUtil.getCurrentUser();
		    defaultGroup.setCategory ("uploads-" + currentUser.getUsername ());
		    String date = new SimpleDateFormat ("dd-MM-yy HH:mm").format (new Date ());
		    defaultGroup.setName (currentUser.getUsername() + " " + date);
		    defaultGroup.setLastModifiedBy(currentUser);
		    defaultGroup.setLastModified(new Date ());
		    defaultGroup.setDescription("Uploaded by " + currentUser.getUsername () + " at " + date);
		    userManager.saveGroup(defaultGroup);
		}
		while (reader.readRecord()) {
			nextLine = reader.getValues();
			List<String> tempList = new ArrayList<String>(Arrays.asList(nextLine));
			if (nextLine.length < fieldCount) {
				// append error
				tempList.add("not enough fields ( < " + fieldCount + ")");
			} else if  (nextLine.length > 13) {
				// append other error
				tempList.add("too many fields ( > " + fieldCount + ")");
			} else {
				// try to create a user object and add it to the system (otherwise
				// we won't be sure
				// the username is unique)
				String givenname = nextLine[0];
				String familyname = nextLine[1];
				String username = nextLine[2];
				boolean gameSessionMaster = !noAdminFlags && "y".equalsIgnoreCase(nextLine[3]);
				boolean gameManifestComposer = !noAdminFlags && "y".equalsIgnoreCase(nextLine[4]);
				boolean gameAuthor = !noAdminFlags && "y".equalsIgnoreCase(nextLine[5]);
				boolean playgroundAuthor = !noAdminFlags && "y".equalsIgnoreCase(nextLine[6]);
				boolean lcmsAdministrator = !noAdminFlags && "y".equalsIgnoreCase(nextLine[7]);
				boolean lmsAdministrator = !noAdminFlags && "y".equalsIgnoreCase(nextLine[8]);
				boolean vleAdministrator = !noAdminFlags && "y".equalsIgnoreCase(nextLine[9]);
				Set<Group> groups = new HashSet<Group>();
				Locale language;
				// nl is default language (in functional specification)
				if (noAdminFlags || "".equals(nextLine[11])) {
					language = new Locale("nl");
				} else {
					language = new Locale(nextLine[11]);
				}
				// convert email to lowercase (from the spec)
				String email = nextLine[fieldCount - 1].toLowerCase();
				String password = passwordGenerator.getPassword();
				try {
					if (defaultGroup != null)
					{
					    groups.add(defaultGroup);
					}
					userManager.addUser(givenname, familyname, username, gameSessionMaster, 
						            gameManifestComposer, gameAuthor, playgroundAuthor, 
						            lcmsAdministrator, lmsAdministrator, vleAdministrator, 
						            groups, language, email, password);
					tempList.add("OK");
					sendEmail(username, email, password);
				} catch (org.springframework.dao.DataIntegrityViolationException dive) {
					tempList.add("ERROR: " + dive.getMostSpecificCause().getMessage());
				} catch (org.hibernate.validator.InvalidStateException ise) {
					for (org.hibernate.validator.InvalidValue iv: ise.getInvalidValues()) {
						tempList.add("ERROR: " +iv.getPropertyName() + ": " + iv.getMessage());
					}
				} catch (Exception e) {
					e.printStackTrace();
					tempList.add("ERROR: " +e.getMessage());
				}
			}
			writer.writeRecord(tempList.toArray(new String[tempList.size()]));
		}
	}

	// XXX multilanguage
	private void sendEmail(String username, String email, String password) {
		SimpleMailMessage message = new SimpleMailMessage();
		String languageCode = gameManager.loadSystemParameters().getDefaultLanguageCode();
		Locale defaultLocale = new Locale(languageCode);
    	message.setTo(email);
    	message.setSubject(messageSource.getMessage("email.newuser.subject", null, defaultLocale));
    	Object[] bodyOptions = {username, password};
    	message.setText(messageSource.getMessage("email.newuser.body", bodyOptions, defaultLocale));
    	message.setFrom(gameManager.loadSystemParameters().getEmail());
    	mailService.send(message);
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void setMessageSource(MultiLanguageSource messageSource) {
		this.messageSource = messageSource;
	}
}
