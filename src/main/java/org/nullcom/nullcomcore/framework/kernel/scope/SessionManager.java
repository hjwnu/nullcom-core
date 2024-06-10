package org.nullcom.nullcomcore.framework.kernel.scope;

import org.nullcom.nullcomcore.constant.CONST_MEMORY;
import org.nullcom.nullcomcore.component.AbstractComponent;
import org.nullcom.nullcomcore.framework.kernel.redis.RedisManager;
import org.springframework.stereotype.Component;

@Component
public class SessionManager extends AbstractComponent {

  private final String USER_LOGIN_ING = "USER_LOGIN_ING";

  private final RedisManager redisManager;

  private final RequestScopeManager requestScopeManager;

  public SessionManager(RedisManager redisManager, RequestScopeManager requestScopeManager) {
    this.redisManager = redisManager;
    this.requestScopeManager = requestScopeManager;
  }

  /*
   * 사용자별로 세션처럼 사용하기 위함
   * JwtAuthenticationFilter, JwtAuthorizationFilter 에서 토큰 생성/검증 후 바로 세팅
   */
  private String getKey(String key) {
    // loginId 세팅은 로그인 시, 리로그인 시
    String sessionKey = this.requestScopeManager.getRequestToken();
    return redisManager.generateKey(CONST_MEMORY.AUTH, CONST_MEMORY.USER, sessionKey, key);
  }

  public Object get(String key) {
    return this.redisManager.getObject(this.getKey(key));
  }

  public void set(String key, Object value) {
    this.redisManager.setObject(this.getKey(key), value);
  }

  public void remove(String key) {
    this.redisManager.deleteObject(this.getKey(key));
  }

  public void setUserLoginIng() {
    this.redisManager.setObject(this.getKey(this.USER_LOGIN_ING), true, 10);//10초: 방어코드(팬딩되지 않도록)
  }

  public Object getUserLoginIng() {
    return this.redisManager.getObject(this.getKey(this.USER_LOGIN_ING));
  }

  public void removeUserLoginIng() {
    this.redisManager.deleteObject(this.getKey(this.USER_LOGIN_ING));
  }
}
