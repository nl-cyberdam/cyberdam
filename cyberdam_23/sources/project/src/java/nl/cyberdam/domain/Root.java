package nl.cyberdam.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "root", propOrder = {
    "gameModel",
    "playground"
})
@XmlRootElement
public class Root {
  private Playground playground;
  private GameModel gameModel;
  @XmlAttribute
  private String version="2.3";

public Playground getPlayground() {
	return playground;
}

public void setPlayground(Playground playground) {
	this.playground = playground;
}

public GameModel getGameModel() {
	return gameModel;
}

public void setGameModel(GameModel gameModel) {
	this.gameModel = gameModel;
}

public String getVersion() {
	return version;
}

public void setVersion(String version) {
	this.version = version;
}
}
