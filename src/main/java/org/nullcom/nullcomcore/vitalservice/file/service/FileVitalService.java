package org.nullcom.nullcomcore.vitalservice.file.service;

import java.util.List;
import org.nullcom.nullcomcore.entity.FileInfo;
import org.nullcom.nullcomcore.entity.FileRoom;
import org.nullcom.nullcomcore.vitalservice.file.repository.FileRoomRepository;
import org.nullcom.nullcomcore.vitalservice.file.repository.FileInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class FileVitalService {
  private final FileRoomRepository fileRoomRepository;
  private final FileInfoRepository fileInfoRepository;

  public FileVitalService(FileRoomRepository fileRoomRepository, FileInfoRepository fileInfoRepository) {
    this.fileRoomRepository = fileRoomRepository;
    this.fileInfoRepository = fileInfoRepository;
  }


  public List<FileInfo> findAllInfoByRoomId(String fileRoomId) {
    return fileInfoRepository.findAllByFileRoomId(fileRoomId);
  }

  public FileRoom findByRoomId(String fileRoomId) {
    return fileRoomRepository.findById(fileRoomId).orElse(null);
  }

  public FileInfo findByInfoId(String fileInfoId) {
    return fileInfoRepository.findById(fileInfoId).orElse(null);
  }

  public void updateRoom(FileRoom newFileRoom) {
    FileRoom fileRoom = fileRoomRepository.findById(newFileRoom.getFileRoomId()).orElseThrow(() -> new IllegalArgumentException("Invalid fileRoomId"));

    fileRoom.setFileRoomClass(newFileRoom.getFileRoomClass());
    fileRoom.setMemo(newFileRoom.getMemo());
    fileRoomRepository.save(fileRoom);
  }

  public void saveRoom(FileRoom fileRoom) {
    fileRoomRepository.save(fileRoom);
  }
  public void saveInfo(List<FileInfo> fileInfos) {
    fileInfoRepository.saveAll(fileInfos);
  }

  public void deleteInfo(String fileInfoId) {
    fileInfoRepository.deleteById(fileInfoId);
  }
}
