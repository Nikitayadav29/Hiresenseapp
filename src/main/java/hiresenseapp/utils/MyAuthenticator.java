package hiresenseapp.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator{

	private static final String EMAIL = "nikitayadav1238@gmail.com";
    private static final String PASSWORD = "ambj yeoe zyjj jypq";

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        PasswordAuthentication pwdAuth=new PasswordAuthentication(EMAIL,PASSWORD);
        return pwdAuth;

    }

}

