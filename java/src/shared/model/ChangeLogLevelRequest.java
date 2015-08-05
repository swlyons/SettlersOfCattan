package shared.model;

import java.io.Serializable;

/**
 * the request for changing a logging level
 *
 * @author Aaron
 *
 */
public class ChangeLogLevelRequest implements Serializable {

    private String logLevel;

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
}
