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
package org.columba.mail.filter.plugins;

import javax.swing.JOptionPane;

import org.columba.api.command.ICommand;
import org.columba.core.filter.AbstractFilterAction;
import org.columba.core.filter.FilterAction;
import org.columba.core.filter.IFilterAction;
import org.columba.core.folder.api.IFolder;
import org.columba.core.gui.frame.FrameManager;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.command.MoveMessageCommand;
import org.columba.mail.gui.tree.FolderTreeModel;

/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class MoveMessageAction extends AbstractFilterAction {
	/**
	 * @see org.columba.modules.mail.filter.action.AbstractFilterAction#execute()
	 *
	 * move message from source- to destination-folder
	 */
	public ICommand getCommand(IFilterAction filterAction,
			IFolder srcFolder, Object[] uids) throws Exception {
		String uid = filterAction.getUid();
		IMailbox destFolder = (IMailbox) FolderTreeModel.getInstance()
				.getFolder(uid);

		if (destFolder == null) {
			JOptionPane
					.showMessageDialog(
							FrameManager.getInstance()
							.getActiveFrame(),
							"Unable to find destination folder, please correct the destination folder path for this filter");
			throw new Exception("File not found");
		}

		MailFolderCommandReference r = new MailFolderCommandReference(srcFolder,
				destFolder, uids);

		MoveMessageCommand c = new MoveMessageCommand(r);

		return c;
	}
}