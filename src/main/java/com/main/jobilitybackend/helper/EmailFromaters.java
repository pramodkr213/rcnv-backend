package com.main.jobilitybackend.helper;


public class EmailFromaters {
    
    public static String getChangeEmailFormat(String oldEmail ,String newEmail){
        StringBuilder sb = new StringBuilder();
        sb.append("Dear User, \n\n");
        sb.append("We have received a request to change your email address from " + oldEmail + " to " + newEmail + ".\n");
        sb.append("If you did not make this request, please ignore this email.\n");
        sb.append("If you did make this request, please click the link below to confirm the change:\n");
        sb.append("http://example.com/confirm-email-change?oldEmail=" + oldEmail + "&newEmail=" + newEmail + "\n\n");
        sb.append("Thank you for using our service.\n");
        sb.append("Best regards,\n");
        sb.append("Jobility Team");
        return sb.toString();

    }
}
