/**
 * 
 */
package com.github.aro_tech.interface_it.meta.arguments;

import java.util.ArrayList;
import java.util.List;

import com.github.aro_tech.interface_it.util.ClassNameUtils;

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

	private String adjustLineForComments(CommentStatus status, final String line) {
		if (line.contains("/*")) {
			return adjustLineContainingCommentStart(status, line);
		} else if (line.contains("*/") && status.inComment) {
			return adjustLineContainingCommentEnd(status, line);
		} else if (line.contains("//")) {
			int ix = line.indexOf("//");
			return line.substring(0, ix);
		} else if (status.inComment) {
			return "";
		}
		return line;
	}


	private String adjustLineContainingCommentEnd(CommentStatus status, final String line) {
		int endIx = line.indexOf("*/");
		status.inComment = false;
		return line.substring(endIx + 1);
	}

	private String adjustLineContainingCommentStart(CommentStatus status, final String line) {
		String lineOut = line;
		int startIx = line.indexOf("/*");
		int endIx = line.indexOf("*/");

		if (endIx < 0) {
			status.inComment = true;
			if (wordContainsMethodName(startIx)) {
				lineOut = lineOut.substring(0, startIx);
			} else {
				lineOut = "";
			}
		} else {
			if (endIx < startIx) {
				lineOut = lineOut.substring(endIx + 2, startIx);
			} else {
				String lineStart = lineOut.substring(0, startIx);
				String lineEnd = lineOut.substring(endIx + 1);
				lineOut = lineStart + lineEnd;
			}
		}
		return lineOut;
	}

	private static enum TokenType {
		METHOD_NAME,
		ARGUMENT_TYPE,
		ARGUMENT_NAME;
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Token [type=" + type + ", value=" + value + "]";
		}

	}

	// allow clean loop execution by encapsulating variables that cross loop
	// boundary
	private static class LoopContext {
		private String currentType = null;
		private boolean insideArgs = false;
		private boolean isDone = false;
		private final List<Token> result;

		public LoopContext(List<Token> result) {
			super();
			this.result = result;
		}

		/**
		 * Add tokens for one argument into the results
		 * 
		 * @param word
		 */
		void addOneArgument(String word) {
			result.add(new Token(TokenType.ARGUMENT_TYPE, currentType));
			result.add(new Token(TokenType.ARGUMENT_NAME, word));
		}

		/**
		 * Add the method name to the results
		 * 
		 * @param name
		 */
		void addMethodName(String name) {
			result.add(new Token(TokenType.METHOD_NAME, name));
		}

		/**
		 * @return the currentType
		 */
		String getCurrentType() {
			return currentType;
		}

		/**
		 * @param currentType
		 *            the currentType to set
		 */
		void setCurrentType(String currentType) {
			this.currentType = currentType;
		}

		/**
		 * @return the insideArgs
		 */
		boolean isInsideArgs() {
			return insideArgs;
		}

		/**
		 * @param insideArgs
		 *            the insideArgs to set
		 */
		void setInsideArgs(boolean insideArgs) {
			this.insideArgs = insideArgs;
		}

		/**
		 * @return the isInTemplateType
		 */
		boolean isInTemplateType() {
			return null != this.currentType && this.currentType.indexOf('<') >= 0 && !this.currentType.endsWith(">");// isInTemplateType;
		}

		public boolean hasNoCurrentType() {
			return null == currentType;
		}

		public void appendToCurrentType(String suffix) {
			this.currentType += suffix;
		}

		@Override
		public String toString() {
			return "LoopContext [currentType=" + currentType + ", insideArgs=" + insideArgs + ", isDone=" + isDone
					+ ", isInTemplateType()=" + isInTemplateType() + ", hasNoCurrentType()=" + hasNoCurrentType() + "]";
		}

		/**
		 * @return the isDone
		 */
		boolean isNotDone() {
			return !isDone;
		}

		/**
		 * @param isDone
		 *            the isDone to set
		 */
		void endLoop() {
			this.isDone = true;
		}

		public void reset() {
			this.currentType = null;
			this.insideArgs = false;
		}

	}

	private List<Token> extractTokens(String[] words) {
		List<Token> result = new ArrayList<>();
		LoopContext ctx = new LoopContext(result);
		for (int i = 0; i < words.length && ctx.isNotDone(); i++) {
			String word = words[i].trim();
			if (word.length() > 0 && !"final".equals(word)) {
				processCurrentNonemptyWord(word, words, ctx, i);
			}
		}

		return result;
	}

	private void processCurrentNonemptyWord(String word, String[] words, LoopContext ctx, int loopCounter) {
		if (ctx.isInsideArgs()) {
			processWordInsideArguments(word, ctx);
		} else {
			processWordAtOrBeforeStartOfArguments(word, words, ctx, loopCounter);
		}
	}

	private void processWordInsideArguments(String word, LoopContext ctx) {
		if (word.indexOf(')') > 0) {
			if (ctx.getCurrentType() != null) {
				ctx.addOneArgument(word.substring(0, word.indexOf(')')));
			}
			ctx.endLoop();
		} else if (word.indexOf(')') == 0) {
			ctx.endLoop();
		} else if (word.indexOf(',') == 0) {
			if (word.length() > 1) {
				ctx.setCurrentType(word.substring(1));
			}
		} else if (isBoundaryBetween2Args(word, ctx)) {
			splitAndProcessArgBoundaryWord(word, ctx);
		} else {
			processTypeOrNameWord(word, ctx);
		}
	}

	private void splitAndProcessArgBoundaryWord(String word, LoopContext ctx) {
		String[] parts = word.split(",");
		if (parts.length > 0) {
			ctx.addOneArgument(parts[0]);
			ctx.setCurrentType(parts.length > 1 && !"final".equals(parts[1]) ? parts[1] : null);
		}
	}

	private boolean isBoundaryBetween2Args(String word, LoopContext ctx) {
		return word.indexOf(',') > 0 && !ctx.isInTemplateType() && null != ctx.getCurrentType();
	}

	private void processTypeOrNameWord(String word, LoopContext ctx) {
		if (ctx.hasNoCurrentType()) {
			ctx.setCurrentType(word);
		} else if (word.equals("...")) {
			ctx.appendToCurrentType(word);
		} else if (ctx.isInTemplateType()) {
			ctx.appendToCurrentType(' ' + word);
		} else {
			ctx.addOneArgument(word);
			ctx.reset();
		}
	}

	private void processWordAtOrBeforeStartOfArguments(String word, String[] words, LoopContext ctx, int loopCounter) {
		int indexOfOpen = word.indexOf('(');
		if (wordContainsMethodName(indexOfOpen)) {
			atStartOfArgsExtractMethodNameAndPossiblyFirstArgType(word, ctx);
		} else if (wordContainsParenOpeningArgumentsButNotMethodName(indexOfOpen)) {
			atStartOfArgsSetPreviousWordAsMethodNameAndPossiblyExtractFirstArgType(word, words, ctx, loopCounter);
		}
	}

	private void atStartOfArgsSetPreviousWordAsMethodNameAndPossiblyExtractFirstArgType(String word, String[] words,
			LoopContext ctx, int loopCounter) {
		ctx.setInsideArgs(true);
		ctx.addMethodName(words[loopCounter - 1].trim());
		if (word.length() > 1) {
			ctx.setCurrentType(word.substring(1));
		}
	}

	private void atStartOfArgsExtractMethodNameAndPossiblyFirstArgType(String word, LoopContext ctx) {
		ctx.setInsideArgs(true);
		String[] parts = word.split("\\(");
		ctx.addMethodName(parts[0]);
		if (parts.length > 1 && isNotEndOfArgs(parts) && !"final".equals(parts[1])) {
			ctx.setCurrentType(parts[1]);
		}
	}

	private boolean isNotEndOfArgs(String[] parts) {
		return !parts[1].startsWith(")");
	}

	private boolean wordContainsParenOpeningArgumentsButNotMethodName(int indexOfOpen) {
		return indexOfOpen == 0;
	}

	private boolean wordContainsMethodName(int indexOfOpen) {
		return indexOfOpen > 0;
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
				target.add(method, index++, simplify(argType), tok.getValue(), countArguments(tokens));
				break;
			}
		}
	}

	private int countArguments(List<Token> tokens) {
		int count = 0;
		for(Token token: tokens) {
			if(token.getType() == TokenType.ARGUMENT_NAME) {
				count++;
			}
		}
		return count;
	}

	private boolean isStaticMethodSignatureStart(String line) {
		int openParenIx = line.indexOf('(');
		int staticIx = line.indexOf("static");
		int eqIx = line.indexOf('=');

		return staticIx >= 0 && openParenIx >= staticIx && eqIx < 0;
	}

	private boolean isStaticMethodSignatureEnd(String line) {
		return line.indexOf('{') >= 0;
	}

	private String simplify(String fullName) {
		return ClassNameUtils.extractSimpleName(fullName);
	}
}
