/**
 * 
 */
package org.interfaceit.meta.arguments;

import java.util.ArrayList;
import java.util.List;

import org.interfaceit.util.ClassNameUtils;

/**
 * Class which parses lines of source code to fill in data in a
 * LookupArgumentNameSource
 * 
 * @author aro_tech
 *
 */
public class SourceLineReadingArgumentNameLoader {
	
	private static class CommentStatus {
		boolean inComment = false;
	}

	/**
	 * Read the source lines for argument names and load the names into target
	 * 
	 * @param sourceLines
	 * @param target
	 */
	public void parseAndLoad(List<String> sourceLines, LookupArgumentNameSource target) {
		StringBuilder combinedLines = null;
		CommentStatus status = new CommentStatus();
		for (String line : sourceLines) {
			line = adjustLineForComments(status, line);
			
			if (isStaticMethodSignatureEnd(line)) {
				if (isStaticMethodSignatureStart(line)) {
					parseAndLoadSignatureLine(target, line);
				} else if (null != combinedLines) {
					combinedLines.append(" ").append(line);
					parseAndLoadSignatureLine(target, combinedLines.toString());
					combinedLines = null;
				}
			} else if (isStaticMethodSignatureStart(line)) {
				combinedLines = new StringBuilder();
				combinedLines.append(line);
			} else if (null != combinedLines) {
				combinedLines.append(" ").append(line);
			}
		}
	}

	private String adjustLineForComments(CommentStatus status, String line) {
		if(line.contains("/*")) {
			int startIx = line.indexOf("/*");
			int endIx = line.indexOf("*/");
			
			if(endIx < 0) {
				status.inComment = true;
				if(startIx > 0) {
					line = line.substring(0, startIx);
				} else {
					line = "";
				}
			} else {
				if(endIx < startIx) {
					line = line.substring(endIx + 2, startIx);
				} else {
					String lineStart = line.substring(0, startIx);
					String lineEnd = line.substring(endIx + 1);
					line = lineStart + lineEnd;
				}
				
			}
		} else if(line.contains("*/") && status.inComment) {
			int endIx = line.indexOf("*/");
			status.inComment = false;
			line = line.substring(endIx + 1);
		} else if(line.contains("//")) {
			int ix = line.indexOf("//");
			line = line.substring(0, ix);
		} else if(status.inComment) {
			line = "";
		}
		return line;
	}

	private static enum TokenType {
		METHOD_NAME, ARGUMENT_TYPE, ARGUMENT_NAME;
	}

	private static class Token {
		private final TokenType type;
		private final String value;

		Token(TokenType type, String value) {
			super();
			this.type = type;
			this.value = value;
		}

		TokenType getType() {
			return type;
		}

		String getValue() {
			return value;
		}
	}

	private List<Token> extractTokens(String[] words) {
		List<Token> result = new ArrayList<>();
		// String method = null;
		String currentType = null;
		boolean insideArgs = false;
		for (int i = 0; i < words.length; i++) {
			String word = words[i].trim();
			if (word.length() > 0) {
				if (insideArgs) {
					if (word.indexOf(')') > 0) {
						if (currentType != null) {
							result.add(new Token(TokenType.ARGUMENT_TYPE, currentType));
							result.add(new Token(TokenType.ARGUMENT_NAME, word.substring(0, word.indexOf(')'))));
						}
						break;
					} else if (word.indexOf(')') == 0) {
						break;
					} else if (word.indexOf(',') == 0) {
						if (word.length() > 1) {
							currentType = word.substring(1);
						}
					} else if (word.indexOf(',') > 0) {
						String[] parts = word.split(",");
						if (parts.length > 0) {
							addOneArgument(result, currentType, parts[0]);
							currentType = parts.length > 1 ? parts[1] : null;
						}
					} else {
						if (currentType == null) {
							currentType = word;
						} else {
							addOneArgument(result, currentType, word);
							currentType = null;
						}
					}
				} else {
					int indexOfOpen = word.indexOf('(');
					if (indexOfOpen > 0) {
						insideArgs = true;
						String[] parts = word.split("\\(");
						result.add(new Token(TokenType.METHOD_NAME, parts[0]));
						if (parts.length > 1) {
							currentType = parts[1];
						}
					} else if (indexOfOpen == 0) {
						insideArgs = true;
						result.add(new Token(TokenType.METHOD_NAME, words[i - 1].trim()));
					}

				}

			}
		}

		return result;
	}

	private void addOneArgument(List<Token> result, String currentType, String word) {
		result.add(new Token(TokenType.ARGUMENT_TYPE, currentType));
		result.add(new Token(TokenType.ARGUMENT_NAME, word));
	}

	/**
	 * @param target
	 * @param line
	 */
	private void parseAndLoadSignatureLine(LookupArgumentNameSource target, String line) {
		String[] words = line.split(" ");
		String method = "";
		String argType = "";
		List<Token> tokens = extractTokens(words);
		int index = 0;
		for (Token tok : tokens) {
			switch (tok.getType()) {
			case METHOD_NAME:
				method = tok.getValue();
				break;
			case ARGUMENT_TYPE:
				argType = tok.getValue();
				break;
			case ARGUMENT_NAME:
				target.add(method, index++, simplify(argType), tok.getValue());
				break;
			}
			;
		}
	}

	private boolean isStaticMethodSignatureStart(String line) {
		return line.indexOf('(') >= 0 && line.contains("static ");
	}

	private boolean isStaticMethodSignatureEnd(String line) {
		return line.indexOf('{') >= 0;
	}

	private String simplify(String fullName) {
		return ClassNameUtils.extractSimpleName(fullName);
	}
}
