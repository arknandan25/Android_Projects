/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package chilkatsoft;

public class CkString {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CkString(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CkString obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        chilkatJNI.delete_CkString(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CkString() {
    this(chilkatJNI.new_CkString(), true);
  }

  public boolean loadFile(String fileName, String charset) {
    return chilkatJNI.CkString_loadFile(swigCPtr, this, fileName, charset);
  }

  public char charAt(int idx) {
    return chilkatJNI.CkString_charAt(swigCPtr, this, idx);
  }

  public int intValue() {
    return chilkatJNI.CkString_intValue(swigCPtr, this);
  }

  public double doubleValue() {
    return chilkatJNI.CkString_doubleValue(swigCPtr, this);
  }

  public int countCharOccurances(char ch) {
    return chilkatJNI.CkString_countCharOccurances(swigCPtr, this, ch);
  }

  public void appendCurrentDateRfc822() {
    chilkatJNI.CkString_appendCurrentDateRfc822(swigCPtr, this);
  }

  public void removeDelimited(String beginDelim, String endDelim, boolean caseSensitive) {
    chilkatJNI.CkString_removeDelimited(swigCPtr, this, beginDelim, endDelim, caseSensitive);
  }

  public void setStr(CkString s) {
    chilkatJNI.CkString_setStr(swigCPtr, this, CkString.getCPtr(s), s);
  }

  public boolean endsWith(String s) {
    return chilkatJNI.CkString_endsWith(swigCPtr, this, s);
  }

  public boolean endsWithStr(CkString s) {
    return chilkatJNI.CkString_endsWithStr(swigCPtr, this, CkString.getCPtr(s), s);
  }

  public boolean beginsWithStr(CkString s) {
    return chilkatJNI.CkString_beginsWithStr(swigCPtr, this, CkString.getCPtr(s), s);
  }

  public int indexOf(String s) {
    return chilkatJNI.CkString_indexOf(swigCPtr, this, s);
  }

  public int indexOfStr(CkString s) {
    return chilkatJNI.CkString_indexOfStr(swigCPtr, this, CkString.getCPtr(s), s);
  }

  public int replaceAll(CkString str, CkString replacement) {
    return chilkatJNI.CkString_replaceAll(swigCPtr, this, CkString.getCPtr(str), str, CkString.getCPtr(replacement), replacement);
  }

  public boolean replaceFirst(CkString str, CkString replacement) {
    return chilkatJNI.CkString_replaceFirst(swigCPtr, this, CkString.getCPtr(str), str, CkString.getCPtr(replacement), replacement);
  }

  public CkString substring(int startCharIdx, int numChars) {
    long cPtr = chilkatJNI.CkString_substring(swigCPtr, this, startCharIdx, numChars);
    return (cPtr == 0) ? null : new CkString(cPtr, false);
  }

  public boolean matchesStr(CkString str) {
    return chilkatJNI.CkString_matchesStr(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public boolean matches(String s) {
    return chilkatJNI.CkString_matches(swigCPtr, this, s);
  }

  public CkString getChar(int idx) {
    long cPtr = chilkatJNI.CkString_getChar(swigCPtr, this, idx);
    return (cPtr == 0) ? null : new CkString(cPtr, false);
  }

  public int removeAll(CkString str) {
    return chilkatJNI.CkString_removeAll(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public boolean removeFirst(CkString str) {
    return chilkatJNI.CkString_removeFirst(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void chopAtStr(CkString str) {
    chilkatJNI.CkString_chopAtStr(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void urlDecode(String charset) {
    chilkatJNI.CkString_urlDecode(swigCPtr, this, charset);
  }

  public void urlEncode(String charset) {
    chilkatJNI.CkString_urlEncode(swigCPtr, this, charset);
  }

  public void base64Decode(String charset) {
    chilkatJNI.CkString_base64Decode(swigCPtr, this, charset);
  }

  public void base64Encode(String charset) {
    chilkatJNI.CkString_base64Encode(swigCPtr, this, charset);
  }

  public void qpDecode(String charset) {
    chilkatJNI.CkString_qpDecode(swigCPtr, this, charset);
  }

  public void qpEncode(String charset) {
    chilkatJNI.CkString_qpEncode(swigCPtr, this, charset);
  }

  public void hexDecode(String charset) {
    chilkatJNI.CkString_hexDecode(swigCPtr, this, charset);
  }

  public void hexEncode(String charset) {
    chilkatJNI.CkString_hexEncode(swigCPtr, this, charset);
  }

  public void entityDecode() {
    chilkatJNI.CkString_entityDecode(swigCPtr, this);
  }

  public void entityEncode() {
    chilkatJNI.CkString_entityEncode(swigCPtr, this);
  }

  public void appendUtf8(String s) {
    chilkatJNI.CkString_appendUtf8(swigCPtr, this, s);
  }

  public void appendAnsi(String s) {
    chilkatJNI.CkString_appendAnsi(swigCPtr, this, s);
  }

  public void clear() {
    chilkatJNI.CkString_clear(swigCPtr, this);
  }

  public void prepend(String s) {
    chilkatJNI.CkString_prepend(swigCPtr, this, s);
  }

  public void appendInt(int n) {
    chilkatJNI.CkString_appendInt(swigCPtr, this, n);
  }

  public void append(String s) {
    chilkatJNI.CkString_append(swigCPtr, this, s);
  }

  public void appendChar(char c) {
    chilkatJNI.CkString_appendChar(swigCPtr, this, c);
  }

  public void appendN(String s, int numBytes) {
    chilkatJNI.CkString_appendN(swigCPtr, this, s, numBytes);
  }

  public void appendStr(CkString str) {
    chilkatJNI.CkString_appendStr(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void appendEnc(String s, String encoding) {
    chilkatJNI.CkString_appendEnc(swigCPtr, this, s, encoding);
  }

  public String getEnc(String encoding) {
    return chilkatJNI.CkString_getEnc(swigCPtr, this, encoding);
  }

  public void setString(String s) {
    chilkatJNI.CkString_setString(swigCPtr, this, s);
  }

  public void setStringAnsi(String s) {
    chilkatJNI.CkString_setStringAnsi(swigCPtr, this, s);
  }

  public void setStringUtf8(String s) {
    chilkatJNI.CkString_setStringUtf8(swigCPtr, this, s);
  }

  public String getAnsi() {
    return chilkatJNI.CkString_getAnsi(swigCPtr, this);
  }

  public String getUtf8() {
    return chilkatJNI.CkString_getUtf8(swigCPtr, this);
  }

  public int compareStr(CkString str) {
    return chilkatJNI.CkString_compareStr(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String getString() {
    return chilkatJNI.CkString_getString(swigCPtr, this);
  }

  public int getSizeUtf8() {
    return chilkatJNI.CkString_getSizeUtf8(swigCPtr, this);
  }

  public int getSizeAnsi() {
    return chilkatJNI.CkString_getSizeAnsi(swigCPtr, this);
  }

  public int getNumChars() {
    return chilkatJNI.CkString_getNumChars(swigCPtr, this);
  }

  public void trim() {
    chilkatJNI.CkString_trim(swigCPtr, this);
  }

  public void trim2() {
    chilkatJNI.CkString_trim2(swigCPtr, this);
  }

  public void trimInsideSpaces() {
    chilkatJNI.CkString_trimInsideSpaces(swigCPtr, this);
  }

  public int replaceAllOccurances(String pattern, String replacement) {
    return chilkatJNI.CkString_replaceAllOccurances(swigCPtr, this, pattern, replacement);
  }

  public boolean replaceFirstOccurance(String pattern, String replacement) {
    return chilkatJNI.CkString_replaceFirstOccurance(swigCPtr, this, pattern, replacement);
  }

  public void toCRLF() {
    chilkatJNI.CkString_toCRLF(swigCPtr, this);
  }

  public void toLF() {
    chilkatJNI.CkString_toLF(swigCPtr, this);
  }

  public void eliminateChar(char ansiChar, int startIndex) {
    chilkatJNI.CkString_eliminateChar(swigCPtr, this, ansiChar, startIndex);
  }

  public char lastChar() {
    return chilkatJNI.CkString_lastChar(swigCPtr, this);
  }

  public void shorten(int n) {
    chilkatJNI.CkString_shorten(swigCPtr, this, n);
  }

  public void toLowerCase() {
    chilkatJNI.CkString_toLowerCase(swigCPtr, this);
  }

  public void toUpperCase() {
    chilkatJNI.CkString_toUpperCase(swigCPtr, this);
  }

  public void encodeXMLSpecial() {
    chilkatJNI.CkString_encodeXMLSpecial(swigCPtr, this);
  }

  public void decodeXMLSpecial() {
    chilkatJNI.CkString_decodeXMLSpecial(swigCPtr, this);
  }

  public boolean containsSubstring(String pattern) {
    return chilkatJNI.CkString_containsSubstring(swigCPtr, this, pattern);
  }

  public boolean containsSubstringNoCase(String pattern) {
    return chilkatJNI.CkString_containsSubstringNoCase(swigCPtr, this, pattern);
  }

  public boolean equals(String s) {
    return chilkatJNI.CkString_equals(swigCPtr, this, s);
  }

  public boolean equalsStr(CkString s) {
    return chilkatJNI.CkString_equalsStr(swigCPtr, this, CkString.getCPtr(s), s);
  }

  public boolean equalsIgnoreCase(String s) {
    return chilkatJNI.CkString_equalsIgnoreCase(swigCPtr, this, s);
  }

  public boolean equalsIgnoreCaseStr(CkString s) {
    return chilkatJNI.CkString_equalsIgnoreCaseStr(swigCPtr, this, CkString.getCPtr(s), s);
  }

  public void removeChunk(int charStartPos, int numChars) {
    chilkatJNI.CkString_removeChunk(swigCPtr, this, charStartPos, numChars);
  }

  public void removeCharOccurances(char c) {
    chilkatJNI.CkString_removeCharOccurances(swigCPtr, this, c);
  }

  public void replaceChar(char c1, char c2) {
    chilkatJNI.CkString_replaceChar(swigCPtr, this, c1, c2);
  }

  public void chopAtFirstChar(char c1) {
    chilkatJNI.CkString_chopAtFirstChar(swigCPtr, this, c1);
  }

  public boolean saveToFile(String filename, String charset) {
    return chilkatJNI.CkString_saveToFile(swigCPtr, this, filename, charset);
  }

  public CkStringArray split(char splitChar, boolean exceptDoubleQuoted, boolean exceptEscaped, boolean keepEmpty) {
    long cPtr = chilkatJNI.CkString_split(swigCPtr, this, splitChar, exceptDoubleQuoted, exceptEscaped, keepEmpty);
    return (cPtr == 0) ? null : new CkStringArray(cPtr, true);
  }

  public CkStringArray split2(String splitCharSet, boolean exceptDoubleQuoted, boolean exceptEscaped, boolean keepEmpty) {
    long cPtr = chilkatJNI.CkString_split2(swigCPtr, this, splitCharSet, exceptDoubleQuoted, exceptEscaped, keepEmpty);
    return (cPtr == 0) ? null : new CkStringArray(cPtr, true);
  }

  public CkStringArray tokenize(String punctuation) {
    long cPtr = chilkatJNI.CkString_tokenize(swigCPtr, this, punctuation);
    return (cPtr == 0) ? null : new CkStringArray(cPtr, true);
  }

  public CkStringArray splitAtWS() {
    long cPtr = chilkatJNI.CkString_splitAtWS(swigCPtr, this);
    return (cPtr == 0) ? null : new CkStringArray(cPtr, true);
  }

  public boolean beginsWith(String sSubstr) {
    return chilkatJNI.CkString_beginsWith(swigCPtr, this, sSubstr);
  }

}
