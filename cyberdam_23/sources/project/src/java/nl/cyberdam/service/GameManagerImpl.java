package nl.cyberdam.service;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.EventActivity;
import nl.cyberdam.domain.FileUploadActivity;
import nl.cyberdam.domain.FormActivity;
import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.MessageActivity;
import nl.cyberdam.domain.MessageBox;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.ProgressActivity;
import nl.cyberdam.domain.Resource;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.RoleActivity;
import nl.cyberdam.domain.RoleToPlaygroundMapping;
import nl.cyberdam.domain.SessionPlaygroundObject;
import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.domain.SessionStatus;
import nl.cyberdam.domain.Status;
import nl.cyberdam.domain.StepOfPlay;
import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.domain.User;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.domain.VariableSessionValue;
import nl.cyberdam.multilanguage.LanguagePack;
import nl.cyberdam.util.GameUtil;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// XXX this class has 2 issues: lots of load/save methods that do not really differ, and tight coupling to hibernate
// instead of using a DAO layer. Currently there is no pressing need to resolve these issues, but we might want to
// fix them in the future
@Repository

public class GameManagerImpl extends DefaultService implements GameManager {

	private MailService mailService;
	// used for getting email templates
	private MessageSource messageSource;
	private Date startupDate = new Date();

	public GameManagerImpl() {
	}

	/**
	 * should be injected before calling deliverMessage() because the
	 * mailService is used to send email notifications of new messages in the
	 * game session.
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
	@Transactional
	public LanguagePack loadLanguagePack(Long id) {
	    return (LanguagePack) sessionFactory
        .getCurrentSession().load(LanguagePack.class, id);
	}
	
	@Transactional
	public Variable loadVariable(Long id) {
	    return (Variable) sessionFactory
        .getCurrentSession().load(Variable.class, id);
	}
	
	@Transactional
	public void save(LanguagePack languagePack) {
	    sessionFactory.getCurrentSession().saveOrUpdate(languagePack);
	}

	/**
	 * order by, owner.username, name
	 */
	public List<GameModel> findAll() {
	    return findAll (null);
	}
	@SuppressWarnings(value = "unchecked")
	public List<GameModel> findAll(String textFilter) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
	    String filterCriteria = (textFilter == null) ? ":textFilter is null" : "lower(g.name || g.caption || g.owner.firstName || g.owner.lastName) like :textFilter ";
		return (List<GameModel>) sessionFactory
				.getCurrentSession()
				.createQuery(
						"select g from GameModel g where " + filterCriteria + " ORDER BY g.owner.username, name")
				.setParameter ("textFilter", textFilter)
				.list();
	}

	/**
	 * all models created by the user, and all public ones order by,
	 * owner.username, name
	 */
	@SuppressWarnings(value = "unchecked")
	public List<GameModel> findAllForUser(User u, String textFilter) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
	    String filterCriteria = (textFilter == null) ? ":textFilter is null" : "lower(g.name || g.caption || g.owner.firstName || g.owner.lastName) like :textFilter ";
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct g from GameModel as g where (g.status is :status or g.owner is :user) and " + filterCriteria + " ORDER BY g.owner.username, name");
		q.setParameter("status", Status.PUBLIC);
		q.setParameter("user", u);
		q.setParameter ("textFilter", textFilter);
		return (List<GameModel>) q.list();
	}

	@SuppressWarnings("unchecked")
	public List<GameModel> findAllReady(User currentUser, String textFilter) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
	    String filterCriteria = (textFilter == null) ? ":textFilter is null" : "lower(g.name) like :textFilter ";
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct g from GameModel as g where (g.status is :status or (g.owner is :user and g.status is :private)) AND " + filterCriteria + " ORDER BY g.name");
		q.setParameter("status", Status.PUBLIC);
		q.setParameter("private", Status.PRIVATE);
		q.setParameter("user", currentUser);
		q.setParameter("textFilter", textFilter);
		return (List<GameModel>) q.list();
	}

	@SuppressWarnings("unchecked")
	public List<GameManifest> findAllReadyManifests(User currentUser, String textFilter) {
		textFilter = (textFilter == null || textFilter.length() == 0) ? "%" : "%" + textFilter.toLowerCase() + "%";
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct g from GameManifest as g where (g.status is :status or (g.owner is :user and g.status is :private)) and lower(g.name || g.owner.username || g.gameModel.name) like :textFilter ORDER BY g.name");
		q.setParameter("status", Status.PUBLIC);
		q.setParameter("private", Status.PRIVATE);
		q.setParameter("user", currentUser);
		q.setParameter("textFilter", textFilter);
		return (List<GameManifest>) q.list();
	}

	public GameModel load(Long id) {
		return (GameModel) sessionFactory.getCurrentSession().load(
				GameModel.class, id);
	}
	
	@Transactional
	public void save(GameModel gameModel) {
		Date now = new Date();
		if (gameModel.getId() == null) {
			gameModel.setCreated(now);
		}
		gameModel.setLastModified(now);
		sessionFactory.getCurrentSession().saveOrUpdate(gameModel);
	}

	@Transactional
	public void save(Activity activity) {
		sessionFactory.getCurrentSession().saveOrUpdate(activity);
	}
	
	@Transactional
	public void save(Variable v) {
		sessionFactory.getCurrentSession().saveOrUpdate(v);
	}
	/**
	 * deletes all kinds of objects
	 */
	@Transactional
	public void delete(Object o) {
		sessionFactory.getCurrentSession().delete(o);
	}

	public Role loadRole(Long roleId) {
		return (Role) sessionFactory.getCurrentSession().load(Role.class,
				roleId);
	}

	public StepOfPlay loadStepOfPlay(Long stepOfPlayId) {
		return (StepOfPlay) sessionFactory.getCurrentSession().load(
				StepOfPlay.class, stepOfPlayId);
	}

	@SuppressWarnings(value = "unchecked")
	public List<GameManifest> findAllManifests(String textFilter) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
	    /* 
	    return (List<GameManifest>) sessionFactory.getCurrentSession()
	    .createCriteria (GameManifest.class)
	    .createAlias ("gameModel", "model")
	    .createAlias ("owner", "owner")
	    .add (Expression.or (
		    Expression.ilike("model.name", textFilter),
		    Expression.or (
			    Expression.ilike("name", textFilter),
			    Expression.ilike("owner.firstName", textFilter)
		    )
	    ))
	    .list (); */
	    final String criteriaString = (textFilter == null) ? ":textFilter is null" : "lower (owner.firstName || owner.lastName || gameModel.name || manifest.name) like :textFilter";
	    final String queryString = "select manifest from GameManifest manifest where " + criteriaString + " ORDER BY manifest.name";
	    return (List<GameManifest>) sessionFactory.getCurrentSession().createQuery(queryString)
	    .setParameter ("textFilter", textFilter)
	    .list();
	}

	/**
	 * find all manifests that use the playgroundobject
	 */
	@SuppressWarnings("unchecked")
	public List<GameManifest> findAllManifests(PlaygroundObject playgroundObject) {
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct g from GameManifest as g, RoleToPlaygroundMapping as rtpm, PlaygroundObject as po where po is :playgroundObject AND rtpm.playgroundObject is po AND rtpm.gameManifest is g");
		q.setParameter("playgroundObject", playgroundObject);
		return (List<GameManifest>) q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<GameManifest> findAllManifests(Playground playground) {
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct g from GameManifest as g, RoleToPlaygroundMapping as rtpm, Playground as p where p is :playground AND rtpm.playground is p AND rtpm.gameManifest is g");
		q.setParameter("playground", playground);
		return (List<GameManifest>) q.list();
	}
	

	@SuppressWarnings("unchecked")
	public List<GameManifest> findAllManifestsForUser(User currentUser, String textFilter) {
		textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
		final String criteriaString = (textFilter == null) ? ":textFilter is null" : "lower (owner.firstName || owner.lastName || gameModel.name || g.name) like :textFilter";
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct g from GameManifest as g where (g.status is :status or g.owner is :user) AND " + criteriaString + " ORDER BY g.name");
		q.setParameter("status", Status.PUBLIC);
		q.setParameter("user", currentUser);
		q.setParameter("textFilter", textFilter);
		return (List<GameManifest>) q.list();
	}

	@SuppressWarnings(value = "unchecked")
	public List<GameManifest> findManifestsForGameModel(GameModel g) {
		Query q = sessionFactory.getCurrentSession().createQuery(
				"from GameManifest g where g.gameModel is :model");
		q.setParameter("model", g);
		return (List<GameManifest>) q.list();
	}

	@SuppressWarnings(value = "unchecked")
	public List<VariableSessionValue> findVariableValuesForSession(GameSession s) {
		Query q = sessionFactory.getCurrentSession().createQuery(
				"from VariableSessionValue v where v.gameSession is :session");
		q.setParameter("session", s);
		return (List<VariableSessionValue>) q.list();
	}

	@SuppressWarnings("unchecked")
	public List<GameSession> findGameSessionsForManifest(GameManifest gm) {
		Query q = sessionFactory.getCurrentSession().createQuery(
				"from GameSession g where g.manifest is :manifest");
		q.setParameter("manifest", gm);
		return (List<GameSession>) q.list();
	}

	public GameManifest loadGameManifest(Long id) {
		return (GameManifest) sessionFactory.getCurrentSession().load(
				GameManifest.class, id);
	}

	@Transactional
	public void save(GameManifest gm) {
		Date now = new Date();
		if (gm.getId() == null) {
			gm.setCreated(now);
		}
		gm.setLastModified(now);
		sessionFactory.getCurrentSession().saveOrUpdate(gm);
	}

	@Transactional
	public void save(SessionPlaygroundObject o){
	    sessionFactory.getCurrentSession().saveOrUpdate(o);
	}

	/**
	 * order by name
	 */
	@SuppressWarnings("unchecked")
	public List<PlaygroundObject> findAllPlaygroundObjects() {
		return (List<PlaygroundObject>) sessionFactory.getCurrentSession()
				.createQuery("from PlaygroundObject p ORDER BY name").list();
	}

	/**
	 * order by name
	 */
	@SuppressWarnings("unchecked")
	public List<PlaygroundObject> findAllPlaygroundObjectsForUser(
			User currentUser) {
		// XXX this is a bit weird - we have both a 'creator' and a
		// 'lastmodifier' here - who is the owner of the object?
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct p from PlaygroundObject as p where p.status is :status or p.creator is :user ORDER BY name");
		q.setParameter("status", Status.PUBLIC);
		q.setParameter("user", currentUser);
		return (List<PlaygroundObject>) q.list();
	}

	@SuppressWarnings("unchecked")
	public List<PlaygroundObject> findAllPlaygroundObjectsForMap(
			Playground playground) {
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct p from PlaygroundObject as p where p.status is :status and p.onMap is :onMap and p.playground = :pg");
		q.setParameter("status", Status.PUBLIC);
		q.setBoolean("onMap", true);
		q.setParameter("pg", playground);
		q.setCacheable(true);
		return (List<PlaygroundObject>) q.list();
	}

	@SuppressWarnings("unchecked")
	public List<PlaygroundObject> findAllPlaygroundObjectsForDirectory(
			Playground playground, String filter) {
		String textFilter = (filter == null) ? "" : "and lower (p.name) like lower (:filter) ";
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select distinct p from PlaygroundObject as p where p.status is :status and p.inDirectory is :inDirectory and p.playground = :pg " + textFilter + "order by p.name");
		q.setParameter("status", Status.PUBLIC);
		q.setBoolean("inDirectory", true);
		q.setParameter("pg", playground);
		if (filter != null)
		{
			q.setParameter("filter", "%" + filter + "%");
		}
		q.setCacheable(true);
		return (List<PlaygroundObject>) q.list();
	}

	@SuppressWarnings("unchecked")
	public List<PlaygroundObject> findAllDirectoryPlaygroundObjects() {
		return (List<PlaygroundObject>) sessionFactory.getCurrentSession()
				.createQuery(
						"from PlaygroundObject p where p.inDirectory = true")
				.list();
	}

	public PlaygroundObject loadPlaygroundObject(Long playgroundObjectId) {
		return (PlaygroundObject) sessionFactory.getCurrentSession().load(
				PlaygroundObject.class, playgroundObjectId);
	}

	@Transactional
	public void save(PlaygroundObject playgroundObject) {
		Date now = new Date();
		if (playgroundObject.getId() == null) {
			playgroundObject.setCreated(now);
		}
		playgroundObject.setLastModified(now);
		sessionFactory.getCurrentSession().saveOrUpdate(playgroundObject);
	}

	public RoleToPlaygroundMapping loadRoleToPlaygroundMapping(Long id) {
		return (RoleToPlaygroundMapping) sessionFactory.getCurrentSession()
				.load(RoleToPlaygroundMapping.class, id);
	}

	public List<GameSession> findAllGameSessions() {
	    return findAllGameSessions (null);
	}
	/**
	 * default sort order on status (asc) and running started (desc)
	 */
	@SuppressWarnings("unchecked")
	public List<GameSession> findAllGameSessions(String textFilter) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
	    final String criteriaString = (textFilter == null) ? ":textFilter is null" : "(lower (gamesession.name || gamesession.owner.firstName || gamesession.owner.lastName) like :textFilter or lower (currentStepOfPlay.name) like :textFilter)";
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select gamesession from GameSession gamesession left join gamesession.currentStepOfPlay as currentStepOfPlay where " + criteriaString + " ORDER BY gamesession.status ASC, gamesession.runningStarted DESC")
				.setParameter ("textFilter", textFilter);
        return (List<GameSession>) q.list();
	}

	/**
	 * default sort order on status (asc) and running started (desc)
	 */
	@SuppressWarnings("unchecked")
	public List<GameSession> findAllGameSessionsForUser(User currentUser, String textFilter) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? "%" : "%" + textFilter.toLowerCase () + "%";
	    final String criteriaString = (textFilter == null) ? ":textFilter is null" : "(lower (gamesession.name || gamesession.owner.firstName || gamesession.owner.lastName) like :textFilter or lower (currentStepOfPlay.name) like :textFilter)";
		Query q = sessionFactory
				.getCurrentSession()
				.createQuery(
						"select gamesession from GameSession gamesession left join gamesession.currentStepOfPlay as currentStepOfPlay where gamesession.owner is :user and " + criteriaString + " ORDER BY gamesession.status ASC, gamesession.runningStarted DESC")
				.setParameter ("textFilter", textFilter);
		q.setParameter("user", currentUser);
		return (List<GameSession>) q.list();
	}
	
	@Transactional
	public void save(GameSession gameSession) {
		Date now = new Date();
		if (gameSession.getId() == null) {
			gameSession.setCreated(now);
		}
		gameSession.setLastModified(now);
		sessionFactory.getCurrentSession().saveOrUpdate(gameSession);
	}

	public GameSession loadGameSession(Long id) {
		return (GameSession) sessionFactory.getCurrentSession().load(
				GameSession.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Participant> findParticipantsForUser(User u, String textFilter) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
		// if user is VLE Administrator or a System Administrator then show all
		// sessions

		// Filter: Only sessions with status = 3 (Running), 4 (Normal end) or 5
		// (Abnormal end) are shown.
		// Filter: if the user is NOT VLE administrator NOR system
		// administrator, all sessions in which the
		// user is participating (has a role) are displayed; each session role
		// results in a separate row in the
		// table.
		Query q;
		User currentUser = GameUtil.getCurrentUser();
		String searchCondition = (textFilter == null) ? " and :textFilter is null" : " and (lower (session.name || session.owner.firstName || session.owner.lastName || participant.roleAndPlayground.role.name || participant.roleAndPlayground.playgroundObject.name) like :textFilter or lower (currentStepOfPlay.name) like :textFilter)";
		if (currentUser.getGameAuthorities().isVleAdministrator()
				|| currentUser.getGameAuthorities().isSystemAdministrator()) {
			q = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select participant from nl.cyberdam.domain.Participant as participant, GameSession session left join session.currentStepOfPlay as currentStepOfPlay where participant.gameSession is session and session.status in (:running, :finished, :aborted)" + searchCondition + " ORDER BY session.name");
			q.setParameter("running", SessionStatus.RUNNING);
			q.setParameter("finished", SessionStatus.FINISHED);
			q.setParameter("aborted", SessionStatus.ABORTED);
			q.setParameter("textFilter", textFilter);

		} else {
			q = sessionFactory
					.getCurrentSession()
					.createQuery(
							"select participant from nl.cyberdam.domain.Participant as participant,  nl.cyberdam.domain.User as user, GameSession session where participant.gameSession is session and session.status in (:running, :finished, :aborted)  and user = :user and user member of participant.users" + searchCondition + " ORDER BY session.name");
			q.setParameter("running", SessionStatus.RUNNING);
			q.setParameter("finished", SessionStatus.FINISHED);
			q.setParameter("aborted", SessionStatus.ABORTED);
			q.setParameter("user", u);
			q.setParameter("textFilter", textFilter);
		}
		return (List<Participant>) q.list();
	}

	@SuppressWarnings("unchecked")
	public List<Variable> findSystemVariables() {
		Query q;
		q = sessionFactory
			.getCurrentSession()
			.createQuery(
					"FROM Variable v WHERE v.gameModel is NULL and v.role is NULL ORDER BY v.name");
		return (List<Variable>) q.list();
	}

	/**
	 * Loads a Resource, can be very heavy since the whole BLOB containing the
	 * file gets loaded too.
	 * 
	 * XXX investigate - caching?
	 */
	public Resource loadResource(Long id) {
		return (Resource) sessionFactory.getCurrentSession().load(
				Resource.class, id);
	}

	@Transactional
	public void save(Resource r) {
		r.setLastModified(new Date());
		sessionFactory.getCurrentSession().saveOrUpdate(r);
	}

	@Transactional
	public void save(Role r) {
		sessionFactory.getCurrentSession().saveOrUpdate(r);
	}

	@Transactional
	public void save(StepOfPlay s) {
		sessionFactory.getCurrentSession().saveOrUpdate(s);
	}

	@Transactional
	public void save(RoleActivity ra) {
		sessionFactory.getCurrentSession().saveOrUpdate(ra);
	}

	@Transactional
	public void save(Message m) {
		sessionFactory.getCurrentSession().saveOrUpdate(m);
	}

	/**
	 * Links the message to the mailboxes of all recipients - there is only one
	 * instance of the actual message. This method also notifies participants by
	 * email if they have set there notifyByEmail to true.
	 */
	@Transactional
	public void deliverMessage(Message m) {

		if (m.getRecipients() == null || m.getRecipients().isEmpty()) {
			logger.error("attempt to send a message with no recipients");
			return;
		}

		// set sent date
		m.setSentDate(new Date());
		
		// copy the attachements
		List<SessionResource> newAttachments = new ArrayList<SessionResource>();
		if(m.getAttachments() != null) {
		for (SessionResource sessionResource : m.getAttachments()) {
			SessionResource newSessionResource = sessionResource.copy();
			save(newSessionResource);
			newAttachments.add(newSessionResource);
    		}
		}
		m.setAttachments(newAttachments);

		// store step of play the message was sent in
		m.setStepOfPlay(m.getSender().getGameSession().getCurrentStepOfPlay());

		// deliver messages
		for (Participant participant : m.getRecipients()) {
			// link message to mailbox
			participant.getInbox().addMessage(m);
			update(participant.getInbox());

			// notify all users belonging to this participant by email
			// XXX use more variables to parameterize the message
			String emailSubject;
			String emailBody;
			for (User user : participant.getUsers()) {
				if (user.isNotifyNewMessages()) {
					Locale userLocale = user.getLocale();
					emailSubject = messageSource.getMessage(
							"email.newmessage.subject", null, userLocale);
					Object[] bodyOptions = { participant.getRoleAndPlayground()
							.toString() };
					emailBody = messageSource.getMessage(
							"email.newmessage.body", bodyOptions, userLocale);
					SimpleMailMessage message = new SimpleMailMessage();
					message.setTo(user.getEmail());
					message.setSubject(emailSubject);
					message.setText(emailBody);
					message.setFrom(loadSystemParameters().getEmail());
					mailService.send(message);
				}
			}    
		}

		// link message to outbox
		m.getSender().getOutbox().addMessage(m);
		update(m.getSender().getOutbox());

		// save 'original' message
		save(m);
	}

	@Transactional
	public void save(MessageBox m) {
		sessionFactory.getCurrentSession().saveOrUpdate(m);
	}

	public Participant loadParticipant(Long id) {
		return (Participant) sessionFactory.getCurrentSession().load(
				Participant.class, id);
	}

	public void update(Object o) {
		sessionFactory.getCurrentSession().update(o);
	}

	public FileUploadActivity loadFileUploadActivity(Long activityId) {
		return (FileUploadActivity) sessionFactory.getCurrentSession().load(
				FileUploadActivity.class, activityId);
	}

	public MessageActivity loadMessageActivity(Long activityId) {
		return (MessageActivity) sessionFactory.getCurrentSession().load(
				MessageActivity.class, activityId);
	}

	public FormActivity loadFormActivity(Long activityId) {
		return (FormActivity) sessionFactory.getCurrentSession().load(
				FormActivity.class, activityId);
	}

	public EventActivity loadEventActivity(Long activityId) {
	    return (EventActivity) sessionFactory.getCurrentSession().load(EventActivity.class,
		    activityId);
	}

	public ProgressActivity loadProgressActivity(Long activityId) {
		return (ProgressActivity) sessionFactory.getCurrentSession().load(
				ProgressActivity.class, activityId);
	}

	@Transactional
	public void save(Participant participant) {
		sessionFactory.getCurrentSession().saveOrUpdate(participant);

	}

	public Playground loadPlayground(Long playgroundId) {
		return (Playground) sessionFactory.getCurrentSession().load(
				Playground.class, playgroundId);
	}

	public Message loadMessage(Long messageId) {
		return (Message) sessionFactory.getCurrentSession().load(Message.class,
				messageId);
	}

	/**
	 * loads system parameters, if an exception occurs a 'fresh'
	 * systemparameters object will be instantiated and returned;
	 */
	@Transactional
	public SystemParameters loadSystemParameters() {
		SystemParameters parameters;
		// Session.get() will return null if the object does not exist in the
		// database
		parameters = (SystemParameters) sessionFactory.getCurrentSession().get(
				SystemParameters.class, new Long(1));
		if (parameters != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("returning systemparameters from database");
			}
		} else {
			logger
					.warn("loadSystemParameters(): no systemparameters object found in the database - instantiating new one");
			parameters = new SystemParameters();
		}
		return parameters;
	}

	@Transactional
	public void save(SystemParameters s) {
		sessionFactory.getCurrentSession().saveOrUpdate(s);
	}

	public SessionResource loadSessionResource(Long id) {
		return (SessionResource) sessionFactory.getCurrentSession().load(
				SessionResource.class, id);
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public Date startupDate() {
		return startupDate;
	}

	@Transactional
	public void save(SessionResource sr) {
		sessionFactory.getCurrentSession().saveOrUpdate(sr);
	}

	@Transactional
	public void save(Playground playground) {
	    for(PlaygroundObject po : playground.getPlaygroundObjects()) {
	        if("".equals(po.getUri())) {
                po.setUri("URI_" + po.getName() + "_" + System.currentTimeMillis());
	        }
        }
        sessionFactory.getCurrentSession().saveOrUpdate(playground);
	}

	@SuppressWarnings("unchecked")
	public List<Playground> findAllPlaygroundsForUser(User user) {
	    String queryString = "select playground from Playground playground ";
	    return (List<Playground>) sessionFactory.getCurrentSession().createQuery(queryString)
	    .list();
	}

	@SuppressWarnings("unchecked")
	public List<Playground> findAllPlaygrounds(String textFilter) {
	    if (textFilter != null)
	    {
		if (textFilter.length () == 0)
		{
		    textFilter = null;
		}
		else
		{
		    textFilter = textFilter.toLowerCase ();
		}
	    }
	    StringBuffer matchingStatuses = new StringBuffer ("-1");
	    if (textFilter != null)
	    {
	        Locale userLocale = GameUtil.getCurrentUser().getLocale();

		if (messageSource.getMessage("status.PUBLIC", null, userLocale).toLowerCase ().contains(textFilter))
		{
		    matchingStatuses.append ("," + Status.PUBLIC.ordinal ());
		}
		if (messageSource.getMessage("status.UNDER_CONSTRUCTION", null, userLocale).toLowerCase ().contains(textFilter))
		{
		    matchingStatuses.append ("," + Status.UNDER_CONSTRUCTION.ordinal ());
		}
		if (messageSource.getMessage("status.PRIVATE", null, userLocale).toLowerCase ().contains(textFilter))
		{
		    matchingStatuses.append ("," + Status.PRIVATE.ordinal ());
		}
		if (messageSource.getMessage("status.OBSOLETE", null, userLocale).toLowerCase ().contains(textFilter))
		{
		    matchingStatuses.append ("," + Status.OBSOLETE.ordinal ());
		}
	    }

	    textFilter = (textFilter == null) ? null : "%" + textFilter + "%";
	    final String criteriaString = (textFilter == null) ? ":textFilter is null" : "playground.status in (" + matchingStatuses + ") or lower (playground.name || playground.caption || playground.uriId || playground.lastModifier.firstName || playground.lastModifier.lastName) like :textFilter"; 
	    String queryString = "select playground from Playground playground where " + criteriaString + " order by name";
	    return (List<Playground>) sessionFactory.getCurrentSession().createQuery(queryString)
	    .setParameter ("textFilter", textFilter)
	    .list();
	}

	@SuppressWarnings("unchecked")
	public List<Playground> findAllPublicPlaygrounds() {
		return (List<Playground>) sessionFactory.getCurrentSession().createQuery(
				"from Playground p where p.status is :status ORDER BY name")
				.setParameter("status", Status.PUBLIC).list();
		//return (List<Playground>) sessionFactory.getCurrentSession().createQuery("from Playground p ORDER BY name").list();
	}

	@SuppressWarnings("unchecked")
	public List<PlaygroundObject> findAllPlaygroundObjects(Long playgroundId, String textFilter) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
	    final String criteriaString = (textFilter == null) ? ":textFilter is null" : "lower(playgroundObject.uri || playgroundObject.name || playgroundObject.lastModifier.firstName || playgroundObject.lastModifier.lastName) like :textFilter"; 
		return (List<PlaygroundObject>) sessionFactory
				.getCurrentSession()
				.createQuery(
						"from PlaygroundObject playgroundObject where playground.id=:p AND " + criteriaString + " ORDER BY name"
					)
				.setParameter ("textFilter", textFilter)
				.setParameter("p", playgroundId).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PlaygroundObject> findAllPlaygroundObjectsForUser(User currentUser,Long playgroundId,String textFilter) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
	    final String criteriaString = (textFilter == null) ? ":textFilter is null" : "lower(playgroundObject.uri || playgroundObject.name || playgroundObject.lastModifier.firstName || playgroundObject.lastModifier.lastName) like :textFilter";
		return (List<PlaygroundObject>) sessionFactory
				.getCurrentSession()
				.createQuery(
						"from PlaygroundObject playgroundObject where playground.id=:p and (playgroundObject.status is :status or playgroundObject.creator is :user) AND " + criteriaString + " ORDER BY name")
				.setParameter ("textFilter", textFilter)
				.setParameter("p", playgroundId)
				.setParameter("status", Status.PUBLIC)
				.setParameter("user", currentUser).list();
	}
	
	public List<?> loadPlayground(String uri) {
		 return sessionFactory.getCurrentSession().createCriteria(Playground.class).add(Restrictions.eq("uriId", uri)).list();
	}
	public Set<Playground> findAllPlaygroundsFormanifest(GameManifest manifest) {
		Set<Playground> playgrounds = new HashSet<Playground>();
		if(manifest.getRolesToPlaygroundObjects()!=null) {
			for (RoleToPlaygroundMapping rtpMapping : manifest.getRolesToPlaygroundObjects()) {
				if(rtpMapping.getPlaygroundObject()!=null) {
					playgrounds.add(rtpMapping.getPlaygroundObject().getPlayground());
				}
			}
		}
		return playgrounds;
	}

	public Map < String, String > variableValuesForSession (Participant participant, GameSession gameSession)
	{
		if (participant != null) {
			gameSession = participant.getGameSession();
		}
		Map < String, String > variableValues = new HashMap < String, String > ();
		
		String now = DateFormat.getDateTimeInstance ().format (new Date ());
		variableValues.put ("system.datetime", now);
		GameModel gameModel = gameSession.getManifest().getGameModel();
		variableValues.put ("model.name", gameModel.getName());
		variableValues.put ("model.caption", gameModel.getCaption());
		variableValues.put ("system.session.name", gameSession.getName());
		if (gameSession.getCurrentStepOfPlay() != null) {
			variableValues.put ("system.stepOfPlay.name", gameSession.getCurrentStepOfPlay().getName());
		}
		Locale userLocale = GameUtil.getCurrentUser().getLocale();
		String status = messageSource.getMessage("status." + gameSession.getStatus().name(), null, userLocale);
        variableValues.put ("system.session.status", status);

		Role role = (participant != null) ? participant.getRoleAndPlayground().getRole() : null;
	    if (participant != null && role != null) {
			variableValues.put ("role.name", role.getName());
	        if (participant.getRoleAndPlayground().getPlaygroundObject() != null) {
	            variableValues.put ("system.playgroundObject.name", participant.getRoleAndPlayground().getPlaygroundObject().getName());
	            variableValues.put ("system.playground.name", participant.getRoleAndPlayground().getPlayground().getName());
	        }
			
	        // role attributes
	        variableValues.put ("system.role.value1", participant.getValue1());
	        variableValues.put ("system.role.value2", participant.getValue2());
	        variableValues.put ("system.role.value3", participant.getValue3());
	        variableValues.put ("system.role.value4", participant.getValue4());
	        variableValues.put ("system.role.value5", participant.getValue5());
	    }
		
	    // insert system variables into replacement map
		for (Variable variable : findSystemVariables ()) {
			variableValues.put(variable.getName (), variable.getInitialValue ());
		}
		for (Variable variable : gameModel.getVariables ()) {
			variableValues.put(variable.getName (), variable.getInitialValue ());
		}
	    if (role != null) {
			for (Variable variable : role.getVariables ()) {
				variableValues.put(variable.getName (), variable.getInitialValue ());
			}
	    }
	    
	    // override values with values set for this session 
		List < VariableSessionValue > sessionValues = gameSession.getSessionVariableValues ();
		for (VariableSessionValue sessionValue: sessionValues) {
			Variable variable = sessionValue.getVariable();
	    	if (variable.getRole() == null || (role != null && 
	    		role.getId().equals((variable.getRole().getId())))) {
				variableValues.put (variable.getName (), sessionValue.getValue());
			}
		}
		return variableValues;
	}

	public String substituteVariablesInText (Map < String, String > variableValues, String text)
	{
	    Pattern pat = Pattern.compile ("\\[%([^%]+)%\\]");
	    Matcher match = pat.matcher (text);
	    StringBuffer substitution = new StringBuffer ();
	    while (match.find ()) {
	    	String substring = text.substring(match.start (1), match.end (1));
	    	String value = "";
			Locale userLocale = GameUtil.getCurrentUser().getLocale();
	    	final String messagePrefix = "system.message.";
			if (substring.startsWith(messagePrefix)) {
				String key = substring.substring(messagePrefix.length ());
				value = messageSource.getMessage(key, null, userLocale);
				if (value == null) {
					value = key;
				}
			} else {
	    	    if (variableValues.containsKey(substring)) {
					value = variableValues.get(substring);
					if (value == null) value = messageSource.getMessage("variablevalue.empty", null, userLocale);
	    	    } else {
					value = messageSource.getMessage("variablevalue.notfound", null, userLocale);
	    	    }
	    	}
	    	match.appendReplacement (substitution, value);
	    }
	    match.appendTail (substitution);
	    return substitution.toString();
	}

	public String substituteVariablesInText (Participant participant, String description)
	{
	    return substituteVariablesInText (variableValuesForSession (participant, null), description);
	}
}
