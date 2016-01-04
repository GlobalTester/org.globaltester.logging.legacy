package org.globaltester.logging.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;
import org.globaltester.logging.logger.GtPatternLayout;

public class GtLogfileContentDescriber implements ITextContentDescriber {
	private static final QualifiedName[] SUPPORTED_OPTIONS = new QualifiedName[] {};
	
	@Override
	public int describe(InputStream contents, IContentDescription description)
			throws IOException {
		return describe(new InputStreamReader(contents), description);
	}

	@Override
	public QualifiedName[] getSupportedOptions() {
		return SUPPORTED_OPTIONS;
	}

	@Override
	public int describe(Reader contents, IContentDescription description)
			throws IOException {
		
		int lengthToRead = GtPatternLayout.HEADER_LINE.length();
		char[] cbuf = new char[lengthToRead];
		
		if (contents.read(cbuf) != lengthToRead) {
			// too short content
			return IContentDescriber.INVALID;
		}

		String beginOfReader = new String(cbuf);
		if (!beginOfReader.equals(GtPatternLayout.HEADER_LINE)) {
			// reader does not start with correct char sequence
			return IContentDescriber.INVALID;
		}

		return IContentDescriber.VALID;
	}
}
