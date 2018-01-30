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

public class ScanCode {

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

	private static final ScanCode[] SCAN_CODES = { new ScanCode(SLEEP, 223), new ScanCode(NEXT, 209),
			new ScanCode(STOP, 149), new ScanCode(CONVERT, 121), new ScanCode(DECIMAL, 83), new ScanCode(X, 45),
			new ScanCode(Y, 21), new ScanCode(ESCAPE, 1), new ScanCode(CIRCUMFLEX, 144), new ScanCode(PAGE_DOWN, 209),
			new ScanCode(DOWN_ARROW, 208), new ScanCode(RIGHT_ARROW, 205), new ScanCode(LEFT_ARROW, 203),
			new ScanCode(PAGE_UP, 201), new ScanCode(UP_ARROW, 200), new ScanCode(RIGHT_ALT, 184),
			new ScanCode(NUM_PAD_SLASH, 181), new ScanCode(NUM_PAD_PERIOD, 83), new ScanCode(NUM_PAD_PLUS, 78),
			new ScanCode(NUM_PAD_MINUS, 74), new ScanCode(CAPS_LOCK, 58), new ScanCode(LEFT_ALT, 56),
			new ScanCode(NUM_PAD_STAR, 55), new ScanCode(BACK_SPACE, 14), new ScanCode(MEDIA_SELECT, 237),
			new ScanCode(MAIL, 236), new ScanCode(MY_COMPUTER, 235), new ScanCode(WEB_BACK, 234),
			new ScanCode(WEB_FORWARD, 233), new ScanCode(WEB_STOP, 232), new ScanCode(WEB_REFRESH, 231),
			new ScanCode(WEB_FAVORITES, 230), new ScanCode(WEB_SEARCH, 229), new ScanCode(WAKE, 227),
			new ScanCode(POWER, 222), new ScanCode(APPS, 221), new ScanCode(RIGHT_WINDOWS, 220),
			new ScanCode(LEFT_WINDOWS, 219), new ScanCode(DOWN, 208), new ScanCode(END, 207), new ScanCode(PRIOR, 201),
			new ScanCode(UP, 200), new ScanCode(HOME, 199), new ScanCode(RIGHT_MENU, 184), new ScanCode(SYS_RQ, 183),
			new ScanCode(DIVIDE, 181), new ScanCode(NUM_PAD_COMMA, 179), new ScanCode(WEB_HOME, 178),
			new ScanCode(VOLUME_UP, 176), new ScanCode(VOLUME_DOWN, 174), new ScanCode(MEDIA_STOP, 164),
			new ScanCode(PLAY_PAUSE, 162), new ScanCode(CALCULATOR, 161), new ScanCode(MUTE, 160),
			new ScanCode(RIGHT_CONTROL, 157), new ScanCode(NUM_PAD_ENTER, 156), new ScanCode(NEXT_TRACK, 153),
			new ScanCode(UNLABELED, 151), new ScanCode(A_X, 150), new ScanCode(KANJI, 148),
			new ScanCode(UNDERLINE, 147), new ScanCode(COLON, 146), new ScanCode(AT, 145),
			new ScanCode(PREV_TRACK, 144), new ScanCode(NUM_PAD_EQUALS, 141), new ScanCode(ABNT_C2, 126),
			new ScanCode(YEN, 125), new ScanCode(NO_CONVERT, 123), new ScanCode(ABNT_C1, 115), new ScanCode(KANA, 112),
			new ScanCode(F15, 102), new ScanCode(F14, 101), new ScanCode(F13, 100), new ScanCode(F12, 88),
			new ScanCode(F11, 87), new ScanCode(OEM102, 86), new ScanCode(NUM_PAD0, 82), new ScanCode(NUM_PAD3, 81),
			new ScanCode(NUM_PAD2, 80), new ScanCode(NUM_PAD1, 79), new ScanCode(NUM_PAD6, 77),
			new ScanCode(NUM_PAD5, 76), new ScanCode(NUM_PAD4, 75), new ScanCode(SUBTRACT, 74),
			new ScanCode(NUM_PAD9, 73), new ScanCode(NUM_PAD8, 72), new ScanCode(NUM_PAD7, 71),
			new ScanCode(SCROLL, 70), new ScanCode(NUMLOCK, 69), new ScanCode(F10, 68), new ScanCode(F9, 67),
			new ScanCode(F8, 66), new ScanCode(F7, 65), new ScanCode(F6, 64), new ScanCode(F5, 63),
			new ScanCode(F4, 62), new ScanCode(F3, 61), new ScanCode(F2, 60), new ScanCode(F1, 59),
			new ScanCode(CAPITAL, 58), new ScanCode(SPACE, 57), new ScanCode(LEFT_MENU, 56), new ScanCode(MULTIPLY, 55),
			new ScanCode(RIGHT_SHIFT, 54), new ScanCode(SLASH, 53), new ScanCode(PERIOD, 52), new ScanCode(COMMA, 51),
			new ScanCode(M, 50), new ScanCode(N, 49), new ScanCode(B, 48), new ScanCode(V, 47), new ScanCode(C, 46),
			new ScanCode(Z, 44), new ScanCode(BACK_SLASH, 43), new ScanCode(LEFT_SHIFT, 42), new ScanCode(GRAVE, 41),
			new ScanCode(APOSTROPHE, 40), new ScanCode(SEMI_COLON, 39), new ScanCode(L, 38), new ScanCode(K, 37),
			new ScanCode(J, 36), new ScanCode(H, 35), new ScanCode(G, 34), new ScanCode(F, 33), new ScanCode(D, 32),
			new ScanCode(S, 31), new ScanCode(A, 30), new ScanCode(LEFT_CONTROL, 29), new ScanCode(RETURN, 28),
			new ScanCode(RIGHT_BRACKET, 27), new ScanCode(LEFT_BRACKET, 26), new ScanCode(P, 25), new ScanCode(O, 24),
			new ScanCode(I, 23), new ScanCode(U, 22), new ScanCode(T, 20), new ScanCode(R, 19), new ScanCode(E, 18),
			new ScanCode(W, 17), new ScanCode(TAB, 15), new ScanCode(BACK, 14), new ScanCode(EQUALS, 13),
			new ScanCode(MINUS, 12), new ScanCode(D0, 11), new ScanCode(D9, 10), new ScanCode(D8, 9),
			new ScanCode(D7, 8), new ScanCode(D6, 7), new ScanCode(D5, 6), new ScanCode(D4, 5), new ScanCode(D3, 4),
			new ScanCode(D2, 3), new ScanCode(D1, 2), new ScanCode(INSERT, 210), new ScanCode(RIGHT, 205),
			new ScanCode(LEFT, 203), new ScanCode(PAUSE, 197), new ScanCode(ADD, 78), new ScanCode(DELETE, 211),
			new ScanCode(Q, 16) };

	public static final Map<String, Integer> nameToScanCodeMap;
	public static final Map<Integer, String> scanCodeToNameMap;

	static {
		nameToScanCodeMap = new TreeMap<>();
		scanCodeToNameMap = new HashMap<>();

		for (final ScanCode sc : SCAN_CODES) {
			nameToScanCodeMap.put(sc.name, sc.scanCode);
			scanCodeToNameMap.put(sc.scanCode, sc.name);
		}
	}

	public final String name;
	public final int scanCode;

	public ScanCode(final String name, final int scanCode) {
		this.name = name;
		this.scanCode = scanCode;
	}

	@Override
	public String toString() {
		return name;
	}

}
