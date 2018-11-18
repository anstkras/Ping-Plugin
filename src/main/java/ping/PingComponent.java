package ping;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import configurable.PingConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PingComponent implements BaseComponent {
    private final Logger logger = Logger.getLogger("ping");
    private final PingConfig config;
    private PingWidget widget = null;
    private StatusBar statusBar = null;
    private String internetAddress;
    private long fastTime;
    private long mediumTime;

    PingComponent() {
        logger.log(Level.INFO, "start");

        config = PingConfig.getInstance();
        internetAddress = config.getInternetAddress();
        fastTime = config.getFastTime();
        mediumTime = config.getMediumTime();
    }

}
