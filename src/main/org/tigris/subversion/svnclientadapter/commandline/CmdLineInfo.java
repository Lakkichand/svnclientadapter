/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */ 
package org.tigris.subversion.svnclientadapter.commandline;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * <p>
 * <strong>NOTE:</strong> Not the most "efficient"
 * implementation, but it works.</p>
 * 
 * @author Philip Schatz (schatz at tigris)
 */
class CmdLineInfo {

	//"Constants"
	private static final String KEY_PATH = "Path";
	private static final String KEY_URL = "URL";
	private static final String KEY_REVISION = "Revision";
	private static final String KEY_NODEKIND = "Node Kind";
	private static final String KEY_LASTCHANGEDAUTHOR = "Last Changed Author";
	private static final String KEY_LASTCHANGEDREV = "Last Changed Rev";
	private static final String KEY_LASTCHANGEDDATE = "Last Changed Date";

	//Fields
	private Map infoMap = new HashMap();

	//Constructors
	CmdLineInfo(String infoString) {
		load(infoString);
	}

	//Methods
	public Date getLastChangedDate() {
		return Helper.toDate(get(KEY_LASTCHANGEDDATE));
	}

	public SVNRevision.Number getLastChangedRevision() {
		return Helper.toRevNum(get(KEY_LASTCHANGEDREV));
	}

	public String getLastCommitAuthor() {
		return get(KEY_LASTCHANGEDAUTHOR);
	}

	public SVNNodeKind getNodeKind() {
		if ("directory".equals(get(KEY_NODEKIND)))
			return SVNNodeKind.DIR;
		if ("file".equals(get(KEY_NODEKIND)))
			return SVNNodeKind.FILE;
		return SVNNodeKind.UNKNOWN;
	}

	public String getPath() {
		return get(KEY_PATH);
	}

	public SVNRevision.Number getRevision() {
		return Helper.toRevNum(get(KEY_REVISION));
	}

	public SVNUrl getUrl() {
		return Helper.toSVNUrl(get(KEY_URL));
	}

	private String get(String key) {
		Object value = infoMap.get(key);
		return (value == null) ? null : value.toString();
	}

	private void load(String infoString) {
		StringTokenizer st = new StringTokenizer(infoString, Helper.NEWLINE);
		//First, go through and take each line and throw
		// it into a map with the key being the text to
		// the left of the colon, and value being to the
		// right.
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			int middle = line.indexOf(':');
			String key = line.substring(0, middle);
			String value = line.substring(middle + 2);
			infoMap.put(key, value);
		}
	}
}
