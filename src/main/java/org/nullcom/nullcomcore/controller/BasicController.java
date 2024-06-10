package org.nullcom.nullcomcore.controller;

import org.nullcom.nullcomcore.dto.SessionUser;
import org.nullcom.nullcomcore.component.AbstractComponent;
import org.nullcom.nullcomcore.framework.kernel.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

abstract class BasicController extends AbstractComponent {

  @Autowired
  private UserManager userManager;

  /**
   * 사용자 세션 정보
   *
   * @return
   */
  protected SessionUser getSessionUser() {
    return this.userManager.getSessionUser();
  }

}
