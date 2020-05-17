package hr.fer.zemris.java.hw07.shell.namebuilder.namebuildersimpl;

import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilder;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderInfo;

/**
 * Class represents implementation of {@link NameBuilder} which
 * generates name part to append from given text.
 * 
 * @author Ante Gazibaric
 *
 */
public class NameBuilderText implements NameBuilder {

	/** text that will be appended to the name */
	private String text;
	
	/**
	 * Constructor that creates new {@link NameBuilderText} object.
	 * 
	 * @param text {@link #text}
	 */
	public NameBuilderText(String text) {
		this.text = text;
	}
	
	@Override
	public void execute(NameBuilderInfo info) {
		info.getStringBuilder().append(text);
	}

}
