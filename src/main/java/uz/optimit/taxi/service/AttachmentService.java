package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.optimit.taxi.entity.Attachment;
import uz.optimit.taxi.exception.FileUploadException;
import uz.optimit.taxi.exception.OriginalFileNameNullException;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.repository.AttachmentRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Value("${attach.upload.folder}")
    public  String attachUploadFolder;

    @Value("${attach.download.url}")
    public  String attachDownloadUrl;


    public String getYearMonthDay() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        return year + "/" + month; // 2022/03
    }


    public String getExtension(String fileName) {
        if (fileName == null) {
            throw new OriginalFileNameNullException(FILE_NAME_NULL);
        }
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public Attachment saveToSystem(MultipartFile file) {
        try {
            String pathFolder = getYearMonthDay();
            File folder = new File(attachUploadFolder + pathFolder);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename());

            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachUploadFolder + pathFolder + "/" + fileName + "." + extension);
           Files.write(path, bytes).toFile();

            Attachment entity = new Attachment();
            entity.setNewName(fileName);
            entity.setOriginName(file.getOriginalFilename());
            entity.setType(extension);
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            entity.setContentType(file.getContentType());

            return attachmentRepository.save(entity);
        } catch (IOException e) {
            throw new FileUploadException(FILE_COULD_NOT_UPLOADED);
        }

    }
    public List<Attachment> saveToSystemListFile(List<MultipartFile> fileList){
        List<Attachment> attachments = new ArrayList<>();
        fileList.forEach((file)->{
            attachments.add(saveToSystem(file));
        });
        return attachments;
    }

    private Attachment getAttachment(String fileName) {
        String newName = fileName.split("\\.")[0];
        Optional<Attachment> optional = attachmentRepository.findByNewName(newName);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException(FILE_NOT_FOUND);
        }
        return optional.get();
    }

    public byte[] open(String fileName) {
        try {
            Attachment entity = getAttachment(fileName);

            Path file = Paths.get(attachDownloadUrl + entity.getPath() + "/" + fileName+"."+entity.getType());
            return Files.readAllBytes(file);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    public AttachmentDownloadDTO download(String fileName) {
//        try {
//            Attachment entity = getAttachment(fileName);
//
//            File file = new File(attachUploadFolder + entity.getPath() + "/" + fileName);
//
//            File dir = file.getParentFile();
//            File rFile = new File(dir, entity.getNewName() + "." + entity.getType());
//
//            Resource resource = new UrlResource(rFile.toURI());
//
//            if (resource.exists() || resource.isReadable()) {
//                AttachmentDownloadDTO attachmentDownloadDTO = new AttachmentDownloadDTO(resource, "image/jpeg");
//                return attachmentDownloadDTO;
//            } else {
//                throw new CouldNotRead("Could not read");
//            }
//        } catch (MalformedURLException | FileNotFoundException e) {
//            throw new SomethingWentWrong("Something went wrong");
//        }
//    }


//    public Page<AttachmentResponseDTO> getWithPage(Integer page, Integer size) {
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<Attachment> pageObj = repository.findAll(pageable);
//
//        List<Attachment> entityList = pageObj.getContent();
//        List<AttachmentResponseDTO> dtoList = new ArrayList<>();
//
//
//        for (Attachment entity : entityList) {
//
//            AttachmentResponseDTO dto = new AttachmentResponseDTO();
//            dto.setId(entity.getId());
//            dto.setPath(entity.getPath());
//            dto.setExtension(entity.getType());
//            dto.setUrl(attachUploadFolder + "/" + entity.getId() + "." + entity.getType());
//            dto.setOriginalName(entity.getOriginName());
//            dto.setSize(entity.getSize());
//            dto.setCreatedData(entity.getCreatedDate());
//            dtoList.add(dto);
//        }
//
//        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
//    }


//    public String deleteById(String fileName) {
//        try {
//            Attachment entity = getAttachment(fileName);
//            Path file = Paths.get(attachUploadFolder + entity.getPath() + "/" + fileName);
//
//            Files.delete(file);
//            repository.deleteById(entity.getId());
//
//            return "deleted";
//        } catch (
//                IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
    public String getUrl(UUID imageId)  {
        Optional<Attachment> optional = attachmentRepository.findById(imageId);
        if (optional.isEmpty()) {
            throw new RecordNotFoundException("File not found");
        }
        return attachUploadFolder + optional.get().getId() + "." + optional.get().getType();
    }


}

