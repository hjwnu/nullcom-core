package org.nullcom.nullcomcore.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionUser {
  private String hostId;
  private String userId;
  private String personInfoId;
  private String loginId;
}
