//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.composer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.event.EventListenerList;

import org.columba.core.desktop.ColumbaDesktop;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.message.ColumbaMessage;
import org.columba.mail.message.IColumbaMessage;
import org.columba.mail.parser.ListBuilder;
import org.columba.mail.parser.ListParser;
import org.columba.mail.parser.NormalizeRecipientListParser;
import org.columba.ristretto.io.FileSource;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.LocalMimePart;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.StreamableMimePart;

/**
 * @author frd
 *
 * Model for message composer dialog
 *
 */
public class ComposerModel {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.gui.composer"); //$NON-NLS-1$

	private ColumbaMessage message;

	private AccountItem accountItem;

	private String bodytext;

	private Charset charset;

	private List attachments;

	private List<String> toList;

	private List<String> ccList;

	private List<String> bccList;

	private boolean signMessage;

	private boolean encryptMessage;

	/**
	 * this regular expression should cover anything from a@a.pt or a@a.com to
	 * a@a.info. Permits usage of invalid top domains though.
	 * <p>
	 * [bug] fdietz: added "." and "-" as regular characters
	 * (example:mail@toplevel.mail.de)
	 * <p>
	 * TODO: see if we can replace the matching code with Ristretto stuff
	 *
	 */
	private static final String emailRegExp = "[a-zA-Z0-9]+([_\\.-][a-zA-Z0-9]+)*@([a-zA-Z0-9]+([\\.-][a-zA-Z0-9]+)*)+\\.[a-zA-Z]{2,}";

	// original: "^[a-zA-Z0-9]+@[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,4}+$";

	private static final Pattern emailPattern = Pattern.compile(emailRegExp);

	/**
	 * source reference
	 * <p>
	 * When replying/forwarding this is the original message you selected in the
	 * message-list and replied to
	 */
	private MailFolderCommandReference ref;

	/**
	 * Flag indicating whether this model holds a html message (true) or plain
	 * text (false)
	 */
	private boolean isHtmlMessage;

	private EventListenerList listenerList = new EventListenerList();

	/**
	 * Create a new model with an empty plain text message (default behaviour)
	 */
	public ComposerModel() {
		this(null, false); // default ~ plain text
	}

	/**
	 * Creates a new model with a plain text message
	 *
	 * @param message
	 *            Initial message to hold in the model
	 */
	public ComposerModel(ColumbaMessage message) {
		this(message, false);
	}

	/**
	 * Creates a new model with an empty message
	 *
	 * @param html
	 *            True for a html message, false for plain text
	 */
	public ComposerModel(boolean html) {
		this(null, html);
	}

	/**
	 * Constructs a new ComposerModel. The parameters are read from the
	 * messageOptions.
	 *
	 * @param messageOptions
	 */
	public ComposerModel(Map<String,Object> messageOptions) {
		this();
		setMessageOptions(messageOptions);
	}

	/**
	 * Creates a new model
	 *
	 * @param message
	 *            Initial message to hold in the model
	 * @param html
	 *            True for a html message, false for plain text
	 */
	public ComposerModel(ColumbaMessage message, boolean html) {
		// set message
		if (message == null) {
			message = new ColumbaMessage();
		}

		this.message = message;

		// set whether the model should handle html or plain text
		isHtmlMessage = html;

		// more initialization
		toList = new Vector();
		ccList = new Vector();
		bccList = new Vector();
		attachments = new Vector();
	}

	/**
	 * Set source reference.
	 * <p>
	 * The message you are for example replying to.
	 *
	 * @param ref
	 *            source reference
	 */
	public void setSourceReference(MailFolderCommandReference ref) {
		this.ref = ref;
	}

	/**
	 * Get source reference.
	 * <p>
	 * The message you are for example replying to.
	 *
	 * @return source reference
	 */
	public MailFolderCommandReference getSourceReference() {
		return ref;
	}

	/**
	 * Set To: header
	 *
	 * @param a
	 *            address array
	 */
	public void setTo(Address[] a) {
		getToList().clear();

		for (int i = 0; i < a.length; i++) {
			getToList().add(a[i].toString());
		}

	}

	/**
	 * Set Cc: header
	 *
	 * @param a
	 *            address array
	 */
	public void setCc(Address[] a) {
		getCcList().clear();
		for (int i = 0; i < a.length; i++) {
			getCcList().add(a[i].toString());
		}
	}

	/**
	 * Set Bcc: header
	 *
	 * @param a
	 *            address array
	 */
	public void setBcc(Address[] a) {
		getBccList().clear();
		for (int i = 0; i < a.length; i++) {
			getBccList().add(a[i].toString());
		}
	}

	public void setTo(String s) {
		LOG.fine("to-headerfield:" + s);

		if (s == null) {
			return;
		}

		if (s.length() == 0) {
			return;
		}

		List v = ListParser.createListFromString(s);
		toList = v;
	}

	public void setHeaderField(String key, String value) {
		message.getHeader().set(key, value);
	}

	public void setHeader(Header header) {
		message.setHeader(header);
	}

	public String getHeaderField(String key) {
		return (String) message.getHeader().get(key);
	}

	public void setToList(List<String> v) {
		this.toList = v;
	}

	public void setCcList(List<String> v) {
		this.ccList = v;
	}

	public void setBccList(List<String> v) {
		this.bccList = v;
	}

	public List<String> getToList() {
		return toList;
	}

	public List<String> getCcList() {
		return ccList;
	}

	public List<String> getBccList() {
		return bccList;
	}

	public void setAccountItem(AccountItem item) {
		this.accountItem = item;
	}

	public AccountItem getAccountItem() {
		if (accountItem == null) {
			return MailConfig.getInstance().getAccountList().get(0);
		} else {
			return accountItem;
		}
	}

	public void setMessage(ColumbaMessage message) {
		this.message = message;
	}

	public IColumbaMessage getMessage() {
		return message;
	}

	public String getHeader(String key) {
		return (String) message.getHeader().get(key);
	}

	public void addMimePart(StreamableMimePart mp) {
		attachments.add(mp);

		// notifyListeners();
	}

	public void addFileAttachment(File file) {
		if (file.isFile()) {

			String mimetype = ColumbaDesktop.getInstance().getMimeType(file);

			MimeHeader header = new MimeHeader(mimetype.substring(0, mimetype
					.indexOf('/')), mimetype
					.substring(mimetype.indexOf('/') + 1));
			header.putContentParameter("name", file.getName());
			header.setContentDisposition("attachment");
			header.putDispositionParameter("filename", file.getName());
			header.setContentTransferEncoding("base64");

			try {
				LocalMimePart mimePart = new LocalMimePart(header,
						new FileSource(file));

				attachments.add(mimePart);
			} catch (IOException e) {
				LOG.warning("Could not add the file '" + file
						+ "' to the attachment list, due to:" + e);
			}
		}

	}

	public void setBodyText(String str) {
		this.bodytext = str;

		// notifyListeners();
	}

	public String getSignature() {
		return "signature";
	}

	public String getBodyText() {
		return bodytext;
	}

	public String getSubject() {
		return (String) message.getHeader().get("Subject");
	}

	public void setSubject(String s) {
		message.getHeader().set("Subject", s);
	}

	public List getAttachments() {
		return attachments;
	}

	public void setAccountItem(String host, String address) {
		setAccountItem(MailConfig.getInstance().getAccountList()
				.hostGetAccount(host, address));
	}

	/**
	 * Returns the charsetName.
	 *
	 * @return String
	 */
	public Charset getCharset() {
		if (charset == null) {
			charset = Charset.forName(System.getProperty("file.encoding"));
		}

		return charset;
	}

	/**
	 * Sets the charsetName.
	 *
	 * @param charsetName
	 *            The charsetName to set
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Returns the signMessage.
	 *
	 * @return boolean
	 */
	public boolean isSignMessage() {
		return signMessage;
	}

	/**
	 * Sets the signMessage.
	 *
	 * @param signMessage
	 *            The signMessage to set
	 */
	public void setSignMessage(boolean signMessage) {
		this.signMessage = signMessage;
	}

	/**
	 * Returns the encryptMessage.
	 *
	 * @return boolean
	 */
	public boolean isEncryptMessage() {
		return encryptMessage;
	}

	/**
	 * Sets the encryptMessage.
	 *
	 * @param encryptMessage
	 *            The encryptMessage to set
	 */
	public void setEncryptMessage(boolean encryptMessage) {
		this.encryptMessage = encryptMessage;
	}

	public String getPriority() {
		if (message.getHeader().get("X-Priority") == null) {
			return "3";
		} else {
			return (String) message.getHeader().get("X-Priority");
		}
	}

	public void setPriority(String s) {
		message.getHeader().set("X-Priority", s);
	}

	/**
	 * Returns whether the model holds a html message or plain text
	 *
	 * @return True for html, false for text
	 */
	public boolean isHtml() {
		return isHtmlMessage;
	}

	/**
	 * Sets whether the model holds a html message or plain text
	 *
	 * @param html
	 *            True for html, false for text
	 */
	public void setHtml(boolean html) {
		isHtmlMessage = html;
	}

	public List getRCPTVector() {
		List<String> output = new Vector<String>();

		List<String> l = new NormalizeRecipientListParser()
				.normalizeRCPTVector(ListBuilder.createFlatList(getToList()));
		if (l != null)
			output.addAll(l);

		l = new NormalizeRecipientListParser().normalizeRCPTVector(ListBuilder
				.createFlatList(getCcList()));
		if (l != null)
			output.addAll(l);
		l = new NormalizeRecipientListParser().normalizeRCPTVector(ListBuilder
				.createFlatList(getBccList()));
		if (l != null)
			output.addAll(l);

		return output;
	}

	public void setMessageOptions(Map<String,Object> options) {

		addAddresses(options, "to");
		addAddresses(options, "cc");
		addAddresses(options, "bcc");

		if (options.get("subject") != null) {
			setSubject((String) options.get("subject"));
		}

		if (options.get("body") != null) {
			String body = (String) options.get("body");
			/*
			 * *20030917, karlpeder* Set the model to html or text based on the
			 * body specified on the command line. This is done using a simple
			 * check: Does the body contain <html> and </html>
			 */
			boolean html = false;
			String lcase = body.toLowerCase();

			if ((lcase.indexOf("<html>") != -1)
					&& (lcase.indexOf("</html>") != -1)) {
				html = true;
			}

			setHtml(html);

			// set the body text
			setBodyText(body);
		}

		if (options.get("attachment") != null) {
			if (options.get("attachment") instanceof String) {
				String s = (String) options.get("attachment");
				try {
					URI uri = new URI(s);
					addFileAttachment(new File(uri));
				} catch (URISyntaxException e) {
					// if this is no URI
					addFileAttachment(new File(s));
				}
			}
		}

	}

	/**
	 * @param options
	 */
	private void addAddresses(Map options, String type) {
		List list;

		if (type.equals("to")) {
			list = getToList();
		} else if (type.equals("cc")) {
			list = getCcList();
		} else {
			list = getBccList();
		}

		if (options.get(type) != null) {
			if (options.get(type) instanceof String) {
				list.add((String) options.get(type));
			} else {
				String[] addresses = (String[]) options.get(type);

				for (int i = 0; i < addresses.length; i++) {
					list.add(addresses[i]);
				}
			}

		}
	}

	/** **************************** event handling ***************************** */

	/**
	 * Adds a listener.
	 */
	public void addModelChangedListener(IComposerModelChangedListener listener) {
		listenerList.add(IComposerModelChangedListener.class, listener);
	}

	/**
	 * Removes a previously registered listener.
	 */
	public void removeModelChangedListener(
			IComposerModelChangedListener listener) {
		listenerList.remove(IComposerModelChangedListener.class, listener);
	}

	/**
	 * Propagates an event to all registered listeners notifying them of changes
	 */
	public void fireModelChanged() {
		ComposerModelChangedEvent e = new ComposerModelChangedEvent(this);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IComposerModelChangedListener.class) {
				((IComposerModelChangedListener) listeners[i + 1])
						.modelChanged(e);
			}
		}
	}

	/**
	 * Propagates an event to all registered listeners notifying them of changes
	 */
	public void fireHtmlModeChanged(boolean htmlEnabled) {
		ComposerModelChangedEvent e = new ComposerModelChangedEvent(this, htmlEnabled);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IComposerModelChangedListener.class) {
				((IComposerModelChangedListener) listeners[i + 1])
						.htmlModeChanged(e);
			}
		}
	}
}