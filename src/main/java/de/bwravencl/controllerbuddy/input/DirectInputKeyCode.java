/* Copyright (C) 2018  Matteo Hausner
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package de.bwravencl.controllerbuddy.input;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DirectInputKeyCode {

	public static final String SLEEP = "Sleep";
	public static final String NEXT = "Next";
	public static final String STOP = "Stop";
	public static final String CONVERT = "Convert";
	public static final String DECIMAL = "Decimal";
	public static final String X = "X";
	public static final String Y = "Y";
	public static final String ESCAPE = "Escape";
	public static final String CIRCUMFLEX = "Circumflex";
	public static final String PAGE_DOWN = "PageDown";
	public static final String DOWN_ARROW = "DownArrow";
	public static final String RIGHT_ARROW = "RightArrow";
	public static final String LEFT_ARROW = "LeftArrow";
	public static final String PAGE_UP = "PageUp";
	public static final String UP_ARROW = "UpArrow";
	public static final String RIGHT_ALT = "RightAlt";
	public static final String NUM_PAD_SLASH = "NumPadSlash";
	public static final String NUM_PAD_PERIOD = "NumPadPeriod";
	public static final String NUM_PAD_PLUS = "NumPadPlus";
	public static final String NUM_PAD_MINUS = "NumPadMinus";
	public static final String CAPS_LOCK = "CapsLock";
	public static final String LEFT_ALT = "LeftAlt";
	public static final String NUM_PAD_STAR = "NumPadStar";
	public static final String BACK_SPACE = "BackSpace";
	public static final String MEDIA_SELECT = "MediaSelect";
	public static final String MAIL = "Mail";
	public static final String MY_COMPUTER = "MyComputer";
	public static final String WEB_BACK = "WebBack";
	public static final String WEB_FORWARD = "WebForward";
	public static final String WEB_STOP = "WebStop";
	public static final String WEB_REFRESH = "WebRefresh";
	public static final String WEB_FAVORITES = "WebFavorites";
	public static final String WEB_SEARCH = "WebSearch";
	public static final String WAKE = "Wake";
	public static final String POWER = "Power";
	public static final String APPS = "Apps";
	public static final String RIGHT_WINDOWS = "RightWindows";
	public static final String LEFT_WINDOWS = "LeftWindows";
	public static final String DOWN = "Down";
	public static final String END = "End";
	public static final String PRIOR = "Prior";
	public static final String UP = "Up";
	public static final String HOME = "Home";
	public static final String RIGHT_MENU = "RightMenu";
	public static final String SYS_RQ = "SysRq";
	public static final String DIVIDE = "Divide";
	public static final String NUM_PAD_COMMA = "NumPadComma";
	public static final String WEB_HOME = "WebHome";
	public static final String VOLUME_UP = "VolumeUp";
	public static final String VOLUME_DOWN = "VolumeDown";
	public static final String MEDIA_STOP = "MediaStop";
	public static final String PLAY_PAUSE = "PlayPause";
	public static final String CALCULATOR = "Calculator";
	public static final String MUTE = "Mute";
	public static final String RIGHT_CONTROL = "RightControl";
	public static final String NUM_PAD_ENTER = "NumPadEnter";
	public static final String NEXT_TRACK = "NextTrack";
	public static final String UNLABELED = "Unlabeled";
	public static final String A_X = "AX";
	public static final String KANJI = "Kanji";
	public static final String UNDERLINE = "Underline";
	public static final String COLON = "Colon";
	public static final String AT = "At";
	public static final String PREV_TRACK = "PrevTrack";
	public static final String NUM_PAD_EQUALS = "NumPadEquals";
	public static final String ABNT_C2 = "AbntC2";
	public static final String YEN = "Yen";
	public static final String NO_CONVERT = "NoConvert";
	public static final String ABNT_C1 = "AbntC1";
	public static final String KANA = "Kana";
	public static final String F15 = "F15";
	public static final String F14 = "F14";
	public static final String F13 = "F13";
	public static final String F12 = "F12";
	public static final String F11 = "F11";
	public static final String OEM102 = "OEM102";
	public static final String NUM_PAD0 = "NumPad0";
	public static final String NUM_PAD3 = "NumPad3";
	public static final String NUM_PAD2 = "NumPad2";
	public static final String NUM_PAD1 = "NumPad1";
	public static final String NUM_PAD6 = "NumPad6";
	public static final String NUM_PAD5 = "NumPad5";
	public static final String NUM_PAD4 = "NumPad4";
	public static final String SUBTRACT = "Subtract";
	public static final String NUM_PAD9 = "NumPad9";
	public static final String NUM_PAD8 = "NumPad8";
	public static final String NUM_PAD7 = "NumPad7";
	public static final String SCROLL = "Scroll";
	public static final String NUMLOCK = "Numlock";
	public static final String F10 = "F10";
	public static final String F9 = "F9";
	public static final String F8 = "F8";
	public static final String F7 = "F7";
	public static final String F6 = "F6";
	public static final String F5 = "F5";
	public static final String F4 = "F4";
	public static final String F3 = "F3";
	public static final String F2 = "F2";
	public static final String F1 = "F1";
	public static final String CAPITAL = "Capital";
	public static final String SPACE = "Space";
	public static final String LEFT_MENU = "LeftMenu";
	public static final String MULTIPLY = "Multiply";
	public static final String RIGHT_SHIFT = "RightShift";
	public static final String SLASH = "Slash";
	public static final String PERIOD = "Period";
	public static final String COMMA = "Comma";
	public static final String M = "M";
	public static final String N = "N";
	public static final String B = "B";
	public static final String V = "V";
	public static final String C = "C";
	public static final String Z = "Z";
	public static final String BACK_SLASH = "BackSlash";
	public static final String LEFT_SHIFT = "LeftShift";
	public static final String GRAVE = "Grave";
	public static final String APOSTROPHE = "Apostrophe";
	public static final String SEMI_COLON = "SemiColon";
	public static final String L = "L";
	public static final String K = "K";
	public static final String J = "J";
	public static final String H = "H";
	public static final String G = "G";
	public static final String F = "F";
	public static final String D = "D";
	public static final String S = "S";
	public static final String A = "A";
	public static final String LEFT_CONTROL = "LeftControl";
	public static final String RETURN = "Return";
	public static final String RIGHT_BRACKET = "RightBracket";
	public static final String LEFT_BRACKET = "LeftBracket";
	public static final String P = "P";
	public static final String O = "O";
	public static final String I = "I";
	public static final String U = "U";
	public static final String T = "T";
	public static final String R = "R";
	public static final String E = "E";
	public static final String W = "W";
	public static final String TAB = "Tab";
	public static final String BACK = "Back";
	public static final String EQUALS = "Equals";
	public static final String MINUS = "Minus";
	public static final String D0 = "D0";
	public static final String D9 = "D9";
	public static final String D8 = "D8";
	public static final String D7 = "D7";
	public static final String D6 = "D6";
	public static final String D5 = "D5";
	public static final String D4 = "D4";
	public static final String D3 = "D3";
	public static final String D2 = "D2";
	public static final String D1 = "D1";
	public static final String INSERT = "Insert";
	public static final String RIGHT = "Right";
	public static final String LEFT = "Left";
	public static final String PAUSE = "Pause";
	public static final String ADD = "Add";
	public static final String DELETE = "Delete";
	public static final String Q = "Q";

	private static final DirectInputKeyCode[] SCAN_CODES = { new DirectInputKeyCode(SLEEP, 223),
			new DirectInputKeyCode(NEXT, 209), new DirectInputKeyCode(STOP, 149), new DirectInputKeyCode(CONVERT, 121),
			new DirectInputKeyCode(DECIMAL, 83), new DirectInputKeyCode(X, 45), new DirectInputKeyCode(Y, 21),
			new DirectInputKeyCode(ESCAPE, 1), new DirectInputKeyCode(CIRCUMFLEX, 144),
			new DirectInputKeyCode(PAGE_DOWN, 209), new DirectInputKeyCode(DOWN_ARROW, 208),
			new DirectInputKeyCode(RIGHT_ARROW, 205), new DirectInputKeyCode(LEFT_ARROW, 203),
			new DirectInputKeyCode(PAGE_UP, 201), new DirectInputKeyCode(UP_ARROW, 200),
			new DirectInputKeyCode(RIGHT_ALT, 184), new DirectInputKeyCode(NUM_PAD_SLASH, 181),
			new DirectInputKeyCode(NUM_PAD_PERIOD, 83), new DirectInputKeyCode(NUM_PAD_PLUS, 78),
			new DirectInputKeyCode(NUM_PAD_MINUS, 74), new DirectInputKeyCode(CAPS_LOCK, 58),
			new DirectInputKeyCode(LEFT_ALT, 56), new DirectInputKeyCode(NUM_PAD_STAR, 55),
			new DirectInputKeyCode(BACK_SPACE, 14), new DirectInputKeyCode(MEDIA_SELECT, 237),
			new DirectInputKeyCode(MAIL, 236), new DirectInputKeyCode(MY_COMPUTER, 235),
			new DirectInputKeyCode(WEB_BACK, 234), new DirectInputKeyCode(WEB_FORWARD, 233),
			new DirectInputKeyCode(WEB_STOP, 232), new DirectInputKeyCode(WEB_REFRESH, 231),
			new DirectInputKeyCode(WEB_FAVORITES, 230), new DirectInputKeyCode(WEB_SEARCH, 229),
			new DirectInputKeyCode(WAKE, 227), new DirectInputKeyCode(POWER, 222), new DirectInputKeyCode(APPS, 221),
			new DirectInputKeyCode(RIGHT_WINDOWS, 220), new DirectInputKeyCode(LEFT_WINDOWS, 219),
			new DirectInputKeyCode(DOWN, 208), new DirectInputKeyCode(END, 207), new DirectInputKeyCode(PRIOR, 201),
			new DirectInputKeyCode(UP, 200), new DirectInputKeyCode(HOME, 199), new DirectInputKeyCode(RIGHT_MENU, 184),
			new DirectInputKeyCode(SYS_RQ, 183), new DirectInputKeyCode(DIVIDE, 181),
			new DirectInputKeyCode(NUM_PAD_COMMA, 179), new DirectInputKeyCode(WEB_HOME, 178),
			new DirectInputKeyCode(VOLUME_UP, 176), new DirectInputKeyCode(VOLUME_DOWN, 174),
			new DirectInputKeyCode(MEDIA_STOP, 164), new DirectInputKeyCode(PLAY_PAUSE, 162),
			new DirectInputKeyCode(CALCULATOR, 161), new DirectInputKeyCode(MUTE, 160),
			new DirectInputKeyCode(RIGHT_CONTROL, 157), new DirectInputKeyCode(NUM_PAD_ENTER, 156),
			new DirectInputKeyCode(NEXT_TRACK, 153), new DirectInputKeyCode(UNLABELED, 151),
			new DirectInputKeyCode(A_X, 150), new DirectInputKeyCode(KANJI, 148),
			new DirectInputKeyCode(UNDERLINE, 147), new DirectInputKeyCode(COLON, 146), new DirectInputKeyCode(AT, 145),
			new DirectInputKeyCode(PREV_TRACK, 144), new DirectInputKeyCode(NUM_PAD_EQUALS, 141),
			new DirectInputKeyCode(ABNT_C2, 126), new DirectInputKeyCode(YEN, 125),
			new DirectInputKeyCode(NO_CONVERT, 123), new DirectInputKeyCode(ABNT_C1, 115),
			new DirectInputKeyCode(KANA, 112), new DirectInputKeyCode(F15, 102), new DirectInputKeyCode(F14, 101),
			new DirectInputKeyCode(F13, 100), new DirectInputKeyCode(F12, 88), new DirectInputKeyCode(F11, 87),
			new DirectInputKeyCode(OEM102, 86), new DirectInputKeyCode(NUM_PAD0, 82),
			new DirectInputKeyCode(NUM_PAD3, 81), new DirectInputKeyCode(NUM_PAD2, 80),
			new DirectInputKeyCode(NUM_PAD1, 79), new DirectInputKeyCode(NUM_PAD6, 77),
			new DirectInputKeyCode(NUM_PAD5, 76), new DirectInputKeyCode(NUM_PAD4, 75),
			new DirectInputKeyCode(SUBTRACT, 74), new DirectInputKeyCode(NUM_PAD9, 73),
			new DirectInputKeyCode(NUM_PAD8, 72), new DirectInputKeyCode(NUM_PAD7, 71),
			new DirectInputKeyCode(SCROLL, 70), new DirectInputKeyCode(NUMLOCK, 69), new DirectInputKeyCode(F10, 68),
			new DirectInputKeyCode(F9, 67), new DirectInputKeyCode(F8, 66), new DirectInputKeyCode(F7, 65),
			new DirectInputKeyCode(F6, 64), new DirectInputKeyCode(F5, 63), new DirectInputKeyCode(F4, 62),
			new DirectInputKeyCode(F3, 61), new DirectInputKeyCode(F2, 60), new DirectInputKeyCode(F1, 59),
			new DirectInputKeyCode(CAPITAL, 58), new DirectInputKeyCode(SPACE, 57),
			new DirectInputKeyCode(LEFT_MENU, 56), new DirectInputKeyCode(MULTIPLY, 55),
			new DirectInputKeyCode(RIGHT_SHIFT, 54), new DirectInputKeyCode(SLASH, 53),
			new DirectInputKeyCode(PERIOD, 52), new DirectInputKeyCode(COMMA, 51), new DirectInputKeyCode(M, 50),
			new DirectInputKeyCode(N, 49), new DirectInputKeyCode(B, 48), new DirectInputKeyCode(V, 47),
			new DirectInputKeyCode(C, 46), new DirectInputKeyCode(Z, 44), new DirectInputKeyCode(BACK_SLASH, 43),
			new DirectInputKeyCode(LEFT_SHIFT, 42), new DirectInputKeyCode(GRAVE, 41),
			new DirectInputKeyCode(APOSTROPHE, 40), new DirectInputKeyCode(SEMI_COLON, 39),
			new DirectInputKeyCode(L, 38), new DirectInputKeyCode(K, 37), new DirectInputKeyCode(J, 36),
			new DirectInputKeyCode(H, 35), new DirectInputKeyCode(G, 34), new DirectInputKeyCode(F, 33),
			new DirectInputKeyCode(D, 32), new DirectInputKeyCode(S, 31), new DirectInputKeyCode(A, 30),
			new DirectInputKeyCode(LEFT_CONTROL, 29), new DirectInputKeyCode(RETURN, 28),
			new DirectInputKeyCode(RIGHT_BRACKET, 27), new DirectInputKeyCode(LEFT_BRACKET, 26),
			new DirectInputKeyCode(P, 25), new DirectInputKeyCode(O, 24), new DirectInputKeyCode(I, 23),
			new DirectInputKeyCode(U, 22), new DirectInputKeyCode(T, 20), new DirectInputKeyCode(R, 19),
			new DirectInputKeyCode(E, 18), new DirectInputKeyCode(W, 17), new DirectInputKeyCode(TAB, 15),
			new DirectInputKeyCode(BACK, 14), new DirectInputKeyCode(EQUALS, 13), new DirectInputKeyCode(MINUS, 12),
			new DirectInputKeyCode(D0, 11), new DirectInputKeyCode(D9, 10), new DirectInputKeyCode(D8, 9),
			new DirectInputKeyCode(D7, 8), new DirectInputKeyCode(D6, 7), new DirectInputKeyCode(D5, 6),
			new DirectInputKeyCode(D4, 5), new DirectInputKeyCode(D3, 4), new DirectInputKeyCode(D2, 3),
			new DirectInputKeyCode(D1, 2), new DirectInputKeyCode(INSERT, 210), new DirectInputKeyCode(RIGHT, 205),
			new DirectInputKeyCode(LEFT, 203), new DirectInputKeyCode(PAUSE, 197), new DirectInputKeyCode(ADD, 78),
			new DirectInputKeyCode(DELETE, 211), new DirectInputKeyCode(Q, 16) };

	public static final Map<String, Integer> nameToScanCodeMap;
	public static final Map<Integer, String> scanCodeToNameMap;

	static {
		nameToScanCodeMap = new TreeMap<>();
		scanCodeToNameMap = new HashMap<>();

		for (final DirectInputKeyCode sc : SCAN_CODES) {
			nameToScanCodeMap.put(sc.name, sc.DirectInputKeyCode);
			scanCodeToNameMap.put(sc.DirectInputKeyCode, sc.name);
		}
	}

	public final String name;
	public final int DirectInputKeyCode;

	public DirectInputKeyCode(final String name, final int DirectInputKeyCode) {
		this.name = name;
		this.DirectInputKeyCode = DirectInputKeyCode;
	}

	@Override
	public String toString() {
		return name;
	}

}
