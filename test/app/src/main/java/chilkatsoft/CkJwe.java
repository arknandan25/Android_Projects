/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package chilkatsoft;

public class CkJwe {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CkJwe(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CkJwe obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        chilkatJNI.delete_CkJwe(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CkJwe() {
    this(chilkatJNI.new_CkJwe(), true);
  }

  public void LastErrorXml(CkString str) {
    chilkatJNI.CkJwe_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void LastErrorHtml(CkString str) {
    chilkatJNI.CkJwe_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void LastErrorText(CkString str) {
    chilkatJNI.CkJwe_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void get_DebugLogFilePath(CkString str) {
    chilkatJNI.CkJwe_get_DebugLogFilePath(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String debugLogFilePath() {
    return chilkatJNI.CkJwe_debugLogFilePath(swigCPtr, this);
  }

  public void put_DebugLogFilePath(String newVal) {
    chilkatJNI.CkJwe_put_DebugLogFilePath(swigCPtr, this, newVal);
  }

  public void get_LastErrorHtml(CkString str) {
    chilkatJNI.CkJwe_get_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String lastErrorHtml() {
    return chilkatJNI.CkJwe_lastErrorHtml(swigCPtr, this);
  }

  public void get_LastErrorText(CkString str) {
    chilkatJNI.CkJwe_get_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String lastErrorText() {
    return chilkatJNI.CkJwe_lastErrorText(swigCPtr, this);
  }

  public void get_LastErrorXml(CkString str) {
    chilkatJNI.CkJwe_get_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String lastErrorXml() {
    return chilkatJNI.CkJwe_lastErrorXml(swigCPtr, this);
  }

  public boolean get_LastMethodSuccess() {
    return chilkatJNI.CkJwe_get_LastMethodSuccess(swigCPtr, this);
  }

  public void put_LastMethodSuccess(boolean newVal) {
    chilkatJNI.CkJwe_put_LastMethodSuccess(swigCPtr, this, newVal);
  }

  public int get_NumRecipients() {
    return chilkatJNI.CkJwe_get_NumRecipients(swigCPtr, this);
  }

  public boolean get_PreferCompact() {
    return chilkatJNI.CkJwe_get_PreferCompact(swigCPtr, this);
  }

  public void put_PreferCompact(boolean newVal) {
    chilkatJNI.CkJwe_put_PreferCompact(swigCPtr, this, newVal);
  }

  public boolean get_PreferFlattened() {
    return chilkatJNI.CkJwe_get_PreferFlattened(swigCPtr, this);
  }

  public void put_PreferFlattened(boolean newVal) {
    chilkatJNI.CkJwe_put_PreferFlattened(swigCPtr, this, newVal);
  }

  public boolean get_VerboseLogging() {
    return chilkatJNI.CkJwe_get_VerboseLogging(swigCPtr, this);
  }

  public void put_VerboseLogging(boolean newVal) {
    chilkatJNI.CkJwe_put_VerboseLogging(swigCPtr, this, newVal);
  }

  public void get_Version(CkString str) {
    chilkatJNI.CkJwe_get_Version(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String version() {
    return chilkatJNI.CkJwe_version(swigCPtr, this);
  }

  public boolean Decrypt(int index, String charset, CkString outStr) {
    return chilkatJNI.CkJwe_Decrypt(swigCPtr, this, index, charset, CkString.getCPtr(outStr), outStr);
  }

  public String decrypt(int index, String charset) {
    return chilkatJNI.CkJwe_decrypt(swigCPtr, this, index, charset);
  }

  public boolean DecryptBd(int index, CkBinData bd) {
    return chilkatJNI.CkJwe_DecryptBd(swigCPtr, this, index, CkBinData.getCPtr(bd), bd);
  }

  public boolean DecryptSb(int index, String charset, CkStringBuilder contentSb) {
    return chilkatJNI.CkJwe_DecryptSb(swigCPtr, this, index, charset, CkStringBuilder.getCPtr(contentSb), contentSb);
  }

  public boolean Encrypt(String content, String charset, CkString outStr) {
    return chilkatJNI.CkJwe_Encrypt(swigCPtr, this, content, charset, CkString.getCPtr(outStr), outStr);
  }

  public String encrypt(String content, String charset) {
    return chilkatJNI.CkJwe_encrypt(swigCPtr, this, content, charset);
  }

  public boolean EncryptBd(CkBinData contentBd, CkStringBuilder jweSb) {
    return chilkatJNI.CkJwe_EncryptBd(swigCPtr, this, CkBinData.getCPtr(contentBd), contentBd, CkStringBuilder.getCPtr(jweSb), jweSb);
  }

  public boolean EncryptSb(CkStringBuilder contentSb, String charset, CkStringBuilder jweSb) {
    return chilkatJNI.CkJwe_EncryptSb(swigCPtr, this, CkStringBuilder.getCPtr(contentSb), contentSb, charset, CkStringBuilder.getCPtr(jweSb), jweSb);
  }

  public int FindRecipient(String paramName, String paramValue, boolean caseSensitive) {
    return chilkatJNI.CkJwe_FindRecipient(swigCPtr, this, paramName, paramValue, caseSensitive);
  }

  public boolean LoadJwe(String jwe) {
    return chilkatJNI.CkJwe_LoadJwe(swigCPtr, this, jwe);
  }

  public boolean LoadJweSb(CkStringBuilder sb) {
    return chilkatJNI.CkJwe_LoadJweSb(swigCPtr, this, CkStringBuilder.getCPtr(sb), sb);
  }

  public boolean SaveLastError(String path) {
    return chilkatJNI.CkJwe_SaveLastError(swigCPtr, this, path);
  }

  public boolean SetAad(String aad, String charset) {
    return chilkatJNI.CkJwe_SetAad(swigCPtr, this, aad, charset);
  }

  public boolean SetAadBd(CkBinData aad) {
    return chilkatJNI.CkJwe_SetAadBd(swigCPtr, this, CkBinData.getCPtr(aad), aad);
  }

  public boolean SetPassword(int index, String password) {
    return chilkatJNI.CkJwe_SetPassword(swigCPtr, this, index, password);
  }

  public boolean SetPrivateKey(int index, CkPrivateKey privKey) {
    return chilkatJNI.CkJwe_SetPrivateKey(swigCPtr, this, index, CkPrivateKey.getCPtr(privKey), privKey);
  }

  public boolean SetProtectedHeader(CkJsonObject json) {
    return chilkatJNI.CkJwe_SetProtectedHeader(swigCPtr, this, CkJsonObject.getCPtr(json), json);
  }

  public boolean SetPublicKey(int index, CkPublicKey pubKey) {
    return chilkatJNI.CkJwe_SetPublicKey(swigCPtr, this, index, CkPublicKey.getCPtr(pubKey), pubKey);
  }

  public boolean SetRecipientHeader(int index, CkJsonObject json) {
    return chilkatJNI.CkJwe_SetRecipientHeader(swigCPtr, this, index, CkJsonObject.getCPtr(json), json);
  }

  public boolean SetUnprotectedHeader(CkJsonObject json) {
    return chilkatJNI.CkJwe_SetUnprotectedHeader(swigCPtr, this, CkJsonObject.getCPtr(json), json);
  }

  public boolean SetWrappingKey(int index, String encodedKey, String encoding) {
    return chilkatJNI.CkJwe_SetWrappingKey(swigCPtr, this, index, encodedKey, encoding);
  }

}
