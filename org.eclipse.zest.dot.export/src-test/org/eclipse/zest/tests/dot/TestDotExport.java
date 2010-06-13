/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.tests.dot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import junit.framework.Assert;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.dot.DotExport;

/**
 * Tests for the {@link DotExport} class.
 * 
 * @author Fabian Steeg (fsteeg)
 */
public class TestDotExport extends TestDotTemplate {
	public static final File OUTPUT = new File("src-gen"); //$NON-NLS-1$

	@Override
	protected void testDotGeneration(final Graph graph) {
		/*
		 * The DotExport class wraps the simple DotTemplate class, so when we
		 * test DotExport, we also run the test in the test superclass:
		 */
		super.testDotGeneration(graph);
		/* DotExport adds stripping of blank lines and file output: */
		DotExport dotExport = new DotExport(graph);
		String dot = dotExport.toDotString();
		assertNoBlankLines(dot);
		System.out.println(dot);
		File file = new File(OUTPUT, new DotExport(graph).toString() + ".dot"); //$NON-NLS-1$
		dotExport.toDotFile(file);
		Assert.assertTrue("Generated file must exist!", file.exists()); //$NON-NLS-1$
		String dotRead = read(file);
		Assert.assertTrue(
				"DOT file output representation must contain simple class name of Zest input!", //$NON-NLS-1$
				dotRead.contains(graph.getClass().getSimpleName()));
		Assert.assertEquals("File output and String output should be equal;", //$NON-NLS-1$
				dot, dotRead);

	}

	private void assertNoBlankLines(final String dot) {
		Scanner scanner = new Scanner(dot);
		while (scanner.hasNextLine()) {
			if (scanner.nextLine().trim().equals("")) { //$NON-NLS-1$
				Assert.fail("Resulting DOT should contain no blank lines;"); //$NON-NLS-1$
			}
		}
	}

	private String read(final File file) {
		try {
			Scanner scanner = new Scanner(file);
			StringBuilder builder = new StringBuilder();
			while (scanner.hasNextLine()) {
				builder.append(scanner.nextLine() + "\n"); //$NON-NLS-1$
			}
			return builder.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
