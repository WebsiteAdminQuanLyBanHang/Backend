package edu.ctu.SpringShopBE.service;

import edu.ctu.SpringShopBE.config.FileStorageProperties;
import edu.ctu.SpringShopBE.dto.UploadedFileInfo;
import edu.ctu.SpringShopBE.exception.entityException.FileNotFoundException;
import edu.ctu.SpringShopBE.exception.entityException.FileStorageException;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileLogoStorageLocation;
    private final Path fileProductImageStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileLogoStorageLocation = Paths.get(fileStorageProperties.getUploadLogoDir())
                .toAbsolutePath();
        this.fileProductImageStorageLocation = Paths.get(fileStorageProperties.getUploadProductImageDir())
                .toAbsolutePath();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(fileLogoStorageLocation);
            Files.createDirectories(fileProductImageStorageLocation);
        } catch (Exception e){
            throw new FileStorageException("Could not create the directory where the upload files will be stored", e);
        }
    }

    //Dùng lưu thông tin về file hình của logo nhà sản xuất.
    public String storeLogoFile(MultipartFile file){
        return storeFile(fileLogoStorageLocation, file);
    }

    public String storeProductImageFile(MultipartFile file){
        return storeFile(fileProductImageStorageLocation, file);
    }

    public UploadedFileInfo storeUploadedProductImageFile(MultipartFile file){
        return storeUploadFile(fileProductImageStorageLocation, file);
    }

    //Tham số đầu vị trí file, tham số 2 : file được upload
    public String storeFile(Path location, MultipartFile file){
        //Tránh việc trùng tên file, sử dụng để tạo 1 chuổi ngẫu nhiên.
        UUID uuid = UUID.randomUUID();

        String ext = StringUtils.getFilenameExtension((file.getOriginalFilename()));

        //Tạo 1 tên file lưu trữ mới
        String filename = uuid.toString()+"."+ext;

        try{
            if(filename.contains("..")){
                throw new FileStorageException("Sorry ! Filename contains invalid path sequence "+filename);
            }

            Path targetLocation = location.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        }catch(Exception e){
            throw new FileStorageException("Could not store file "+filename+". Please try again!",e);
        }
    }

    public UploadedFileInfo storeUploadFile(Path location, MultipartFile file){
        //Tránh việc trùng tên file, sử dụng để tạo 1 chuổi ngẫu nhiên.
        UUID uuid = UUID.randomUUID();

        String ext = StringUtils.getFilenameExtension((file.getOriginalFilename()));

        //Tạo 1 tên file lưu trữ mới
        String filename = uuid.toString()+"."+ext;

        try{
            if(filename.contains("..")){
                throw new FileStorageException("Sorry ! Filename contains invalid path sequence "+filename);
            }

            Path targetLocation = location.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            UploadedFileInfo info = new UploadedFileInfo();
            info.setFileName(filename);
            info.setUid(uuid.toString());
            info.setName(StringUtils.getFilename(file.getOriginalFilename()));
            return info;
        }catch(Exception e){
            throw new FileStorageException("Could not store file "+filename+". Please try again!",e);
        }
    }
    public Resource loadLogoFileAsResource(String filename){
        return loadFileAsResource(fileLogoStorageLocation, filename);
    }
    public Resource loadProductImageFileAsResource(String filename){
        return loadFileAsResource(fileProductImageStorageLocation, filename);
    }
    public Resource loadFileAsResource(Path location, String filename){
        try{
            Path filePath = location.resolve(filename).normalize();

            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            } else {
                throw new FileNotFoundException("File not found "+filename);
            }
        } catch (Exception e){
            throw new FileNotFoundException("File not found "+filename, e);
        }
    }
    public void deleteLogoFile(String filename){
        deleteFile(fileLogoStorageLocation, filename);
    }

    public void deleteProductImageFile(String filename){
        deleteFile(fileProductImageStorageLocation, filename);
    }
    public void deleteFile(Path location, String filename){
        try{
            Path filePath = location.resolve(filename).normalize();

            if(!Files.exists(filePath)){
                throw new FileNotFoundException("File not found "+filename);
            }

            Files.delete(filePath);
        } catch (Exception e){
            throw new FileNotFoundException("File not found "+filename, e);
        }
    }
}
