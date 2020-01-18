package db.train;

import org.webrepogen.annotations.*;

@WebPackage
@RepoPackage
@ServicePackage
@AllEntities
@WRConfiguration(controllerBaseClass = "db.train.web.AbstractWebController")
public interface WebRepoGenConfig {
}
