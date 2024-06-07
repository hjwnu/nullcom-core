package org.nullcom.nullcomcore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UploadFileDto extends BasicDto {
  private String fileClass;
  private String note;
  private MultipartFile multipartFile;

}
