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

	public static final String DIK_0 = "0";
	public static final String DIK_1 = "1";
	public static final String DIK_2 = "2";
	public static final String DIK_3 = "3";
	public static final String DIK_4 = "4";
	public static final String DIK_5 = "5";
	public static final String DIK_6 = "6";
	public static final String DIK_7 = "7";
	public static final String DIK_8 = "8";
	public static final String DIK_9 = "9";
	public static final String DIK_A = "A";
	public static final String DIK_ABNT_C1 = "bzAbnt_C1";
	public static final String DIK_ABNT_C2 = "bzAbnt_C2";
	public static final String DIK_ADD = "Num+";
	public static final String DIK_APOSTROPHE = "'";
	public static final String DIK_APPS = "App Menu";
	public static final String DIK_AT = "pc-98 @";
	public static final String DIK_AX = "jAX";
	public static final String DIK_B = "B";
	public static final String DIK_BACK = "Back";
	public static final String DIK_BACKSLASH = "\\";
	public static final String DIK_C = "C";
	public static final String DIK_CALCULATOR = "Calc";
	public static final String DIK_CAPITAL = "CapsLock";
	public static final String DIK_COLON = "pc-98 :";
	public static final String DIK_COMMA = ",";
	public static final String DIK_CONVERT = "jConvert";
	public static final String DIK_D = "D";
	public static final String DIK_DECIMAL = "Num.";
	public static final String DIK_DELETE = "Del";
	public static final String DIK_DIVIDE = "Num/";
	public static final String DIK_DOWN = "Down";
	public static final String DIK_E = "E";
	public static final String DIK_END = "End";
	public static final String DIK_EQUALS = "=";
	public static final String DIK_ESCAPE = "Esc";
	public static final String DIK_F = "F";
	public static final String DIK_F1 = "F1";
	public static final String DIK_F10 = "F10";
	public static final String DIK_F11 = "F11";
	public static final String DIK_F12 = "F12";
	public static final String DIK_F13 = "pc-98 F13";
	public static final String DIK_F14 = "pc-98 F14";
	public static final String DIK_F15 = "pc-98 F15";
	public static final String DIK_F2 = "F2";
	public static final String DIK_F3 = "F3";
	public static final String DIK_F4 = "F4";
	public static final String DIK_F5 = "F5";
	public static final String DIK_F6 = "F6";
	public static final String DIK_F7 = "F7";
	public static final String DIK_F8 = "F8";
	public static final String DIK_F9 = "F9";
	public static final String DIK_G = "G";
	public static final String DIK_GRAVE = "`";
	public static final String DIK_H = "H";
	public static final String DIK_HOME = "Home";
	public static final String DIK_I = "I";
	public static final String DIK_INSERT = "Ins";
	public static final String DIK_J = "J";
	public static final String DIK_K = "K";
	public static final String DIK_KANA = "jKana";
	public static final String DIK_KANJI = "jKanji";
	public static final String DIK_L = "L";
	public static final String DIK_LBRACKET = "[";
	public static final String DIK_LCONTROL = "L Ctrl";
	public static final String DIK_LEFT = "Left Arrow";
	public static final String DIK_LMENU = "L Alt";
	public static final String DIK_LSHIFT = "L Shift";
	public static final String DIK_LWIN = "L Win";
	public static final String DIK_M = "M";
	public static final String DIK_MAIL = "Mail";
	public static final String DIK_MEDIASELECT = "Media Select";
	public static final String DIK_MEDIASTOP = "Stop";
	public static final String DIK_MINUS = "-";
	public static final String DIK_MULTIPLY = "Num*";
	public static final String DIK_MUTE = "Mute";
	public static final String DIK_MYCOMPUTER = "My Computer";
	public static final String DIK_N = "N";
	public static final String DIK_NEXT = "PgDn";
	public static final String DIK_NEXTTRACK = "Next";
	public static final String DIK_NOCONVERT = "jNoConvert";
	public static final String DIK_NUMLOCK = "NumLock";
	public static final String DIK_NUMPAD0 = "Num0";
	public static final String DIK_NUMPAD1 = "Num1";
	public static final String DIK_NUMPAD2 = "Num2";
	public static final String DIK_NUMPAD3 = "Num3";
	public static final String DIK_NUMPAD4 = "Num4";
	public static final String DIK_NUMPAD5 = "Num5";
	public static final String DIK_NUMPAD6 = "Num6";
	public static final String DIK_NUMPAD7 = "Num7";
	public static final String DIK_NUMPAD8 = "Num8";
	public static final String DIK_NUMPAD9 = "Num9";
	public static final String DIK_NUMPADCOMMA = "Num,";
	public static final String DIK_NUMPADENTER = "NumEnter";
	public static final String DIK_NUMPADEQUALS = "Num=";
	public static final String DIK_O = "O";
	public static final String DIK_OEM_102 = "OEM_102";
	public static final String DIK_P = "P";
	public static final String DIK_PAUSE = "Pause";
	public static final String DIK_PERIOD = ".";
	public static final String DIK_PLAYPAUSE = "Play/Pause";
	public static final String DIK_POWER = "Power";
	public static final String DIK_PREVTRACK = "Prev";
	public static final String DIK_PRIOR = "PgUp";
	public static final String DIK_Q = "Q";
	public static final String DIK_R = "R";
	public static final String DIK_RBRACKET = "]";
	public static final String DIK_RCONTROL = "R Ctrl";
	public static final String DIK_RETURN = "Return";
	public static final String DIK_RIGHT = "Right Arrow";
	public static final String DIK_RMENU = "R Alt";
	public static final String DIK_RSHIFT = "R Shift";
	public static final String DIK_RWIN = "R Win";
	public static final String DIK_S = "S";
	public static final String DIK_SCROLL = "ScrollLock";
	public static final String DIK_SEMICOLON = ";";
	public static final String DIK_SLASH = "/";
	public static final String DIK_SLEEP = "Sleep";
	public static final String DIK_SPACE = "Space";
	public static final String DIK_STOP = "pc-98 Stop";
	public static final String DIK_SUBTRACT = "Num-";
	public static final String DIK_SYSRQ = "SysRq";
	public static final String DIK_T = "T";
	public static final String DIK_TAB = "Tab";
	public static final String DIK_U = "U";
	public static final String DIK_UNDERLINE = "pc-98 _";
	public static final String DIK_UNLABELED = "J3100";
	public static final String DIK_UP = "Up Arrow";
	public static final String DIK_V = "V";
	public static final String DIK_VOLUMEDOWN = "Vol-";
	public static final String DIK_VOLUMEUP = "Vol+";
	public static final String DIK_W = "W";
	public static final String DIK_WAKE = "Wake";
	public static final String DIK_WEBBACK = "webBack";
	public static final String DIK_WEBFAVORITES = "webFavs";
	public static final String DIK_WEBFORWARD = "webForward";
	public static final String DIK_WEBHOME = "webHome";
	public static final String DIK_WEBREFRESH = "webRefresh";
	public static final String DIK_WEBSEARCH = "webSearch";
	public static final String DIK_WEBSTOP = "webStop";
	public static final String DIK_X = "X";
	public static final String DIK_Y = "Y";
	public static final String DIK_YEN = "jYen";
	public static final String DIK_Z = "Z";

	public static final Map<String, Integer> nameToScanCodeMap;

	private static final DirectInputKeyCode[] SCAN_CODES = { new DirectInputKeyCode(DIK_SLEEP, 223),
			new DirectInputKeyCode(DIK_NEXT, 209), new DirectInputKeyCode(DIK_STOP, 149),
			new DirectInputKeyCode(DIK_CONVERT, 121), new DirectInputKeyCode(DIK_DECIMAL, 83),
			new DirectInputKeyCode(DIK_X, 45), new DirectInputKeyCode(DIK_Y, 21), new DirectInputKeyCode(DIK_ESCAPE, 1),
			new DirectInputKeyCode(DIK_MEDIASELECT, 237), new DirectInputKeyCode(DIK_MAIL, 236),
			new DirectInputKeyCode(DIK_MYCOMPUTER, 235), new DirectInputKeyCode(DIK_WEBBACK, 234),
			new DirectInputKeyCode(DIK_WEBFORWARD, 233), new DirectInputKeyCode(DIK_WEBSTOP, 232),
			new DirectInputKeyCode(DIK_WEBREFRESH, 231), new DirectInputKeyCode(DIK_WEBFAVORITES, 230),
			new DirectInputKeyCode(DIK_WEBSEARCH, 229), new DirectInputKeyCode(DIK_WAKE, 227),
			new DirectInputKeyCode(DIK_POWER, 222), new DirectInputKeyCode(DIK_APPS, 221),
			new DirectInputKeyCode(DIK_RWIN, 220), new DirectInputKeyCode(DIK_LWIN, 219),
			new DirectInputKeyCode(DIK_DOWN, 208), new DirectInputKeyCode(DIK_END, 207),
			new DirectInputKeyCode(DIK_PRIOR, 201), new DirectInputKeyCode(DIK_UP, 200),
			new DirectInputKeyCode(DIK_HOME, 199), new DirectInputKeyCode(DIK_RMENU, 184),
			new DirectInputKeyCode(DIK_SYSRQ, 183), new DirectInputKeyCode(DIK_DIVIDE, 181),
			new DirectInputKeyCode(DIK_NUMPADCOMMA, 179), new DirectInputKeyCode(DIK_WEBHOME, 178),
			new DirectInputKeyCode(DIK_VOLUMEUP, 176), new DirectInputKeyCode(DIK_VOLUMEDOWN, 174),
			new DirectInputKeyCode(DIK_MEDIASTOP, 164), new DirectInputKeyCode(DIK_PLAYPAUSE, 162),
			new DirectInputKeyCode(DIK_CALCULATOR, 161), new DirectInputKeyCode(DIK_MUTE, 160),
			new DirectInputKeyCode(DIK_RCONTROL, 157), new DirectInputKeyCode(DIK_NUMPADENTER, 156),
			new DirectInputKeyCode(DIK_NEXTTRACK, 153), new DirectInputKeyCode(DIK_UNLABELED, 151),
			new DirectInputKeyCode(DIK_AX, 150), new DirectInputKeyCode(DIK_KANJI, 148),
			new DirectInputKeyCode(DIK_UNDERLINE, 147), new DirectInputKeyCode(DIK_COLON, 146),
			new DirectInputKeyCode(DIK_AT, 145), new DirectInputKeyCode(DIK_PREVTRACK, 144),
			new DirectInputKeyCode(DIK_NUMPADEQUALS, 141), new DirectInputKeyCode(DIK_ABNT_C2, 126),
			new DirectInputKeyCode(DIK_YEN, 125), new DirectInputKeyCode(DIK_NOCONVERT, 123),
			new DirectInputKeyCode(DIK_ABNT_C1, 115), new DirectInputKeyCode(DIK_KANA, 112),
			new DirectInputKeyCode(DIK_F15, 102), new DirectInputKeyCode(DIK_F14, 101),
			new DirectInputKeyCode(DIK_F13, 100), new DirectInputKeyCode(DIK_F12, 88),
			new DirectInputKeyCode(DIK_F11, 87), new DirectInputKeyCode(DIK_OEM_102, 86),
			new DirectInputKeyCode(DIK_NUMPAD0, 82), new DirectInputKeyCode(DIK_NUMPAD3, 81),
			new DirectInputKeyCode(DIK_NUMPAD2, 80), new DirectInputKeyCode(DIK_NUMPAD1, 79),
			new DirectInputKeyCode(DIK_NUMPAD6, 77), new DirectInputKeyCode(DIK_NUMPAD5, 76),
			new DirectInputKeyCode(DIK_NUMPAD4, 75), new DirectInputKeyCode(DIK_SUBTRACT, 74),
			new DirectInputKeyCode(DIK_NUMPAD9, 73), new DirectInputKeyCode(DIK_NUMPAD8, 72),
			new DirectInputKeyCode(DIK_NUMPAD7, 71), new DirectInputKeyCode(DIK_SCROLL, 70),
			new DirectInputKeyCode(DIK_NUMLOCK, 69), new DirectInputKeyCode(DIK_F10, 68),
			new DirectInputKeyCode(DIK_F9, 67), new DirectInputKeyCode(DIK_F8, 66), new DirectInputKeyCode(DIK_F7, 65),
			new DirectInputKeyCode(DIK_F6, 64), new DirectInputKeyCode(DIK_F5, 63), new DirectInputKeyCode(DIK_F4, 62),
			new DirectInputKeyCode(DIK_F3, 61), new DirectInputKeyCode(DIK_F2, 60), new DirectInputKeyCode(DIK_F1, 59),
			new DirectInputKeyCode(DIK_CAPITAL, 58), new DirectInputKeyCode(DIK_SPACE, 57),
			new DirectInputKeyCode(DIK_LMENU, 56), new DirectInputKeyCode(DIK_MULTIPLY, 55),
			new DirectInputKeyCode(DIK_RSHIFT, 54), new DirectInputKeyCode(DIK_SLASH, 53),
			new DirectInputKeyCode(DIK_PERIOD, 52), new DirectInputKeyCode(DIK_COMMA, 51),
			new DirectInputKeyCode(DIK_M, 50), new DirectInputKeyCode(DIK_N, 49), new DirectInputKeyCode(DIK_B, 48),
			new DirectInputKeyCode(DIK_V, 47), new DirectInputKeyCode(DIK_C, 46), new DirectInputKeyCode(DIK_Z, 44),
			new DirectInputKeyCode(DIK_BACKSLASH, 43), new DirectInputKeyCode(DIK_LSHIFT, 42),
			new DirectInputKeyCode(DIK_GRAVE, 41), new DirectInputKeyCode(DIK_APOSTROPHE, 40),
			new DirectInputKeyCode(DIK_SEMICOLON, 39), new DirectInputKeyCode(DIK_L, 38),
			new DirectInputKeyCode(DIK_K, 37), new DirectInputKeyCode(DIK_J, 36), new DirectInputKeyCode(DIK_H, 35),
			new DirectInputKeyCode(DIK_G, 34), new DirectInputKeyCode(DIK_F, 33), new DirectInputKeyCode(DIK_D, 32),
			new DirectInputKeyCode(DIK_S, 31), new DirectInputKeyCode(DIK_A, 30),
			new DirectInputKeyCode(DIK_LCONTROL, 29), new DirectInputKeyCode(DIK_RETURN, 28),
			new DirectInputKeyCode(DIK_RBRACKET, 27), new DirectInputKeyCode(DIK_LBRACKET, 26),
			new DirectInputKeyCode(DIK_P, 25), new DirectInputKeyCode(DIK_O, 24), new DirectInputKeyCode(DIK_I, 23),
			new DirectInputKeyCode(DIK_U, 22), new DirectInputKeyCode(DIK_T, 20), new DirectInputKeyCode(DIK_R, 19),
			new DirectInputKeyCode(DIK_E, 18), new DirectInputKeyCode(DIK_W, 17), new DirectInputKeyCode(DIK_TAB, 15),
			new DirectInputKeyCode(DIK_BACK, 14), new DirectInputKeyCode(DIK_EQUALS, 13),
			new DirectInputKeyCode(DIK_MINUS, 12), new DirectInputKeyCode(DIK_0, 11), new DirectInputKeyCode(DIK_9, 10),
			new DirectInputKeyCode(DIK_8, 9), new DirectInputKeyCode(DIK_7, 8), new DirectInputKeyCode(DIK_6, 7),
			new DirectInputKeyCode(DIK_5, 6), new DirectInputKeyCode(DIK_4, 5), new DirectInputKeyCode(DIK_3, 4),
			new DirectInputKeyCode(DIK_2, 3), new DirectInputKeyCode(DIK_1, 2), new DirectInputKeyCode(DIK_INSERT, 210),
			new DirectInputKeyCode(DIK_RIGHT, 205), new DirectInputKeyCode(DIK_LEFT, 203),
			new DirectInputKeyCode(DIK_PAUSE, 197), new DirectInputKeyCode(DIK_ADD, 78),
			new DirectInputKeyCode(DIK_DELETE, 211), new DirectInputKeyCode(DIK_Q, 16) };
	public static final Map<Integer, String> scanCodeToNameMap;

	static {
		nameToScanCodeMap = new TreeMap<>();
		scanCodeToNameMap = new HashMap<>();

		for (final DirectInputKeyCode sc : SCAN_CODES) {
			nameToScanCodeMap.put(sc.name, sc.DirectInputKeyCode);
			scanCodeToNameMap.put(sc.DirectInputKeyCode, sc.name);
		}
	}

	public final int DirectInputKeyCode;
	public final String name;

	public DirectInputKeyCode(final String name, final int DirectInputKeyCode) {
		this.name = name;
		this.DirectInputKeyCode = DirectInputKeyCode;
	}

	@Override
	public String toString() {
		return name;
	}

}
