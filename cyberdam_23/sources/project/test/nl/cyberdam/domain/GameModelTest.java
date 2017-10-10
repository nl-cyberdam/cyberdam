package nl.cyberdam.domain;

import junit.framework.TestCase;

public class GameModelTest extends TestCase {
	
	private String COPYTESTNAME = "copytest";
	private String COPYTESTDESCRIPTION = "copytestdescription";

	/**
	 * test deleting a resource
	 */
	public void testDeleteResource() {
		GameModel model = new GameModel();
		Resource resource = new Resource();
		FileUploadActivity activity = new FileUploadActivity();
		model.addActivity(activity);
		model.addResource(resource);
		
		assertEquals("there should be one resource after adding one", 1, model.getResources().size());
		
		model.removeResource(resource);
		assertEquals("there should be no resource left after deleting one", 0, model.getResources().size());

	}
	
	/**
	 * test deleting a resource that is in use
	 */
	public void testDeleteInUseResource() {
		GameModel model = new GameModel();
		Resource resource = new Resource();
		FileUploadActivity activity = new FileUploadActivity();
		activity.getAttachments().add(resource);
		
		model.addActivity(activity);
		model.addResource(resource);
		
		assertEquals("there should be one resource after adding one", 1, model.getResources().size());
		
		try {
			model.removeResource(resource);
			fail("Attempt to remove a resource should fail and throw an exception");
		} catch (GameModelModificationException gmme) {
			// do nothing
		}
		assertEquals("there should still be one resource because delete should fail (since resource is in use by an activity)", 1, model.getResources().size());
	}
	
	public void testAddRemoveRole() {
		GameModel model = new GameModel();
		Role role = new Role();
		model.addRole(role);
		assertEquals("there should be one role after adding one", 1, model.getRoles().size());
		model.removeRole(role);
		assertEquals("there should be no role after deleting one", 0, model.getRoles().size());
	}
	
	/**
	 * test removing a role that is in use by a step of play (RoleActivity) 
	 * and removing a role that is in use by a MessageActivity
	 */
	public void testRemoveInUseRole() {
		GameModel model = new GameModel();
		Role roleInStepOfPlay = new Role();
		Role roleInMessageActivity = new Role();

		model.addRole(roleInStepOfPlay);
		model.addRole(roleInMessageActivity);
		
		StepOfPlay step = new StepOfPlay();
		RoleActivity roleActivity = new RoleActivity();
		roleActivity.setRole(roleInStepOfPlay);
		step.getRoleActivities().add(roleActivity);
		model.addStepOfPlay(step);
		
		MessageActivity activity = new MessageActivity();
		activity.getRecipients().add(roleInMessageActivity);
		model.addActivity(activity);
		
		assertEquals("there should be two roles", 2, model.getRoles().size());
		
		try {
			model.removeRole(roleInStepOfPlay);
			fail("Attempt to remove this role should fail and throw an exception");
		} catch (GameModelModificationException gmme) {
			// do nothing
		}
		try {
			model.removeRole(roleInMessageActivity);
			fail("Attempt to remove this role should fail and throw an exception");
		} catch (GameModelModificationException gmme) {
			// do nothing
		}
		
		assertEquals("there should be still two roles after attempted delete (role is in use)", 2, model.getRoles().size());
	}
	
	public void testAddRemoveActivity() {
		GameModel model = new GameModel();
		MessageActivity activity = new MessageActivity();
		model.addActivity(activity);
		assertEquals("there should be one activity", 1, model.getActivities().size());
		model.removeActivity(activity);
		assertEquals("there should be no activity after remove", 0, model.getActivities().size());
	}
	
	public void testRemoveInUseActivity() {
		GameModel model = new GameModel();
		MessageActivity activity = new MessageActivity();
		model.addActivity(activity);
		assertEquals("there should be one activity", 1, model.getActivities().size());
		
		StepOfPlay step = new StepOfPlay();
		RoleActivity roleActivity = new RoleActivity();
		roleActivity.setActivity(activity);
		step.getRoleActivities().add(roleActivity);
		model.addStepOfPlay(step);
		
		try {
			model.removeActivity(activity);
			fail("Attempt to remove this activity should fail and throw an exception");
		} catch (GameModelModificationException gmme) {
			// do nothing
		}
		
		assertEquals("there should still be one activity after remove", 1, model.getActivities().size());
	}
	
	public void testCopyGameModel() {
		User owner = new User();
		owner.setUsername("testuser");
		
		User copyowner = new User();
		copyowner.setUsername("copytestuser");
		
		GameModel original = new GameModel();
		
		// basic settings
		original.setName(COPYTESTNAME);
		original.setDescription(COPYTESTDESCRIPTION);
		original.setOwner(owner);

		Role role1 = new Role();
		Role role2 = new Role();
		StepOfPlay step1 = new StepOfPlay();
		StepOfPlay step2 = new StepOfPlay();
		StepOfPlay step3 = new StepOfPlay();
		Resource resource1 = new Resource();
		Resource resource2 = new Resource();
		Resource resource3 = new Resource();
		
		// add roles
		role1.setName("role1");
		role2.setName("role2");
		original.addRole(role1);
		original.addRole(role2);

		// add resources
		resource1.setName("resource1");
		resource2.setName("resource2");
		resource3.setName("resource3");
		original.addResource(resource1);
		original.addResource(resource2);
		original.addResource(resource3);
		
		// add activities
		FileUploadActivity activity1 = new FileUploadActivity();
		activity1.setName("activity1");
		// add two attachments
		activity1.getAttachments().add(resource1);
		activity1.getAttachments().add(resource2);
		
		MessageActivity activity2 = new MessageActivity();
		activity2.setName("activity2");
		
		ProgressActivity activity3 = new ProgressActivity();
		activity3.setName("activity3");
		NextStepOfPlay nextStep = new NextStepOfPlay();
		nextStep.setDescription("description1");
		nextStep.setStep(step1);
		activity3.getNextStepOfPlayOptions().add(nextStep);
		activity3.setDefaultOption(12);
		assertEquals("number of next step of play options in original should be 1", 1, activity3.getNextStepOfPlayOptions().size());
		
		original.addActivity(activity1);
		original.addActivity(activity2);
		original.addActivity(activity3);

		// add steps of play
		step1.setName("step1");
		step2.setName("step2");
		step3.setName("step3");
		original.addStepOfPlay(step1);
		original.addStepOfPlay(step2);
		original.addStepOfPlay(step3);
		
		// ad activity1 for role1 to step1
		RoleActivity roleAct1 = new RoleActivity();
		roleAct1.setActivity(activity1);
		roleAct1.setRole(role1);
		step1.getRoleActivities().add(roleAct1);
		
		// set initial step of play
		// XXX also make a test without initial step of play!
		original.setInitialStepOfPlay(step1);
		
		// now make a copy
		GameModel copy = original.copy(copyowner);
		
		// asserts
		assertEquals("name is different", COPYTESTNAME, copy.getName());
		assertEquals("description is different", COPYTESTDESCRIPTION, copy.getDescription());
		assertEquals("owner is different", owner, copy.getOwner());
		assertEquals("status of the copy should be UNDER_CONSTRUCTION", Status.UNDER_CONSTRUCTION, copy.getStatus());
		assertEquals("the name of the initial step of play is different", original.getInitialStepOfPlay().getName(), copy.getInitialStepOfPlay().getName());
		
		assertEquals("number of steps of play is different", original.getStepsOfPlay().size(), copy.getStepsOfPlay().size());
		assertEquals("number of roles is different", original.getRoles().size(), copy.getRoles().size());
		assertEquals("number of activities is different", original.getActivities().size(), copy.getActivities().size());
		assertEquals("number of resources is different", original.getResources().size(), copy.getResources().size());
		
		assertEquals("name of step of play 1 is incorrect", original.getStepsOfPlay().get(0).getName(), copy.getStepsOfPlay().get(0).getName());
		assertEquals("name of role 1 is incorrect", original.getRoles().get(0).getName(), copy.getRoles().get(0).getName());
		
		assertEquals("name of activity 1 is incorrect", original.getActivities().get(0).getName(), copy.getActivities().get(0).getName());
		assertEquals("name of activity 2 is incorrect", original.getActivities().get(1).getName(), copy.getActivities().get(1).getName());
		assertEquals("name of activity 3 is incorrect", original.getActivities().get(2).getName(), copy.getActivities().get(2).getName());
		
		assertEquals("name of resource 1 is incorrect", original.getResources().get(0).getName(), copy.getResources().get(0).getName());
		assertEquals("name of resource 2 is incorrect", original.getResources().get(1).getName(), copy.getResources().get(1).getName());
		assertEquals("name of resource 3 is incorrect", original.getResources().get(2).getName(), copy.getResources().get(2).getName());
		
		// check resources added to activities
		assertEquals("activity1 number of resources is incorrect", original.getActivities().get(0).getAttachments().size(), copy.getActivities().get(0).getAttachments().size());
		assertEquals("activity2 number of resources is incorrect", original.getActivities().get(1).getAttachments().size(), copy.getActivities().get(1).getAttachments().size());
		assertEquals("activity3 number of resources is incorrect", original.getActivities().get(2).getAttachments().size(), copy.getActivities().get(2).getAttachments().size());
		
		ProgressActivity pOrig = (ProgressActivity) original.getActivities().get(2);
		ProgressActivity pCopy = (ProgressActivity) copy.getActivities().get(2);
		assertEquals("activity3 default next step option is incorrect", pOrig.getDefaultOption(), pCopy.getDefaultOption());
		assertEquals("activity3 number of next steps of play is incorrect", pOrig.getNextStepOfPlayOptions().size(), pCopy.getNextStepOfPlayOptions().size());
		assertEquals("activity3 next step of play 1 description is not the same as the original", pOrig.getNextStepOfPlayOptions().get(0).getDescription(), pCopy.getNextStepOfPlayOptions().get(0).getDescription());
		assertEquals("activity3 next step of play 1 reference to step of play is not step of play number 1", copy.getStepsOfPlay().get(0), pCopy.getNextStepOfPlayOptions().get(0).getStep());
		
		// check activities per role per step of play
		// we put activity1 for role1 on step1
		assertEquals("step1 number of roleactivities is incorrect", original.getStepsOfPlay().get(0).getRoleActivities().size(), copy.getStepsOfPlay().get(0).getRoleActivities().size());
		assertEquals("step1 roleactivity 1 role is not the same as role1", copy.getRoles().get(0), copy.getStepsOfPlay().get(0).getRoleActivities().get(0).getRole());
		assertEquals("step1 roleactivity 1 activity is not the same as activity1", copy.getActivities().get(0), copy.getStepsOfPlay().get(0).getRoleActivities().get(0).getActivity());
		assertEquals("step1 roleactivity 1 role is not the same as role1", copy.getRoles().get(0), copy.getStepsOfPlay().get(0).getRoleActivities().get(0).getRole());
		
		assertEquals("step2 number of roleactivities is incorrect", original.getStepsOfPlay().get(1).getRoleActivities().size(), copy.getStepsOfPlay().get(1).getRoleActivities().size());
		assertEquals("step3 number of roleactivities is incorrect", original.getStepsOfPlay().get(2).getRoleActivities().size(), copy.getStepsOfPlay().get(2).getRoleActivities().size());
	}
}
