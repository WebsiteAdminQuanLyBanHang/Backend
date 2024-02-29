package edu.ctu.SpringShopBE.service;

import edu.ctu.SpringShopBE.dto.ManufacturerDto;
import edu.ctu.SpringShopBE.entity.Manufacturer;
import edu.ctu.SpringShopBE.exception.entityException.ManufacturerException;
import edu.ctu.SpringShopBE.repository.ManufacturerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {
    private ManufacturerRepository manufacturerRepository;
    private FileStorageService fileStorageService;

    @Autowired
    public ManufacturerService(ManufacturerRepository manufacturerRepository, FileStorageService fileStorageService) {
        this.manufacturerRepository = manufacturerRepository;
        this.fileStorageService = fileStorageService;
    }



    public Manufacturer insertManufacturer(ManufacturerDto dto){
        // Kiểm tra xem đã có nhà sản xuất với tên tương tự chưa
        List<?> foundList = manufacturerRepository.findByNameContainsIgnoreCase(dto.getName());
        if(foundList.size() > 0){
            throw new ManufacturerException("Manufacturer name is already existed");
        }

        // Nếu không tìm thấy nhà sản xuất có tên tương tự, tiếp tục thêm mới
        Manufacturer manufacturer = new Manufacturer();

        // Copy dữ liệu từ DTO sang Entity sử dụng BeanUtils.copyProperties
        BeanUtils.copyProperties(dto, manufacturer);

        // Kiểm tra xem có tệp tin đính kèm (logoFile) không
        if(dto.getLogoFile() != null){
            // Nếu có, lưu trữ tệp tin và lưu tên tệp vào trường 'logo' của nhà sản xuất
            String filename = fileStorageService.storeLogoFile(dto.getLogoFile());
            manufacturer.setLogo(filename);
            dto.setLogoFile(null); // Gán null để tránh việc lưu trữ thông tin về tệp tin trong cơ sở dữ liệu
        }
        // Lưu nhà sản xuất mới vào cơ sở dữ liệu
        return manufacturerRepository.save(manufacturer);
    }

    public Manufacturer updateManufacturer(Long id,ManufacturerDto dto){
        Manufacturer manufacturer = findById(id);
        manufacturer.setName(dto.getName());
        manufacturer.setLogo(dto.getLogo());

        return manufacturer;
    }
    public List<?> findAll(){
        return manufacturerRepository.findAll();
    }


    public Page<Manufacturer> findByName(String name, Pageable pageable){
        return manufacturerRepository.findByNameContainsIgnoreCase(name, pageable);
    }
    public Page<Manufacturer> findAll(Pageable pageable){
        return manufacturerRepository.findAll(pageable);
    }

    public Manufacturer findById(Long id){
        Optional<Manufacturer> found = manufacturerRepository.findById(id);

        if(found.isEmpty()){
            throw new ManufacturerException("Manufacturer with id : "+id+" does not existed");
        }
        return found.get();
    }

    public void deleteManufacturerById(Long id){
        Manufacturer manufacturer = findById(id);
        manufacturerRepository.delete(manufacturer);
    }


}
