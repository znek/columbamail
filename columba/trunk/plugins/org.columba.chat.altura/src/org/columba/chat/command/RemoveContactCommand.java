// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.chat.command;

import org.columba.api.command.ICommandReference;
import org.columba.api.command.IWorkerStatusController;
import org.columba.chat.Connection;
import org.columba.chat.ui.frame.api.IChatFrameMediator;
import org.columba.core.command.Command;
import org.jivesoftware.smack.RosterEntry;

public class RemoveContactCommand extends Command {

	private String jabberId;

	private PopulateRoasterCommand populateCommand;

	public RemoveContactCommand(IChatFrameMediator mediator,
			ICommandReference reference) {
		super(reference);

		populateCommand = new PopulateRoasterCommand(mediator, reference);
	}

	/**
	 * @see org.columba.core.command.Command#updateGUI()
	 */
	@Override
	public void updateGUI() throws Exception {
		populateCommand.updateGUI();
	}

	@Override
	public void execute(IWorkerStatusController worker) throws Exception {
		ChatCommandReference ref = (ChatCommandReference) getReference();

		jabberId = ref.getJabberId();

		RosterEntry entry = Connection.XMPPConnection.getRoster().getEntry(
				jabberId);

		Connection.XMPPConnection.getRoster().removeEntry(entry);
		populateCommand.execute(worker);
	}

}
