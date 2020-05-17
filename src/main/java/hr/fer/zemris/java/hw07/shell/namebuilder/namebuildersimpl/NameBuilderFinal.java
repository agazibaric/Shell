package hr.fer.zemris.java.hw07.shell.namebuilder.namebuildersimpl;

import java.util.List;

import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilder;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderInfo;

/**
 * Class represents name builder which generates final name from given list of name builders.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class NameBuilderFinal implements NameBuilder {

	/** list of name builders */
	private List<NameBuilder> nameBuilders;
	
	/**
	 * Constructor that creates new {@code NameBuilderFinal} object.
	 * 
	 * @param nameBuilders list of {@link NameBuilder} objects
	 */
	public NameBuilderFinal(List<NameBuilder> nameBuilders) {
		this.nameBuilders = nameBuilders;
	}
	@Override
	public void execute(NameBuilderInfo info) {
		nameBuilders.forEach(b -> b.execute(info));
	}

}
