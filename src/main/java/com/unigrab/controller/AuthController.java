package com.unigrab.controller;

import com.unigrab.model.dto.Token;
import com.unigrab.model.dto.*;
import com.unigrab.service.IAuthService;
import com.unigrab.service.IImageService;
import com.unigrab.service.IItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final IItemService itemService;
    private final IImageService imageService;

    @PostMapping("/register")
    public Token register(@RequestBody RegisterDto dto) {
        log.info("About to register: {}", dto);
        return authService.register(dto);
    }

    @PostMapping("/authenticate")
    public Token authenticate(@RequestBody SignInDto dto) {
        log.info("About to signIn user: {}", dto.getEmail());
        return authService.signIn(dto);
    }

    @GetMapping("/items/{offset}/{pageSize}")
    public ToPage<ItemDto> findAllItems(@PathVariable int offset,
                                        @PathVariable int pageSize,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "price", required = false) String price,
                                        @RequestParam(value = "location", required = false) String location,
                                        @RequestParam(value = "type", required = false) String[] type){

        log.info("About to find all available ItemDto");

        SearchDto searchDto = SearchDto.builder()
                .name(name)
                .price(price)
                .location(location)
                .type(type)
                .build();

        return itemService.findAllAvailableItemDto(offset, pageSize, searchDto);
    }

    @GetMapping("/item/{id}")
    public ItemDto findItem(@PathVariable String id){
        log.info("About to find ItemDto with id: {}", id);
        return itemService.findItemDto(id);
    }

    @GetMapping(value = "/image/{url}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> findImage(@PathVariable String url){
        log.info("Getting image by url: {}", url);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.DAYS))
                .body(imageService.findImageByUrl(url));
    }

}
