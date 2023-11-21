package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Shop;
import hu.bme.aut.haulagecompany.model.dto.ShopDTO;
import hu.bme.aut.haulagecompany.repository.ShopRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public ShopService(ShopRepository shopRepository, ModelMapper modelMapper) {
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
    }

    public ShopDTO createShop(ShopDTO shopDTO) {
        Shop shop = convertToEntity(shopDTO);
        shop.setOrders(new ArrayList<>());

        Shop createdShop = shopRepository.save(shop);
        return convertToDTO(createdShop);
    }

    public List<ShopDTO> getAllShops() {
        List<Shop> shops = (List<Shop>) shopRepository.findAll();
        return shops.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ShopDTO getShopDTOById(Long id) {
        Optional<Shop> shop = shopRepository.findById(id);
        return shop.map(this::convertToDTO).orElse(null);
    }

    public Shop getShopById(Long id) {
        Optional<Shop> shop = shopRepository.findById(id);
        return shop.orElse(null);
    }

    public ShopDTO updateShop(Long id, ShopDTO updatedShopDTO) {
        Optional<Shop> existingShop = shopRepository.findById(id);

        if (existingShop.isPresent()) {
            Shop updatedShop = convertToEntity(updatedShopDTO);
            updatedShop.setId(id);

            Shop savedShop = shopRepository.save(updatedShop);
            return convertToDTO(savedShop);
        } else {
            return null;
        }
    }

    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }

    private ShopDTO convertToDTO(Shop shop) {
        return modelMapper.map(shop, ShopDTO.class);
    }

    private Shop convertToEntity(ShopDTO shopDTO) {
        return modelMapper.map(shopDTO, Shop.class);
    }
}
