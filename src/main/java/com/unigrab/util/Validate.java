package com.unigrab.util;

import com.unigrab.model.dto.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.Objects;
import static com.unigrab.util.HandleExceptions.throwBadRequestException;
import static com.unigrab.util.Common.*;


@Data
@Slf4j
public class Validate {

    public static void campusDto(CampusDto dto){
        log.info("Validating: {}", dto);

        if(dto == null){
            throwBadRequestException("0040", "Campus object not provided");
        }

       /* try{ TODO: remove this, no longer using entity id, now id can be ""
            if(!isValidLong(dto.getId())){
                throwBadRequestException("0041", "Invalid id provided in Campus");
            }
        } catch (Exception e){
            throwBadRequestException("0042", "Invalid id provided in Campus");
        } */

        if(!isValidString(dto.getCampusName())){
            throwBadRequestException("0043", "Invalid campus name provided in Campus");
        }

        if(!isValidString(dto.getSchoolName())){
            throwBadRequestException("0044", "Invalid school name provided in Campus");
        }

        if(!isValidString(dto.getSuburb())){
            throwBadRequestException("0045", "Invalid suburb provided in Campus");
        }

    }

    /*public static Set<Error> collageDto(CollageDto dto){
        log.info("Validating: {}", dto);
        errors = new HashSet<>();

        if(dto == null){
            errors.add(Error.builder().code("0050").description("Collage object not provided").build());
            return errors;
        }

        try {
            if(!isValidLong(dto.getId())){
                errors.add(Error.builder().code("0041").description("Invalid id provided in Collage").build());
            }
        } catch (Exception e){
            errors.add(Error.builder().code("0042").description("Invalid id provided in Collage").build());
        }

        if(!isValid(dto.getName())){
            errors.add(Error.builder().code("0043").description("Invalid name provided in Collage").build());
        }

        return errors;
    }*/

    public static void imageDto(ImageDto dto){
        log.info("Validating: {}", dto);

        if(dto == null){
            throwBadRequestException("0060", "Image object not provided");
        }

        if(!isValidString(dto.getId())){
            throwBadRequestException("0061", "Invalid id provided in Image");
        }

        /*
        try { TODO: replaced by the check above, no longer using entity id, now id can be string a.k.a. UUID
            if(!isValidLong(dto.getId())){
                throwBadRequestException("0061", "Invalid id provided in Image");
            }
        }
        catch (Exception e){
            throwBadRequestException("0062", "Invalid id provided in Image");
        }*/

        if(!isValidString(dto.getUrl())){
            throwBadRequestException("0063", "Invalid url provided in Image");
        }

        if(!isValidString(dto.getItemId())){
            throwBadRequestException("0064", "Invalid itemId provided in Image");
        }
        /*
        try{ TODO: replaced by the check above, no longer using entity id, now id can be string a.k.a. UUID
            if(!isValidLong(dto.getItemId())){
                throwBadRequestException("0064", "Invalid itemId provided in Image");
            }
        } catch (Exception e){
            throwBadRequestException("0065", "Invalid itemId provided in Image");
        } */

    }

    public static void itemDto(ItemDto dto){
        log.info("Validating: {}", dto);

        if(dto == null){
            throwBadRequestException("0070", "Item object not provided");
        }

        if(!isValidString(dto.getId())){
            throwBadRequestException("0071", "Invalid id provided in Item");
        }
        /*try { TODO: replaced by the check above, no longer using entity id, now id can be string a.k.a. UUID
            if(!isValidLong(dto.getId())){
                throwBadRequestException("0071", "Invalid id provided in Item");
            }
        }
        catch (Exception e){
            throwBadRequestException("0072", "Invalid id provided in Item");
        }*/

        if(!isValidString(dto.getName())){
            throwBadRequestException("0073", "Invalid name provided in Item");
        }

        if(!isValidString(dto.getType())){
            throwBadRequestException("0074", "Invalid type provided in Item");
        }

        try{
            if(!isValidDouble(dto.getPrice())){
                throwBadRequestException("0075", "Invalid price provided in Item");
            }
        } catch (Exception e){
            throwBadRequestException("0076", "Invalid price provided in Item");
        }

        if(!isValidString(dto.getDescription())){
            throwBadRequestException("0077", "Invalid description provided in Item");
        }

        if(!isValidString(dto.getTown())){
            throwBadRequestException("0170", "Invalid town provided in Item");
        }

        if(!isValidString(dto.getSuburb())){
            throwBadRequestException("0171", "Invalid suburb provided in Item");
        }

        dto.getImages().forEach(Validate::imageDto);

    }

    public static void townDto(TownDto dto){
        log.info("Validating: {}", dto);

        if(dto == null){
            throwBadRequestException("0080", "Town object not provided");
        }

        if(!isValidString(dto.getTownName())){
            throwBadRequestException("0083", "Invalid townName provided in Town");
        }

        if(!isValidString(dto.getSuburb())){
            throwBadRequestException("0084", "Invalid suburb provided in Town");
        }

    }

    /*public static void universityDto(UniversityDto dto){
        log.info("Validating: {}", dto);
        if(dto == null){
            errors.add(Error.builder().code("0090").description("University object not provided").build());
            return errors;
        }

        try {
            if(!isValidLong(dto.getId())){
                errors.add(Error.builder().code("0091").description("Invalid id provided in University").build());
            }
        } catch (Exception e){
            errors.add(Error.builder().code("0092").description("Invalid id provided in University").build());
        }

        if(!isValid(dto.getName())){
            errors.add(Error.builder().code("0093").description("Invalid name provided in University").build());
        }

        return errors;
    }*/

    public static void signInDto(SignInDto dto){
        log.info("Validating with email: {}", dto.getEmail());

        if(!isValidEmail(dto.getEmail()) || !isValidPassword(dto.getPassword())){
            throwBadRequestException("0110", "Invalid credentials provided");
        }

    }

    public static void registerDto(RegisterDto dto){
        log.info("Validating: {}", dto.getEmail());

        if(!isValidEmail(dto.getEmail())){
            throwBadRequestException("0120", "Invalid email provided");
        }

        if(!isValidPassword(dto.getPassword()) || !isValidPassword(dto.getConfirmPassword())){
            throwBadRequestException("0122", "Invalid password/confirm password. Minimum length required: 6");
        } else if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throwBadRequestException("0123", "Password and confirm password do not match");
        }

    }

    public static void newItemDto(NewItemDto dto){
        log.info("Validating: {}", dto);

        int ALLOWED_IMAGE_SIZE = 5000000;
        int ALLOWED_IMAGES_NUMBER = 3;
        boolean fileTypeError = false;
        boolean fileSizeError = false;

        if(dto == null){
            throwBadRequestException("0080", "New Item object not provided");
        }

        if(!isValidString(dto.getName())){
            throwBadRequestException("0081", "Invalid name provided in New Item");
        }

        if(!isValidString(dto.getType())){
            throwBadRequestException("0082", "Invalid type provided in New Item");
        }

        try{
            if(!isValidDouble(dto.getPrice())){
                throwBadRequestException("0083", "Invalid price provided in New Item");
            }
        } catch (Exception e){
            throwBadRequestException("0084", "Invalid price provided in New Item");
        }

        if(!isValidString(dto.getDescription())){
            throwBadRequestException("0085", "Invalid description provided in New Item");
        }

        if(dto.getImages() == null || (dto.getImages().isEmpty() || dto.getImages().size() > ALLOWED_IMAGES_NUMBER)){
            throwBadRequestException("0086", "Invalid Image length in New Item, expected (1 - 3) image(s) per Item");
        }

        for(MultipartFile file : dto.getImages()){
            if(!Objects.equals(file.getContentType(), "image/jpg") && !Objects.equals(file.getContentType(), "image/jpeg")
                    && !Objects.equals(file.getContentType(), "image/png")){
                fileTypeError = true;
            }
            if(file.getSize() > ALLOWED_IMAGE_SIZE){
                fileSizeError = true;
            }
        }

        if(fileTypeError){
            throwBadRequestException("0087", "Invalid Image format detected. Only jpg, jpeg and png accepted");
        }

        if(fileSizeError){
            throwBadRequestException("0088", "Invalid Image size detected. Max size 5MB");
        }

    }

    public static void passwordUpdateDto(PasswordUpdate dto){
        log.info("Validating Password Update request");

        if(dto == null){
            throwBadRequestException("XX01", "Password object not provided");
        }

        if(!isValidPassword(dto.getCurrentPassword())){
            throwBadRequestException("XX02", "Invalid current password provided");
        }

        if(!dto.getNewPassword().equals(dto.getConfirmPassword())){
            throwBadRequestException("XX03", "New password do not match with confirm password");
        }

        if(!isValidPassword(dto.getNewPassword())){
            throwBadRequestException("XX04", "Invalid new password provided");
        }

    }

    public static void searchData(SearchDto dto){
        log.info("Validating Search Parameters: {}", dto);

        if(!ObjectUtils.isEmpty(dto.getPrice())){
            try{
                Integer.parseInt(dto.getPrice());
            } catch (NumberFormatException e){
                throwBadRequestException("PO99", "Invalid Price provided");
            }
        }

        if(dto.getName() != null){
            dto.setName(dto.getName().trim());
        }
        if(dto.getLocation() != null){
            dto.setLocation(dto.getLocation().trim());
        }

    }

}