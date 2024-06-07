package org.nullcom.nullcomcore.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.nullcom.nullcomcore.dto.BasicEntity;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FileInfo extends BasicEntity {

  private String fileInfoId;
  @ManyToOne()
  @JoinColumn(name = "customer_id")
  private String fileRoomId;
  private String fileClass;
  private String filePath;
  private String fileOriginName;
  private String fileMediaType;
  private String fileSize;
  private String inDate;
  private String note;
}

