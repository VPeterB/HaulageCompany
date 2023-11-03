package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Shop;
import hu.bme.aut.haulagecompany.model.dto.ShopDTO;
import hu.bme.aut.haulagecompany.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;

    public Shop createShop(ShopDTO shopDTO) {
        Shop shop = new Shop();
        shop.setName(shopDTO.getName());
        shop.setLocationId(shopDTO.getLocationId());
        shop.setContact(shopDTO.getContact());
        return shopRepository.save(shop);
    }

    public Iterable<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    public Shop getShopById(Long id) {
        return shopRepository.findById(id).orElse(null);
    }

    public Shop updateShop(Long id, ShopDTO shopDTO) {
        Shop existingShop = getShopById(id);
        if (existingShop != null) {
            existingShop.setName(shopDTO.getName());
            existingShop.setLocationId(shopDTO.getLocationId());
            existingShop.setContact(shopDTO.getContact());
            return shopRepository.save(existingShop);
        }
        return null;
    }

    public void deleteShop(Long id) {
        shopRepository.deleteById(id);
    }
}
