package org.nullcom.nullcomcore.controller;


import org.nullcom.nullcomcore.framework.kernel.file.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

abstract public class BasicFileController extends BasicController {

  @Autowired
  private FileManager fileManager;


  /**
   * 파일 다운로드
   *
   * @param fileInfoId
   * @return
   */
  protected ResponseEntity<byte[]> downloadFile(String fileInfoId) {
    return this.fileManager.download(fileInfoId);
  }

  /**
   * 파일 다운로드
   *
   * @param fileInfoId
   * @return
   */
  protected ResponseEntity<Resource> downloadToOctetStream(String fileInfoId) {
    return this.fileManager.downloadToOctetStream(fileInfoId);
  }

}
