package org.nullcom.nullcomcore.framework.kernel.scope;

import org.nullcom.nullcomcore.component.AbstractComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ScopeNotActiveException;
import org.springframework.stereotype.Component;

/**
 * UI에서 각각의 호출에 대하여 unique 하며, UI로 반환되기전까지만 유지한다.
 */
@Component
public class RequestScopeManager extends AbstractComponent {

  @Autowired
  private RequestScopeData requestScopeData;

  private final String API = "API";
  private final String LOG_API_ID = "LOG_API_ID";
  private final String SESSION_ID = "SESSION_ID";
  private final String REQUEST_TOKEN = "REQUEST_TOKEN";

  public void put(String key, Object obj) {
    try {
      this.requestScopeData.put(key, obj);
    } catch (ScopeNotActiveException e) {
      //
    }
  }

  public Object get(String key) {
    try {
      return this.requestScopeData.get(key);
    } catch (ScopeNotActiveException e) {
      return null;
    }
  }

  public void remove(String key) {
    try {
      this.requestScopeData.remove(key);
    } catch (ScopeNotActiveException e) {
      //
    }
  }

  public void setApi(String api) {
    this.put(this.API, api);
  }

  public String getApi() {
    return (String) this.get(this.API);
  }

  public void setLogApiId(String logApiId) {
    this.put(this.LOG_API_ID, logApiId);
  }

  public String getLogApiId() {
    return (String) this.get(this.LOG_API_ID);
  }

  public void setRequestToken(String token) {
    this.put(this.REQUEST_TOKEN, token);
  }

  public String getRequestToken() {
    return (String) this.get(this.REQUEST_TOKEN);
  }


}
