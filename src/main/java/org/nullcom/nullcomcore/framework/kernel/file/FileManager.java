package org.nullcom.nullcomcore.framework.kernel.file;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.nullcom.nullcomcore.component.AbstractComponent;
import org.nullcom.nullcomcore.constant.ENUM_PHOTO_SIZE;
import org.nullcom.nullcomcore.dto.EntityDto;
import org.nullcom.nullcomcore.dto.UploadFileDto;
import org.nullcom.nullcomcore.entity.FileInfo;
import org.nullcom.nullcomcore.entity.FileRoom;
import org.nullcom.nullcomcore.exception.NullComException;
import org.nullcom.nullcomcore.framework.kernel.user.UserManager;
import org.nullcom.nullcomcore.util.DateUtil;
import org.nullcom.nullcomcore.util.FileUtil;
import org.nullcom.nullcomcore.util.ObjectUtil;
import org.nullcom.nullcomcore.util.StringUtil;
import org.nullcom.nullcomcore.util.UuidUtil;
import org.nullcom.nullcomcore.vitalservice.file.service.FileVitalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileManager extends AbstractComponent {

  private final String photoExtension = "jpg";
  private final String photoPersonInfoId = "personInfoId";
  private final String photoField = "photo";
  private final String photoEmpty = "empty";
  private final UserManager userManager;
  private final FileVitalService fileVitalService;
  private final Map<String, String> mediaType = new HashMap<>();
  @Value("${nullcom.files.photos.path}")
  private String photosPath;
  @Value("${nullcom.files.upload.path}")
  private String uploadPath;

  public FileManager(UserManager userManager, FileVitalService fileVitalService) {
    this.userManager = userManager;
    this.fileVitalService = fileVitalService;
  }

  @PostConstruct
  public void init() {
    this.mediaType.put("jpg", "image/jpeg");
    this.mediaType.put("jpeg", "image/jpeg");
    this.mediaType.put("gif", "iamge/gif");
    this.mediaType.put("bmp", "image/bmp");
    this.mediaType.put("zip", "application/zip");
    this.mediaType.put("csv", "text/csv");
    this.mediaType.put("ppt", "application/vnd.ms-powerpoint");
    this.mediaType.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    this.mediaType.put("xls", "application/vnd.ms-excel");
    this.mediaType.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    this.mediaType.put("doc", "application/msword");
    this.mediaType.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    this.mediaType.put("pdf", "application/pdf");
    this.mediaType.put("txt", "text/plain");

    this.mediaType.put("mp3", "audio/mpeg");
    this.mediaType.put("mp4", "video/mp4");
    this.mediaType.put("mpeg", "video/mpeg");
    this.mediaType.put("html", "text/html");
    this.mediaType.put("htm", "text/html");
  }

  /**
   * 개인 사진 저장: fileId가 없을 경우(추가)
   *
   * @param multipartFile
   * @return
   */
  public String uploadPhoto(MultipartFile multipartFile) {
    return this.uploadPhoto(null, multipartFile);
  }

  /**
   * 개인 사진 저장: personInfoId가 있을 경우(수정)
   *
   * @param personInfoId
   * @param multipartFile
   * @return
   */
  public String uploadPhoto(String personInfoId, MultipartFile multipartFile) {
    if (StringUtil.isEmpty(personInfoId)) {
      personInfoId = this.userManager.getSessionUser().getPersonInfoId();
    }

    try {
      if (multipartFile == null) {
        //TODO 실제 코드로 변경
        throw new NullComException("업로드 할 사진이 없습니다");
      }

      String savePath = this.photosPath + this.userManager.getHostId();
      String fileName = personInfoId + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

      FileUtil.save(multipartFile, savePath, fileName);
      FileUtil.createThumbnail(savePath, fileName,
          personInfoId + ENUM_PHOTO_SIZE.XS.sizeName() + "." + this.photoExtension,
          ENUM_PHOTO_SIZE.XS.value());
      FileUtil.createThumbnail(savePath, fileName,
          personInfoId + ENUM_PHOTO_SIZE.M.sizeName() + "." + this.photoExtension,
          ENUM_PHOTO_SIZE.M.value());
      FileUtil.createThumbnail(savePath, fileName,
          personInfoId + ENUM_PHOTO_SIZE.XL.sizeName() + "." + this.photoExtension,
          ENUM_PHOTO_SIZE.XL.value());
    } catch (IOException e) {
      super.error(e);
      //TODO 실제 코드로 변경
      throw new NullComException("exception.file.save");
    }
    return personInfoId;
  }

  public String getPhoto(String psnId, ENUM_PHOTO_SIZE photoSize) {
    String savePath = this.photosPath + this.userManager.getHostId();
    String sizeName = this.getPhotoSizeName(photoSize);
    String fileName = this.getPhotoFileName(savePath, psnId, sizeName);
    return this.getPhotoData(savePath, fileName, sizeName);
  }

  public byte[] getPhotoToBytes(String empId, ENUM_PHOTO_SIZE photoSize) {
    String savePath = this.photosPath + this.userManager.getHostId();
    String sizeName = this.getPhotoSizeName(photoSize);
    String fileName = this.getPhotoFileName(savePath, empId, sizeName);
    return this.getPhotoBytes(savePath, fileName, sizeName);
  }

  public void setPhoto(EntityDto entity, ENUM_PHOTO_SIZE photoSize) {
    String savePath = this.photosPath + this.userManager.getHostId();
    String sizeName = this.getPhotoSizeName(photoSize);

    if (ObjectUtil.containsFields(entity.getClass(), this.photoPersonInfoId)) {
      String fileName = this.getPhotoFileName(savePath, ObjectUtil.getValueToString(entity, this.photoPersonInfoId),
          sizeName);
      String data = this.getPhotoData(savePath, fileName, sizeName);

      ObjectUtil.setValue(entity, this.photoField, data);
    }
    // ((BasicVo)entity).setPhoto(data);
  }

  public <T extends EntityDto> void setPhotos(List<T> list, ENUM_PHOTO_SIZE photoSize) {
    String savePath = this.photosPath + this.userManager.getHostId();
    String sizeName = this.getPhotoSizeName(photoSize);
    String fileName;
    String data;

    if (list != null && !list.isEmpty()) {
      if (ObjectUtil.containsFields(list.get(0).getClass(), this.photoPersonInfoId)) {
        for (T vo : list) {
          fileName = this.getPhotoFileName(savePath, ObjectUtil.getValueToString(vo, this.photoPersonInfoId), sizeName);
          data = this.getPhotoData(savePath, fileName, sizeName);
          ObjectUtil.setValue(vo, this.photoField, data);
          // ((BasicVo)vo).setPhoto(data);
        }
      }
    }
  }

  private String getPhotoFileName(String savePath, String fileName, String sizeName) {
    return savePath + File.separator + fileName + sizeName + "." + this.photoExtension;
  }

  private String getPhotoSizeName(ENUM_PHOTO_SIZE photoSize) {
    return switch (photoSize) {
      case XS, M, XL -> photoSize.sizeName();
      case S -> ENUM_PHOTO_SIZE.M.sizeName();
      case L -> ENUM_PHOTO_SIZE.XL.sizeName();
    };
  }

  private String getPhotoData(String savePath, String fileName, String sizeName) {
    String data = null;

    try {
      if (FileUtil.isExist(fileName)) {
        data = FileUtil.getFileToBase64(fileName, this.mediaType.get(this.photoExtension));
      } else {
        data = FileUtil.getFileToBase64(this.getPhotoFileName(savePath, this.photoEmpty, sizeName),
            this.mediaType.get(this.photoExtension));
      }
    } catch (IOException e) {
      super.error(e);
    }
    return data;
  }

  private byte[] getPhotoBytes(String savePath, String fileName, String sizeName) {
    byte[] bytes;

    if (FileUtil.isExist(fileName)) {
      bytes = FileUtil.fileToByte(fileName);
    } else {
      bytes = FileUtil.fileToByte(this.getPhotoFileName(savePath, this.photoEmpty, sizeName));
    }
    return bytes;
  }

  /**
   * 파일 목록 조회
   *
   * @param fileRoomId
   * @return
   */
  public List<FileInfo> getFiles(String fileRoomId) {
    return this.fileVitalService.findAllInfoByRoomId(fileRoomId);
  }

  /**
   * 파일 업로드: 신규
   *
   * @param multipartFiles
   * @return
   */
  // public String upload(List<UploadFileDto> uploadFileDtos) {
  //   return this.doUpload(null, null, uploadFileDtos);
  // }

  // public String upload(String fileRoomNm, List<UploadFileDto> uploadFileDtos) {
  //   return this.doUpload(null, fileRoomNm, uploadFileDtos);
  // }
  public String upload(MultipartFile[] multipartFiles, String fileRoomClass, String fileClass, String memo) {
    List<UploadFileDto> uploadFileDtos = new ArrayList<>();

    for (MultipartFile multipartFile : multipartFiles) {
      uploadFileDtos.add(UploadFileDto.builder()
          .multipartFile(multipartFile)
          .fileClass(fileClass)
          .note(memo)
          .build());
    }

    return this.doUpload(null, fileRoomClass, memo, uploadFileDtos);
  }

  /**
   * 파일 업로드: 추가
   *
   * @param fileRoomId
   * @param multipartFiles
   * @return
   */
  public String append(MultipartFile[] multipartFiles, String fileRoomId, String fileClass, String note) {
    List<UploadFileDto> uploadFileDtos = new ArrayList<>();

    for (MultipartFile multipartFile : multipartFiles) {
      uploadFileDtos.add(UploadFileDto.builder()
          .multipartFile(multipartFile)
          .fileClass(fileClass)
          .note(note)
          .build());
    }

    return this.doUpload(fileRoomId, null, null, uploadFileDtos);
  }

  public FileRoom findByRoomId(String fileRoomId) {
    return this.fileVitalService.findByRoomId(fileRoomId);
  }

  public void updateRoom(FileRoom fileRoom) {
    this.fileVitalService.updateRoom(fileRoom);
  }

  /**
   * 파일 업로드
   *
   * @param fileRoomId
   * @param multipartFiles
   * @return
   */
  private String doUpload(String fileRoomId, String fileRoomClass, String memo, List<UploadFileDto> uploadFileDtos) {
    if (uploadFileDtos == null || uploadFileDtos.isEmpty()) {
      //TODO 실제 코드로 변경
      throw new NullComException("업로드 할 파일이 없습니다");
    }

    //확장자 검사
    this.validContentType(uploadFileDtos);

    FileRoom fileRoom;

    if (StringUtil.isEmpty(fileRoomId)) {
      fileRoomId = UuidUtil.generateString();

      String filePath = this.uploadPath
          + File.separator + this.userManager.getHostId()
          + File.separator + DateUtil.getTodayYmd()
          + File.separator + fileRoomId;

      fileRoom = FileRoom.builder()
          .fileRoomId(fileRoomId)
          .fileRoomClass(fileRoomClass)
          .filePath(filePath)
          .memo(memo)
          .build();

      // 새로운 파일 DB 저장
      this.fileVitalService.saveRoom(fileRoom);
    } else {
      fileRoom = this.fileVitalService.findByRoomId(fileRoomId);
    }

    // 새로운 파일 생성
    this.createFiles(fileRoom, uploadFileDtos);

    // 새로운 파일 DB 저장
    this.fileVitalService.saveInfo(fileRoom.getFileInfos());

    return fileRoomId;
  }

  /**
   * 물리적 파일 저장 & 파일 정보 반환
   *
   * @param fileRoom
   * @param multipartFiles
   */
  private void createFiles(FileRoom fileRoom, List<UploadFileDto> uploadFileDtos) {
    try {
      String fileInfoId;
      MultipartFile multipartFile;
      String inDate = DateUtil.getTodayYmd();
      List<FileInfo> fileInfoVos = new ArrayList<>();

      for (UploadFileDto uploadFileDto : uploadFileDtos) {
        multipartFile = uploadFileDto.getMultipartFile();
        fileInfoId = UuidUtil.generateString();

        fileInfoVos.add(FileInfo.builder()
            .fileInfoId(fileInfoId)
            .fileRoomId(fileRoom.getFileRoomId())
            .fileOriginName(multipartFile.getOriginalFilename())
            .fileMediaType(multipartFile.getContentType())
            .filePath(fileRoom.getFilePath())
            .fileSize(String.valueOf(multipartFile.getSize()))
            .inDate(inDate)
            .fileClass(uploadFileDto.getFileClass())
//                                    .note(uploadFileDto.getNote())
            .build());

        FileUtil.save(multipartFile, fileRoom.getFilePath(), fileInfoId);
      }

      fileRoom.setFileInfos(fileInfoVos);
    } catch (IOException e) {
      //TODO rollback > 파일 삭제?
      super.error(e);
      //TODO 실제 코드로 변경
      throw new NullComException("exception.file.save");
    }
  }

  /**
   * 파일 전체 삭제
   *
   * @param fileRoomId
   */
  public void deleteAll(String fileRoomId) {
    List<FileInfo> fileInfoVos = this.fileVitalService.findAllInfoByRoomId(fileRoomId);

    for (FileInfo fileInfoVo : fileInfoVos) {
      this.delete(fileInfoVo);
    }
  }

  /**
   * 파일 삭제
   *
   * @param fileInfoId
   */
  public void delete(String fileInfoId) {
    FileInfo fileInfo = this.fileVitalService.findByInfoId(fileInfoId);
    this.delete(fileInfo);
  }

  private void delete(FileInfo fileInfo) {
    // 파일 DB 삭제
    this.fileVitalService.deleteInfo(fileInfo.getFileInfoId());

    // 물리적 파일 삭제
    try {
      FileUtil.delete(fileInfo.getFilePath() + File.separator + fileInfo.getFileInfoId());
    } catch (IOException e) {
      super.error(e);
    }
  }

  /**
   * 파일 다운로드(byte[])
   *
   * @param fileInfoId
   * @return
   */
  public ResponseEntity<byte[]> download(String fileInfoId) {
    FileInfo fileInfoVo = this.fileVitalService.findByInfoId(fileInfoId);
    byte[] data = FileUtil.fileToByte(fileInfoVo.getFilePath() + File.separator + fileInfoVo.getFileInfoId());
    return new ResponseEntity<>(data,
        this.getHttpHeaders(fileInfoVo.getFileMediaType(), fileInfoVo.getFileOriginName()), HttpStatus.OK);
  }

  /**
   * 파일 다운로드(Resource)
   *
   * @param fileInfoId
   * @return
   */
  public ResponseEntity<Resource> downloadToOctetStream(String fileInfoId) {
    try {
      FileInfo fileInfo = this.fileVitalService.findByInfoId(fileInfoId);
      Resource resource = FileUtil.fileToOctetStream(
          fileInfo.getFilePath() + File.separator + fileInfo.getFileInfoId());
      return new ResponseEntity<>(resource,
          this.getHttpHeaders(fileInfo.getFileMediaType(), fileInfo.getFileOriginName()), HttpStatus.OK);
    } catch (IOException e) {
      super.error(e);
      return ResponseEntity.noContent().build();
    }
  }

  /**
   * 파일 다운로드 용 HttpHeader 생성
   *
   * @param mediaType
   * @param fileName
   * @return
   */
  private HttpHeaders getHttpHeaders(String mediaType, String fileName) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.valueOf(mediaType));
    fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
    headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fileName).build());
    return headers;
  }

  /*
   * TODO MediaType 보안 이슈 확인: 사용자가 변경할 경우
   *
   * uploadFileDto.getMultipartFile().getContentType()를 기준으로 검증을 해야 하나
   */
  private void validContentType(List<UploadFileDto> uploadFileDtos) {
    String ext;

    for (UploadFileDto uploadFileDto : uploadFileDtos) {
      ext = FilenameUtils.getExtension(uploadFileDto.getMultipartFile().getOriginalFilename());

      if (!this.mediaType.get(ext).equals(uploadFileDto.getMultipartFile().getContentType())) {
        throw new NullComException("파일 형식에 문제가 있습니다");
      }
    }
  }
}
