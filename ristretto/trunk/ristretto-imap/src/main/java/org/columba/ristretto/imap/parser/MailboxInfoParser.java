/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Ristretto Mail API.
 *
 * The Initial Developers of the Original Code are
 * Timo Stich and Frederik Dietz.
 * Portions created by the Initial Developers are Copyright (C) 2004
 * All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package org.columba.ristretto.imap.parser;

import org.columba.ristretto.imap.IMAPResponse;
import org.columba.ristretto.imap.ResponseTextCode;
import org.columba.ristretto.message.MailboxInfo;
import org.columba.ristretto.parser.ParserException;

import java.util.regex.Pattern;

/**
 * Parser for the MailboxInfo.
 * 
 * @author tstich
 *
 */
public class MailboxInfoParser {
	private static final Pattern stringListPattern = Pattern
			.compile("(\\s|\\()?([^\\s\\)]+)");

	/**
	 * Parse the MailboxInfo of the IMAP response.
	 * 
	 * @param in
	 * @return the MailboxInfo
	 * @throws ParserException
	 */
	public static MailboxInfo parse(IMAPResponse in) throws ParserException {
		return parse( in, null);
	}
	
	/**
	 * Parse the MailboxInfo in the IMAP response and
	 * add the information to the given MailboxInfo.
	 * 
	 * @param in
	 * @param mailboxInfo a preset MailboxInfo
	 * @return the MailboxInfo
	 * @throws ParserException
	 */
	public static MailboxInfo parse(IMAPResponse in, MailboxInfo mailboxInfo) throws ParserException {		
		MailboxInfo result;
		
		if( mailboxInfo != null ) {
			result  = mailboxInfo;
		} else 
		result = new MailboxInfo();

			String type = in.getResponseSubType();
			if (type.equals("RECENT")) {
				result.setRecent(in.getPreNumber());
			} else if (type.equals("EXISTS")) {
				result.setExists(in.getPreNumber());
			} else if (type.equals("FLAGS")) {
				result.setDefinedFlags(StringListParser.parse(in
						.getResponseMessage()));
			} else if (type.equals("OK")) {
				ResponseTextCode info = in.getResponseTextCode();
				if (info != null) {
					switch (info.getType()) {
						case ResponseTextCode.UNSEEN : {
							result.setFirstUnseen(info.getIntValue());
							break;
						}
						case ResponseTextCode.UIDVALIDITY : {
							result.setUidValidity(info.getIntValue());
							break;
						}
						case ResponseTextCode.UIDNEXT : {
							result.setUidNext(info.getIntValue());
							break;
						}
						case ResponseTextCode.PERMANENTFLAGS : {
							result
									.setPermanentFlags(info
											.getStringArrayValue());
							break;
						}

						case ResponseTextCode.READ_ONLY : {
							result.setWriteAccess(false);
							break;
						}

						case ResponseTextCode.READ_WRITE : {
							result.setWriteAccess(true);
							break;
						}

						default : {
							// Ignore unknown Response Type
						}
					}
				}
		}

		return result;
	}
}