package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * Interface represents general form of builder used creating name from given {@link NameBuilderInfo}.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public interface NameBuilder {
	
	/**
	 * Method executes name building process.
	 * 
	 * @param info {@link NameBuilderInfo} used for getting needed information to generate name.
	 */
	void execute(NameBuilderInfo info);

}
