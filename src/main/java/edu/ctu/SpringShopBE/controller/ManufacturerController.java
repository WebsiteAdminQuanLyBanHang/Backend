package edu.ctu.SpringShopBE.controller;

import edu.ctu.SpringShopBE.dto.ManufacturerDto;
import edu.ctu.SpringShopBE.entity.Manufacturer;
import edu.ctu.SpringShopBE.exception.entityException.FileStorageException;
import edu.ctu.SpringShopBE.service.FileStorageService;
import edu.ctu.SpringShopBE.service.ManufacturerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/manufacturers")
public class ManufacturerController {
    private ManufacturerService manufacturerService;
    private FileStorageService fileStorageService;

    @Autowired
    public ManufacturerController(ManufacturerService manufacturerService, FileStorageService fileStorageService) {
        this.manufacturerService = manufacturerService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createManufacrer(@Valid @ModelAttribute ManufacturerDto dto){
        Manufacturer entity = manufacturerService.insertManufacturer(dto);
        dto.setId(entity.getId());
        dto.setLogo(entity.getLogo());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/logo/{filename:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadLogoFileAsResource(filename);

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch(Exception e){
            throw new FileStorageException("Could not determine file type.");
        }

        if(contentType==null){
            contentType="application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attacchment;filename=\""
                    + resource.getFilename()+"\"")
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<?> getManufacturer(){
        var list = manufacturerService.findAll();
        var newList = list.stream().map(item -> {
            ManufacturerDto dto = new ManufacturerDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect((Collectors.toList()));
        return new ResponseEntity<>(newList,HttpStatus.OK);
    }

//    @GetMapping("/page")
//    public ResponseEntity<?> getManufacturer(@PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
//        var list = manufacturerService.findAll();
//        var newList = list.stream().map(item -> {
//            ManufacturerDto dto = new ManufacturerDto();
//            BeanUtils.copyProperties(item, dto);
//            return dto;
//        }).collect((Collectors.toList()));
//        return new ResponseEntity<>(newList,HttpStatus.OK);
//    }
    @GetMapping("/page")
    public ResponseEntity<Page<ManufacturerDto>> getManufacturer(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Manufacturer> manufacturerPage = manufacturerService.findAll(pageable);
        List<ManufacturerDto> manufacturerDtoList = manufacturerPage.getContent().stream()
                .map(manufacturer -> {
                    ManufacturerDto dto = new ManufacturerDto();
                    BeanUtils.copyProperties(manufacturer, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(new PageImpl<>(manufacturerDtoList, pageable, manufacturerPage.getTotalElements()), HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getManufacturer(@RequestParam("query") String query,
                                             @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        var list = manufacturerService.findByName(query, pageable);

        var newList = list.getContent().stream().map(item -> {
            ManufacturerDto dto = new ManufacturerDto();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect((Collectors.toList()));

        var newPage = new PageImpl<ManufacturerDto>(newList,list.getPageable(),list.getTotalPages());
        return new ResponseEntity<>(newPage,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getManufacturer(@PathVariable Long id){
        var entity = manufacturerService.findById(id);
        ManufacturerDto dto = new ManufacturerDto();
        BeanUtils.copyProperties(entity, dto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable Long id){
        manufacturerService.deleteManufacturerById(id);
        return new ResponseEntity<>("Manufacturer with id "+id+ " was deleted",HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateManufacturer(@PathVariable Long id, @RequestBody ManufacturerDto dto){
        Manufacturer manufacturer = manufacturerService.updateManufacturer(id,dto);
        return new ResponseEntity<>(manufacturer, HttpStatus.OK);
    }
}
