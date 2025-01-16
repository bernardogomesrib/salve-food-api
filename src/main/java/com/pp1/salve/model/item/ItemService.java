package com.pp1.salve.model.item;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pp1.salve.api.item.ItemRequest;
import com.pp1.salve.api.item.ItemRequestSave;
import com.pp1.salve.exceptions.ResourceNotFoundException;
import com.pp1.salve.minio.MinIOInterfacing;
import com.pp1.salve.model.loja.LojaService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final MinIOInterfacing minIOInterfacing;
    private final ItemRepository repository;
    private final LojaService lojaService;
    private final CategoriaItemService categoriaItemService;

    @Transactional
    public Page<Item> findAll(Pageable pageable) throws Exception {
        Page<Item> items = repository.findAll(pageable);
        return monta(items);
    }

    @Transactional
    public Item findById(Long id) throws Exception {
        Item i = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com id: " + id));

        return monta(i);
    }

    @Transactional
    public Item save(ItemRequestSave itemRequest) throws Exception {
        Item i = itemRequest.build();
        i.setLoja(lojaService.findById(itemRequest.getLojaId()));
        i.setCategoriaItem(categoriaItemService.findById(itemRequest.getCategoriaItemId()));
        i = repository.save(i);

        i.setItemImage(minIOInterfacing.uploadFile(itemRequest.getLojaId() + "loja", i.getId().toString(),
                itemRequest.getFile()));
        return i;
    }

    @Transactional
    public Item save(Item item) {
        Item i = repository.save(item);
        return i;
    }

    public void deleteById(Long id) throws Exception {
        Item i = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com id: " + id));
        minIOInterfacing.deleteFile(i.getLoja().getId() + "loja", i.getId().toString());
        repository.delete(i);
    }

    public Item editarItem(Long id, ItemRequest itemRequest) throws Exception {
        Item i = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com id: " + id));
        i.setNome(itemRequest.getNome());
        i.setValor(itemRequest.getValor());
        i.setAlteravel(itemRequest.getAlteravel());
        i.setLoja(lojaService.findById(itemRequest.getLojaId()));
        i.setCategoriaItem(categoriaItemService.findById(itemRequest.getCategoriaItemId()));
        i = repository.save(i);
        if (itemRequest.getFile() != null) {
            i.setItemImage(minIOInterfacing.uploadFile(itemRequest.getLojaId() + "loja", i.getId().toString(),
                    itemRequest.getFile()));
        } else {
            i = monta(i);
        }
        return i;
    }

    public List<Item> findByLojaId(Long id) throws Exception {
        return monta(repository.findByLojaId(id));
    }

    @Transactional(readOnly = true)
    public Item monta(Item i) throws Exception {
        i.setItemImage(minIOInterfacing.getSingleUrl(i.getLoja().getId() + "loja", i.getId().toString()));
        return i;
    }

    @Transactional(readOnly = true)
    public List<Item> monta(List<Item> items) throws Exception {
        for (Item item : items) {
            item = monta(item);
        }
        return items;
    }

    @Transactional(readOnly = true)
    public Page<Item> monta(Page<Item> items) throws Exception {
        for (Item item : items) {
            item = monta(item);
        }
        return items;
    }

}
