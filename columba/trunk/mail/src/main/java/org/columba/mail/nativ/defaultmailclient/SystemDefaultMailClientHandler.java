//The contents of this file are subject to the Mozilla Public License Version
//1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
//Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.mail.nativ.defaultmailclient;

import org.columba.core.base.OSInfo;

/**
 * @author Timo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SystemDefaultMailClientHandler implements SystemDefaultMailClient {
	
	public boolean platfromSupportsDefaultMailClient() {
		return OSInfo.isWin32Platform();
	}
	
	/**
	 * @see org.columba.mail.nativ.defaultmailclient.SystemDefaultMailClient#isDefaultMailClient()
	 */
	public boolean isDefaultMailClient() {
		
		if( OSInfo.isWin32Platform()) {
			return new Win32SystemDefaultMailClient().isDefaultMailClient();
		} else {
			return false;
		}
	}

	/**
	 * @see org.columba.mail.nativ.defaultmailclient.SystemDefaultMailClient#setDefaultMailClient()
	 */
	public void setDefaultMailClient() {
		if( OSInfo.isWin32Platform()) {
			new Win32SystemDefaultMailClient().setDefaultMailClient();
		} 
	}
}