/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package chilkatsoft;

public class CkBounce {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CkBounce(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CkBounce obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        chilkatJNI.delete_CkBounce(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CkBounce() {
    this(chilkatJNI.new_CkBounce(), true);
  }

  public void LastErrorXml(CkString str) {
    chilkatJNI.CkBounce_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void LastErrorHtml(CkString str) {
    chilkatJNI.CkBounce_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void LastErrorText(CkString str) {
    chilkatJNI.CkBounce_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void get_BounceAddress(CkString str) {
    chilkatJNI.CkBounce_get_BounceAddress(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String bounceAddress() {
    return chilkatJNI.CkBounce_bounceAddress(swigCPtr, this);
  }

  public void get_BounceData(CkString str) {
    chilkatJNI.CkBounce_get_BounceData(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String bounceData() {
    return chilkatJNI.CkBounce_bounceData(swigCPtr, this);
  }

  public int get_BounceType() {
    return chilkatJNI.CkBounce_get_BounceType(swigCPtr, this);
  }

  public void get_DebugLogFilePath(CkString str) {
    chilkatJNI.CkBounce_get_DebugLogFilePath(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String debugLogFilePath() {
    return chilkatJNI.CkBounce_debugLogFilePath(swigCPtr, this);
  }

  public void put_DebugLogFilePath(String newVal) {
    chilkatJNI.CkBounce_put_DebugLogFilePath(swigCPtr, this, newVal);
  }

  public void get_LastErrorHtml(CkString str) {
    chilkatJNI.CkBounce_get_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String lastErrorHtml() {
    return chilkatJNI.CkBounce_lastErrorHtml(swigCPtr, this);
  }

  public void get_LastErrorText(CkString str) {
    chilkatJNI.CkBounce_get_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String lastErrorText() {
    return chilkatJNI.CkBounce_lastErrorText(swigCPtr, this);
  }

  public void get_LastErrorXml(CkString str) {
    chilkatJNI.CkBounce_get_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String lastErrorXml() {
    return chilkatJNI.CkBounce_lastErrorXml(swigCPtr, this);
  }

  public boolean get_LastMethodSuccess() {
    return chilkatJNI.CkBounce_get_LastMethodSuccess(swigCPtr, this);
  }

  public void put_LastMethodSuccess(boolean newVal) {
    chilkatJNI.CkBounce_put_LastMethodSuccess(swigCPtr, this, newVal);
  }

  public boolean get_VerboseLogging() {
    return chilkatJNI.CkBounce_get_VerboseLogging(swigCPtr, this);
  }

  public void put_VerboseLogging(boolean newVal) {
    chilkatJNI.CkBounce_put_VerboseLogging(swigCPtr, this, newVal);
  }

  public void get_Version(CkString str) {
    chilkatJNI.CkBounce_get_Version(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String version() {
    return chilkatJNI.CkBounce_version(swigCPtr, this);
  }

  public boolean ExamineEmail(CkEmail email) {
    return chilkatJNI.CkBounce_ExamineEmail(swigCPtr, this, CkEmail.getCPtr(email), email);
  }

  public boolean ExamineEml(String emlFilename) {
    return chilkatJNI.CkBounce_ExamineEml(swigCPtr, this, emlFilename);
  }

  public boolean ExamineMime(String mimeText) {
    return chilkatJNI.CkBounce_ExamineMime(swigCPtr, this, mimeText);
  }

  public boolean SaveLastError(String path) {
    return chilkatJNI.CkBounce_SaveLastError(swigCPtr, this, path);
  }

  public boolean UnlockComponent(String unlockCode) {
    return chilkatJNI.CkBounce_UnlockComponent(swigCPtr, this, unlockCode);
  }

}
