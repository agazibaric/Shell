package hr.fer.zemris.java.hw07.shell.namebuilder;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.namebuilder.namebuildersimpl.NameBuilderFinal;
import hr.fer.zemris.java.hw07.shell.namebuilder.namebuildersimpl.NameBuilderGroup;
import hr.fer.zemris.java.hw07.shell.namebuilder.namebuildersimpl.NameBuilderText;

/**
 * Class represents a syntax analyzer for parsing name builder pattern. </br>
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class NameBuilderParser {
	
	/** lexical analyzer */
	private NameBuilderLexer lexer;
	/** list of name builders */
	private List<NameBuilder> nameBuilders = new LinkedList<>();
	
	/**
	 * Constructor for creating new {@code NameBuilderParser} object.
	 * 
	 * @param expression {@code String} that is analyzed
	 */
	public NameBuilderParser(String expression) {
		lexer = new NameBuilderLexer(expression);
		parseInput();
	}
	
	/**
	 * Method parses input
	 */
	private void parseInput() {
		try {
			while (true) {
				lexer.nextToken();
				
				if (isTokenOfType(TokenType.EOF)) 
					break;

				if (isTokenOfType(TokenType.TEXT)) {
					String text = (String) lexer.getToken().getValue();
					nameBuilders.add(new NameBuilderText(text));
					continue;
				}

				if (isTokenOfType(TokenType.START_SUBSTITUTION)) {
					lexer.setLexerState(NameBuilderLexerState.SUBSTITUTION);
					parseSubstitution();
				}
			}
			
		} catch (NameBuilderLexerException ex) {
			throw new NameBuilderParserException(ex.getMessage());
		}
		
	}
	
	/**
	 * Method parser substitution input
	 */
	private void parseSubstitution() {
		lexer.nextToken();
		if (!isTokenOfType(TokenType.NUMBER))
			throw new NameBuilderParserException("Invalid input. Number was expected in substitution.");
		
		Integer groupIndex = (Integer) lexer.getToken().getValue();
		lexer.nextToken();
		if (isTokenOfType(TokenType.END_SUBSTITUTION)) {
			nameBuilders.add(new NameBuilderGroup(groupIndex, 0, false));
			lexer.setLexerState(NameBuilderLexerState.TEXT);
			return;
		}
		
		if (!isTokenOfType(TokenType.SEPARATOR))
			throw new NameBuilderParserException("Invalid input. Separator ',' was expected in substitution.");
		
		lexer.nextToken();
		if (!isTokenOfType(TokenType.NUMBER))
			throw new NameBuilderParserException("Invalid input. Number was expected in substitution.");
		
		Integer value = (Integer) lexer.getToken().getValue();
		if (value == 0) {
			lexer.nextToken();
			if (!isTokenOfType(TokenType.NUMBER))
				throw new NameBuilderParserException("Invalid input. Number was expected in substitution.");
			
			Integer numberOfPlaces = (Integer) lexer.getToken().getValue();
			nameBuilders.add(new NameBuilderGroup(groupIndex, numberOfPlaces, true));
		} else {
			nameBuilders.add(new NameBuilderGroup(groupIndex, value, false));
		}
		lexer.nextToken();
		if (!isTokenOfType(TokenType.END_SUBSTITUTION))
			throw new NameBuilderParserException("Invalid input. Substitution must be closed. Missing '}'");
		lexer.setLexerState(NameBuilderLexerState.TEXT);
	}

	/**
	 * Method returns {@link NameBuilder} object for given input.
	 * 
	 * @return {@link NameBuilder} object for given input
	 */
	public NameBuilder getNameBuilder() {
		return new NameBuilderFinal(nameBuilders);
	}	
	
	/**
	 * Method that checks if a current token is of a given type.
	 * 
	 * @param type {@link TokenType} whose equality is checked
	 * @return     <code>true</code> if current token type is equal to given token type, </br>
	 * 			   <code>false</code> otherwise
	 */
	private boolean isTokenOfType(TokenType type) {
		return lexer.getToken().getType() == type;
	}

}
