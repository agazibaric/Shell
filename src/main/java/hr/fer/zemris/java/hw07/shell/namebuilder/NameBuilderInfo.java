package hr.fer.zemris.java.hw07.shell.namebuilder;

import java.util.regex.Matcher;

/**
 * Interface represents general form of name builder info
 * which provide needed informations to generate name.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public interface NameBuilderInfo {
	
	/**
	 * Method returns {@link StringBuilder} object used for concatenating parts of name.
	 * 
	 * @return {@link StringBuilder} object used for concatenating parts of name
	 */
	StringBuilder getStringBuilder();
	
	/**
	 * Method returns group content of {@link Matcher} object at given {@code index}.
	 * 
	 * @param index index of group
	 * @return      {@code String} content of group at given {@code index}
	 */
	String getGroup(int index);

}
