package org.apache.james.mime4j.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.TreeSet;
import org.apache.james.mime4j.Log;
import org.apache.james.mime4j.LogFactory;

public class CharsetUtil
{
  public static final Charset ISO_8859_1;
  private static Charset[] JAVA_CHARSETS;
  public static final Charset US_ASCII;
  public static final Charset UTF_8;
  private static HashMap<String, Charset> charsetMap;
  private static TreeSet<String> decodingSupported;
  private static TreeSet<String> encodingSupported;
  private static Log log = LogFactory.getLog(CharsetUtil.class);

  static
  {
    Charset[] arrayOfCharset = new Charset[''];
    arrayOfCharset[0] = new Charset("ISO8859_1", "ISO-8859-1", new String[] { "ISO_8859-1:1987", "iso-ir-100", "ISO_8859-1", "latin1", "l1", "IBM819", "CP819", "csISOLatin1", "8859_1", "819", "IBM-819", "ISO8859-1", "ISO_8859_1" }, null);
    arrayOfCharset[1] = new Charset("ISO8859_2", "ISO-8859-2", new String[] { "ISO_8859-2:1987", "iso-ir-101", "ISO_8859-2", "latin2", "l2", "csISOLatin2", "8859_2", "iso8859_2" }, null);
    arrayOfCharset[2] = new Charset("ISO8859_3", "ISO-8859-3", new String[] { "ISO_8859-3:1988", "iso-ir-109", "ISO_8859-3", "latin3", "l3", "csISOLatin3", "8859_3" }, null);
    arrayOfCharset[3] = new Charset("ISO8859_4", "ISO-8859-4", new String[] { "ISO_8859-4:1988", "iso-ir-110", "ISO_8859-4", "latin4", "l4", "csISOLatin4", "8859_4" }, null);
    arrayOfCharset[4] = new Charset("ISO8859_5", "ISO-8859-5", new String[] { "ISO_8859-5:1988", "iso-ir-144", "ISO_8859-5", "cyrillic", "csISOLatinCyrillic", "8859_5" }, null);
    arrayOfCharset[5] = new Charset("ISO8859_6", "ISO-8859-6", new String[] { "ISO_8859-6:1987", "iso-ir-127", "ISO_8859-6", "ECMA-114", "ASMO-708", "arabic", "csISOLatinArabic", "8859_6" }, null);
    arrayOfCharset[6] = new Charset("ISO8859_7", "ISO-8859-7", new String[] { "ISO_8859-7:1987", "iso-ir-126", "ISO_8859-7", "ELOT_928", "ECMA-118", "greek", "greek8", "csISOLatinGreek", "8859_7", "sun_eu_greek" }, null);
    arrayOfCharset[7] = new Charset("ISO8859_8", "ISO-8859-8", new String[] { "ISO_8859-8:1988", "iso-ir-138", "ISO_8859-8", "hebrew", "csISOLatinHebrew", "8859_8" }, null);
    arrayOfCharset[8] = new Charset("ISO8859_9", "ISO-8859-9", new String[] { "ISO_8859-9:1989", "iso-ir-148", "ISO_8859-9", "latin5", "l5", "csISOLatin5", "8859_9" }, null);
    arrayOfCharset[9] = new Charset("ISO8859_13", "ISO-8859-13", new String[0], null);
    arrayOfCharset[10] = new Charset("ISO8859_15", "ISO-8859-15", new String[] { "ISO_8859-15", "Latin-9", "8859_15", "csISOlatin9", "IBM923", "cp923", "923", "L9", "IBM-923", "ISO8859-15", "LATIN9", "LATIN0", "csISOlatin0", "ISO8859_15_FDIS" }, null);
    arrayOfCharset[11] = new Charset("KOI8_R", "KOI8-R", new String[] { "csKOI8R", "koi8" }, null);
    arrayOfCharset[12] = new Charset("ASCII", "US-ASCII", new String[] { "ANSI_X3.4-1968", "iso-ir-6", "ANSI_X3.4-1986", "ISO_646.irv:1991", "ISO646-US", "us", "IBM367", "cp367", "csASCII", "ascii7", "646", "iso_646.irv:1983" }, null);
    arrayOfCharset[13] = new Charset("UTF8", "UTF-8", new String[0], null);
    arrayOfCharset[14] = new Charset("UTF-16", "UTF-16", new String[] { "UTF_16" }, null);
    arrayOfCharset[15] = new Charset("UnicodeBigUnmarked", "UTF-16BE", new String[] { "X-UTF-16BE", "UTF_16BE", "ISO-10646-UCS-2" }, null);
    arrayOfCharset[16] = new Charset("UnicodeLittleUnmarked", "UTF-16LE", new String[] { "UTF_16LE", "X-UTF-16LE" }, null);
    arrayOfCharset[17] = new Charset("Big5", "Big5", new String[] { "csBig5", "CN-Big5", "BIG-FIVE", "BIGFIVE" }, null);
    arrayOfCharset[18] = new Charset("Big5_HKSCS", "Big5-HKSCS", new String[] { "big5hkscs" }, null);
    arrayOfCharset[19] = new Charset("EUC_JP", "EUC-JP", new String[] { "csEUCPkdFmtJapanese", "Extended_UNIX_Code_Packed_Format_for_Japanese", "eucjis", "x-eucjp", "eucjp", "x-euc-jp" }, null);
    arrayOfCharset[20] = new Charset("EUC_KR", "EUC-KR", new String[] { "csEUCKR", "ksc5601", "5601", "ksc5601_1987", "ksc_5601", "ksc5601-1987", "ks_c_5601-1987", "euckr" }, null);
    arrayOfCharset[21] = new Charset("GB18030", "GB18030", new String[] { "gb18030-2000" }, null);
    arrayOfCharset[22] = new Charset("EUC_CN", "GB2312", new String[] { "x-EUC-CN", "csGB2312", "euccn", "euc-cn", "gb2312-80", "gb2312-1980", "CN-GB", "CN-GB-ISOIR165" }, null);
    arrayOfCharset[23] = new Charset("GBK", "windows-936", new String[] { "CP936", "MS936", "ms_936", "x-mswin-936" }, null);
    arrayOfCharset[24] = new Charset("Cp037", "IBM037", new String[] { "ebcdic-cp-us", "ebcdic-cp-ca", "ebcdic-cp-wt", "ebcdic-cp-nl", "csIBM037" }, null);
    arrayOfCharset[25] = new Charset("Cp273", "IBM273", new String[] { "csIBM273" }, null);
    arrayOfCharset[26] = new Charset("Cp277", "IBM277", new String[] { "EBCDIC-CP-DK", "EBCDIC-CP-NO", "csIBM277" }, null);
    arrayOfCharset[27] = new Charset("Cp278", "IBM278", new String[] { "CP278", "ebcdic-cp-fi", "ebcdic-cp-se", "csIBM278" }, null);
    arrayOfCharset[28] = new Charset("Cp280", "IBM280", new String[] { "ebcdic-cp-it", "csIBM280" }, null);
    arrayOfCharset[29] = new Charset("Cp284", "IBM284", new String[] { "ebcdic-cp-es", "csIBM284" }, null);
    arrayOfCharset[30] = new Charset("Cp285", "IBM285", new String[] { "ebcdic-cp-gb", "csIBM285" }, null);
    arrayOfCharset[31] = new Charset("Cp297", "IBM297", new String[] { "ebcdic-cp-fr", "csIBM297" }, null);
    arrayOfCharset[32] = new Charset("Cp420", "IBM420", new String[] { "ebcdic-cp-ar1", "csIBM420" }, null);
    arrayOfCharset[33] = new Charset("Cp424", "IBM424", new String[] { "ebcdic-cp-he", "csIBM424" }, null);
    arrayOfCharset[34] = new Charset("Cp437", "IBM437", new String[] { "437", "csPC8CodePage437" }, null);
    arrayOfCharset[35] = new Charset("Cp500", "IBM500", new String[] { "ebcdic-cp-be", "ebcdic-cp-ch", "csIBM500" }, null);
    arrayOfCharset[36] = new Charset("Cp775", "IBM775", new String[] { "csPC775Baltic" }, null);
    arrayOfCharset[37] = new Charset("Cp838", "IBM-Thai", new String[0], null);
    arrayOfCharset[38] = new Charset("Cp850", "IBM850", new String[] { "850", "csPC850Multilingual" }, null);
    arrayOfCharset[39] = new Charset("Cp852", "IBM852", new String[] { "852", "csPCp852" }, null);
    arrayOfCharset[40] = new Charset("Cp855", "IBM855", new String[] { "855", "csIBM855" }, null);
    arrayOfCharset[41] = new Charset("Cp857", "IBM857", new String[] { "857", "csIBM857" }, null);
    arrayOfCharset[42] = new Charset("Cp858", "IBM00858", new String[] { "CCSID00858", "CP00858", "PC-Multilingual-850+euro" }, null);
    arrayOfCharset[43] = new Charset("Cp860", "IBM860", new String[] { "860", "csIBM860" }, null);
    arrayOfCharset[44] = new Charset("Cp861", "IBM861", new String[] { "861", "cp-is", "csIBM861" }, null);
    arrayOfCharset[45] = new Charset("Cp862", "IBM862", new String[] { "862", "csPC862LatinHebrew" }, null);
    arrayOfCharset[46] = new Charset("Cp863", "IBM863", new String[] { "863", "csIBM863" }, null);
    arrayOfCharset[47] = new Charset("Cp864", "IBM864", new String[] { "cp864", "csIBM864" }, null);
    arrayOfCharset[48] = new Charset("Cp865", "IBM865", new String[] { "865", "csIBM865" }, null);
    arrayOfCharset[49] = new Charset("Cp866", "IBM866", new String[] { "866", "csIBM866" }, null);
    arrayOfCharset[50] = new Charset("Cp868", "IBM868", new String[] { "cp-ar", "csIBM868" }, null);
    arrayOfCharset[51] = new Charset("Cp869", "IBM869", new String[] { "cp-gr", "csIBM869" }, null);
    arrayOfCharset[52] = new Charset("Cp870", "IBM870", new String[] { "ebcdic-cp-roece", "ebcdic-cp-yu", "csIBM870" }, null);
    arrayOfCharset[53] = new Charset("Cp871", "IBM871", new String[] { "ebcdic-cp-is", "csIBM871" }, null);
    arrayOfCharset[54] = new Charset("Cp918", "IBM918", new String[] { "ebcdic-cp-ar2", "csIBM918" }, null);
    arrayOfCharset[55] = new Charset("Cp1026", "IBM1026", new String[] { "csIBM1026" }, null);
    arrayOfCharset[56] = new Charset("Cp1047", "IBM1047", new String[] { "IBM-1047" }, null);
    arrayOfCharset[57] = new Charset("Cp1140", "IBM01140", new String[] { "CCSID01140", "CP01140", "ebcdic-us-37+euro" }, null);
    arrayOfCharset[58] = new Charset("Cp1141", "IBM01141", new String[] { "CCSID01141", "CP01141", "ebcdic-de-273+euro" }, null);
    arrayOfCharset[59] = new Charset("Cp1142", "IBM01142", new String[] { "CCSID01142", "CP01142", "ebcdic-dk-277+euro", "ebcdic-no-277+euro" }, null);
    arrayOfCharset[60] = new Charset("Cp1143", "IBM01143", new String[] { "CCSID01143", "CP01143", "ebcdic-fi-278+euro", "ebcdic-se-278+euro" }, null);
    arrayOfCharset[61] = new Charset("Cp1144", "IBM01144", new String[] { "CCSID01144", "CP01144", "ebcdic-it-280+euro" }, null);
    arrayOfCharset[62] = new Charset("Cp1145", "IBM01145", new String[] { "CCSID01145", "CP01145", "ebcdic-es-284+euro" }, null);
    arrayOfCharset[63] = new Charset("Cp1146", "IBM01146", new String[] { "CCSID01146", "CP01146", "ebcdic-gb-285+euro" }, null);
    arrayOfCharset[64] = new Charset("Cp1147", "IBM01147", new String[] { "CCSID01147", "CP01147", "ebcdic-fr-297+euro" }, null);
    arrayOfCharset[65] = new Charset("Cp1148", "IBM01148", new String[] { "CCSID01148", "CP01148", "ebcdic-international-500+euro" }, null);
    arrayOfCharset[66] = new Charset("Cp1149", "IBM01149", new String[] { "CCSID01149", "CP01149", "ebcdic-is-871+euro" }, null);
    arrayOfCharset[67] = new Charset("Cp1250", "windows-1250", new String[0], null);
    arrayOfCharset[68] = new Charset("Cp1251", "windows-1251", new String[0], null);
    arrayOfCharset[69] = new Charset("Cp1252", "windows-1252", new String[0], null);
    arrayOfCharset[70] = new Charset("Cp1253", "windows-1253", new String[0], null);
    arrayOfCharset[71] = new Charset("Cp1254", "windows-1254", new String[0], null);
    arrayOfCharset[72] = new Charset("Cp1255", "windows-1255", new String[0], null);
    arrayOfCharset[73] = new Charset("Cp1256", "windows-1256", new String[0], null);
    arrayOfCharset[74] = new Charset("Cp1257", "windows-1257", new String[0], null);
    arrayOfCharset[75] = new Charset("Cp1258", "windows-1258", new String[0], null);
    arrayOfCharset[76] = new Charset("ISO2022CN", "ISO-2022-CN", new String[0], null);
    arrayOfCharset[77] = new Charset("ISO2022JP", "ISO-2022-JP", new String[] { "csISO2022JP", "JIS", "jis_encoding", "csjisencoding" }, null);
    arrayOfCharset[78] = new Charset("ISO2022KR", "ISO-2022-KR", new String[] { "csISO2022KR" }, null);
    arrayOfCharset[79] = new Charset("JIS_X0201", "JIS_X0201", new String[] { "X0201", "JIS0201", "csHalfWidthKatakana" }, null);
    arrayOfCharset[80] = new Charset("JIS_X0212-1990", "JIS_X0212-1990", new String[] { "iso-ir-159", "x0212", "JIS0212", "csISO159JISX02121990" }, null);
    arrayOfCharset[81] = new Charset("JIS_C6626-1983", "JIS_C6626-1983", new String[] { "x-JIS0208", "JIS0208", "csISO87JISX0208", "x0208", "JIS_X0208-1983", "iso-ir-87" }, null);
    arrayOfCharset[82] = new Charset("SJIS", "Shift_JIS", new String[] { "MS_Kanji", "csShiftJIS", "shift-jis", "x-sjis", "pck" }, null);
    arrayOfCharset[83] = new Charset("TIS620", "TIS-620", new String[0], null);
    arrayOfCharset[84] = new Charset("MS932", "Windows-31J", new String[] { "windows-932", "csWindows31J", "x-ms-cp932" }, null);
    arrayOfCharset[85] = new Charset("EUC_TW", "EUC-TW", new String[] { "x-EUC-TW", "cns11643", "euctw" }, null);
    arrayOfCharset[86] = new Charset("x-Johab", "johab", new String[] { "johab", "cp1361", "ms1361", "ksc5601-1992", "ksc5601_1992" }, null);
    arrayOfCharset[87] = new Charset("MS950_HKSCS", "", new String[0], null);
    arrayOfCharset[88] = new Charset("MS874", "windows-874", new String[] { "cp874" }, null);
    arrayOfCharset[89] = new Charset("MS949", "windows-949", new String[] { "windows949", "ms_949", "x-windows-949" }, null);
    arrayOfCharset[90] = new Charset("MS950", "windows-950", new String[] { "x-windows-950" }, null);
    arrayOfCharset[91] = new Charset("Cp737", null, new String[0], null);
    arrayOfCharset[92] = new Charset("Cp856", null, new String[0], null);
    arrayOfCharset[93] = new Charset("Cp875", null, new String[0], null);
    arrayOfCharset[94] = new Charset("Cp921", null, new String[0], null);
    arrayOfCharset[95] = new Charset("Cp922", null, new String[0], null);
    arrayOfCharset[96] = new Charset("Cp930", null, new String[0], null);
    arrayOfCharset[97] = new Charset("Cp933", null, new String[0], null);
    arrayOfCharset[98] = new Charset("Cp935", null, new String[0], null);
    arrayOfCharset[99] = new Charset("Cp937", null, new String[0], null);
    arrayOfCharset[100] = new Charset("Cp939", null, new String[0], null);
    arrayOfCharset[101] = new Charset("Cp942", null, new String[0], null);
    arrayOfCharset[102] = new Charset("Cp942C", null, new String[0], null);
    arrayOfCharset[103] = new Charset("Cp943", null, new String[0], null);
    arrayOfCharset[104] = new Charset("Cp943C", null, new String[0], null);
    arrayOfCharset[105] = new Charset("Cp948", null, new String[0], null);
    arrayOfCharset[106] = new Charset("Cp949", null, new String[0], null);
    arrayOfCharset[107] = new Charset("Cp949C", null, new String[0], null);
    arrayOfCharset[108] = new Charset("Cp950", null, new String[0], null);
    arrayOfCharset[109] = new Charset("Cp964", null, new String[0], null);
    arrayOfCharset[110] = new Charset("Cp970", null, new String[0], null);
    arrayOfCharset[111] = new Charset("Cp1006", null, new String[0], null);
    arrayOfCharset[112] = new Charset("Cp1025", null, new String[0], null);
    arrayOfCharset[113] = new Charset("Cp1046", null, new String[0], null);
    arrayOfCharset[114] = new Charset("Cp1097", null, new String[0], null);
    arrayOfCharset[115] = new Charset("Cp1098", null, new String[0], null);
    arrayOfCharset[116] = new Charset("Cp1112", null, new String[0], null);
    arrayOfCharset[117] = new Charset("Cp1122", null, new String[0], null);
    arrayOfCharset[118] = new Charset("Cp1123", null, new String[0], null);
    arrayOfCharset[119] = new Charset("Cp1124", null, new String[0], null);
    arrayOfCharset[120] = new Charset("Cp1381", null, new String[0], null);
    arrayOfCharset[121] = new Charset("Cp1383", null, new String[0], null);
    arrayOfCharset[122] = new Charset("Cp33722", null, new String[0], null);
    arrayOfCharset[123] = new Charset("Big5_Solaris", null, new String[0], null);
    arrayOfCharset[124] = new Charset("EUC_JP_LINUX", null, new String[0], null);
    arrayOfCharset[125] = new Charset("EUC_JP_Solaris", null, new String[0], null);
    arrayOfCharset[126] = new Charset("ISCII91", null, new String[] { "x-ISCII91", "iscii" }, null);
    arrayOfCharset[127] = new Charset("ISO2022_CN_CNS", null, new String[0], null);
    arrayOfCharset[''] = new Charset("ISO2022_CN_GB", null, new String[0], null);
    arrayOfCharset[''] = new Charset("x-iso-8859-11", null, new String[0], null);
    arrayOfCharset[''] = new Charset("JISAutoDetect", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacArabic", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacCentralEurope", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacCroatian", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacCyrillic", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacDingbat", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacGreek", "MacGreek", new String[0], null);
    arrayOfCharset[''] = new Charset("MacHebrew", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacIceland", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacRoman", "MacRoman", new String[] { "Macintosh", "MAC", "csMacintosh" }, null);
    arrayOfCharset[''] = new Charset("MacRomania", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacSymbol", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacThai", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacTurkish", null, new String[0], null);
    arrayOfCharset[''] = new Charset("MacUkraine", null, new String[0], null);
    arrayOfCharset[''] = new Charset("UnicodeBig", null, new String[0], null);
    arrayOfCharset[''] = new Charset("UnicodeLittle", null, new String[0], null);
    JAVA_CHARSETS = arrayOfCharset;
    decodingSupported = null;
    encodingSupported = null;
    charsetMap = null;
    decodingSupported = new TreeSet();
    encodingSupported = new TreeSet();
    byte[] arrayOfByte = { 100, 117, 109, 109, 121 };
    int i = 0;
    while (true)
    {
      if (i < JAVA_CHARSETS.length);
      try
      {
        new String(arrayOfByte, JAVA_CHARSETS[i].canonical);
        decodingSupported.add(JAVA_CHARSETS[i].canonical.toLowerCase());
        try
        {
          label4917: "dummy".getBytes(JAVA_CHARSETS[i].canonical);
          encodingSupported.add(JAVA_CHARSETS[i].canonical.toLowerCase());
          label4950: i++;
          continue;
          charsetMap = new HashMap();
          for (int j = 0; j < JAVA_CHARSETS.length; j++)
          {
            Charset localCharset = JAVA_CHARSETS[j];
            charsetMap.put(localCharset.canonical.toLowerCase(), localCharset);
            if (localCharset.mime != null)
              charsetMap.put(localCharset.mime.toLowerCase(), localCharset);
            if (localCharset.aliases != null)
              for (int k = 0; k < localCharset.aliases.length; k++)
                charsetMap.put(localCharset.aliases[k].toLowerCase(), localCharset);
          }
          if (log.isDebugEnabled())
          {
            log.debug("Character sets which support decoding: " + decodingSupported);
            log.debug("Character sets which support encoding: " + encodingSupported);
          }
          US_ASCII = Charset.forName("US-ASCII");
          ISO_8859_1 = Charset.forName("ISO-8859-1");
          UTF_8 = Charset.forName("UTF-8");
          return;
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException1)
        {
          break label4950;
        }
        catch (UnsupportedOperationException localUnsupportedOperationException2)
        {
          break label4950;
        }
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException2)
      {
        break label4917;
      }
      catch (UnsupportedOperationException localUnsupportedOperationException1)
      {
        break label4917;
      }
    }
  }

  public static boolean isDecodingSupported(String paramString)
  {
    return decodingSupported.contains(paramString.toLowerCase());
  }

  public static boolean isWhitespace(char paramChar)
  {
    return (paramChar == ' ') || (paramChar == '\t') || (paramChar == '\r') || (paramChar == '\n');
  }

  public static boolean isWhitespace(String paramString)
  {
    if (paramString == null)
      throw new IllegalArgumentException("String may not be null");
    int i = paramString.length();
    for (int j = 0; j < i; j++)
      if (!isWhitespace(paramString.charAt(j)))
        return false;
    return true;
  }

  public static String toJavaCharset(String paramString)
  {
    Charset localCharset = (Charset)charsetMap.get(paramString.toLowerCase());
    if (localCharset != null)
      return localCharset.canonical;
    return null;
  }

  private static class Charset
    implements Comparable<Charset>
  {
    private String[] aliases = null;
    private String canonical = null;
    private String mime = null;

    private Charset(String paramString1, String paramString2, String[] paramArrayOfString)
    {
      this.canonical = paramString1;
      this.mime = paramString2;
      this.aliases = paramArrayOfString;
    }

    public int compareTo(Charset paramCharset)
    {
      return this.canonical.compareTo(paramCharset.canonical);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     org.apache.james.mime4j.util.CharsetUtil
 * JD-Core Version:    0.6.2
 */