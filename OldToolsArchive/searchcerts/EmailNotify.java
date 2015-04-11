package com.sun.authtech.appvtool.searchcerts;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class EmailNotify {

	private String msgText1 = null;
	private String msgText2 = null;
	private String email_address = null;

	public EmailNotify( 
			String message_in, String attachment_in, String email_add_in ) {

		msgText1 = message_in;
		msgText2 = attachment_in;
		email_address = email_add_in;
	}

	public void send() {

		Properties props = new Properties();
		props.put("mail.smtp.host", "mailhost" );

		Session session = Session.getInstance(props, null);

		try {
			MimeMessage msg = new MimeMessage( session );
			msg.setFrom( new InternetAddress( "pkiops@sun.com" ));
			InternetAddress[] address = { new InternetAddress( email_address ) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("Sun PKI Services");
			msg.setSentDate( new Date() );

			// create and fill the first message part
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText( msgText1 );

			// create and fill the second message part
			MimeBodyPart mbp2 = new MimeBodyPart();
			// Use setText(text, charset), to show it off !
			mbp2.setText( msgText2, "us-ascii" );

			// create the Multipart and its parts to it
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);

			// add the Multipart to the message
			msg.setContent(mp);

			// send the message
			Transport.send(msg);

		} catch (MessagingException mex) {
			mex.printStackTrace();
			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				ex.printStackTrace();
			}
		}
	}
}
