package org.nullcom.nullcomcore.framework.kernel.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.nullcom.nullcomcore.dto.SessionUser;
import org.nullcom.nullcomcore.framework.kernel.scope.SessionManager;
import org.nullcom.nullcomcore.framework.kernel.scope.ThreadScopeManager;
import org.nullcom.nullcomcore.util.StringUtil;
import org.nullcom.nullcomcore.component.AbstractComponent;
import org.nullcom.nullcomcore.vitalservice.user.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserManager extends AbstractComponent {
  private final ThreadScopeManager threadScopeManager;

  private final SessionManager sessionManager;

  public UserManager(ThreadScopeManager threadScopeManager, SessionManager sessionManager) {
    this.threadScopeManager = threadScopeManager;
    this.sessionManager = sessionManager;
  }


  /**
   * devices 세팅
   *
   * @param devices
   */
  public void setDevices(List<String> devices) {
    //device 정보 세션에 저장
    this.sessionManager.set(SESSION_ITEMS.USER_DEVICES.value, devices);
  }

  /**
   * 등록된 자신의 디바이스 목록
   *
   * @return sessionDevices
   */
  @SuppressWarnings("unchecked")
  public List<String> getDevices() {
    Object sessionDevices = this.sessionManager.get(SESSION_ITEMS.USER_DEVICES.value);

    if (sessionDevices != null) {
      return (List<String>) sessionDevices;
    }
    return null;
  }


  /**
   * SessionUser 조회
   *
   * @return SessionUser
   */
  public SessionUser getSessionUser() {
    return (SessionUser) this.sessionManager.get(SESSION_ITEMS.SESSION_USER.value);
  }

  /**
   * SessionUser 세션에 저장
   *
   * @param dto
   */
  private void setSessionUser(UserDto dto) {
    SessionUser sessionUser = SessionUser.builder()
        .hostId(dto.getHostId())
        .loginId(dto.getLoginId())
        .build();

    this.sessionManager.set(SESSION_ITEMS.SESSION_USER.value, sessionUser);
  }

  /**
   * 권한 전체 등록
   *
   * @param adminIds
   */
  public void setAuthAdmin(List<String> adminIds) {
    this.sessionManager.set(SESSION_ITEMS.ADMIN.value, adminIds);
  }

  /**
   * 나에게 주어진 권한 전체 조회
   *
   * @return adminIds
   */
  @SuppressWarnings("unchecked")
  private List<String> getAuthAdmin() {
    List<String> adminIds = (List<String>) this.sessionManager.get(SESSION_ITEMS.ADMIN.value);

    if (adminIds == null) {
      return new ArrayList<>();
    }
    return adminIds;
  }

  /**
   * 나에게 있는 권한인지 검사
   *
   * @param adminId
   * @return boolean
   */
  public boolean containsAdmin(String adminId) {
    List<String> adminIds = this.getAuthAdmin();

    return adminIds.contains(adminId);
  }


  /**
   * Admin-Auth 설정
   *
   * @param adminId
   * @param adminTargetUsers
   */
  public void setAuthAdmTrg(String adminId, List<String> adminTargetUsers) {
    Map<String, List<String>> adminTargetUserMap = this.getAuthAdmins();
    adminTargetUserMap.put(adminId, adminTargetUsers);
    this.sessionManager.set(SESSION_ITEMS.ADMIN_TARGET_USER_MAP.value, adminTargetUserMap);
  }

  /**
   * Admin-Auth 조회
   *
   * @return authAdminIds
   */
  public List<String> getAuthAdmin(String jobAdmId) {
    Map<String, List<String>> adminTargetUserMap = this.getAuthAdmins();
    return adminTargetUserMap.get(jobAdmId);
  }

  /**
   * Admin-Auth 전체 조회
   *
   * @return authAdminIds
   */
  @SuppressWarnings("unchecked")
  public Map<String, List<String>> getAuthAdmins() {
    Map<String, List<String>> adminTargetUserMap = (Map<String, List<String>>) this.sessionManager.get(
        SESSION_ITEMS.ADMIN_TARGET_USER_MAP.value);

    if (adminTargetUserMap == null) {
      return new HashMap<>();
    }

    return adminTargetUserMap;
  }

  /**
   * Host-ID 조회
   *
   * @return hostId
   */
  public String getHostId() {

    if (this.getSessionUser() != null) {
      String clientId = this.getSessionUser().getHostId();

      if (StringUtil.isEmpty(clientId)) {
        return this.threadScopeManager.getHostId();
      }

      return clientId;
    } else {
      return this.threadScopeManager.getHostId();
    }
  }

  public void clearSession() {
    SESSION_ITEMS[] sessionItems = SESSION_ITEMS.values();

    for (SESSION_ITEMS sessionItem : sessionItems) {
      this.sessionManager.remove(sessionItem.value);
    }
  }

  @AllArgsConstructor
  private enum SESSION_ITEMS {
    USER_DEVICES("USER_DEVICES"), SESSION_USER("SESSION_USER"), ADMIN("ADMIN"), ADMIN_TARGET_USER_MAP("ADMIN_TARGET_USER_MAP");

    private final String value;
  }

}
