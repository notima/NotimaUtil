package org.notima.util;

// More languages are welcome. Please send your list of country names, error messages, page text and explain text
// to TBG5 administrator. More advice at the end of this script. Thnx

// Special thx to:
// italian: Renato Polo                         norwegian: Siri-Anne dos Santos
// french: Walter Hoffmann & Pierrick Daumain   polish: Norbert Wozniak
// romanian: Kalman Pusztai                     serbian: Aleksandar Blagojevic
// bulgarian: Luben Karavelov                   slovenian: Alfred Anzlovar
// dutsh: Mark Sensen                           russian: Vasiliy Marhotsky
// hungarian: Tibor Albert                      lithuanian: Edmundas Kausikas
// turkish: Oguz Cekmeceligil                   catalan: Cristòfol-Josep Bordes i Figuerola
// swedish: Lars Madej                          chinese: May Chen
// greek: Stamatis Liatsos & Ian Gourley        spanish: Jesús Ferreiro
// albanian: Artan Rrustemi                     portuguese: Antonio Aguiar
// czech: Pavel Pech & Eva Marešová             finnish: Juha Keski-Nisula
// slovak: Lubomir Duchon                       danish: Christian Staal
// croatian: Zoran Babić                        latvian: Toms Abeltins
// estonian: Sven-Olav Paavel                   japanese: Kenji Itoh
// georgian: Mikheil Kapanadze                  mongolian: Solongo Luvsan

// Other: Markus Olderdissen, Gabriele Del Prete, scripting; google.com, translation;

// Scripting: Hendrik Muus

// add in_array method to arrays

public class IbanCheck {

/*	Array.prototype.in_array = function(value) {
		var found = false;
		for (var i=0; i<this.length; i++) {
			if (this[i] == value) {
				found = i;
				break; }}
		return found; }
	
	// add ISO13616Prepare method to strings
	String.prototype.ISO13616Prepare = function() {
		var isostr = this.toUpperCase();
		isostr = isostr.substr(4) + isostr.substr(0,4);
		for (var i = 0; i <= 25; i++) {
			while (isostr.search(String.fromCharCode(i+65)) != -1) {
				isostr = isostr.replace(String.fromCharCode(i+65), String(i+10)); }}
		return isostr; }
	
	// add ISO7064Mod97_10 method to strings
	String.prototype.ISO7064Mod97_10 = function() {
		var parts = Math.ceil(this.length/7);
		var remainer = "";
		for (var i = 1; i <= parts; i++) {
			remainer = String(parseFloat(remainer+this.substr((i-1)*7, 7))%97); }
		return remainer; }
	// replacement of === for javascript version < 1.2
	function is_ident(a,b) {
		var identical = false;
		if (typeof(a)==typeof(b)) {
			if (a==b) {
				identical = true; }}
		return identical; }

	// language codes
//				  1    2    3    4    5    6    7    8    9   10   11   12   13   14   15   16   17   18   19   20   21   22   23   24   25   26   27   28   29   30   31   32
	String[] lngc = new String[] {	
			"de","en","it","no","fr","pl","ro","sr","bg","sl","nl","ru",
			"hu","lt","tr","ca","sv","zh","el","es","sq","pt","cs","fi",
			"sk","da","hr","lv","et","ja","ka","mn" };
	// we have currently these languages:	1 deutsch, german, 2 english, english, 3 italiano, italian, 4 norske, norwegian, 5 français, french, 6 polska, polish, 7 română, romanian, 
//						8 srpska, serbian, 9 Български, bulgarian, 10 slovenščina, slovenian, 11 nederlands, dutch, 12 русско, russian, 13 magyar, hungarian, 
//						14 lietuvos, lithuanian, 15 türk, turkish, 16 català, catalan, 17 svenska, swedish, 18 中文, chinese, 19 Έλληνες, greek, 20 español, spanish, 
//						21 shqiptar, albanian, 22 português, portuguese, 23 česky, czech, 24 suomalainen, finnish, 25 slovenskému, slovak, 26 dansk, danish, 
//						27 hrvatski, croatian, 28 latvijā, latvian, 29 eesti, estonian, 30 japanese, 31 ქართული, georgian, 32 mонгол хэлээр, mongolian
	// language selector
	var lngsel =	"<option value=\"9\">Български, bulgarian</option>"+
			"<option value=\"16\">català, catalan</option>"+
			"<option value=\"23\">česky, czech</option>"+
			"<option value=\"18\">中文, chinese</option>"+
			"<option value=\"27\">hrvatski, croatian</option>"+
			"<option value=\"26\">dansk, danish</option>"+
			"<option value=\"1\">deutsch, german</option>"+
			"<option value=\"29\">eesti, estonian</option>"+
			"<option value=\"2\">english, english</option>"+
			"<option value=\"20\">español, spanish</option>"+
			"<option value=\"5\">français, french</option>"+
			"<option value=\"31\">ქართული, georgian</option>"+
			"<option value=\"19\">Έλληνες, greek</option>"+
			"<option value=\"3\">italiano, italian</option>"+
			"<option value=\"30\">日本, japanese</option>"+
			"<option value=\"14\">lietuvos, lithuanian</option>"+
			"<option value=\"28\">latvijā, latvian</option>"+
			"<option value=\"13\">magyar, hungarian</option>"+
			"<option value=\"32\">mонгол хэлээр, mongolian</option>"+
			"<option value=\"11\">nederlands, dutch</option>"+
			"<option value=\"4\">norske, norwegian</option>"+
			"<option value=\"6\">polska, polish</option>"+
			"<option value=\"22\">português, portuguese</option>"+
			"<option value=\"7\">română, romanian</option>"+
			"<option value=\"12\">русско, russian</option>"+
			"<option value=\"21\">shqiptar, albanian</option>"+
			"<option value=\"25\">slovenskému, slovak</option>"+
			"<option value=\"10\">slovenščina, slovenian</option>"+
			"<option value=\"8\">srpska, serbian</option>"+
			"<option value=\"24\">suomalainen, finnish</option>"+
			"<option value=\"17\">svenska, swedish</option>"+
			"<option value=\"15\">türk, turkish</option>";
	// country codes, fixed length for those countries, inner structure, appliance of EU REGULATION 924/2009, IBAN requirement and IBAN example
	var ilbced = new Array ("AD", 24, "F04F04A12",		"n", "n", "AD1200012030200359100100",
				"AE", 23, "F03F16",		"n", "n", "AE070331234567890123456",
				"AL", 28, "F08A16",		"n", "n", "AL47212110090000000235698741",
				"AT", 20, "F05F11",		"y", "n", "AT611904300234573201",
				"BA", 20, "F03F03F08F02",	"n", "n", "BA391290079401028494",
				"BE", 16, "F03F07F02",		"y", "n", "BE68539007547034",
				"BG", 22, "U04F04F02A08",	"y", "n", "BG80BNBG96611020345678",
				"CH", 21, "F05A12",		"n", "n", "CH9300762011623852957",
				"CY", 28, "F03F05A16",		"y", "n", "CY17002001280000001200527600",
				"CZ", 24, "F04F06F10",		"y", "n", "CZ6508000000192000145399",
				"DE", 22, "F08F10",		"y", "n", "DE89370400440532013000",
				"DK", 18, "F04F09F01",		"y", "n", "DK5000400440116243",
				"EE", 20, "F02F02F11F01",	"y", "n", "EE382200221020145685",
				"ES", 24, "F04F04F01F01F10",	"y", "n", "ES9121000418450200051332",
				"FI", 18, "F06F07F01",		"y", "n", "FI2112345600000785",
				"FO", 18, "F04F09F01",		"n", "n", "FO6264600001631634",
				"FR", 27, "F05F05A11F02",	"y", "n", "FR1420041010050500013M02606",
				"GB", 22, "U04F06F08",		"y", "n", "GB29NWBK60161331926819",
				"GE", 22, "U02F16",		"n", "n", "GE29NB0000000101904917",
				"GI", 23, "U04A15",		"y", "n", "GI75NWBK000000007099453",
				"GL", 18, "F04F09F01",		"n", "n", "GL8964710001000206",
				"GR", 27, "F03F04A16",		"y", "n", "GR1601101250000000012300695",
				"HR", 21, "F07F10",		"n", "n", "HR1210010051863000160",
				"HU", 28, "F03F04F01F15F01",	"y", "n", "HU42117730161111101800000000",
				"IE", 22, "U04F06F08",		"y", "n", "IE29AIBK93115212345678",
				"IL", 23, "F03F03F13",		"n", "n", "IL620108000000099999999",
				"IS", 26, "F04F02F06F10",	"y", "n", "IS140159260076545510730339",
				"IT", 27, "U01F05F05A12",	"y", "n", "IT60X0542811101000000123456",
				"KW", 30, "U04A22",		"n", "n", "KW81CBKU0000000000001234560101",
				"KZ", 20, "F03A13",		"n", "n", "KZ86125KZT5004100100",
				"LB", 28, "F04A20",		"n", "n", "LB62099900000001001901229114",
				"LI", 21, "F05A12",		"y", "n", "LI21088100002324013AA",
				"LT", 20, "F05F11",		"y", "n", "LT121000011101001000",
				"LU", 20, "F03A13",		"y", "n", "LU280019400644750000",
				"LV", 21, "U04A13",		"y", "n", "LV80BANK0000435195001",
				"MC", 27, "F05F05A11F02",	"y", "n", "MC5811222000010123456789030",
				"ME", 22, "F03F13F02",		"n", "n", "ME25505000012345678951",
				"MK", 19, "F03A10F02",		"n", "n", "MK07250120000058984",
				"MR", 27, "F05F05F11F02",	"n", "n", "MR1300020001010000123456753",
				"MT", 31, "U04F05A18",		"y", "n", "MT84MALT011000012345MTLCAST001S",
				"MU", 30, "U04F02F02F12F03U03",	"n", "n", "MU17BOMM0101101030300200000MUR",
				"NL", 18, "U04F10",		"y", "n", "NL91ABNA0417164300",
				"NO", 15, "F04F06F01",		"y", "n", "NO9386011117947",
				"PL", 28, "F08F16",		"y", "n", "PL27114020040000300201355387",
				"PT", 25, "F04F04F11F02",	"y", "n", "PT50000201231234567890154",
				"RO", 24, "U04A16",		"y", "n", "RO49AAAA1B31007593840000",
				"RS", 22, "F03F13F02",		"n", "n", "RS35260005601001611379",
				"SA", 24, "F02A18",		"n", "n", "SA0380000000608010167519",
				"SE", 24, "F03F16F01",		"y", "n", "SE4550000000058398257466",
				"SI", 19, "F05F08F02",		"y", "n", "SI56191000000123438",
				"SK", 24, "F04F06F10",		"y", "n", "SK3112000000198742637541",
				"SM", 27, "U01F05F05A12",	"n", "n", "SM86U0322509800000000270100",
				"TN", 24, "F02F03F13F02",	"n", "n", "TN5914207207100707129648",
				"TR", 26, "F05A01A16",		"n", "n", "TR330006100519786457841326");
	// we have currently # countries
	var ctcnt = ilbced.length/6;
	// use language 2 (english) by default
	if ((typeof(lngg) == 'undefined') || (lngg > lngc.length) || (lngg < 1)) {
		lngg = 2;
		// workaround
		if ((location.search != "") && (location.search != "?")) {
			sstr = window.location.search.substr(1,window.location.search.length-1);
			if (!isNaN(sstr ) && (sstr*1 > 0) && (sstr*1 <= lngc.length)) {
				lngg = Number(sstr); }}}
	// rearange country codes and related info
	var ilbc = new Array ();
	for (j = 0; j < 6; j++) {
		for (i = 0; i < ctcnt; i++) {
			ilbc[ilbc.length]=ilbced[j+i*6];  }}
	// the magic core routine
	function checkibancore(iban) {
		var standard = -1;
		illegal = /\W|_/; // contains chars other than (a-zA-Z0-9) 
		if(illegal.test(iban)) { // yes, alert and exit
			illegal = /((\W|_)+)/g;
			var ichars;
			var aliban = "";
			var lindex = -1;
			while (ichars = illegal.exec(iban)) {
				aliban += iban.substring(lindex+1,ichars.index)+"<strong>"+ichars[1]+"</strong>";
				lindex = ichars.index;	
			}
			aliban += iban.substr(lindex+1);
			aliban = aliban.replace(/\|/g, "%7C");
			alert(aliban+"\n\n"+altxt[0]);
			return "0"; }
		else { // no, continue
			illegal = /^\D\D\d\d.+/; // first chars are letter letter digit digit
			if(illegal.test(iban) == false) { // no, alert and exit
				alert("|"+iban.substr(0,4)+"|"+iban.substr(5)+"\n\n"+altxt[1]); 
				return "0"; }
			else { // yes, continue
				illegal = /^\D\D00.+|^\D\D01.+|^\D\D99.+/; // check digit are 00 or 01 or 99
				if(illegal.test(iban)) { // yes, alert and exit
					alert(iban.substr(0,2)+"|"+iban.substr(2,2)+"|"+iban.substr(5)+"\n\n"+altxt[2]);
					return "0"; }
				else { // no, continue
					lofi = ilbc.slice(0,ctcnt).in_array(iban.substr(0,2).toUpperCase()); // test if country respected
					if (is_ident(false,lofi)) { ctck=-1; lofi=6; }  // country not respected
					else { ctck=lofi; lofi=ilbc[lofi+ctcnt*1]; } // country respected
					if (lofi == 6) { // not respected, alert
						alert(altxt[3]+iban.substr(0,2).toUpperCase()+altxt[4]); 
						lofi = iban.length; }  // but continue
					if ((iban.length-lofi) != 0) { // fits length to country
						alert(altxt[5]+ctynm[ctck]+altxt[6]+ilbc[ctck+ctcnt*1]+altxt[7]); // no, alert and exit
						return "0"; } // yes, continue
					if (ctck >= 0) { illegal = buildtest("B04" +ilbc[ctck+ctcnt*2],standard); } // fetch sub structure of respected country
					else { illegal = /.+/; } // or take care of not respected country
					if (illegal.test(iban) == false) { // fits sub structure to country
						alert(getstructurealert(ilbc[ctck+ctcnt*2],iban)+"\n\n"+altxt[1]); // no, alert and exit
						return "0"; }
					else { // yes, continue
						return iban.ISO13616Prepare().ISO7064Mod97_10(); }}}}} // calculate and return the remainer
	// perform the check
	function checkiban(iban) {
		if (checkibancore(iban) == "1") { alert(altxt[8]); } // and prompt result
		else { alert(altxt[9]); }}
	function buildtest(structure,kind) {
		var result = "";
		var testpattern = structure.match(/([ABCFLUW]\d{2})/g);
		var patterncount = testpattern.length;
		for (var i = 0; i < patterncount; ++i) {
			if (((kind >= 0)&&(i != kind))||(kind == -2)) {
				result += testpart(testpattern[i],"any"); }
			else {
				result += testpart(testpattern[i],"standard"); }}
		return new RegExp(result); }
	function testpart(pattern,kind) {
		var testpattern = "(";
		if (kind == "any") {
			testpattern += "."; }
		else {
			testpattern += "[";
			if (kind == "reverse") {
				testpattern += "^"; }
			switch (pattern.substr(0,1)) {
				case "A": testpattern += "0-9A-Za-z"; break;
				case "B": testpattern += "0-9A-Z"; break;
				case "C": testpattern += "A-Za-z"; break;
				case "F": testpattern += "0-9"; break;
				case "L": testpattern += "a-z"; break;
				case "U": testpattern += "A-Z"; break;
				case "W": testpattern += "0-9a-z"; break; }
			testpattern += "]"; }
		if (((pattern.substr(1,2)*1) > 1) && (kind != "reverse")) {
			testpattern += "{"+String(pattern.substr(1,2)*1)+"}"; }
		testpattern += ")";
		return testpattern; }
	function getstructurealert(structure,iban) {
		any = -2;
		structure = "B04" + structure;
		var result = "";
		var failpattern = "";
		var testpattern = structure.match(/([ABCFLUW]\d{2})/g);
		var patterncount = testpattern.length;
		for (var i = 0; i < patterncount; ++i) {
			failpattern = buildtest(structure,i);
			if (!failpattern.test(iban)) {
				failpattern = buildtest(structure,any);
				var ibanparts = iban.match(failpattern);
				var ibanpart = ibanparts[i+1];
				var partpattern = structure.match(/([ABCFLUW]\d{2})/g);
				var failures = ibanpart.match(new RegExp(testpart(partpattern[i],"reverse"),"g"));
				var failure = 0;
				var pos = 0;
				while ((pos < ibanpart.length) && (failure < failures.length)) {
					if (ibanpart.substr(pos,1) == failures[failure]) {
						result += "|"+ibanpart.substr(pos,1)+"|";
						++failure; }
					else {
						result += ibanpart.substr(pos,1); }
					++pos; }
				result += ibanpart.substr(pos)+" "; }
			else {
				var ibanparts = iban.match(failpattern);
				result += ibanparts[i+1]+" "; }}
		result = result.replace(/||/g, "");
		return result;}
	// some convenience for web page
	function langsel() { document.write(lngsel); } //language selector
	function lginfo() {
		if (is_ident(false,lngc.in_array(hlc.substr(1,2)))) {
			document.write("<BR><small>"+hlc+" currently not translated, like to translate?</small>"); }}
	function rwt(tnum) { 
		document.write(wptxt[tnum]); } //web page text by number
	function rwt2(pos) { 
		if (wptxt[0] == pos) {
			document.write(" <a href='http://eur-lex.europa.eu/Result.do?RechType=RECH_celex&code=32009R0924&checktexts=checkbox' target='_blank' title='Regulation 924 on eur-lex.europa.eu'>EU Regulation 924/2009</a> "); }} //special web page text
	// whole line of special web page text
	function rwt3() { 
		document.write("<small><BR>(");
		rwt2(0);
		document.write("<span class=\"tbg5eray\">");
		rwt(3);
		document.write("</span> / <span class=\"tbg5eran\">");
		rwt(4);
		document.write("</span>");
		rwt2(1);
		document.write(")<BR><BR></small>"); }
	// some convenience to list the countries
	function rcr(tnum) { return ilbc[tnum+ctcnt*3]; } //country regulatory info by number (EU regulation)
	function rcl(tnum) { return wptxt[8]+ilbc[tnum+ctcnt*1]; } //hover text for code (length)
	function rce(tnum) { return wptxt[9]+ilbc[tnum+ctcnt*5]; } //hover text for name (example)
	function rir(tnum) {  //note mark (IBAN requirement)
		if (ilbc[tnum+ctcnt*4] == "y") {
			return "<span title=\""+wptxt[10]+"\"><sup>*</sup></span>"; } 
		else {
			return ""; }}
	// list countries in x columns with language y ##### old, use new please #####
	function listcountries(columni,llngg) {
		listcountriesnew(columni,llngg,"n",""); }
	// list countries in x columns with language y show flag y/n from path ##### new #####
	// make sure the flag images are available at path and provide a style declaration like "img.tbg5cdf { height:20px; }" !!
	function listcountriesnew(columni,llngg,flag,ipath) {
		lngg = llngg; //use language parameter
		if ((lngg < 1) || (lngg > lngc.length)) {
			lngg = 2; }
		if (flag == "n") {
			document.write("<table class=\"tbg5ct\">\r\n<TR class=\"tbg5crt\"><TD class=\"tbg5cdt\" colspan=\""+columni*2+"\"> </TD></TR>\r\n"); }
		else {	document.write("<table class=\"tbg5ct\">\r\n<TR class=\"tbg5crt\"><TD class=\"tbg5cdt\" colspan=\""+columni*3+"\"> </TD></TR>\r\n"); }
		var numberofrows = Math.ceil(ctcnt/columni); //calculate rows
		for (i = 0; i < numberofrows; i++) { //for each row
			document.write("<tr class=\"tbg5crl\">");
			for (var j = 0; j < columni; j++) { //for each column
				if ((i*columni+j)<ctcnt) { //fill cells
					document.write("<td class=\"tbg5cdc\" title=\""+rcl(i*columni+j)+"\"><span class=\"tbg5era"+rcr(i*columni+j)+"\">"+ilbc[i*columni+j]+rir(i*columni+j)+"</span></td>"); //country code
					if (flag == "y") { //show flag
						document.write("<td class=\"tbg5cdf\"><img class=\"tbg5cdf\" src=\""+ipath+ilbc[i*columni+j]+".png\" alt=\""+ctynm[i*columni+j]+"\" title=\""+ctynm[i*columni+j]+"\"></td>"); }
					document.write("<td class=\"tbg5cdn\" title=\""+rce(i*columni+j)+"\"><span class=\"tbg5era"+rcr(i*columni+j)+"\">"+ctynm[i*columni+j]+"</span></td>"); }} //country name
			document.write("</tr>\r\n"); }
		document.write("</table>\r\n");
		if (document.dlang) {
			if (document.dlang.mlang) {
				for (var i = 1; i <= document.dlang.mlang.length; i++) {
					if (document.dlang.mlang[i-1].value==lngg) {
						document.dlang.mlang[i-1].selected=true; }}}}}
	// some convenience for additional explanations
	function doexplain(llngg) {
		document.write(xpltxt); }
	// load languagefile
	function llanguagefile(language) {
		document.write('<script charset="utf-8" src="'+language+'" type="text/JavaScript"><\/script>'); }
	function llanguage(language) {
		jsfile = 'http://www.tbg5-finance.org/'+lngc[language-1]+'.js';
		llanguagefile(jsfile); }
	// load helperfiles
	function helperjsfile(file) {
		document.write('<script src="'+file+'" type="text/JavaScript"><\/script>'); }
	function helpers() {
		jsfile = 'http://www.tbg5-finance.org/dragdrop.js';
		helperjsfile(jsfile);
		jsfile = 'http://www.tbg5-finance.org/customalert.js';
		helperjsfile(jsfile); }
	llanguage(lngg);
	helpers();


	// translators please take a source language including // language ++++... from laguage file e.g. en.js
	// translate and store in UTF-8 encoding to support your national characters
	// only care about text surrounded by quotes, i.e. leave all other characters and format as is 
	// send it
	//
	// please observe following special notation in translation of "web page" text
	//
//	                                    V
	// wptxt = wptxt.concat ( new Array ( 0 ,"text 1","text 2","text 3","text 4","text 5","text 6","text 7","text 8","text 9"));
	// results in
	// "("+" EU Regulation 924/2009 "+"text 3"+"/"+"text 4"+")"
	//
	// while
//	                                    V
	// wptxt = wptxt.concat ( new Array ( 1 ,"text 1","text 2","text 3","text 4","text 5","text 6","text 7","text 8","text 9"));
	// results in
	// "("+"text 3"+"/"+"text 4"+" EU Regulation 924/2009 "+")"
	//
	// i.e. it is a language adaptor. Please choose properly. Thx
	//
	// therefore
	// with 0 make it "text3","text 4 "
	// with 1 make it " text3","text 4"

	// script end
	*/
	
}
