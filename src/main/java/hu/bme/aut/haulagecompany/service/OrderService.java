package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.*;
import hu.bme.aut.haulagecompany.model.dto.*;
import hu.bme.aut.haulagecompany.repository.OrderRepository;
import hu.bme.aut.haulagecompany.repository.OrderedGoodRepository;
import hu.bme.aut.haulagecompany.repository.TransportOperationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderedGoodRepository orderedGoodRepository;
    private final ModelMapper modelMapper;
    private final ShopService shopService;
    private final GoodService goodService;
    private final TransportOperationRepository transportOperationRepository;
    private final VehicleService vehicleService;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            OrderedGoodRepository orderedGoodRepository,
            ShopService shopService,
            GoodService goodService,
            ModelMapper modelMapper,
            TransportOperationRepository transportOperationRepository,
            VehicleService vehicleService) {
        this.orderedGoodRepository = orderedGoodRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.shopService = shopService;
        this.goodService = goodService;
        this.transportOperationRepository = transportOperationRepository;
        this.vehicleService = vehicleService;
    }

    public GetOrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        if(orderDTO.getShopID() == null){
            return null;
        }
        order.setShop(shopService.getShopById(orderDTO.getShopID()));
        if(order.getShop() == null){
            return null;
        }
        Order createdOrder = orderRepository.save(order);
        if(orderDTO.getStackedGoodDTOs() == null){
            return null;
        }else if (orderDTO.getStackedGoodDTOs().isEmpty()){
            return null;
        }else {
            Long orderId = createdOrder.getId();
            List<OrderedGood> goodList = new ArrayList<>();
            for(StackedGoodDTO g : orderDTO.getStackedGoodDTOs()){
                goodList.add(convertToOrderedGood(orderId, g));
            }
            List<OrderedGood> aggGoodList = aggregateGoods(goodList, true);
            List<OrderedGood> savedAggGoodList = saveGoods(aggGoodList);
            createdOrder.setGoods( savedAggGoodList);
        }
        Order newCreatedOrder = orderRepository.save(createdOrder);
        return convertToGetDTO(newCreatedOrder);
    }

    private List<OrderedGood> saveGoods(List<OrderedGood> aggregatedGoodList) {
        List<OrderedGood> res = new ArrayList<>();
        for(OrderedGood g : aggregatedGoodList){
            res.add(orderedGoodRepository.save(g));
        }
        return res;
    }

    public List<OrderedGood> aggregateGoods(List<OrderedGood> goods, boolean add){
        Map<String, OrderedGood> aggregatedGoods = new HashMap<>();
        for (OrderedGood good : goods) {
            String key = getKey(good);
            if (aggregatedGoods.containsKey(key)) {
                OrderedGood existingGood = aggregatedGoods.get(key);
                if(add){
                    existingGood.setQuantity(existingGood.getQuantity() + good.getQuantity());
                }else{
                    existingGood.setQuantity(existingGood.getQuantity() - good.getQuantity());
                }
            } else {
                aggregatedGoods.put(key, good);
            }
        }
        return new ArrayList<>(aggregatedGoods.values());
    }

    private String getKey(OrderedGood good) { //Good unique key
        if(good.getGood() != null){
            return good.getGood().getName() + "_" + good.getGood().getSize() + "_" + good.getGood().getWeight();
        }
        return "";
    }

    private OrderedGood convertToOrderedGood(Long orderId, StackedGoodDTO g) {
        OrderedGood og = new OrderedGood();
        Order o = orderRepository.findById(orderId).orElse(null);
        og.setOrder(o);
        og.setQuantity(g.getQuantity());
        Good good = goodService.findById(g.getGoodId()).orElse(null);
        og.setGood(good);
        return og;

    }

    private GetOrderDTO convertToGetDTO(Order createdOrder) {
        GetOrderDTO newDTO = modelMapper.map(createdOrder, GetOrderDTO.class);
        newDTO.setShopDTO(shopService.convertToDTO(createdOrder.getShop()));
        newDTO.setGoodDTOs(convertToGetStackedGoodDTO(createdOrder.getGoods()));
        if(createdOrder.getTransportOperation() != null){
            newDTO.setTransportOperationID(createdOrder.getTransportOperation().getId());
        }
        return newDTO;
    }

    private List<GetStackedGoodDTO> convertToGetStackedGoodDTO(List<OrderedGood> goods) {
        List<GetStackedGoodDTO> sgList = new ArrayList<>();
        for(OrderedGood g : goods){
            GetStackedGoodDTO sgDTO = new GetStackedGoodDTO();
            GoodDTO gDTO = goodService.convertToDTO(g.getGood());
            if(gDTO == null){
                continue;
            }
            sgDTO.setGoodDTO(gDTO);
            sgDTO.setQuantity(g.getQuantity());
            sgList.add(sgDTO);
        }
        return sgList;
    }

    public List<GetOrderDTO> getAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .map(this::convertToGetDTO)
                .toList();
    }

    public GetOrderDTO getOrderDTOById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(this::convertToGetDTO).orElse(null);
    }

    public GetOrderDTO updateOrder(Long id, OrderDTO updatedOrderDTO) {
        Optional<Order> existingOrder = orderRepository.findById(id);

        if (existingOrder.isPresent()) {
            Order updatedOrder = existingOrder.get();
            updatedOrder.setId(id);
            updatedOrder.setShop(shopService.getShopById(updatedOrderDTO.getShopID()));
            if(updatedOrderDTO.getStackedGoodDTOs() != null && !updatedOrderDTO.getStackedGoodDTOs().isEmpty()){
                List<OrderedGood> goodList = updatedOrder.getGoods();
                updatedOrder.setGoods(new ArrayList<>());
                orderedGoodRepository.deleteAll(goodList);
            }
            Order savedOrder = orderRepository.save(updatedOrder);
            if(updatedOrderDTO.getStackedGoodDTOs() != null && !updatedOrderDTO.getStackedGoodDTOs().isEmpty()){
                Long orderId = savedOrder.getId();
                List<OrderedGood> goodList = savedOrder.getGoods();
                goodList.removeAll(savedOrder.getGoods());
                for(StackedGoodDTO g : updatedOrderDTO.getStackedGoodDTOs()){
                    goodList.add(convertToOrderedGood(orderId, g));
                }
                List<OrderedGood> aggGoodList = aggregateGoods(goodList, true);
                List<OrderedGood> savedAggGoodList = saveGoods(aggGoodList);
                savedOrder.setGoods(savedAggGoodList);
                if(savedOrder.getTransportOperation() != null){
                    Long toid = savedOrder.getTransportOperation().getId();
                    savedOrder.setTransportOperation(null);
                    orderRepository.save(savedOrder);
                    var to = transportOperationRepository.findById(toid);
                    if(to.isPresent()){
                        List<Vehicle> vl = to.get().getUsedVehicles();
                        for(var v : vl){
                            vehicleService.removeTransportOperation(v, to);
                        }
                        to.get().setUsedVehicles(null);
                    }
                    transportOperationRepository.deleteById(toid);
                }
            }
            Order newCreatedOrder = orderRepository.save(savedOrder);
            return convertToGetDTO(newCreatedOrder);
        } else {
            return null;
        }
    }

    public void deleteOrder(Long id) {
        Optional<Order> o = orderRepository.findById(id);
        if(o.isPresent()){
            Order realO = o.get();
            shopService.removeOrder(realO);
            List<OrderedGood> ogList = realO.getGoods();
            orderedGoodRepository.deleteAll(ogList);
            realO.setGoods(null);
            realO.setShop(null);
            if(realO.getTransportOperation() != null){
                Long toid = realO.getTransportOperation().getId();
                realO.setTransportOperation(null);
                orderRepository.save(realO);
                var to = transportOperationRepository.findById(toid);
                if(to.isPresent()){
                    List<Vehicle> vl = to.get().getUsedVehicles();
                    for(var v : vl){
                        vehicleService.removeTransportOperation(v, to);
                    }
                    to.get().setUsedVehicles(null);
                }
                transportOperationRepository.deleteById(toid);
            }
            realO.setTransportOperation(null);
            orderRepository.save(realO);
        }
        orderRepository.deleteById(id);
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    public Order getOrderById(Long orderID) {
        Optional<Order> order = orderRepository.findById(orderID);
        return order.orElse(null);
    }

    public void setTransportOperation(TransportOperation createdTransportOperation) {
        Order o = orderRepository.findById(createdTransportOperation.getOrder().getId()).orElse(null);
        if(o != null){
            o.setTransportOperation(createdTransportOperation);
            orderRepository.save(o);
        }
    }

    public void removeTransportOperation(Order o) {
        if(o != null){
            Optional<Order> order = orderRepository.findById(o.getId());
            if(order.isPresent()){
                Order realO = order.get();
                realO.setTransportOperation(null);
                orderRepository.save(realO);
            }
        }
    }
}
