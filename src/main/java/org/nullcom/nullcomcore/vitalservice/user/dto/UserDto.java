package org.nullcom.nullcomcore.vitalservice.user.dto;

import lombok.Getter;

@Getter
public class UserDto {
  private String hostId;
  private String userId;
  private String personInfoId;
  private String loginId;
  private String oauthId;
  private String pwd;
  private Integer errCnt;
  private String email;

}
