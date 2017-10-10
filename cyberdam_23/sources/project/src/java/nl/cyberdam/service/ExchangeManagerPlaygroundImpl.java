package nl.cyberdam.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.Root;
import nl.cyberdam.domain.User;
import nl.cyberdam.util.NoCloseZipInputStream;

import org.hibernate.proxy.HibernateProxy;

public class ExchangeManagerPlaygroundImpl extends ExchangeManager<Playground> {
	
	@Override
	public void export(Playground playground, OutputStream zip, String swfPath)
			throws IOException, JAXBException {
		Root root = new Root();
		Playground obj = null;
		if (playground instanceof HibernateProxy) {
			obj = (Playground) ((HibernateProxy) playground)
					.getHibernateLazyInitializer().getImplementation();
		} else {
			obj = playground;
		}
		root.setPlayground(obj);
		InputStream is = null;

		if (obj.getLink() != null && !"".equals(obj.getLink())
			&& !obj.isHttpLink()) {
			is = new FileInputStream(new File(getServletContext().getRealPath(swfPath),
									 obj.getLink()));
		}

		HashMap<String, ByteArrayInputStream> resources = new HashMap<String, ByteArrayInputStream>();

		if (obj.getPlaygroundObjects() != null
				&& !obj.getPlaygroundObjects().isEmpty()) {
			for (PlaygroundObject playgroundObject : obj.getPlaygroundObjects()) {
				getResource(playgroundObject.getPicture(), resources);
				getResource(playgroundObject.getThumbnail(), resources);
			}
		}

		zip(marshall(root), is, zip, resources);
	}

	public HashMap<String, Integer> save(ExchangeBean<Playground> exchangeBean,
			String playgroundUri, User user, String swfPath) throws IOException {
		HashMap<String, Integer> counters = new HashMap<String, Integer>();
		String name = null;
		if (exchangeBean.getExchangeObject() != null) {
			if (playgroundUri != null) {
				exchangeBean.getExchangeObject().setUriId(playgroundUri);
			}
			Set<PlaygroundObject> playgroundObjects = exchangeBean
					.getExchangeObject().getPlaygroundObjects();
			int resourceCounter = 0;
			if (playgroundObjects != null && !playgroundObjects.isEmpty()) {
				for (PlaygroundObject playgroundObject : playgroundObjects) {
					if (playgroundObject.getPicture() != null) {
						resourceCounter++;
					}
					if (playgroundObject.getThumbnail() != null) {
						resourceCounter++;
					}
					playgroundObject.setPlayground(exchangeBean
							.getExchangeObject());
				}
				counters.put(getMLCodeFromClass(PlaygroundObject.class.getSimpleName()), Integer
						.valueOf(playgroundObjects.size()));
				counters.put("import.summary.playgroundResources.count", Integer
						.valueOf(resourceCounter));
			}

			name = getNext(exchangeBean.getZipFile());
			while (name != null && !"swf".equals(name)) {
				copyResources(name, exchangeBean.getExchangeObject(),
						exchangeBean.getZipFile());
				name = getNext(exchangeBean.getZipFile());
			}

			Playground copy = exchangeBean.getExchangeObject().copy(user);

			
			Set<PlaygroundObject> s = copy.getPlaygroundObjects();
			Iterator<PlaygroundObject> i = s.iterator();
			while(i.hasNext()) {
			    PlaygroundObject po = i.next();
			    if (po.getAddress()== null || po.getAddress().length() == 0) {
			        po.setAddress("address");
			    }
			    if(po.getCaption() == null || po.getCaption().length() == 0) {
			        po.setCaption("caption");
			    }
			}
			gameManager.save(copy);
			if ("swf".equals(name) && exchangeBean.getExchangeObject() != null
					&& exchangeBean.getExchangeObject().getLink() != null
					&& !isExternal(exchangeBean.getExchangeObject().getLink())) {
				saveFile(exchangeBean, exchangeBean.getExchangeObject(),
						counters, swfPath);
			}
		} else {
			return null;
		}
		
		return counters;
	}

	private void copyResources(String name, Playground playground,
			NoCloseZipInputStream ncis) throws IOException {
		if (name.lastIndexOf(".") > -1) {
			name = name.substring(0, name.lastIndexOf("."));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			write(ncis, out);
			for (PlaygroundObject playgroundObject : playground
					.getPlaygroundObjects()) {
				setResource(playgroundObject.getPicture(), name, out);
				setResource(playgroundObject.getThumbnail(), name, out);
			}
			out.close();
		}
	}

	private void saveFile(ExchangeBean<Playground> exchangeBean,
			Playground playground, Map<String, Integer> counters, String swfPath)
			throws IOException {
		if (exchangeBean.getZipFile() != null) {
			
			
			File f = new File(getServletContext().getRealPath(swfPath),
					playground.getLink());

			if (!f.exists()) {
				FileOutputStream out = new FileOutputStream(f);
				write(exchangeBean.getZipFile(), out);
				out.close();
			} else {
				counters.put(SWF_NOT_SAVED, Integer.valueOf(0));
			}
		}
	}

	public void setExchangeObject(ExchangeBean<Playground> k, Root root) {
		k.setExchangeObject(root.getPlayground());
	}

	public boolean existsExchangeObject(Playground playground, String uri) {
		if (playground == null) {
			return false;
		}
		if (uri == null || "".equals(uri)) {
			return gameManager.loadPlayground(playground.getUriId()).size() > 0;
		} else {
			return gameManager.loadPlayground(uri).size() > 0;
		}
	}

	private boolean isExternal(String link) {
		return link.trim().toLowerCase().startsWith("http");
	}

	public LogService getGameLogService() {
		// TODO Auto-generated method stub
		return null;
	}
}
