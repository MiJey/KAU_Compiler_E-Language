/**
 * ASCII 코드 대응 이모지
 * 
 * 깨질까봐 unicode escape 문자를 사용하였으며
 * Java에서 5-digit 유니코드를 escape 문자로 표현할 수 없어서 Surrogate Pair로 작성하였다.
 * static final로 선언해놓았으므로 Emoji.exclamation <-- 이런식으로 쓰면 된다.
 * 
 * @author Moon Yeji
 */
public class Emoji {
	// ! " # $ % & ' ( ) * + , - . / : ; < = > ? @ [ \ ] ^ _ ` { | } ~
	public static final String exclamation		= "\u2757";			// ❗️
	public static final String quotation		= "\uD83D\uDCA6";	// 💦
	public static final String hashtag			= "\uD83D\uDDD1";	// 🗑
	public static final String dollar			= "\uD83D\uDCB0";	// 💰
	public static final String percent			= "\uD83C\uDF70";	// 🍰
	public static final String ampersand		= "\uD83C\uDF80";	// 🎀
	public static final String apostrophe		= "\uD83C\uDF88";	// 🎈
	public static final String leftparenthesis	= "\uD83D\uDCD6";	// 📖
	public static final String rightparenthesis	= "\uD83D\uDCD5";	// 📕
	public static final String asterisk			= "\u2B50";			// ⭐
	public static final String plus				= "\u2795";			// ➕
	public static final String comma			= "\uD83D\uDCAC";	// 💬
	public static final String hyphen			= "\u2796";			// ➖
	public static final String period			= "\uD83C\uDF10";	// 🌐
	public static final String slash			= "\uD83D\uDD8B";	// 🖋️
	public static final String colon			= "\uD83D\uDE07";	// 😇
	public static final String semicolon		= "\uD83D\uDE09";	// 😉
	public static final String lessthan			= "\u26F0";			// ⛰
	public static final String equal			= "\uD83C\uDF8F";	// 🎏
	public static final String greaterthan		= "\uD83C\uDFC4";	// 🏄
	public static final String question			= "\u2754";			// ❔
	public static final String at				= "\uD83D\uDC0C";	// 🐌
	public static final String leftbracket		= "\uD83D\uDCC8";	// 📈
	public static final String backslash		= "\uD83D\uDC60";	// 👠
	public static final String rightbracket		= "\uD83D\uDCC9";	// 📉
	public static final String caret			= "\uD83E\uDD86";	// 🦆
	public static final String underscore		= "\uD83D\uDEAC";	// 🚬
	public static final String accent			= "\uD83D\uDCCD";	// 📍
	public static final String leftbrace		= "\uD83D\uDE4B";	// 🙋
	public static final String verticalbar		= "\uD83D\uDEA6";	// 🚦
	public static final String rightbrace		= "\uD83D\uDC81";	// 💁
	public static final String tilde			= "\uD83C\uDF0A";	// 🌊
	
	// 0 ~ 9
	public static final String one		= "\uD83D\uDD5B";	// 🕛
	public static final String two		= "\uD83D\uDD50";	// 🕐
	public static final String three	= "\uD83D\uDD51";	// 🕑
	public static final String four		= "\uD83D\uDD52";	// 🕒
	public static final String five		= "\uD83D\uDD53";	// 🕓
	public static final String six		= "\uD83D\uDD54";	// 🕔
	public static final String seven	= "\uD83D\uDD55";	// 🕕
	public static final String eight	= "\uD83D\uDD56";	// 🕖
	public static final String nine		= "\uD83D\uDD57";	// 🕗
	public static final String ten		= "\uD83D\uDD58";	// 🕘
	
	// a ~ z
	public static final String a = "\uD83C\uDF4F";	// 🍏
    public static final String b = "\uD83C\uDF4C";	// 🍌
    public static final String c = "\uD83E\uDD55";	// 🥕
    public static final String d = "\uD83D\uDC8E";	// 💎
    public static final String e = "\uD83D\uDC18";	// 🐘
    public static final String f = "\uD83D\uDD95";	// 🖕
    public static final String g = "\uD83D\uDC53";	// 👓
    public static final String h = "\uD83C\uDF54";	// 🍔
    public static final String i = "\uD83D\uDC40";	// 👀
    public static final String j = "\uD83C\uDF79";	// 🍹
    public static final String k = "\uD83E\uDD34";	// 🤴🏻
    public static final String l = "\uD83D\uDC8B";	// 💋
    public static final String m = "\uD83C\uDF19";	// 🌙
    public static final String n = "\uD83D\uDCD2";	// 📒
    public static final String o = "\uD83C\uDF4A";	// 🍊
    public static final String p = "\uD83C\uDF51";	// 🍑
    public static final String q = "\uD83D\uDC78";	// 👸🏻
    public static final String r = "\uD83C\uDF08";	// 🌈
    public static final String s = "\uD83D\uDC0D";	// 🐍
    public static final String t = "\uD83D\uDE95";	// 🚕
    public static final String u = "\u2602";		// ☂️
    public static final String v = "\u270C";		// ✌️
    public static final String w = "\uD83C\uDF0F";	// 🌏
    public static final String x = "\uD83C\uDF85";	// 🎅🏻
    public static final String y = "\u26F5";		// ⛵️
    public static final String z = "\uD83D\uDCA4";	// 💤
    
    // A~Z
	public static final String A = "\uD83D\uDC46\uD83C\uDF4F";	// 👆🍏
    public static final String B = "\uD83D\uDC46\uD83C\uDF4C";	// 👆🍌
    public static final String C = "\uD83D\uDC46\uD83E\uDD55";	// 👆🥕
    public static final String D = "\uD83D\uDC46\uD83D\uDC8E";	// 👆💎
    public static final String E = "\uD83D\uDC46\uD83D\uDC18";	// 👆🐘
    public static final String F = "\uD83D\uDC46\uD83D\uDD95";	// 👆🖕
    public static final String G = "\uD83D\uDC46\uD83D\uDC53";	// 👆👓
    public static final String H = "\uD83D\uDC46\uD83C\uDF54";	// 👆🍔
    public static final String I = "\uD83D\uDC46\uD83D\uDC40";	// 👆👀
    public static final String J = "\uD83D\uDC46\uD83C\uDF79";	// 👆🍹
    public static final String K = "\uD83D\uDC46\uD83E\uDD34";	// 👆🏻🤴
    public static final String L = "\uD83D\uDC46\uD83D\uDC8B";	// 👆💋
    public static final String M = "\uD83D\uDC46\uD83C\uDF19";	// 👆🌙
    public static final String N = "\uD83D\uDC46\uD83D\uDCD2";	// 👆📒
    public static final String O = "\uD83D\uDC46\uD83C\uDF4A";	// 👆🍊
    public static final String P = "\uD83D\uDC46\uD83C\uDF51";	// 👆🍑
    public static final String Q = "\uD83D\uDC46\uD83D\uDC78";	// 👆🏻👸
    public static final String R = "\uD83D\uDC46\uD83C\uDF08";	// 👆🌈
    public static final String S = "\uD83D\uDC46\uD83D\uDC0D";	// 👆🐍
    public static final String T = "\uD83D\uDC46\uD83D\uDE95";	// 👆🚕
    public static final String U = "\uD83D\uDC46\u2602";		// 👆☂️
    public static final String V = "\uD83D\uDC46\u270C";		// 👆✌️
    public static final String W = "\uD83D\uDC46\uD83C\uDF0F";	// 👆🌏
    public static final String X = "\uD83D\uDC46\uD83C\uDF85";	// 👆🎅🏻
    public static final String Y = "\uD83D\uDC46\u26F5";		// 👆⛵️
    public static final String Z = "\uD83D\uDC46\uD83D\uDCA4";	// 👆💤
}
