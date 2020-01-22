package exceptions;

public class ExceptionAlert extends Exception {

    private String alertTitle;
    private String alertHeader;
    private String alertMessage;

    public ExceptionAlert(String alertTitle, String alertHeader, String alertMessage) {
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

    public String getAlertMessage() {
        return alertMessage;
    }

}
