package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.ActionWithCountDto;
import com.epam.esm.gcs.business.dto.ActualityStateDto;
import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.business.dto.UserOrderPositionDto;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.service.UserOrderService;
import com.epam.esm.gcs.business.validation.UserOrderValidator;
import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.model.UserOrderPositionModel;
import com.epam.esm.gcs.persistence.repository.UserOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserOrderServiceImplTest {

    private final UserOrderService userOrderService;

    private final GiftCertificateService giftCertificateService;
    private final UserOrderRepository userOrderRepository;
    private final UserOrderValidator userOrderValidator;
    private final AppUserService appUserService;

    public UserOrderServiceImplTest(@Mock GiftCertificateService giftCertificateService,
                                    @Mock UserOrderRepository userOrderRepository,
                                    @Mock UserOrderValidator userOrderValidator,
                                    @Mock AppUserService appUserService) {
        this.userOrderService = new UserOrderServiceImpl(appUserService, giftCertificateService, userOrderRepository,
                new ModelMapperTestConfig().modelMapper(), userOrderValidator);

        this.appUserService = appUserService;
        this.giftCertificateService = giftCertificateService;
        this.userOrderRepository = userOrderRepository;
        this.userOrderValidator = userOrderValidator;
    }

    @Test
    void create_shouldInvokeValidators() {
        Long userId = 1L;
        AppUserDto foundUser = AppUserDto.builder()
                .id(userId)
                .email("someEmail@gmail.com")
                .build();

        Long certificateId = 2L;
        Integer certificateCount = 202;
        BigDecimal certificatePrice = BigDecimal.valueOf(44.21);
        Integer positionCount = 33;

        GiftCertificateDto foundGiftCertificate = GiftCertificateDto.builder()
                .id(certificateId)
                .state(ActualityStateDto.ACTUAL)
                .price(certificatePrice)
                .count(certificateCount)
                .build();

        UserOrderPositionDto inputPosition = UserOrderPositionDto.builder()
                .giftCertificate(foundGiftCertificate)
                .count(positionCount)
                .build();

        UserOrderPositionModel positionToValidate = UserOrderPositionModel.builder()
                .giftCertificate(GiftCertificateModel.builder().id(certificateId).build())
                .count(positionCount)
                .build();
        List<UserOrderPositionModel> positionsToValidate = List.of(positionToValidate);

        UserOrderDto inputUserOrder = UserOrderDto.builder()
                .user(foundUser)
                .positions(List.of(inputPosition))
                .build();

        when(appUserService.findById(userId)).thenReturn(foundUser);
        when(giftCertificateService.findById(certificateId)).thenReturn(foundGiftCertificate);
        when(userOrderRepository.create(any())).thenReturn(UserOrderModel.builder().build());

        userOrderService.create(inputUserOrder);

        verify(userOrderValidator, times(1)).validateCountsAreEnough(positionsToValidate);
        verify(userOrderValidator, times(1)).validateStates(positionsToValidate);
    }

    @Test
    void create_shouldCalculateValidTotalPriceAndInvokeCertificateCountReduce() {
        Long userId = 1L;
        String userEmail = "someEmail@gmail.com";
        AppUserDto foundUser = AppUserDto.builder()
                .id(userId)
                .email(userEmail)
                .build();

        Long certificateId = 2L;
        Integer certificateCount = 202;
        BigDecimal certificatePrice = BigDecimal.valueOf(44.21);
        Integer positionCount = 33;

        GiftCertificateDto foundGiftCertificate = GiftCertificateDto.builder()
                .id(certificateId)
                .state(ActualityStateDto.ACTUAL)
                .price(certificatePrice)
                .count(certificateCount)
                .build();

        UserOrderPositionDto inputPosition = UserOrderPositionDto.builder()
                .giftCertificate(foundGiftCertificate)
                .count(positionCount)
                .build();

        UserOrderPositionModel positionToValidate = UserOrderPositionModel.builder()
                .giftCertificate(GiftCertificateModel.builder().id(certificateId).build())
                .count(positionCount)
                .build();
        List<UserOrderPositionModel> positionsToValidate = List.of(positionToValidate);

        UserOrderDto inputUserOrder = UserOrderDto.builder()
                .user(foundUser)
                .positions(List.of(inputPosition))
                .build();

        when(appUserService.findById(userId)).thenReturn(foundUser);
        when(giftCertificateService.findById(certificateId)).thenReturn(foundGiftCertificate);
        when(userOrderRepository.create(any())).thenReturn(UserOrderModel.builder().build());

        userOrderService.create(inputUserOrder);

        BigDecimal expectedTotalPrice = BigDecimal.valueOf(8930.42);

        UserOrderModel expectedUserOrder = UserOrderModel.builder()
                .user(AppUserModel.builder().id(userId).email(userEmail).build())
                .positions(positionsToValidate)
                .price(expectedTotalPrice)
                .build();

        verify(userOrderRepository, times(1)).create(expectedUserOrder);
        verify(giftCertificateService, times(1))
                .updateCount(certificateId, new ActionWithCountDto(ActionWithCountDto.Mode.REDUCE, positionCount));
    }
}