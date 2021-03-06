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
/*
 * Created on 2003-okt-30
 */
package org.columba.mail.filter;

import org.columba.core.filter.Filter;
import org.columba.core.filter.FilterList;
import org.columba.core.filter.IFilter;
import org.columba.core.xml.XmlElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the <code>FilterList</code> class.
 * 
 * @author redsolo
 */
public class FilterListTest {
	/**
	 * Test to add filters to the list. The method should be able to handle
	 * nulls as well.
	 */
    @Test
	public void testAdd() {
		FilterList filterList = new FilterList(new XmlElement());
		filterList.add(createNamedFilter("ONE"));
		Assert.assertEquals("Wrong number of filters in the list.", 1, filterList
				.count());
		filterList.add(null);
		Assert.assertEquals("Wrong number of filters in the list.", 1, filterList
				.count());
		filterList.add(createNamedFilter("ONE"));
		Assert.assertEquals("Wrong number of filters in the list.", 2, filterList
				.count());
	}

	/**
	 * Test to remove filters from the list.
	 */
    @Test
	public void testRemoveFilter() {
		FilterList filterList = new FilterList(new XmlElement());
		IFilter filterTwo = createNamedFilter("TWO");
		filterList.add(createNamedFilter("ONE"));
		filterList.add(filterTwo);
		filterList.add(createNamedFilter("THREE"));
		Assert.assertEquals("Wrong number of filters in the list.", 3, filterList
				.count());

		filterList.remove(filterTwo);
		Assert.assertEquals("Wrong number of filters in the list.", 2, filterList
				.count());
		filterList.remove(null);
		Assert.assertEquals("Wrong number of filters in the list.", 2, filterList
				.count());
	}

	/**
	 * Test the count() method.
	 *  
	 */
    @Test
	public void testCount() {
		FilterList filterList = new FilterList(new XmlElement());
		Assert.assertEquals("Expected an empty filter list", 0, filterList.count());
		filterList.add(FilterList.createDefaultFilter());
		Assert.assertEquals("Expected a filter list with one filter", 1, filterList
				.count());
		filterList.remove(0);
		Assert.assertEquals("Expected an empty filter list", 0, filterList.count());
	}

	/**
	 * Test for Filter#get(int)
	 */
    @Test
	public void testGetint() {
		FilterList filterList = new FilterList(new XmlElement());
		IFilter filterOne = createNamedFilter("ONE");
		IFilter filterTwo = createNamedFilter("TWO");
		IFilter filterThree = createNamedFilter("THREE");
		filterList.add(filterOne);
		filterList.add(filterTwo);
		filterList.add(filterThree);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterOne, filterList.get(0));
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterTwo, filterList.get(1));
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterThree, filterList.get(2));
	}

	/**
	 * Test for insert(Filter, int) method.
	 */
    @Test
	public void testInsert() {
		FilterList filterList = new FilterList(new XmlElement());
		IFilter filterOne = createNamedFilter("ONE");
		IFilter filterTwo = createNamedFilter("TWO");
		IFilter filterThree = createNamedFilter("THREE");
		filterList.add(filterOne);
		filterList.add(filterTwo);
		filterList.add(filterThree);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterOne, filterList.get(0));
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterTwo, filterList.get(1));

		IFilter filterFour = createNamedFilter("FOUR");
		filterList.insert(filterFour, 1);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterOne, filterList.get(0));
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterFour, filterList.get(1));
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterTwo, filterList.get(2));
	}

	/**
	 * Test to move up a filter in the list.
	 */
    @Test
	public void testMoveUp() {
		FilterList filterList = new FilterList(new XmlElement());
		IFilter filterOne = createNamedFilter("ONE");
		IFilter filterTwo = createNamedFilter("TWO");
		IFilter filterThree = createNamedFilter("THREE");
		filterList.add(filterOne);
		filterList.add(filterTwo);
		filterList.add(filterThree);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterOne, filterList.get(0));
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterThree, filterList.get(2));
		filterList.moveUp(filterThree);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterThree, filterList.get(1));
		filterList.moveUp(filterThree);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterThree, filterList.get(0));
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterOne, filterList.get(1));
		filterList.moveUp(filterThree);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterThree, filterList.get(0));
	}

	/**
	 * Test to move down a filter in the list.
	 */
    @Test
	public void testMoveDown() {
		FilterList filterList = new FilterList(new XmlElement());
		IFilter filterOne = createNamedFilter("ONE");
		IFilter filterTwo = createNamedFilter("TWO");
		IFilter filterThree = createNamedFilter("THREE");
		filterList.add(filterOne);
		filterList.add(filterTwo);
		filterList.add(filterThree);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterOne, filterList.get(0));
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterThree, filterList.get(2));
		filterList.moveDown(filterOne);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterOne, filterList.get(1));
		filterList.moveDown(filterOne);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterOne, filterList.get(2));
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterThree, filterList.get(1));
		filterList.moveDown(filterOne);
		Assert.assertEquals("The get(int) method returned the wrong filter.",
				filterOne, filterList.get(2));
	}

	/**
	 * Test for indexOf() method.
	 */
    @Test
	public void testIndexOf() {
		FilterList filterList = new FilterList(new XmlElement());
		IFilter filterOne = createNamedFilter("ONE");
		IFilter filterTwo = createNamedFilter("TWO");
		IFilter filterThree = createNamedFilter("THREE");
		filterList.add(filterOne);
		filterList.add(filterTwo);
		filterList.add(filterThree);

		Assert.assertEquals("The indexof() method did not return the right index", -1,
				filterList.indexOf(null));
		Assert.assertEquals("The indexof() method did not return the right index", 0,
				filterList.indexOf(filterOne));
		Assert.assertEquals("The indexof() method did not return the right index", 1,
				filterList.indexOf(filterTwo));
		Assert.assertEquals("The indexof() method did not return the right index", 2,
				filterList.indexOf(filterThree));
		Assert.assertEquals("The indexof() method did not return the right index", -1,
				filterList.indexOf(createNamedFilter("NONE")));
	}

	/**
	 * Returns an empty filter with a specified name.
	 * 
	 * @param name
	 *            the name of the filter.
	 * @return a <code>Filter</code> with the specified name.
	 */
	private IFilter createNamedFilter(String name) {
		IFilter filter = FilterList.createDefaultFilter();
		filter.setName(name);

		return filter;
	}

	/**
	 * Asserts that the filters are the same.
	 * 
	 * @param msg
	 *            the message to output if the assertion fails.
	 * @param expected
	 *            the expected filter.
	 * @param actual
	 *            the actual filter.
	 */
	private void assertEquals(String msg, Filter expected, Filter actual) {
		Assert.assertEquals(msg, expected.getName(), actual.getName());
	}
}