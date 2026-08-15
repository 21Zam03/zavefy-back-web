package com.example.ventas_bodega.service.impl;

import com.example.ventas_bodega.dto.CompanyDto;
import com.example.ventas_bodega.dto.FileDto;
import com.example.ventas_bodega.entity.CompanyEntity;
import com.example.ventas_bodega.entity.UserEntity;
import com.example.ventas_bodega.entity.YapeEntity;
import com.example.ventas_bodega.mapper.CompanyMapper;
import com.example.ventas_bodega.repository.CompanyRepository;
import com.example.ventas_bodega.repository.UserRepository;
import com.example.ventas_bodega.repository.YapeRepository;
import com.example.ventas_bodega.response.MessageResponse;
import com.example.ventas_bodega.service.ConfigurationService;
import com.example.ventas_bodega.service.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private final CompanyRepository companyRepository;
    private final YapeRepository yapeRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final UserRepository userRepository;

    @Autowired
    public ConfigurationServiceImpl(
            CompanyRepository companyRepository,
            FirebaseStorageService firebaseStorageService,
            YapeRepository yapeRepository,
            UserRepository userRepository
    ) {
        this.companyRepository = companyRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.yapeRepository = yapeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CompanyDto getMyBussiness(Long companyId) {
        Optional<CompanyEntity> companyOptional = companyRepository.findById(companyId);
        if(companyOptional.isPresent()) {
            CompanyEntity companyEntity = companyOptional.get();
            return CompanyMapper.entityToDto(companyEntity);
        }
        return null;
    }

    @Override
    public MessageResponse updateBussiness(CompanyDto companyDto, UserEntity userEntity) throws Exception {
        MessageResponse messageResponse = new MessageResponse();
        if(companyDto.getCompanyId() == null) {
            messageResponse.setStatus(false);
            messageResponse.setMessage("El id del producto a actualizar no existe en el sistema");
            return messageResponse;
        }

        try {
            CompanyEntity companyToUpdate = companyRepository.findByRuc(userEntity.getCompany().getRuc());

            if(companyDto.getFile() != null) {
                //Cuando el cliente sube una imagen de su escritorio
                String filePath = "bodega-sistemas/clients/" + userEntity.getCompany().getRuc() + "/"+companyToUpdate.getRuc()+"-company-logo";
                FileDto fileDto = firebaseStorageService.uploadFile(companyDto.getFile(), filePath);
                companyToUpdate.setImageUrl(fileDto.getUrl());
                companyToUpdate.setFilePath(filePath);
            }

            if(companyDto.getSocialReason() != null) {
                companyToUpdate.setSocialReason(companyDto.getSocialReason());
            }

            if(companyDto.getComertialName() != null) {
                companyToUpdate.setComertialName(companyDto.getComertialName());
            }

            if(companyDto.getAddress() != null) {
                companyToUpdate.setAddress(companyDto.getAddress());
            }

            if(companyDto.getEmail() != null) {
                companyToUpdate.setEmail(companyDto.getEmail());
            }

            if(companyDto.getPhoneNumber() != null) {
                companyToUpdate.setPhoneNumber(companyDto.getPhoneNumber());
            }

            companyToUpdate.setHasBarcode(companyDto.isHasBarcode());
            companyToUpdate.setHasPrinter(companyDto.isHasPrinter());
            companyRepository.save(companyToUpdate);

            messageResponse.setStatus(true);
            messageResponse.setMessage("Negocio actualizado exitosamente");
            return messageResponse;
        } catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
            messageResponse.setStatus(false);
            messageResponse.setMessage("Error al actualizar el producto: "+e.getMessage());
        }
        return messageResponse;
    }

    @Override
    public MessageResponse updateBusinessFiscalInfo(String socialReason, UserEntity userEntity) {
        if (socialReason == null || socialReason.isBlank()) {
            return new MessageResponse(
                    "La razón social es obligatoria",
                    false
            );
        }

        int result = companyRepository.updateSocialReason(
                userEntity.getCompany().getCompanyId(),
                socialReason
        );

        if (result == 0) {
            return new MessageResponse(
                    "No se pudo actualizar la razón social",
                    false
            );
        }

        return new MessageResponse(
                "Razón social actualizada correctamente",
                true
        );
    }

    @Override
    public MessageResponse updateBusinessContactInfo(String comertialName, String address, String email, String phoneNumber, UserEntity userEntity) {
        if (comertialName == null || comertialName.isBlank()) {
            return new MessageResponse(
                    "El nombre comercial es obligatorio",
                    false
            );
        }

        int result = companyRepository.updateBusinessContactInfo(
                userEntity.getCompany().getCompanyId(),
                comertialName,
                address,
                email,
                phoneNumber
        );

        if (result == 1) {
            return new MessageResponse(
                    "Información del negocio actualizada correctamente",
                    true
            );
        }

        return new MessageResponse(
                "No se pudo actualizar la información del negocio",
                false
        );
    }

    @Override
    public MessageResponse updateBusinessOperativeInfo(String hasBarcode, String hasPrinter, UserEntity userEntity) {
        boolean barcode = Boolean.parseBoolean(hasBarcode);
        boolean printer = Boolean.parseBoolean(hasPrinter);

        Long companyId = userEntity.getCompany().getCompanyId();

        int result = companyRepository.updateBusinessConfiguration(
                companyId,
                printer,
                barcode
        );

        if (result == 1) {
            return new MessageResponse(
                    "Configuración actualizada correctamente",
                    true
            );
        }

        return new MessageResponse(
                "No se pudo actualizar la configuración",
                false
        );
    }

    @Override
    public MessageResponse updateBusinessBrandInfo(MultipartFile file, UserEntity userEntity) throws Exception {
        Long companyId = userEntity.getCompany().getCompanyId();

        // El usuario subió un nuevo logo
        if (file != null && !file.isEmpty()) {

            String filePath =
                    "bodega-sistemas/clients/"
                            + userEntity.getCompany().getRuc()
                            + "/"
                            + userEntity.getCompany().getRuc()
                            + "-company-logo";

            FileDto fileDto = firebaseStorageService.uploadFile(
                    file,
                    filePath
            );

            int result = companyRepository.updateBusinessBrandInfo(
                    companyId,
                    fileDto.getUrl(),
                    filePath
            );

            if (result == 1) {
                return new MessageResponse(
                        "Logo actualizado correctamente",
                        true
                );
            }

            return new MessageResponse(
                    "No se pudo actualizar el logo",
                    false
            );
        }

        // No se subió un nuevo archivo
        return new MessageResponse(
                "No se realizaron cambios",
                true
        );
    }

    @Override
    public MessageResponse createBusinessYape(String aliasName, String phoneNumber, boolean isDefault, MultipartFile file, UserEntity userEntity) throws Exception {
        YapeEntity yape = new YapeEntity();

        yape.setAliasName(aliasName);
        yape.setPhoneNumber(phoneNumber);
        yape.setDefault(isDefault);
        yape.setCompany(userEntity.getCompany());

        if (file != null && !file.isEmpty()) {

            String filePath =
                    "bodega-sistemas/clients/"
                            + userEntity.getCompany().getRuc()
                            + "/yapes/"
                            + phoneNumber;

            FileDto fileDto =
                    firebaseStorageService.uploadFile(
                            file,
                            filePath
                    );

            yape.setImageQr(fileDto.getUrl());
        }

        yapeRepository.save(yape);

        return new MessageResponse(
                "Yape registrado correctamente",
                true
        );
    }

    @Override
    public MessageResponse updateBusinessYape(Integer yapeId, String aliasName, String phoneNumber, boolean isDefault, MultipartFile qrFile, UserEntity user) throws Exception {
        Long companyId = user.getCompany().getCompanyId();

        YapeEntity yape = yapeRepository
                .findByYapeIdAndCompanyCompanyId(yapeId, companyId)
                .orElseThrow(() ->
                        new RuntimeException("Yape no encontrado")
                );

        yape.setAliasName(aliasName);
        yape.setPhoneNumber(phoneNumber);
        yape.setDefault(isDefault);

        if (qrFile != null && !qrFile.isEmpty()) {

            String filePath =
                    "bodega-sistemas/clients/"
                            + user.getCompany().getRuc()
                            + "/yapes/"
                            + phoneNumber;

            FileDto fileDto =
                    firebaseStorageService.uploadFile(
                            qrFile,
                            filePath
                    );

            yape.setImageQr(fileDto.getUrl());
        }

        yapeRepository.save(yape);

        return new MessageResponse(
                "Yape actualizado correctamente",
                true
        );
    }

    @Override
    public MessageResponse deleteBusinessYape(Integer yapeId, UserEntity user) {
        Long companyId = user.getCompany().getCompanyId();

        YapeEntity yape = yapeRepository
                .findByYapeIdAndCompanyCompanyId(yapeId, companyId)
                .orElseThrow(() ->
                        new RuntimeException("Yape no encontrado")
                );

        boolean wasDefault = yape.isDefault();
        yapeRepository.delete(yape);

        if (wasDefault) {
            // Buscar otro Yape y convertirlo en default
            Optional<YapeEntity> nextYape =
                    yapeRepository.findFirstByCompanyCompanyIdOrderByYapeIdAsc(
                            companyId
                    );

            nextYape.ifPresent(next -> {
                next.setDefault(true);
                yapeRepository.save(next);
            });
        }

        return new MessageResponse(
                "Yape eliminado correctamente",
                true
        );
    }

    @Override
    public MessageResponse updateAccountInfo(String firstname, String lastname, String email, UserEntity userEntity) {
        Optional<UserEntity> existingUser =
                userRepository.findByEmail(email);
        if (existingUser.isPresent()
                && !existingUser.get().getUserId().equals(userEntity.getUserId())) {
            return new MessageResponse(
                    "El correo electrónico ya está registrado",
                    false
            );
        }

        int result = userRepository.updateAccountInfo(
                userEntity.getUserId(),
                firstname,
                lastname,
                email,
                userEntity.getUserId().longValue()
        );

        if (result == 1) {
            return new MessageResponse(
                    "Información de cuenta actualizada correctamente",
                    true
            );
        }

        return new MessageResponse(
                "No se pudo actualizar la información de la cuenta",
                false
        );
    }

}
