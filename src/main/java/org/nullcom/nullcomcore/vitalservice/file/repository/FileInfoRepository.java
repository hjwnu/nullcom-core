package org.nullcom.nullcomcore.vitalservice.file.repository;

import java.util.List;
import org.nullcom.nullcomcore.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo, String> {

  List<FileInfo> findAllByFileRoomId(String fileRoomId);

}
