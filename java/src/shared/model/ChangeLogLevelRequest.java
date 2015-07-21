package shared.model;

/**
 * the request for changing a logging level
 *
 * @author Aaron
 *
 */
public class ChangeLogLevelRequest {

    private String logLevel;

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
}
