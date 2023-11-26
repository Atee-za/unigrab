package com.unigrab.controller;

import com.unigrab.model.dto.ItemDto;
import com.unigrab.model.dto.NewItemDto;
import com.unigrab.service.IItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final IItemService itemService;

    @PostMapping("/save")
    public ItemDto uploadImage(@RequestParam("name") String name,
                               @RequestParam("type") String type,
                               @RequestParam("price") String price,
                               @RequestParam("description") String description,
                               @RequestParam("images") Set<MultipartFile> images){

        log.info("About to save new item; name: {}, type: {}, price: R {}, description: {}", name, type, price, description);
        log.info("About to save new Img size: {}", images.size());

        NewItemDto dto = NewItemDto.builder()
                .name(name)
                .type(type)
                .price(price)
                .description(description)
                .images(images)
                .build();

        return itemService.saveNewItemDto(dto);
    }

    @PostMapping("/update")
    public ItemDto update(@RequestBody ItemDto dto) {
        log.info("About to update ItemDto with: {}", dto);
        return itemService.updateItemDto(dto);
    }

    @GetMapping("/delete/{id}")
    public boolean delete(@PathVariable String id) {
        log.info("About to delete ItemDto with id: {}", id);
        return itemService.deleteItemDto(id);
    }

}
