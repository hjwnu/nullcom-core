package org.nullcom.nullcomcore.framework.kernel.scope;

import org.nullcom.nullcomcore.component.AbstractComponent;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class ThreadScopeManager extends AbstractComponent {

  public void setHostId(String hostId) {
    MDC.put("hostId", hostId);
  }

  public String getHostId() {
    return MDC.get("hostId");
  }

}
