import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HeadNTail {

	private boolean DEBUG = false;

	public void putHead(PrintWriter out) {

		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");

		out.println("</head>");

		out.println("<body bgcolor=\"#666699\" LINK=\"#666699\" VLINK=\"#666699\" ALINK=\"#FF0000\">");
		out.println("<!--stopindex-->");

		out.println("<BODY BGCOLOR=\"#666699\" LINK=\"#666699\" VLINK=\"#666699\" ALINK=\"#FF0000\">");

		out.println("<CENTER>");

		out.println("<A NAME=\"top\"></A>");

		out.println("<!-- MASTER TABLE BEGIN -->");

		out.println("<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\" WIDTH=\"95%\">");
		out.println("<TR><TD BGCOLOR=\"#FFFFFF\">");

		out.println("<!-- MASTHEAD TABLE BEGIN-->");

		out.println("<TABLE WIDTH=\"100%\" BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\">");

		out.println("<!-- FIRST ROW ---- LOGO / ID / GENERAL LINKS BEGIN -->");

		out.println("<TR BGCOLOR=\"#000000\">");

		out.println("<TD WIDTH=\"1%\" VALIGN=\"BOTTOM\" NOWRAP><IMG SRC=\"/images/logo_top2.gif\" WIDTH=\"128\" HEIGHT=\"21\" ALT=\"Sun Microsystems, Inc.\" BORDER=\"0\" HSPACE=\"0\" VSPACE=\"0\"></TD>");

		out.println("<!-- BEGIN APPLICATION TITLE -->");

		out.println("<TD ALIGN=\"LEFT\" VALIGN=\"MIDDLE\"  NOWRAP>");
		out.println("<FONT SIZE=\"3\" COLOR=\"#FFFFFF\" FACE=\"Geneva, Arial, Helvetica, San-Serif\">");
		out.println("<B>Token Card Administration</B>");
		out.println("</FONT>");
		out.println("</TD>");

		out.println("<!-- END APPLICATION TITLE -->");

		out.println("<TD ALIGN=\"RIGHT\" VALIGN=\"MIDDLE\" NOWRAP>");

		out.println("<!-- GENERAL LINKS BEGIN -->");

		out.println("<FONT COLOR=\"#FF9900\" FACE=\"Geneva, Arial, Helvetica\" SIZE=\"2\">");
		out.println("<!--A HREF=\"LINK_ONE\"><FONT COLOR=\"#FFFFFF\" CLASS=\"nonuw\">Link One</FONT></A-->   ");
		out.println("<!--A HREF=\"LINK_TWO\"><FONT COLOR=\"#FFFFFF\" CLASS=\"nonuw\">Link Two</FONT></A> |  ");
		out.println("<!--A HREF=\"LINK_THREE\"><FONT COLOR=\"#FFFFFF\" CLASS=\"nonuw\">Link Three</FONT></A-->     ");
		out.println("<!--A HREF=\"LINK_FOUR\"><FONT COLOR=\"#FFFFFF\" CLASS=\"nonuw\">Link Four</FONT></A-->&nbsp;</FONT>    ");

		out.println("<!-- GENERAL LINKS END -->");

		out.println("</TD>");
		out.println("</TR>");

		out.println("<!-- FIRST ROW ---- LOGO / ID / GENERAL LINKS END -->");

		out.println("<!-- SECOND ROW / LIGHT BLUE LINE BEGIN -->");

		out.println("<TR BGCOLOR=\"#CCCCFF\"><TD WIDTH=\"1%\"><IMG SRC=\"/images/logo_mid2.gif\" BORDER=\"0\" WIDTH=\"119\" HEIGHT=\"1\" ALT=\"Sun Microsystems, Inc.\" HSPACE=\"0\"></TD>");
		out.println("<TD><IMG SRC=\"/images/dot.gif\" WIDTH=\"250\" HEIGHT=\"1\" ALT=\"spacer\" BORDER=\"0\"></TD>");
		out.println("<TD ALIGN=\"RIGHT\"><IMG SRC=\"/images/kdot.gif\" BORDER=\"0\" WIDTH=\"1\" HEIGHT=\"1\" ALT=\"black spacer\"></TD></TR>");

		out.println("<!-- SECOND ROW / LIGHT BLUE LINE END -->");

		out.println("<!-- THIRD ROW ---- LOGO -->");

		out.println("<TR BGCOLOR=\"#666699\">");
		out.println("<TD WIDTH=\"1%\" VALIGN=\"TOP\" NOWRAP><IMG SRC=\"/images/logo_bot2.gif\" WIDTH=\"119\" HEIGHT=\"38\" ALT=\"Sun Microsystems, Inc.\" BORDER=\"0\"></TD>");
		out.println("<TD COLSPAN=\"2\" ALIGN=\"RIGHT\" VALIGN=\"TOP\"><IMG SRC=\"/images/r_fade.gif\" BORDER=\"0\" WIDTH=\"1\" HEIGHT=\"31\" ALT=\"black fade\"></TD>");
		out.println("</TR>");

		out.println("<!-- THIRD ROW ---- LOGO END -->");

		out.println("<!-- FOURTH ROW BLACK LINE BEGIN -->");

		out.println("<TR BGCOLOR=\"#000000\">");
		out.println("<TD COLSPAN=\"3\"><IMG SRC=\"/images/dot.gif\" WIDTH=\"100%\" HEIGHT=\"2\" ALT=\"spacer\" BORDER=\"0\"></TD>");
		out.println("</TR>");

		out.println("<!-- FOURTH ROW BLACK LINE END -->");

		out.println("</TABLE>");

		out.println("<!-- MASTHEAD TABLE END -->");

		out.println("<!-- CONTENT TABLE BEGIN -->");

		out.println("<TABLE BORDER=\"0\" CELLSPACING=\"1\" CELLPADDING=\"0\" WIDTH=\"100%\">                        ");
		out.println("<TR><TD VALIGN=\"TOP\">");
		out.println("<FONT FACE=\"Geneva, Arial, Helvetica\" SIZE=3>");

		out.println("<!--startindex-->");

		out.println("<!--                       MAIN CONTENT                           -->");
		out.println("<!-- ============================================================ -->");

	}


	/**
	 *  Generate the footer portion of the servlet page.
	 *
	 *@param  out  Reference to PrintWriter for output of html.
	 */
	public void putTail(PrintWriter out) {

		out.println("<p>");
		out.println("<p>");
		out.println("<h2>Getting Help and Information</h2>");

		out.println("<p>");
		out.println("For online help, see:");
		out.println("<p>");
		out.println(
				"<a href=\"https://hanoi.central:443/TokenCards/ToknCrdEsntls.html\"> Token Card Essentials");

		out.println("</a>");

		out.println("<p>");
		out.println("Contact your nearest Resolution Center:");
		out.println("<p>");
		out.println("Americas: <b>x27000</b> or <b>303-272-7000</b>");
		out.println("<br>");
		out.println("Europe: <b>x15555</b> or <b>+31-33-454-2555</b>");
		out.println("<br>");
		out.println("Japan: <b>x55119</b> or <b>+81-3-5717-5119</b>");
		out.println("<br>");
		out.println("Korea: <b>x85388</b> or <b>+82-2-2193-5388</b>");
		out.println("<br>");
		out.println("China/Taiwan: <b>x88888</b> or <b>+65-63959888</b>");
		out.println("<br>");
		out.println("Asia South: <b>x88888</b> or <b>+65-63959888</b>");
		out.println("<br>");
		out.println("Australia/NZ: <b>x88888</b> or <b>+65-63959888</b>");
<b>x57300</b> or <b>+61-2-9844-5300</b>");
		out.println("<!-- ============================================================ -->");
		out.println("<!--                       END MAIN CONTENT                           -->");

		out.println("<TR> ");
		out.println("  <TD> </TD>");
		out.println("</TR>");
		out.println("</TABLE>");

		out.println("</TD></TR></TABLE>");

		out.println("</FONT>");
		out.println("</TD>");
		out.println("</TR>");
		out.println("</TABLE>");

		out.println("</TD>");
		out.println("</TR>");
		out.println("<TR>");
		out.println("<TD>");


		out.println("<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"2\" WIDTH=\"100%\">");

		out.println("<TR>");
		out.println("<TD BGCOLOR=\"#CC0033\"><IMG SRC=\"/images/dot.gif\" WIDTH=\"151\" HEIGHT=\"1\" ALT=\"webtone\" BORDER=\"0\"></TD>");
		out.println("<TD BGCOLOR=\"#CC9900\"><IMG SRC=\"/images/dot.gif\" WIDTH=\"151\" HEIGHT=\"1\" ALT=\"webtone\" BORDER=\"0\"></TD>");
		out.println("<TD BGCOLOR=\"#CCCC33\"><IMG SRC=\"/images/dot.gif\" WIDTH=\"151\" HEIGHT=\"1\" ALT=\"webtone\" BORDER=\"0\"></TD>");
		out.println("<TD BGCOLOR=\"#FF9900\"><IMG SRC=\"/images/dot.gif\" WIDTH=\"151\" HEIGHT=\"1\" ALT=\"webtone\" BORDER=\"0\"></TD>");
		out.println("</TR>");

		out.println("<TR><TD BGCOLOR=\"#000000\" COLSPAN=\"4\">");

		out.println("<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"4\">");
		out.println("<TR><TD>");

		out.println("<FONT FACE=\"Geneva, Helvetica, Arial, SunSans-Regular\" COLOR=\"#FFFFFF\" SIZE=\"2\">");
		out.println("Copyright 1994-2000 Sun Microsystems, Inc., 901 San Antonio Road, Palo Alto, CA 94303 USA. All rights reserved.<BR>");
		out.println("<A HREF=\"http://www.sun.com/share/text/SMICopyright.html\"><FONT COLOR=\"#FFFFFF\">Legal Terms</FONT></A>. ");
		out.println("<A HREF=\"http://www.sun.com/privacy/\"><FONT COLOR=\"#FFFFFF\">Privacy Policy</FONT></A>. ");
		out.println("<A HREF=\"mailto:Mark.Hotchkiss@Central.Sun.COM\"><FONT COLOR=\"#FFFFFF\">Feedback</FONT></A></FONT>");

		out.println("</TD></TR>");
		out.println("</TABLE>");

		out.println("</TD></TR>");
		out.println("</TABLE>");

		out.println("</TD>");
		out.println("</TR>");
		out.println("</TABLE>");

		out.println("</TD>");
		out.println("</TR>");
		out.println("</TABLE>");

		out.println("</CENTER>");

		out.println("</BODY>");
		out.println("</HTML>");
	}
}

