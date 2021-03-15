/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package chilkatsoft;

public class CkJsonObject {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CkJsonObject(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CkJsonObject obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        chilkatJNI.delete_CkJsonObject(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CkJsonObject() {
    this(chilkatJNI.new_CkJsonObject(), true);
  }

  public void LastErrorXml(CkString str) {
    chilkatJNI.CkJsonObject_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void LastErrorHtml(CkString str) {
    chilkatJNI.CkJsonObject_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void LastErrorText(CkString str) {
    chilkatJNI.CkJsonObject_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public void get_DebugLogFilePath(CkString str) {
    chilkatJNI.CkJsonObject_get_DebugLogFilePath(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String debugLogFilePath() {
    return chilkatJNI.CkJsonObject_debugLogFilePath(swigCPtr, this);
  }

  public void put_DebugLogFilePath(String newVal) {
    chilkatJNI.CkJsonObject_put_DebugLogFilePath(swigCPtr, this, newVal);
  }

  public void get_DelimiterChar(CkString str) {
    chilkatJNI.CkJsonObject_get_DelimiterChar(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String delimiterChar() {
    return chilkatJNI.CkJsonObject_delimiterChar(swigCPtr, this);
  }

  public void put_DelimiterChar(String newVal) {
    chilkatJNI.CkJsonObject_put_DelimiterChar(swigCPtr, this, newVal);
  }

  public boolean get_EmitCompact() {
    return chilkatJNI.CkJsonObject_get_EmitCompact(swigCPtr, this);
  }

  public void put_EmitCompact(boolean newVal) {
    chilkatJNI.CkJsonObject_put_EmitCompact(swigCPtr, this, newVal);
  }

  public boolean get_EmitCrLf() {
    return chilkatJNI.CkJsonObject_get_EmitCrLf(swigCPtr, this);
  }

  public void put_EmitCrLf(boolean newVal) {
    chilkatJNI.CkJsonObject_put_EmitCrLf(swigCPtr, this, newVal);
  }

  public int get_I() {
    return chilkatJNI.CkJsonObject_get_I(swigCPtr, this);
  }

  public void put_I(int newVal) {
    chilkatJNI.CkJsonObject_put_I(swigCPtr, this, newVal);
  }

  public int get_J() {
    return chilkatJNI.CkJsonObject_get_J(swigCPtr, this);
  }

  public void put_J(int newVal) {
    chilkatJNI.CkJsonObject_put_J(swigCPtr, this, newVal);
  }

  public int get_K() {
    return chilkatJNI.CkJsonObject_get_K(swigCPtr, this);
  }

  public void put_K(int newVal) {
    chilkatJNI.CkJsonObject_put_K(swigCPtr, this, newVal);
  }

  public void get_LastErrorHtml(CkString str) {
    chilkatJNI.CkJsonObject_get_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String lastErrorHtml() {
    return chilkatJNI.CkJsonObject_lastErrorHtml(swigCPtr, this);
  }

  public void get_LastErrorText(CkString str) {
    chilkatJNI.CkJsonObject_get_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String lastErrorText() {
    return chilkatJNI.CkJsonObject_lastErrorText(swigCPtr, this);
  }

  public void get_LastErrorXml(CkString str) {
    chilkatJNI.CkJsonObject_get_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String lastErrorXml() {
    return chilkatJNI.CkJsonObject_lastErrorXml(swigCPtr, this);
  }

  public boolean get_LastMethodSuccess() {
    return chilkatJNI.CkJsonObject_get_LastMethodSuccess(swigCPtr, this);
  }

  public void put_LastMethodSuccess(boolean newVal) {
    chilkatJNI.CkJsonObject_put_LastMethodSuccess(swigCPtr, this, newVal);
  }

  public void get_PathPrefix(CkString str) {
    chilkatJNI.CkJsonObject_get_PathPrefix(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String pathPrefix() {
    return chilkatJNI.CkJsonObject_pathPrefix(swigCPtr, this);
  }

  public void put_PathPrefix(String newVal) {
    chilkatJNI.CkJsonObject_put_PathPrefix(swigCPtr, this, newVal);
  }

  public int get_Size() {
    return chilkatJNI.CkJsonObject_get_Size(swigCPtr, this);
  }

  public boolean get_VerboseLogging() {
    return chilkatJNI.CkJsonObject_get_VerboseLogging(swigCPtr, this);
  }

  public void put_VerboseLogging(boolean newVal) {
    chilkatJNI.CkJsonObject_put_VerboseLogging(swigCPtr, this, newVal);
  }

  public void get_Version(CkString str) {
    chilkatJNI.CkJsonObject_get_Version(swigCPtr, this, CkString.getCPtr(str), str);
  }

  public String version() {
    return chilkatJNI.CkJsonObject_version(swigCPtr, this);
  }

  public boolean AddArrayAt(int index, String name) {
    return chilkatJNI.CkJsonObject_AddArrayAt(swigCPtr, this, index, name);
  }

  public boolean AddArrayCopyAt(int index, String name, CkJsonArray jarr) {
    return chilkatJNI.CkJsonObject_AddArrayCopyAt(swigCPtr, this, index, name, CkJsonArray.getCPtr(jarr), jarr);
  }

  public boolean AddBoolAt(int index, String name, boolean value) {
    return chilkatJNI.CkJsonObject_AddBoolAt(swigCPtr, this, index, name, value);
  }

  public boolean AddIntAt(int index, String name, int value) {
    return chilkatJNI.CkJsonObject_AddIntAt(swigCPtr, this, index, name, value);
  }

  public boolean AddNullAt(int index, String name) {
    return chilkatJNI.CkJsonObject_AddNullAt(swigCPtr, this, index, name);
  }

  public boolean AddNumberAt(int index, String name, String numericStr) {
    return chilkatJNI.CkJsonObject_AddNumberAt(swigCPtr, this, index, name, numericStr);
  }

  public boolean AddObjectAt(int index, String name) {
    return chilkatJNI.CkJsonObject_AddObjectAt(swigCPtr, this, index, name);
  }

  public boolean AddObjectCopyAt(int index, String name, CkJsonObject jsonObj) {
    return chilkatJNI.CkJsonObject_AddObjectCopyAt(swigCPtr, this, index, name, CkJsonObject.getCPtr(jsonObj), jsonObj);
  }

  public boolean AddStringAt(int index, String name, String value) {
    return chilkatJNI.CkJsonObject_AddStringAt(swigCPtr, this, index, name, value);
  }

  public CkJsonArray AppendArray(String name) {
    long cPtr = chilkatJNI.CkJsonObject_AppendArray(swigCPtr, this, name);
    return (cPtr == 0) ? null : new CkJsonArray(cPtr, true);
  }

  public boolean AppendArrayCopy(String name, CkJsonArray jarr) {
    return chilkatJNI.CkJsonObject_AppendArrayCopy(swigCPtr, this, name, CkJsonArray.getCPtr(jarr), jarr);
  }

  public boolean AppendBool(String name, boolean value) {
    return chilkatJNI.CkJsonObject_AppendBool(swigCPtr, this, name, value);
  }

  public boolean AppendInt(String name, int value) {
    return chilkatJNI.CkJsonObject_AppendInt(swigCPtr, this, name, value);
  }

  public CkJsonObject AppendObject(String name) {
    long cPtr = chilkatJNI.CkJsonObject_AppendObject(swigCPtr, this, name);
    return (cPtr == 0) ? null : new CkJsonObject(cPtr, true);
  }

  public boolean AppendObjectCopy(String name, CkJsonObject jsonObj) {
    return chilkatJNI.CkJsonObject_AppendObjectCopy(swigCPtr, this, name, CkJsonObject.getCPtr(jsonObj), jsonObj);
  }

  public boolean AppendString(String name, String value) {
    return chilkatJNI.CkJsonObject_AppendString(swigCPtr, this, name, value);
  }

  public boolean AppendStringArray(String name, CkStringTable values) {
    return chilkatJNI.CkJsonObject_AppendStringArray(swigCPtr, this, name, CkStringTable.getCPtr(values), values);
  }

  public CkJsonArray ArrayAt(int index) {
    long cPtr = chilkatJNI.CkJsonObject_ArrayAt(swigCPtr, this, index);
    return (cPtr == 0) ? null : new CkJsonArray(cPtr, true);
  }

  public CkJsonArray ArrayOf(String jsonPath) {
    long cPtr = chilkatJNI.CkJsonObject_ArrayOf(swigCPtr, this, jsonPath);
    return (cPtr == 0) ? null : new CkJsonArray(cPtr, true);
  }

  public boolean BoolAt(int index) {
    return chilkatJNI.CkJsonObject_BoolAt(swigCPtr, this, index);
  }

  public boolean BoolOf(String jsonPath) {
    return chilkatJNI.CkJsonObject_BoolOf(swigCPtr, this, jsonPath);
  }

  public boolean BytesOf(String jsonPath, String encoding, CkBinData bd) {
    return chilkatJNI.CkJsonObject_BytesOf(swigCPtr, this, jsonPath, encoding, CkBinData.getCPtr(bd), bd);
  }

  public void Clear() {
    chilkatJNI.CkJsonObject_Clear(swigCPtr, this);
  }

  public CkJsonObject Clone() {
    long cPtr = chilkatJNI.CkJsonObject_Clone(swigCPtr, this);
    return (cPtr == 0) ? null : new CkJsonObject(cPtr, true);
  }

  public boolean DateOf(String jsonPath, CkDateTime dateTime) {
    return chilkatJNI.CkJsonObject_DateOf(swigCPtr, this, jsonPath, CkDateTime.getCPtr(dateTime), dateTime);
  }

  public boolean Delete(String name) {
    return chilkatJNI.CkJsonObject_Delete(swigCPtr, this, name);
  }

  public boolean DeleteAt(int index) {
    return chilkatJNI.CkJsonObject_DeleteAt(swigCPtr, this, index);
  }

  public boolean DtOf(String jsonPath, boolean bLocal, CkDtObj dt) {
    return chilkatJNI.CkJsonObject_DtOf(swigCPtr, this, jsonPath, bLocal, CkDtObj.getCPtr(dt), dt);
  }

  public boolean Emit(CkString outStr) {
    return chilkatJNI.CkJsonObject_Emit(swigCPtr, this, CkString.getCPtr(outStr), outStr);
  }

  public String emit() {
    return chilkatJNI.CkJsonObject_emit(swigCPtr, this);
  }

  public boolean EmitBd(CkBinData bd) {
    return chilkatJNI.CkJsonObject_EmitBd(swigCPtr, this, CkBinData.getCPtr(bd), bd);
  }

  public boolean EmitSb(CkStringBuilder sb) {
    return chilkatJNI.CkJsonObject_EmitSb(swigCPtr, this, CkStringBuilder.getCPtr(sb), sb);
  }

  public boolean EmitWithSubs(CkHashtable subs, boolean omitEmpty, CkString outStr) {
    return chilkatJNI.CkJsonObject_EmitWithSubs(swigCPtr, this, CkHashtable.getCPtr(subs), subs, omitEmpty, CkString.getCPtr(outStr), outStr);
  }

  public String emitWithSubs(CkHashtable subs, boolean omitEmpty) {
    return chilkatJNI.CkJsonObject_emitWithSubs(swigCPtr, this, CkHashtable.getCPtr(subs), subs, omitEmpty);
  }

  public CkJsonObject FindObjectWithMember(String name) {
    long cPtr = chilkatJNI.CkJsonObject_FindObjectWithMember(swigCPtr, this, name);
    return (cPtr == 0) ? null : new CkJsonObject(cPtr, true);
  }

  public CkJsonObject FindRecord(String arrayPath, String relPath, String value, boolean caseSensitive) {
    long cPtr = chilkatJNI.CkJsonObject_FindRecord(swigCPtr, this, arrayPath, relPath, value, caseSensitive);
    return (cPtr == 0) ? null : new CkJsonObject(cPtr, true);
  }

  public boolean FindRecordString(String arrayPath, String relPath, String value, boolean caseSensitive, String retRelPath, CkString outStr) {
    return chilkatJNI.CkJsonObject_FindRecordString(swigCPtr, this, arrayPath, relPath, value, caseSensitive, retRelPath, CkString.getCPtr(outStr), outStr);
  }

  public String findRecordString(String arrayPath, String relPath, String value, boolean caseSensitive, String retRelPath) {
    return chilkatJNI.CkJsonObject_findRecordString(swigCPtr, this, arrayPath, relPath, value, caseSensitive, retRelPath);
  }

  public boolean FirebaseApplyEvent(String name, String data) {
    return chilkatJNI.CkJsonObject_FirebaseApplyEvent(swigCPtr, this, name, data);
  }

  public boolean FirebasePatch(String jsonPath, String jsonData) {
    return chilkatJNI.CkJsonObject_FirebasePatch(swigCPtr, this, jsonPath, jsonData);
  }

  public boolean FirebasePut(String jsonPath, String value) {
    return chilkatJNI.CkJsonObject_FirebasePut(swigCPtr, this, jsonPath, value);
  }

  public CkJsonObject GetDocRoot() {
    long cPtr = chilkatJNI.CkJsonObject_GetDocRoot(swigCPtr, this);
    return (cPtr == 0) ? null : new CkJsonObject(cPtr, true);
  }

  public boolean HasMember(String jsonPath) {
    return chilkatJNI.CkJsonObject_HasMember(swigCPtr, this, jsonPath);
  }

  public int IndexOf(String name) {
    return chilkatJNI.CkJsonObject_IndexOf(swigCPtr, this, name);
  }

  public int IntAt(int index) {
    return chilkatJNI.CkJsonObject_IntAt(swigCPtr, this, index);
  }

  public int IntOf(String jsonPath) {
    return chilkatJNI.CkJsonObject_IntOf(swigCPtr, this, jsonPath);
  }

  public boolean IsNullAt(int index) {
    return chilkatJNI.CkJsonObject_IsNullAt(swigCPtr, this, index);
  }

  public boolean IsNullOf(String jsonPath) {
    return chilkatJNI.CkJsonObject_IsNullOf(swigCPtr, this, jsonPath);
  }

  public int JsonTypeOf(String jsonPath) {
    return chilkatJNI.CkJsonObject_JsonTypeOf(swigCPtr, this, jsonPath);
  }

  public boolean Load(String json) {
    return chilkatJNI.CkJsonObject_Load(swigCPtr, this, json);
  }

  public boolean LoadBd(CkBinData bd) {
    return chilkatJNI.CkJsonObject_LoadBd(swigCPtr, this, CkBinData.getCPtr(bd), bd);
  }

  public boolean LoadFile(String path) {
    return chilkatJNI.CkJsonObject_LoadFile(swigCPtr, this, path);
  }

  public boolean LoadPredefined(String name) {
    return chilkatJNI.CkJsonObject_LoadPredefined(swigCPtr, this, name);
  }

  public boolean LoadSb(CkStringBuilder sb) {
    return chilkatJNI.CkJsonObject_LoadSb(swigCPtr, this, CkStringBuilder.getCPtr(sb), sb);
  }

  public boolean MoveMember(int fromIndex, int toIndex) {
    return chilkatJNI.CkJsonObject_MoveMember(swigCPtr, this, fromIndex, toIndex);
  }

  public boolean NameAt(int index, CkString outStr) {
    return chilkatJNI.CkJsonObject_NameAt(swigCPtr, this, index, CkString.getCPtr(outStr), outStr);
  }

  public String nameAt(int index) {
    return chilkatJNI.CkJsonObject_nameAt(swigCPtr, this, index);
  }

  public CkJsonObject ObjectAt(int index) {
    long cPtr = chilkatJNI.CkJsonObject_ObjectAt(swigCPtr, this, index);
    return (cPtr == 0) ? null : new CkJsonObject(cPtr, true);
  }

  public CkJsonObject ObjectOf(String jsonPath) {
    long cPtr = chilkatJNI.CkJsonObject_ObjectOf(swigCPtr, this, jsonPath);
    return (cPtr == 0) ? null : new CkJsonObject(cPtr, true);
  }

  public boolean Predefine(String name) {
    return chilkatJNI.CkJsonObject_Predefine(swigCPtr, this, name);
  }

  public boolean Rename(String oldName, String newName) {
    return chilkatJNI.CkJsonObject_Rename(swigCPtr, this, oldName, newName);
  }

  public boolean RenameAt(int index, String name) {
    return chilkatJNI.CkJsonObject_RenameAt(swigCPtr, this, index, name);
  }

  public boolean SaveLastError(String path) {
    return chilkatJNI.CkJsonObject_SaveLastError(swigCPtr, this, path);
  }

  public boolean SetBoolAt(int index, boolean value) {
    return chilkatJNI.CkJsonObject_SetBoolAt(swigCPtr, this, index, value);
  }

  public boolean SetBoolOf(String jsonPath, boolean value) {
    return chilkatJNI.CkJsonObject_SetBoolOf(swigCPtr, this, jsonPath, value);
  }

  public boolean SetIntAt(int index, int value) {
    return chilkatJNI.CkJsonObject_SetIntAt(swigCPtr, this, index, value);
  }

  public boolean SetIntOf(String jsonPath, int value) {
    return chilkatJNI.CkJsonObject_SetIntOf(swigCPtr, this, jsonPath, value);
  }

  public boolean SetNullAt(int index) {
    return chilkatJNI.CkJsonObject_SetNullAt(swigCPtr, this, index);
  }

  public boolean SetNullOf(String jsonPath) {
    return chilkatJNI.CkJsonObject_SetNullOf(swigCPtr, this, jsonPath);
  }

  public boolean SetNumberAt(int index, String value) {
    return chilkatJNI.CkJsonObject_SetNumberAt(swigCPtr, this, index, value);
  }

  public boolean SetNumberOf(String jsonPath, String value) {
    return chilkatJNI.CkJsonObject_SetNumberOf(swigCPtr, this, jsonPath, value);
  }

  public boolean SetStringAt(int index, String value) {
    return chilkatJNI.CkJsonObject_SetStringAt(swigCPtr, this, index, value);
  }

  public boolean SetStringOf(String jsonPath, String value) {
    return chilkatJNI.CkJsonObject_SetStringOf(swigCPtr, this, jsonPath, value);
  }

  public int SizeOfArray(String jsonPath) {
    return chilkatJNI.CkJsonObject_SizeOfArray(swigCPtr, this, jsonPath);
  }

  public boolean StringAt(int index, CkString outStr) {
    return chilkatJNI.CkJsonObject_StringAt(swigCPtr, this, index, CkString.getCPtr(outStr), outStr);
  }

  public String stringAt(int index) {
    return chilkatJNI.CkJsonObject_stringAt(swigCPtr, this, index);
  }

  public boolean StringOf(String jsonPath, CkString outStr) {
    return chilkatJNI.CkJsonObject_StringOf(swigCPtr, this, jsonPath, CkString.getCPtr(outStr), outStr);
  }

  public String stringOf(String jsonPath) {
    return chilkatJNI.CkJsonObject_stringOf(swigCPtr, this, jsonPath);
  }

  public boolean StringOfSb(String jsonPath, CkStringBuilder sb) {
    return chilkatJNI.CkJsonObject_StringOfSb(swigCPtr, this, jsonPath, CkStringBuilder.getCPtr(sb), sb);
  }

  public boolean Swap(int index1, int index2) {
    return chilkatJNI.CkJsonObject_Swap(swigCPtr, this, index1, index2);
  }

  public int TypeAt(int index) {
    return chilkatJNI.CkJsonObject_TypeAt(swigCPtr, this, index);
  }

  public boolean UpdateBd(String jsonPath, String encoding, CkBinData bd) {
    return chilkatJNI.CkJsonObject_UpdateBd(swigCPtr, this, jsonPath, encoding, CkBinData.getCPtr(bd), bd);
  }

  public boolean UpdateBool(String jsonPath, boolean value) {
    return chilkatJNI.CkJsonObject_UpdateBool(swigCPtr, this, jsonPath, value);
  }

  public boolean UpdateInt(String jsonPath, int value) {
    return chilkatJNI.CkJsonObject_UpdateInt(swigCPtr, this, jsonPath, value);
  }

  public boolean UpdateNewArray(String jsonPath) {
    return chilkatJNI.CkJsonObject_UpdateNewArray(swigCPtr, this, jsonPath);
  }

  public boolean UpdateNewObject(String jsonPath) {
    return chilkatJNI.CkJsonObject_UpdateNewObject(swigCPtr, this, jsonPath);
  }

  public boolean UpdateNull(String jsonPath) {
    return chilkatJNI.CkJsonObject_UpdateNull(swigCPtr, this, jsonPath);
  }

  public boolean UpdateNumber(String jsonPath, String numericStr) {
    return chilkatJNI.CkJsonObject_UpdateNumber(swigCPtr, this, jsonPath, numericStr);
  }

  public boolean UpdateSb(String jsonPath, CkStringBuilder sb) {
    return chilkatJNI.CkJsonObject_UpdateSb(swigCPtr, this, jsonPath, CkStringBuilder.getCPtr(sb), sb);
  }

  public boolean UpdateString(String jsonPath, String value) {
    return chilkatJNI.CkJsonObject_UpdateString(swigCPtr, this, jsonPath, value);
  }

  public boolean WriteFile(String path) {
    return chilkatJNI.CkJsonObject_WriteFile(swigCPtr, this, path);
  }

}