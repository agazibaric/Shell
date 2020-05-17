package hr.fer.zemris.java.hw07.shell.namebuilder.namebuildersimpl;

import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilder;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderInfo;

/**
 * Class represents implementation of {@link NameBuilder} 
 * which creates name for substitution.
 * 
 * @author Ante Gazibaric
 *
 */
public class NameBuilderGroup implements NameBuilder {

	/** group index */
	private int groupIndex;
	/** how many places name should take */
	private int numberOfPlaces;
	/** flag shows will the empty places be replaced with zeros */
	private boolean toFillWithZeros;
	
	/**
	 * Constructor that creates new {@link NameBuilderGroup} object.
	 * 
	 * @param groupIndex      {@link #groupIndex}
	 * @param numberOfPlaces  {@link #numberOfPlaces}
	 * @param toFillWithZeros {@link #toFillWithZeros}
	 */
	public NameBuilderGroup(int groupIndex, int numberOfPlaces, boolean toFillWithZeros) {
		this.groupIndex = groupIndex;
		this.numberOfPlaces = numberOfPlaces;
		this.toFillWithZeros = toFillWithZeros;
	}
	
	@Override
	public void execute(NameBuilderInfo info) {
		String group = getGroupValue(info.getGroup(groupIndex));
		info.getStringBuilder().append(group);
	}
	
	/** 
	 * Method returns valid group value that takes into account</br>
	 * number of places that should be filled with zeros or with empty space.
	 * 
	 * @param group
	 * @return
	 */
	private String getGroupValue(String group) {
		int groupLength = group.length();
		if (groupLength >= numberOfPlaces) {
			return group;
		}
		
		StringBuilder sb = new StringBuilder();
		String stringToAppend = getStringToAppend();
		for (int i = 0, n = numberOfPlaces - groupLength; i < n; i++) {
			sb.append(stringToAppend);
		}
		return sb.append(group).toString();
	}
	
	/**
	 * Method returns proper string that will be used in filling empty spaces.
	 * 
	 * @return string with whom empty spaces are filled
	 */
	private String getStringToAppend() {
		return toFillWithZeros ? "0" : " ";
	}

}
