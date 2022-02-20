package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.service.UserOrderService;
import com.epam.esm.gcs.business.validation.UserOrderValidator;
import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.model.UserOrderPositionModel;
import com.epam.esm.gcs.persistence.repository.UserOrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserOrderServiceImpl implements UserOrderService {

    private final static String ID_FIELD = "id";

    private final static int DEFAULT_SCALE = 2;
    private final static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private final AppUserService appUserService;
    private final GiftCertificateService giftCertificateService;
    private final UserOrderRepository userOrderRepository;
    private final ModelMapper modelMapper;
    private final UserOrderValidator userOrderValidator;

    @Override
    @Transactional
    public UserOrderDto create(UserOrderDto userOrderDto) {
        UserOrderModel userOrder = modelMapper.map(userOrderDto, UserOrderModel.class);

        AppUserDto foundAppUserDto = appUserService.findById(userOrder.getUser().getId());
        AppUserModel foundAppUser = modelMapper.map(foundAppUserDto, AppUserModel.class);
        userOrder.setUser(foundAppUser);

        List<UserOrderPositionModel> positions = fulfillPositions(userOrder);
        positions = distinctPositionsByGiftCertificateId(positions);

        userOrderValidator.validateStates(positions);
        userOrderValidator.validateCountsAreEnough(positions);
        userOrder.setPositions(positions);

        BigDecimal orderPrice = new BigDecimal(0);
        orderPrice = orderPrice.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);

        for (UserOrderPositionModel position : positions) {
            GiftCertificateModel giftCertificate = position.getGiftCertificate();
            orderPrice = orderPrice.add(giftCertificate.getPrice().multiply(BigDecimal.valueOf(position.getCount())));
            giftCertificateService.reduceCount(giftCertificate.getId(), position.getCount());
        }

        userOrder.setPrice(orderPrice);

        userOrder = userOrderRepository.create(userOrder);
        return modelMapper.map(userOrder, UserOrderDto.class);
    }

    @Override
    public UserOrderDto findById(Long id) {
        UserOrderModel userOrder = userOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UserOrderDto.class, ID_FIELD, String.valueOf(id)));
        return modelMapper.map(userOrder, UserOrderDto.class);
    }

    private List<UserOrderPositionModel> fulfillPositions(UserOrderModel userOrder) {
        return userOrder.getPositions().stream()
                .peek(position -> {
                    GiftCertificateDto giftCertificateDto =
                            giftCertificateService.findById(position.getGiftCertificate().getId());
                    GiftCertificateModel giftCertificate = modelMapper.map(giftCertificateDto,
                            GiftCertificateModel.class);
                    position.setGiftCertificate(giftCertificate);
                })
                .collect(Collectors.toList());
    }

    private List<UserOrderPositionModel> distinctPositionsByGiftCertificateId(List<UserOrderPositionModel> positions) {
        Map<Long, UserOrderPositionModel> positionByIdMap = new LinkedHashMap<>();
        positions.forEach(position -> positionByIdMap.put(position.getGiftCertificate().getId(), position));
        return new ArrayList<>(positionByIdMap.values());
    }

    @Override
    public List<UserOrderDto> findAll() {
        return userOrderRepository.findAll().stream()
                .map(model -> modelMapper.map(model, UserOrderDto.class))
                .collect(Collectors.toList());
    }
}
