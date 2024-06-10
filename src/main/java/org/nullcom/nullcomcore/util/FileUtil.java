package org.nullcom.nullcomcore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

  /**
   * 파일 존재 유무
   *
   * @param filePath
   * @return boolean
   */
  public static boolean isExist(String filePath) {
    File file = new File(filePath);
    return file.exists();
  }

  /**
   * 해당 디렉토리의 파일 목록
   *
   * @param dirPath
   * @return dirFileList
   */
  public static String[] listFiles(String dirPath) {
    File dir = new File(dirPath);
    return dir.list();
  }

  /**
   * MultipartFile를 이용한 파일 저장
   *
   * @param multipartFile
   * @param savePath
   * @param fileName
   * @return
   * @throws IOException
   */
  public static Path save(MultipartFile multipartFile, String savePath, String fileName) throws IOException {
    // parent directory를 찾는다.
    Path directory = Paths.get(savePath).normalize();

    // directory 해당 경로까지 디렉토리를 모두 만든다.
    Files.createDirectories(directory);

    // 파일을 저장할 경로를 Path 객체로 받는다.
    Path targetPath = directory.resolve(StringUtils.cleanPath(fileName)).normalize();

    multipartFile.transferTo(targetPath);

    return targetPath;
  }

  /**
   * 파일 생성(경로에 없는 디렉토리 생성 포함)
   *
   * @param sourcePath
   * @return
   * @throws IOException
   */
  public static Path create(String sourcePath) throws IOException {
    Path source = Paths.get(sourcePath);

    // directory 해당 경로까지 디렉토리를 모두 만든다.
    Path directory = source.getParent().toAbsolutePath().normalize();
    Files.createDirectories(directory);

    return Files.createFile(source);
  }

  /**
   * 디렉토리 생성
   *
   * @param sourcePath
   * @return
   * @throws IOException
   */
  public static Path createDirectory(String sourcePath) throws IOException {
    Path source = Paths.get(sourcePath);
    return Files.createDirectories(source);
  }

  /**
   * 파일 이동
   *
   * @param sourcePath
   * @param targetPath
   * @throws IOException
   */
  public static void move(String sourcePath, String targetPath) throws IOException {
    Path source = Paths.get(sourcePath);
    Path target = Paths.get(targetPath);

    // directory 해당 경로까지 디렉토리를 모두 만든다.
    Path directory = target.getParent().toAbsolutePath().normalize();
    Files.createDirectories(directory);

    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
  }

  /**
   * 파일 복사
   *
   * @param sourcePath
   * @param targetPath
   * @throws IOException
   */
  public static void copy(String sourcePath, String targetPath) throws IOException {
    Path source = Paths.get(sourcePath);
    Path target = Paths.get(targetPath);

    // directory 해당 경로까지 디렉토리를 모두 만든다.
    Path directory = target.getParent().toAbsolutePath().normalize();
    Files.createDirectories(directory);

    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
  }

  /**
   * 파일 삭제
   *
   * @param sourcePath
   * @return
   * @throws IOException
   */
  public static boolean delete(String sourcePath) throws IOException {
    if (StringUtil.isEmpty(sourcePath)) {
      return false;
    }

    Path deleteSource = Paths.get(sourcePath);
    return Files.deleteIfExists(deleteSource);
  }

  /**
   * 파일이 존재하면 삭제
   *
   * @param path
   * @return
   * @throws IOException
   */
  public static boolean delete(Path path) throws IOException {
    return Files.deleteIfExists(path);
  }

  /**
   * 디렉토리 삭제(상위 디렉토리 삭제 포함)
   *
   * @param sourcePath
   * @throws IOException
   */
  public static void deleteParentDirectory(String sourcePath) throws IOException {
    Path deleteSource = Paths.get(sourcePath);
    Files.deleteIfExists(deleteSource);
    Files.deleteIfExists(deleteSource.getParent());
  }

  /**
   * 파일을 OctetStream로 변환
   *
   * @param filePath
   * @return
   * @throws IOException
   */
  public static Resource fileToOctetStream(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    return new InputStreamResource(Files.newInputStream(path));
  }

  /**
   * 파일을 byte[]로 변환
   *
   * @param filePath
   * @return
   */
  public static byte[] fileToByte(String filePath) {
    try (
        InputStream inputStream = new FileInputStream(filePath)
    ) {
      return IOUtils.toByteArray(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 파일을 base64로 변환
   *
   * @param filePath
   * @param extension
   * @return
   * @throws IOException
   */
  public static String getFileToBase64(String filePath, String extension) throws IOException {
    Path path = Paths.get(filePath);
    byte[] bytes = Files.readAllBytes(path);
    return "data:" + extension + ";base64," + Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * 이미지 파일의 썸네일 생성
   *
   * @param filePath
   * @param orgnFileName
   * @param thumbnailFileName
   * @param size
   * @throws IOException
   */
  public static void createThumbnail(String filePath, String orgnFileName, String thumbnailFileName, int size)
      throws IOException {
    Thumbnails
        .of(new File(filePath, orgnFileName))
        .forceSize(size, size)
        .toFile(new File(filePath, thumbnailFileName));
  }

}
