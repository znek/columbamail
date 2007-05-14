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
package org.columba.mail.folder;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Abstract testcase creates a folder in setUp and removes it in tearDown.
 * <p>
 * Create new testcases by subclassing this class and using getSourceFolder()
 * and getDestFolder() directly.
 * 
 * @author fdietz
 * @author redsolo
 */
public class AbstractFolderTst extends TestCase {

	/** A source folder. */
	protected AbstractMessageFolder sourceFolder;

	/** A destination folder. */
	protected AbstractMessageFolder destFolder;

	/** A set with all created folders. */
	private Set folders;

	private static int folderId = 0;

	private MailboxTstFactory factory;

	/**
	 * Constructor for test.
	 * <p>
	 * This is used when executing this individual test only or by the ant task.
	 * <p>
	 */
	public AbstractFolderTst(String test) {
		super(test);

		this.factory = new MHFolderFactory();
	}

	/**
	 * Constructor for test.
	 * <p>
	 * Used by {@link AllTests}.
	 */
	public AbstractFolderTst(MailboxTstFactory factory, String test) {
		super(test);

		this.factory = factory;
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {

		// create config-folder
//		File file = new File("test_config");
//		file.mkdir();
//
//		new Config(file);
//
//		Logging.DEBUG = true;
//		Logging.createDefaultHandler();
//
//		// init mail component
//		new MailMain().init();
//		new AddressbookMain().init();
//
//		// now load all available plugins
//		PluginManager.getInstance().initExternalPlugins();

		folders = new HashSet();
		sourceFolder = factory.createFolder(folderId++);
		folders.add(sourceFolder);
		destFolder = factory.createFolder(folderId++);
		folders.add(destFolder);

	}

	public AbstractMessageFolder createFolder() {
		AbstractMessageFolder folder = factory.createFolder(folderId++);
		folders.add(folder);

		return folder;
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		recursiveDelete(new File(FolderTstHelper.homeDirectory + "/folders/"));
	}

	private void recursiveDelete(File folder) {
		// delete all files in folder
		File[] list = folder.listFiles();

		for (int i = 0; i < list.length; i++) {
			if( list[i].isDirectory() ) {
				recursiveDelete(list[i]);
			} else {
				list[i].delete();				
			}
		}
		
		folder.delete();
	}
	
	/**
	 * @return Returns the folder.
	 */
	public AbstractMessageFolder getSourceFolder() {
		return sourceFolder;
	}

	/**
	 * @return Returns the destFolder.
	 */
	public AbstractMessageFolder getDestFolder() {
		return destFolder;
	}

}