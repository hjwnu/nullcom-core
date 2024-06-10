package org.nullcom.nullcomcore.framework.kernel.scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * scope = request로 처리 (호출 시 생성, 반환 후 소멸)
 */
@Component
// @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode =
// ScopedProxyMode.TARGET_CLASS)
@RequestScope
public class RequestScopeData {

  private final Map<String, Object> requestMap = Collections.synchronizedMap(new HashMap<>());

  public Map<String, Object> getRequestMap() {
    return requestMap;
  }

  public void put(String key, Object value) {
    this.getRequestMap().put(key, value);
  }

  public Object get(String key) {
    return this.getRequestMap().get(key);
  }

  public void remove(String key) {
    this.getRequestMap().remove(key);
  }

}
