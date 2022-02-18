package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.service.UserOrderService;
import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.model.UserOrderPositionModel;
import com.epam.esm.gcs.persistence.repository.UserOrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Override
    @Transactional
    public UserOrderDto create(UserOrderDto userOrderDto) {
        UserOrderModel userOrder = modelMapper.map(userOrderDto, UserOrderModel.class);

        AppUserDto foundAppUserDto = appUserService.findById(userOrder.getUser().getId());
        AppUserModel foundAppUser = modelMapper.map(foundAppUserDto, AppUserModel.class);
        userOrder.setUser(foundAppUser);

        //todo: distinct positions by gift certificate
        List<UserOrderPositionModel> positions = fulfillPositions(userOrder);
        validateStates(positions);
        userOrder.setPositions(positions);

        BigDecimal orderPrice = new BigDecimal(0);
        orderPrice = orderPrice.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        //todo: reduce count in gift certificate
        for (UserOrderPositionModel position : positions) {
            GiftCertificateModel giftCertificate = position.getGiftCertificate();
            if (giftCertificate.getCount() < position.getCount()) {
                //todo: create some exception
                throw new RuntimeException();
            }

            //todo: separate somehow orderPrice calculation and giftCertificate reduce count
            orderPrice = orderPrice.add(giftCertificate.getPrice().multiply(BigDecimal.valueOf(position.getCount())));

            giftCertificateService.reduceCount(giftCertificate.getId(), position.getCount());
            giftCertificate.setCount(giftCertificate.getCount() - position.getCount()); //todo: do i need this?
        }

        userOrder.setPrice(orderPrice);

        userOrder = userOrderRepository.create(userOrder);
        return modelMapper.map(userOrder, UserOrderDto.class);
    }

    @Override
    public void delete(Long id) {//todo: think, do i need it

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

    //todo: move to validator??
    private void validateStates(List<UserOrderPositionModel> positions) {
        List<Pair<Long, Long>> archivedToActual = new ArrayList<>();
        List<Long> unavailable = new ArrayList<>();
        for (UserOrderPositionModel position : positions) {
            GiftCertificateModel giftCertificate = position.getGiftCertificate();
            if (ActualityStateModel.ARCHIVED.equals(giftCertificate.getState())) {
                Long archivedId = giftCertificate.getId();
                Optional<Long> optionalActualId = giftCertificateService.findActualId(archivedId);
                if (optionalActualId.isPresent()) {
                    archivedToActual.add(Pair.of(archivedId, optionalActualId.get()));
                } else {
                    unavailable.add(archivedId);
                }
            }
        }
        if (!archivedToActual.isEmpty()) {
            throw new EntitiesArchivedException(GiftCertificateModel.class, archivedToActual, unavailable);
        }
    }

    @Override
    public List<UserOrderDto> findAll() {
        return userOrderRepository.findAll().stream()
                .map(model -> modelMapper.map(model, UserOrderDto.class))
                .collect(Collectors.toList());
    }
}
