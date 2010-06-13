/*******************************************************************************
 * Copyright (c) 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest;

import org.eclipse.osgi.util.NLS;

public class DotExportMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.zest.dot.messages"; //$NON-NLS-1$
	public static String DotFileUtils_0;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, DotExportMessages.class);
	}

	private DotExportMessages() {
	}
}
