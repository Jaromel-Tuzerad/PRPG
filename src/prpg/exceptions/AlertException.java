package prpg.exceptions;

public class AlertException extends Exception {

    private String alertTitle;
    private String alertHeader;
    private String alertMessage;

    public AlertException(String alertTitle, String alertHeader, String alertMessage) {
        super("Call alert message");
        this.alertTitle = alertTitle;
        this.alertHeader = alertHeader;
        this.alertMessage = alertMessage;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public String getAlertHeader() {
        return alertHeader;
    }

    public String getAlertContent() {
        return alertMessage;
    }

}
