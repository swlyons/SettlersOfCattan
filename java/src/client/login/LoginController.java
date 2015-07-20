package client.login;

import client.base.*;
import client.managers.UserManager;
import client.misc.*;
import client.communication.ClientCommunicator;
/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController {

    private IMessageView messageView;
    private IAction loginAction;

    /**
     * LoginController constructor
     *
     * @param view Login view
     * @param messageView Message view (used to display error messages that
     * occur during the login process)
     */
    public LoginController(ILoginView view, IMessageView messageView) {

        super(view);

        this.messageView = messageView;
    }

    public ILoginView getLoginView() {

        return (ILoginView) super.getView();
    }

    public IMessageView getMessageView() {

        return messageView;
    }

    /**
     * Sets the action to be executed when the user logs in
     *
     * @param value The action to be executed when the user logs in
     */
    public void setLoginAction(IAction value) {

        loginAction = value;
    }

    /**
     * Returns the action to be executed when the user logs in
     *
     * @return The action to be executed when the user logs in
     */
    public IAction getLoginAction() {   

        return loginAction;
    }

    @Override
    public void start() {
        ClientCommunicator.getSingleton().setJoinedGame(false);
        getLoginView().showModal();
    }

    @Override
    public void signIn() {
        UserManager userManager = new UserManager();
        String username = getLoginView().getLoginUsername();
        String password = getLoginView().getLoginPassword();
        boolean sucessful = userManager.authenticateUser(username, password);

        // If log in succeeded
        if (sucessful) {
            getLoginView().closeModal();
            loginAction.execute();
        } else {
            messageView.setTitle("Error");
            messageView.setMessage("Sign in failed!");
            messageView.showModal();
        }
    }

    @Override
    public void register() {
        UserManager userManager = new UserManager();
        String username = getLoginView().getRegisterUsername();
        String password = getLoginView().getRegisterPassword();
        String passConf = getLoginView().getRegisterPasswordRepeat();
        boolean sucessful = userManager.createUser(username, password, passConf);

        // If register succeeded
        if (sucessful) {
            if (userManager.authenticateUser(username, password)) {
                getLoginView().closeModal();
                loginAction.execute();
            } else {
                messageView.setTitle("Error");
                messageView.setMessage("Sign in failed!");
                messageView.showModal();
            }
        } else {
            messageView.setTitle("Error");
            if (userManager.validatePasswordsMatch(password, passConf)) {
                messageView.setMessage("Invalid username or password!");
            } else {
                messageView.setMessage("Passwords don't match!");
            }

            messageView.showModal();
        }
    }

}
