package org.nullcom.nullcomcore.entity;

import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.nullcom.nullcomcore.dto.BasicEntity;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FileRoom extends BasicEntity {

  private String fileRoomId;
  private String fileRoomClass;
  private String filePath;
  private String memo;
  @OneToMany(mappedBy = "file_info")
  private List<FileInfo> fileInfos;

}
