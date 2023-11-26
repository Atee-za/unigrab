package com.unigrab.service;

import com.unigrab.model.constant.AvailabilityStatus;
import com.unigrab.model.dto.ItemDto;
import com.unigrab.model.dto.NewItemDto;
import com.unigrab.model.dto.SearchDto;
import com.unigrab.model.dto.ToPage;
import com.unigrab.model.entity.Item;

import java.util.List;

public interface IItemService extends IService<Item, Long> {
    boolean deleteItemDto(String id);
    ItemDto findItemDto(String id);
    ItemDto saveNewItemDto(NewItemDto dto);
    ItemDto updateItemDto(ItemDto dto);
    ToPage<ItemDto> findAllAvailableItemDto(int offset, int pageSize, SearchDto searchDto);
    List<Item> findAllByUserIdAndStatusEquals(String userId, AvailabilityStatus status);
}
