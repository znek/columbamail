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
package org.columba.mail.connector;

import org.columba.addressbook.facade.IContactFacade;
import org.columba.addressbook.facade.IFolderFacade;
import org.columba.addressbook.facade.IModelFacade;
import org.columba.api.exception.ServiceNotFoundException;
import org.columba.core.facade.ServiceFacadeRegistry;

/**
 * Provides access to internal functionality for external components.
 * 
 * @author fdietz
 */
public final class ServiceConnector {

	private ServiceConnector() {
	}

	public static IContactFacade getContactFacade()
			throws ServiceNotFoundException {
		return (IContactFacade) ServiceFacadeRegistry.getInstance().getService(
				IContactFacade.class);
	}

	public static IFolderFacade getFolderFacade()
			throws ServiceNotFoundException {
		return (IFolderFacade) ServiceFacadeRegistry.getInstance().getService(
				IFolderFacade.class);
	}
	
	public static IModelFacade getModelFacade() throws ServiceNotFoundException {
		return (IModelFacade) ServiceFacadeRegistry.getInstance().getService(
				IModelFacade.class);
	}

}