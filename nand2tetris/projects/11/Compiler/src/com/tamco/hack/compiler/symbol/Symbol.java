package com.tamco.hack.compiler.symbol;

/**
 * Created by tam-co on 20/09/2017.
 */

public class Symbol {

	public enum Kind {
		STATIC("static"), FIELD("field"), ARGUMENT("argument"), LOCAL("local");

		private String value;

		Kind(String s) {
			this.value = s;
		}

		public static Kind fromString(String s) {
			if ("static".equals(s)) {
				return STATIC;
			} else if ("field".equals(s)) {
				return FIELD;
			} else if ("argument".equals(s)) {
				return ARGUMENT;
			} else if ("local".equals(s)) {
				return LOCAL;
			} else {
				return null;
			}
		}
	}

	private String name;

	private String type;

	private Kind kind;

	private int no;

	public Symbol(String name, String type, Kind kind, int no) {
		this.name = name;
		this.type = type;
		this.kind = kind;
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
}
