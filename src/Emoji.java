import java.util.ArrayList;

/**
 * ASCII 코드 대응 이모지
 * 
 * Java에서 5-digit 유니코드를 escape 문자로 표현할 수 없어서 Surrogate Pair로 작성하였다.
 * 
 * @author Moon Yeji
 */
public class Emoji {
	// 다음 유니코드를 읽으라는 의미의 특수 이모지
	public static final Emoji next = new Emoji("<NEXT>", new int[] {});
	
	// white spaces
	public static final Emoji eof     = new Emoji("<EOF>", new int[] { 0x0004 });
	public static final Emoji space   = new Emoji("<SP>",  new int[] { 0x0020 });
	public static final Emoji tab     = new Emoji("<TAB>", new int[] { 0x0009 });
	public static final Emoji newline = new Emoji("<LF>",  new int[] { 0x000A });
	
	// keywords and functions
	public static final Emoji assignEmoji    = new Emoji("\uD83D\uDC48", new int[] { 0xD83D, 0xDC48 }); // 👈
	public static final Emoji ifEmoji        = new Emoji("\uD83E\uDD14", new int[] { 0xD83E, 0xDD14 }); // 🤔
	public static final Emoji elseEmoji      = new Emoji("\uD83D\uDE1E", new int[] { 0xD83D, 0xDE1E }); // 😞
	public static final Emoji whileEmoji     = new Emoji("\u267B\uFE0F", new int[] { 0x267B, 0xFE0F }); // ♻️
	
	public static final Emoji orEmoji        = new Emoji("\uD83E\uDD37", new int[] { 0xD83E, 0xDD37 }); // 🤷
	public static final Emoji andEmoji       = new Emoji("\uD83D\uDC6B", new int[] { 0xD83D, 0xDC6B }); // 👫
	public static final Emoji notEmoji       = new Emoji("\uD83E\uDD5C", new int[] { 0xD83E, 0xDD5C }); // 🥜
	public static final Emoji equalsEmoji    = new Emoji("\u2696\uFE0F", new int[] { 0x2696, 0xFE0F }); // ⚖️
	public static final Emoji greaterEmoji   = new Emoji("\uD83D\uDE4B", new int[] { 0xD83D, 0xDE4B }); // 🙋 >
	public static final Emoji lessEmoji      = new Emoji("\uD83D\uDC81", new int[] { 0xD83D, 0xDC81 }); // 💁 <
	
	public static final Emoji charEmoji      = new Emoji("\uD83D\uDCCE", new int[] { 0xD83D, 0xDCCE }); // 📎
	public static final Emoji stringEmoji    = new Emoji("\uD83D\uDD87", new int[] { 0xD83D, 0xDD87 }); // 🖇
	
	public static final Emoji trueEmoji      = new Emoji("\uD83D\uDE46", new int[] { 0xD83D, 0xDE46 }); // 🙆
	public static final Emoji falseEmoji     = new Emoji("\uD83D\uDE45", new int[] { 0xD83D, 0xDE45 }); // 🙅
	
	public static final Emoji plusEmoji      = new Emoji("\u2795",       new int[] { 0x2795 });         // ➕
	public static final Emoji minusEmoji     = new Emoji("\u2796",       new int[] { 0x2796 });         // ➖
	public static final Emoji multiflyEmoji  = new Emoji("\uD83D\uDC07", new int[] { 0xD83D, 0xDC07 }); // 🐇
	public static final Emoji divideEmoji    = new Emoji("\u2702",       new int[] { 0x2702 });         // ✂️
	
	public static final Emoji printEmoji     = new Emoji("\uD83D\uDCFA", new int[] { 0xD83D, 0xDCFA });	// 📺
	public static final Emoji scanEmoji      = new Emoji("\uD83C\uDFB9", new int[] { 0xD83C, 0xDFB9 });	// 🎹
	public static final Emoji randomEmoji    = new Emoji("\uD83C\uDFB2", new int[] { 0xD83C, 0xDFB2 });	// 🎲
	public static final Emoji timeEmoji      = new Emoji("\u23F0",       new int[] { 0x23F0 });	        // ⏰
	
	// ASCII Codes(키워드로 쓰이는 경우 뒤에 Emoji를 붙힘)
	// ! " # $ % & ' ( ) * + , - . / : ; < = > ? @ [ \ ] ^ _ ` { | } ~
	public static final Emoji exclamation       = new Emoji("\u2757\uFE0F", new int[] { 0x2757, 0xFE0F }); // ❗️
	public static final Emoji quotation         = new Emoji("\uD83D\uDCA6", new int[] { 0xD83D, 0xDCA6 }); // 💦
	public static final Emoji hashtag           = new Emoji("\uD83D\uDDD1", new int[] { 0xD83D, 0xDDD1 }); // 🗑
	public static final Emoji dollar            = new Emoji("\uD83D\uDCB0", new int[] { 0xD83D, 0xDCB0 }); // 💰
	public static final Emoji percent           = new Emoji("\uD83C\uDF70", new int[] { 0xD83C, 0xDF70 }); // 🍰
	public static final Emoji ampersand         = new Emoji("\uD83C\uDF80", new int[] { 0xD83C, 0xDF80 }); // 🎀
	public static final Emoji apostrophe        = new Emoji("\uD83C\uDF88", new int[] { 0xD83C, 0xDF88 }); // 🎈
	public static final Emoji leftparenEmoji    = new Emoji("\uD83D\uDCD6", new int[] { 0xD83D, 0xDCD6 }); // 📖
	public static final Emoji rightparenEmoji   = new Emoji("\uD83D\uDCD5", new int[] { 0xD83D, 0xDCD5 }); // 📕
	public static final Emoji asterisk          = new Emoji("\uD83C\uDF1F", new int[] { 0xD83C, 0xDF1F }); // 🌟
	public static final Emoji plus              = new Emoji("\uD83C\uDF81", new int[] { 0xD83C, 0xDF81 }); // 🎁
	public static final Emoji comma             = new Emoji("\uD83D\uDCAC", new int[] { 0xD83D, 0xDCAC }); // 💬
	public static final Emoji hyphen            = new Emoji("\uD83D\uDCB3", new int[] { 0xD83D, 0xDCB3 }); // 💳
	public static final Emoji periodEmoji       = new Emoji("\uD83C\uDF10", new int[] { 0xD83C, 0xDF10 }); // 🌐
	public static final Emoji slash             = new Emoji("\uD83D\uDD8B", new int[] { 0xD83D, 0xDD8B }); // 🖋️
	public static final Emoji colon             = new Emoji("\uD83D\uDE07", new int[] { 0xD83D, 0xDE07 }); // 😇
	public static final Emoji semicolon         = new Emoji("\uD83D\uDE09", new int[] { 0xD83D, 0xDE09 }); // 😉
	public static final Emoji lessthan          = new Emoji("\u26F0",       new int[] { 0x26F0 });         // ⛰
	public static final Emoji equal             = new Emoji("\uD83C\uDF8F", new int[] { 0xD83C, 0xDF8F }); // 🎏
	public static final Emoji greaterthan       = new Emoji("\uD83C\uDFC4", new int[] { 0xD83C, 0xDFC4 }); // 🏄
	public static final Emoji question          = new Emoji("\u2754",       new int[] { 0x2754 });         // ❔
	public static final Emoji at                = new Emoji("\uD83D\uDC0C", new int[] { 0xD83D, 0xDC0C }); // 🐌
	public static final Emoji leftbracketEmoji  = new Emoji("\uD83D\uDCC8", new int[] { 0xD83D, 0xDCC8 }); // 📈
	public static final Emoji backslash         = new Emoji("\uD83D\uDC60", new int[] { 0xD83D, 0xDC60 }); // 👠
	public static final Emoji rightbracketEmoji = new Emoji("\uD83D\uDCC9", new int[] { 0xD83D, 0xDCC9 }); // 📉
	public static final Emoji caret             = new Emoji("\uD83E\uDD86", new int[] { 0xD83E, 0xDD86 }); // 🦆
	public static final Emoji underscore        = new Emoji("\uD83D\uDEAC", new int[] { 0xD83D, 0xDEAC }); // 🚬
	public static final Emoji accent            = new Emoji("\uD83D\uDCCD", new int[] { 0xD83D, 0xDCCD }); // 📍
	public static final Emoji leftbrace         = new Emoji("\uD83D\uDC27", new int[] { 0xD83D, 0xDC27 }); // 🐧
	public static final Emoji verticalbar       = new Emoji("\uD83D\uDEA6", new int[] { 0xD83D, 0xDEA6 }); // 🚦
	public static final Emoji rightbrace        = new Emoji("\uD83D\uDC08", new int[] { 0xD83D, 0xDC08 }); // 🐈
	public static final Emoji tilde             = new Emoji("\uD83C\uDF0A", new int[] { 0xD83C, 0xDF0A }); // 🌊
	 
	// 0 ~ 9
	public static final Emoji zero  = new Emoji("\uD83D\uDD5B", new int[] { 0xD83D, 0xDD5B }); // 🕛
	public static final Emoji one   = new Emoji("\uD83D\uDD50", new int[] { 0xD83D, 0xDD50 }); // 🕐
	public static final Emoji two   = new Emoji("\uD83D\uDD51", new int[] { 0xD83D, 0xDD51 }); // 🕑
	public static final Emoji three = new Emoji("\uD83D\uDD52", new int[] { 0xD83D, 0xDD52 }); // 🕒
	public static final Emoji four  = new Emoji("\uD83D\uDD53", new int[] { 0xD83D, 0xDD53 }); // 🕓
	public static final Emoji five  = new Emoji("\uD83D\uDD54", new int[] { 0xD83D, 0xDD54 }); // 🕔
	public static final Emoji six   = new Emoji("\uD83D\uDD55", new int[] { 0xD83D, 0xDD55 }); // 🕕
	public static final Emoji seven = new Emoji("\uD83D\uDD56", new int[] { 0xD83D, 0xDD56 }); // 🕖
	public static final Emoji eight = new Emoji("\uD83D\uDD57", new int[] { 0xD83D, 0xDD57 }); // 🕗
	public static final Emoji nine  = new Emoji("\uD83D\uDD58", new int[] { 0xD83D, 0xDD58 }); // 🕘
	 
	// a ~ z
	public static final Emoji a = new Emoji("\uD83C\uDF4F", new int[] { 0xD83C, 0xDF4F }); // 🍏
	public static final Emoji b = new Emoji("\uD83C\uDF4C", new int[] { 0xD83C, 0xDF4C }); // 🍌
	public static final Emoji c = new Emoji("\uD83E\uDD55", new int[] { 0xD83E, 0xDD55 }); // 🥕
	public static final Emoji d = new Emoji("\uD83D\uDC8E", new int[] { 0xD83D, 0xDC8E }); // 💎
	public static final Emoji e = new Emoji("\uD83D\uDC18", new int[] { 0xD83D, 0xDC18 }); // 🐘
	public static final Emoji f = new Emoji("\uD83D\uDD95", new int[] { 0xD83D, 0xDD95 }); // 🖕
	public static final Emoji g = new Emoji("\uD83D\uDC53", new int[] { 0xD83D, 0xDC53 }); // 👓
	public static final Emoji h = new Emoji("\uD83C\uDF54", new int[] { 0xD83C, 0xDF54 }); // 🍔
	public static final Emoji i = new Emoji("\uD83D\uDC40", new int[] { 0xD83D, 0xDC40 }); // 👀
	public static final Emoji j = new Emoji("\uD83C\uDF79", new int[] { 0xD83C, 0xDF79 }); // 🍹
	public static final Emoji k = new Emoji("\uD83E\uDD34", new int[] { 0xD83E, 0xDD34 }); // 🤴🏻
	public static final Emoji l = new Emoji("\uD83D\uDC8B", new int[] { 0xD83D, 0xDC8B }); // 💋
	public static final Emoji m = new Emoji("\uD83C\uDF19", new int[] { 0xD83C, 0xDF19 }); // 🌙
	public static final Emoji n = new Emoji("\uD83D\uDCD2", new int[] { 0xD83D, 0xDCD2 }); // 📒
	public static final Emoji o = new Emoji("\uD83C\uDF4A", new int[] { 0xD83C, 0xDF4A }); // 🍊
	public static final Emoji p = new Emoji("\uD83C\uDF51", new int[] { 0xD83C, 0xDF51 }); // 🍑
	public static final Emoji q = new Emoji("\uD83D\uDC78", new int[] { 0xD83D, 0xDC78 }); // 👸🏻
	public static final Emoji r = new Emoji("\uD83C\uDF08", new int[] { 0xD83C, 0xDF08 }); // 🌈
	public static final Emoji s = new Emoji("\uD83D\uDC0D", new int[] { 0xD83D, 0xDC0D }); // 🐍
	public static final Emoji t = new Emoji("\uD83D\uDE95", new int[] { 0xD83D, 0xDE95 }); // 🚕
	public static final Emoji u = new Emoji("\u2602",       new int[] { 0x2602 });         // ☂️
	public static final Emoji v = new Emoji("\u270C",       new int[] { 0x270C });         // ✌️
	public static final Emoji w = new Emoji("\uD83C\uDF0F", new int[] { 0xD83C, 0xDF0F }); // 🌏
	public static final Emoji x = new Emoji("\uD83C\uDF85", new int[] { 0xD83C, 0xDF85 }); // 🎅🏻
	public static final Emoji y = new Emoji("\u26F5",       new int[] { 0x26F5 });         // ⛵️
	public static final Emoji z = new Emoji("\uD83D\uDCA4", new int[] { 0xD83D, 0xDCA4 }); // 💤
	    
	// A~Z
	public static final Emoji A = new Emoji("\uD83D\uDC46\uD83C\uDF4F", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF4F }); // 👆🍏
	public static final Emoji B = new Emoji("\uD83D\uDC46\uD83C\uDF4C", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF4C }); // 👆🍌
	public static final Emoji C = new Emoji("\uD83D\uDC46\uD83E\uDD55", new int[] { 0xD83D, 0xDC46, 0xD83E, 0xDD55 }); // 👆🥕
	public static final Emoji D = new Emoji("\uD83D\uDC46\uD83D\uDC8E", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDC8E }); // 👆💎
	public static final Emoji E = new Emoji("\uD83D\uDC46\uD83D\uDC18", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDC18 }); // 👆🐘
	public static final Emoji F = new Emoji("\uD83D\uDC46\uD83D\uDD95", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDD95 }); // 👆🖕
	public static final Emoji G = new Emoji("\uD83D\uDC46\uD83D\uDC53", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDC53 }); // 👆👓
	public static final Emoji H = new Emoji("\uD83D\uDC46\uD83C\uDF54", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF54 }); // 👆🍔
	public static final Emoji I = new Emoji("\uD83D\uDC46\uD83D\uDC40", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDC40 }); // 👆👀
	public static final Emoji J = new Emoji("\uD83D\uDC46\uD83C\uDF79", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF79 }); // 👆🍹
	public static final Emoji K = new Emoji("\uD83D\uDC46\uD83E\uDD34", new int[] { 0xD83D, 0xDC46, 0xD83E, 0xDD34 }); // 👆🤴🏻
	public static final Emoji L = new Emoji("\uD83D\uDC46\uD83D\uDC8B", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDC8B }); // 👆💋
	public static final Emoji M = new Emoji("\uD83D\uDC46\uD83C\uDF19", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF19 }); // 👆🌙
	public static final Emoji N = new Emoji("\uD83D\uDC46\uD83D\uDCD2", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDCD2 }); // 👆📒
	public static final Emoji O = new Emoji("\uD83D\uDC46\uD83C\uDF4A", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF4A }); // 👆🍊
	public static final Emoji P = new Emoji("\uD83D\uDC46\uD83C\uDF51", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF51 }); // 👆🍑
	public static final Emoji Q = new Emoji("\uD83D\uDC46\uD83D\uDC78", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDC78 }); // 👆👸🏻
	public static final Emoji R = new Emoji("\uD83D\uDC46\uD83C\uDF08", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF08 }); // 👆🌈
	public static final Emoji S = new Emoji("\uD83D\uDC46\uD83D\uDC0D", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDC0D }); // 👆🐍
	public static final Emoji T = new Emoji("\uD83D\uDC46\uD83D\uDE95", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDE95 }); // 👆🚕
	public static final Emoji U = new Emoji("\uD83D\uDC46\u2602",       new int[] { 0xD83D, 0xDC46, 0x2602 });         // 👆☂️
	public static final Emoji V = new Emoji("\uD83D\uDC46\u270C",       new int[] { 0xD83D, 0xDC46, 0x270C });         // 👆✌️
	public static final Emoji W = new Emoji("\uD83D\uDC46\uD83C\uDF0F", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF0F }); // 👆🌏
	public static final Emoji X = new Emoji("\uD83D\uDC46\uD83C\uDF85", new int[] { 0xD83D, 0xDC46, 0xD83C, 0xDF85 }); // 👆🎅🏻
	public static final Emoji Y = new Emoji("\uD83D\uDC46\u26F5",       new int[] { 0xD83D, 0xDC46, 0x26F5 });         // 👆⛵️
	public static final Emoji Z = new Emoji("\uD83D\uDC46\uD83D\uDCA4", new int[] { 0xD83D, 0xDC46, 0xD83D, 0xDCA4 }); // 👆💤
	
	// Emoji class
	private String emoji;
	public int unicodes[];
	
	private Emoji(String e, int u[]) {
		emoji = e;
		unicodes = u;
	}
	
	public String toString() {
		return emoji;
	}
}