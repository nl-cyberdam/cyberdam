package nl.cyberdam.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

import javax.xml.bind.JAXBException; 

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.FormActivity;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.NextStepOfPlay;
import nl.cyberdam.domain.ProgressActivity;
import nl.cyberdam.domain.Resource;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.RoleActivity;
import nl.cyberdam.domain.Root;
import nl.cyberdam.domain.StepOfPlay;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.domain.ActivityVariable;
import nl.cyberdam.domain.User;
import nl.cyberdam.util.NoCloseZipInputStream;

import org.hibernate.proxy.HibernateProxy;

public class ExchangeManagerGameModelImpl extends ExchangeManager<GameModel> { 

	@Override
	public void export(GameModel gameModel, OutputStream zip, String path)
			throws IOException, JAXBException {

		Root root = new Root();
		GameModel obj = null;
		
		if (gameModel instanceof HibernateProxy) {
			obj = (GameModel) ((HibernateProxy) gameModel)
					.getHibernateLazyInitializer().getImplementation();
		} else {
			obj = gameModel;
		}
		root.setGameModel(obj);

		HashMap<String, ByteArrayInputStream> resources = new HashMap<String, ByteArrayInputStream>();

		if (obj.getResources() != null && !obj.getResources().isEmpty()) {
			for (Resource resource : obj.getResources()) {

				getResource(resource, resources);
			}
		}
		
		for (StepOfPlay sop : obj.getStepsOfPlay()) {
			for (RoleActivity ra : sop.getRoleActivities()) {
				ra.setStepOfPlay(sop);
			}
		}
		
		for (Activity a : obj.getActivities()) {
			if (a instanceof ProgressActivity
					&& ((ProgressActivity) a).getNextStepOfPlayOptions() != null) {
				List<NextStepOfPlay> list = new ArrayList<NextStepOfPlay>();
				for (int i = 0; i < 15; i++) {
					list.add(((ProgressActivity) a).getNextStepOfPlayOptions().get(i));
				}
				((ProgressActivity) a).setNextStepOfPlayOptions(list);
			}
		}

		zip(marshall(root), null, zip, resources);

	}

	@Override
	public void setExchangeObject(ExchangeBean<GameModel> k, Root root) {
		k.setExchangeObject(root.getGameModel());
	}

	@Override
	public boolean existsExchangeObject(GameModel exchangeObject, String uri) {
		return false;
	}

	@Override
	public HashMap<String, Integer> save(ExchangeBean<GameModel> exchangeBean,
			String playgroundUri, User user, String swfPath) throws IOException, JAXBException {

		HashMap<String, Integer> counters = new HashMap<String, Integer>();
		if (exchangeBean.getExchangeObject() != null) {
			GameModel gameModel = exchangeBean.getExchangeObject();
			convertXmlBean(counters, gameModel);

			String name = getNext(exchangeBean.getZipFile());
			while (name != null) {
				copyResources(name, exchangeBean.getExchangeObject(),
						exchangeBean.getZipFile());
				name = getNext(exchangeBean.getZipFile());
			}

			GameModel newGameModel = gameModel.copy(user, true);
			gameModel = null;

			gameManager.save(newGameModel);

		} else {
			return null;
		}
		return counters;
	}
	
	private void copyRoleActivitys(StepOfPlay sop, GameModel gm) {
		StepOfPlay gmSop = null;
		for (StepOfPlay stepOfPlay : gm.getStepsOfPlay()) {
			if (sop.getId().equals(stepOfPlay.getId())) {
				gmSop = stepOfPlay;
			}
		}
		if (gmSop != null) {
			for (RoleActivity ra : sop.getRoleActivities()) {
				boolean isExist = false;
				for (RoleActivity gmRa : gmSop.getRoleActivities()) {
					if (gmRa.getId().equals(ra.getId())) {
						isExist = true;
					}
				}
				if (!isExist) {
					gmSop.getRoleActivities().add(ra);
				}
			}
			sop.setRoleActivities(gmSop.getRoleActivities());
		}
	}

	private void convertXmlBean(HashMap<String, Integer> counters,
			GameModel gameModel) {
		int roleActivityCounter = 0;
		int nextStepOfPlayCounter = 0;
		for (Activity a : gameModel.getActivities()) {
			if( a.getRoleActivities() != null) {
				for (RoleActivity ra : a
						.getRoleActivities()) {
					ra.setActivity(a);
					if(ra.getStepOfPlay() !=null) {
					    if(ra.getStepOfPlay().getRoleActivities() != null) {
        					if (!ra.getStepOfPlay().getRoleActivities().contains(ra)) {
        						ra.getStepOfPlay().getRoleActivities().add(ra);
        					}
    					
        					copyRoleActivitys(ra.getStepOfPlay(), gameModel);
					    }
					}    
					roleActivityCounter++;
				}
				if (a instanceof ProgressActivity && ((ProgressActivity) a).getNextStepOfPlayOptions()!= null) {
					for (NextStepOfPlay nsop : ((ProgressActivity) a).getNextStepOfPlayOptions()) {
						if (nsop.getStep() != null) {
							nextStepOfPlayCounter++;
						}
					}
				}
			}
		}

		for (Activity a : gameModel.getActivities()) {
			if (a instanceof ProgressActivity
					&& ((ProgressActivity) a).getNextStepOfPlayOptions() != null) {
				
				
			for (NextStepOfPlay nsop : ((ProgressActivity) a)
						.getNextStepOfPlayOptions()) {
					if (nsop.getStep() != null
							&& gameModel.getStepsOfPlay() != null) {
						for (StepOfPlay sop : gameModel.getStepsOfPlay()) {
							if (sop.getId().equals(nsop.getStep().getId())
									&& ((ProgressActivity) a)
											.getRoleActivities() != null) {
								nsop.setStep(sop);
							}
						}
					}
				}
			}
		}
		// check on references to removed sysvars and if so, remove them
		List<Variable> lv = gameManager.findSystemVariables();
		lv.addAll(gameModel.getVariables());
		for (Role r: gameModel.getRoles()) {
			lv.addAll(r.getVariables());
		}
		for (Activity a : gameModel.getActivities()) {
			if (a instanceof FormActivity) {
				FormActivity fa = (FormActivity)a;
				for (Iterator<ActivityVariable> it= fa.getActivityVariables().iterator();it.hasNext();) {
					boolean found = false;
					ActivityVariable av = it.next();
					for (Variable v: lv) {
						if (v.getId().equals(av.getVariable().getId())) found = true; 
					}
					if (!found) it.remove();
				}
			}
		}

		countBeans(counters, gameModel, roleActivityCounter,
				nextStepOfPlayCounter);
	}

	private void copyResources(String name, GameModel gm,
			NoCloseZipInputStream ncis) throws IOException {
		if (name.lastIndexOf(".") > -1) {
			name = name.substring(0, name.lastIndexOf("."));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			write(ncis, out);
			for (Resource resource : gm.getResources()) {
				setResource(resource, name, out);
			}
			out.close();
		}
	}

	private void countBeans(HashMap<String, Integer> counters,
			GameModel gameModel, int roleActivityCounter,
			int nextStepOfPlayCounter) {
		counters.put(getMLCodeFromClass(Activity.class.getSimpleName()), Integer.valueOf(gameModel
				.getActivities().size()));
		counters.put(getMLCodeFromClass(RoleActivity.class.getSimpleName()), Integer
				.valueOf(roleActivityCounter));
		counters.put("import.summary.gamemodelResources.count", Integer.valueOf(gameModel
				.getResources().size()));
		counters.put(getMLCodeFromClass(Role.class.getSimpleName()), Integer.valueOf(gameModel
				.getRoles().size()));
		counters.put(getMLCodeFromClass(StepOfPlay.class.getSimpleName()), Integer
				.valueOf(gameModel.getStepsOfPlay().size()));
		counters.put(getMLCodeFromClass(NextStepOfPlay.class.getSimpleName()), Integer
				.valueOf(nextStepOfPlayCounter));
		counters.put(getMLCodeFromClass(Variable.class.getSimpleName()), Integer
				.valueOf(gameModel.getVariables().size()));
	}
}
