package com.yanolja.scbj.domain.product.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.yanolja.scbj.domain.hotelRoom.entity.Hotel;
import com.yanolja.scbj.domain.hotelRoom.entity.HotelRoomImage;
import com.yanolja.scbj.domain.hotelRoom.entity.Room;
import com.yanolja.scbj.domain.hotelRoom.entity.RoomTheme;
import com.yanolja.scbj.domain.member.entity.Authority;
import com.yanolja.scbj.domain.member.entity.Member;
import com.yanolja.scbj.domain.member.entity.YanoljaMember;
import com.yanolja.scbj.domain.paymentHistory.entity.PaymentHistory;
import com.yanolja.scbj.domain.product.dto.request.ProductSearchRequest;
import com.yanolja.scbj.domain.product.dto.response.ProductSearchResponse;
import com.yanolja.scbj.domain.product.entity.Product;
import com.yanolja.scbj.domain.reservation.entity.Reservation;
import com.yanolja.scbj.global.config.QuerydslConfig;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(QuerydslConfig.class)
public class ProductSearchRepositoryTest {



    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Member createMember(String email, String phoneNumber) {
        Member member = Member.builder()
            .email(email)
            .password("password")
            .name("홍길동")
            .phone(phoneNumber)
            .authority(Authority.ROLE_USER)
            .build();
        entityManager.persist(member);
        return member;
    }

    private RoomTheme createRoomTheme(Boolean parking, Boolean pool) {
        RoomTheme roomTheme = RoomTheme.builder()
            .parkingZone(parking)
            .breakfast(false)
            .pool(pool)
            .oceanView(false)
            .build();
        entityManager.persist(roomTheme);
        return roomTheme;
    }

    private Hotel createHotel(RoomTheme roomTheme, String hotelAddress, Integer maxPeople) {
        Room room = Room.builder()
            .roomName("Deluxe Room")
            .checkIn(LocalTime.of(14, 0))
            .checkOut(LocalTime.of(11, 0))
            .bedType("Double Bed")
            .standardPeople(100)
            .maxPeople(maxPeople)
            .roomTheme(roomTheme)
            .build();

        Hotel hotel = Hotel.builder()
            .hotelName("롯데 시그니엘 호텔")
            .hotelMainAddress(hotelAddress)
            .hotelDetailAddress("123 Yanolja St, Gangnam-gu")
            .hotelInfoUrl("http://yanoljahotel.com")
            .room(room)
            .build();

        entityManager.persist(hotel);
        return hotel;
    }

    private HotelRoomImage createHotelRoomImage(Hotel hotel) {
        HotelRoomImage hotelRoomImage = HotelRoomImage.builder()
            .hotel(hotel)
            .url("http://example.com/hotel-room-image.jpg")
            .build();
        entityManager.persist(hotelRoomImage);
        return hotelRoomImage;
    }


    private YanoljaMember createYanoljaMember(String email) {
        YanoljaMember yanoljaMember = YanoljaMember.builder()
            .email(email)
            .build();
        entityManager.persist(yanoljaMember);
        return yanoljaMember;
    }

    private Reservation createReservation(Hotel hotel, YanoljaMember yanoljaMember,
                                          LocalDate checkIn, LocalDate checkOut, int purchasePrice) {
        Reservation reservation = Reservation.builder()
            .hotel(hotel)
            .yanoljaMember(yanoljaMember)
            .startDate(checkIn.atStartOfDay())
            .endDate(checkOut.atStartOfDay())
            .purchasePrice(purchasePrice)
            .build();
        entityManager.persist(reservation);
        return reservation;
    }

    private Product createProduct(Member member, Reservation reservation, int firstPrice, int secondPrice, int time) {
        Product product = Product.builder()
            .reservation(reservation)
            .member(member)
            .bank("하나 은행")
            .accountNumber("123123")
            .firstPrice(firstPrice)
            .secondPrice(secondPrice)
            .secondGrantPeriod(time)
            .build();
        entityManager.persist(product);
        return product;
    }

    private PaymentHistory createPaymentHistory( Product product) {
        PaymentHistory paymentHistory = PaymentHistory.builder()

            .product(product)
            .price(10000) // 예시 가격
            .customerName("홍길동")
            .customerEmail("hong.gildong@example.com")
            .customerPhoneNumber("010-1234-5678")
            .paymentType("신용카드")
            .settlement(true)
            .build();
        entityManager.persist(paymentHistory);
        return paymentHistory;
    }

    @BeforeEach
    void init() {
        IntStream.rangeClosed(1, 10) // 결제내역이 있으니 포함 X
            .forEach(i -> {
                String randomAddress = "이천";
                Member member = createMember("user" + i + "@example.com", "홍길동" + i);
                RoomTheme roomTheme = createRoomTheme(true,true);
                Hotel hotel2 = createHotel(roomTheme, randomAddress, 3);
                createHotelRoomImage(hotel2);
                YanoljaMember yanoljaMember = createYanoljaMember("yanolja" + i + "@example.com");
                Reservation reservation = createReservation(hotel2, yanoljaMember, LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(5),200000);
                Product product = createProduct(member, reservation, 100000,50000,0);
                createPaymentHistory(product);
               entityManager.flush();
            });


        IntStream.rangeClosed(1, 5)
            .forEach(i -> {
                String randomAddress = "강릉";
                Member member = createMember("user" + i + 20 + "@example.com", "홍길동" + i + 20);
                RoomTheme roomTheme = createRoomTheme(true,true);
                Hotel hotel2 = createHotel(roomTheme, randomAddress, 4);
                createHotelRoomImage(hotel2);
                YanoljaMember yanoljaMember =
                    createYanoljaMember("yanolja" + i + 20 + "@example.com");
                Reservation reservation =
                    createReservation(hotel2, yanoljaMember, LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),300000);
                Product product = createProduct(member, reservation, 200000, 100000,1);
                entityManager.flush();
            });

        IntStream.rangeClosed(1, 5)
            .forEach(i -> {
                String randomAddress = "서울";
                Member member = createMember("user" + i + 30 + "@example.com", "홍길동" + i + 30);
                RoomTheme roomTheme = createRoomTheme(true,true);
                Hotel hotel2 = createHotel(roomTheme, randomAddress, 3);
                createHotelRoomImage(hotel2);
                YanoljaMember yanoljaMember =
                    createYanoljaMember("yanolja" + i + 30 + "@example.com");
                Reservation reservation =
                    createReservation(hotel2, yanoljaMember, LocalDate.now().plusDays(8),
                        LocalDate.now().plusDays(13),400000);
                Product product = createProduct(member, reservation, 300000, 250000,0);
                entityManager.flush();
            });
        entityManager.clear();
    }


    @Nested
    @DisplayName("상품 검색 조회는")
    class Context_searchProduct {

        @Test
        @DisplayName("서울을 선택하면 서울에 관한 숙박업소가 보인다")
        public void will_success_testAreaSearchProduct() {
            //given
            ProductSearchRequest searchRequest = ProductSearchRequest.builder()
                .location("서울")
                .build();

            // when
            Page<ProductSearchResponse> results =
                productRepository.search(PageRequest.of(0, 1), searchRequest);

            // then
            assertThat(results).isNotEmpty();
            List<ProductSearchResponse> responses = results.getContent();
            for (ProductSearchResponse respons : responses) {
                System.err.println(respons.getOriginalPrice());
            }
            assertThat(responses.size()).isEqualTo(5);
        }

        @Test
        @DisplayName("날짜를 선택하면 체크인 날짜를 기준으로 숙박업소가 보인다")
        public void will_success_testDateSearchProduct() {
            //given
            ProductSearchRequest searchRequest =
                ProductSearchRequest.builder().checkIn(LocalDate.now())
                    .checkOut(LocalDate.now().plusDays(5)).build();

            //when

            Page<ProductSearchResponse> results =
                productRepository.search(PageRequest.of(0, 10), searchRequest);

            //then
            assertThat(results).isNotEmpty();
            List<ProductSearchResponse> content = results.getContent();
            for (ProductSearchResponse productSearchResponse : content) {
                System.out.println(productSearchResponse.getOriginalPrice());
            }

            assertThat(content.size()).isEqualTo(5);
        }

        @Test
        @DisplayName("인원을 설정하면 해당 최대인원의 상품들이 조회되야한다")
        public void will_success_testMaximumPeopleProduct() {
            //given
            ProductSearchRequest searchRequest = ProductSearchRequest.builder()
                .quantityPeople(3)
                .build();

            //when
            Page<ProductSearchResponse> results =
                productRepository.search(PageRequest.of(0, 10), searchRequest);


            //then
            assertThat(results).isNotEmpty();
            for (ProductSearchResponse result : results) {
                System.out.println(result.getOriginalPrice());

            }
            List<ProductSearchResponse> content = results.getContent();
            assertThat(content.size()).isEqualTo(10);
        }

        @Test
        @DisplayName("테마를 통해 상품을 조회한다")
        public void will_success_testThemeSearch() {
            //given
            ProductSearchRequest searchParkingRequest = ProductSearchRequest.builder()
                .parking(true)
                .build();

            ProductSearchRequest searchPoolRequest = ProductSearchRequest.builder()
                .pool(true)
                .build();

            //when
            Page<ProductSearchResponse> parkingResults =
                productRepository.search(PageRequest.of(0, 10), searchParkingRequest);

            Page<ProductSearchResponse> poolResults =
                productRepository.search(PageRequest.of(0, 10), searchPoolRequest);

            //then
            assertThat(parkingResults).isNotEmpty();
            assertThat(poolResults).isNotEmpty();
            List<ProductSearchResponse> parkingProduct = parkingResults.getContent();
            List<ProductSearchResponse> poolProduct = poolResults.getContent();
            assertThat(parkingProduct.size()).isEqualTo(10);
            assertThat(poolProduct.size()).isEqualTo(10);

        }

        @Test
        @DisplayName("정렬을 통해서 조회한다")
        public void will_success() {
            //given
            ProductSearchRequest highSaleSearchRequest = ProductSearchRequest.builder()
                .sorted("높은 할인 순")
                .build();

            ProductSearchRequest lowPriceRequest = ProductSearchRequest.builder()
                .sorted("낮은 가격 순")
                .build();

            //when
            Page<ProductSearchResponse> highSearchResult =
                productRepository.search(PageRequest.of(0, 10), highSaleSearchRequest);

            Page<ProductSearchResponse> lowPriceResult =
                productRepository.search(PageRequest.of(0, 10), lowPriceRequest);
            //then
            assertThat(highSearchResult).isNotEmpty();
            List<ProductSearchResponse> content = highSearchResult.getContent();

            for (int i = 0; i < 5; i++) {
                System.out.println(content.get(i).getSalePrice());
                assertThat(content.get(i).getSalePercentage()).isEqualTo(0.3333333333333333);
            }

            assertThat(lowPriceResult).isNotEmpty();
            for (int i = 0; i < 5; i++) {
                assertThat(lowPriceResult.getContent().get(i).getSalePrice()).isEqualTo(200000);
            }
        }

        @Test
        @DisplayName("판매중인것만 조회가 된다")
        public  void will_success_get_on_sale() {
            //given
            ProductSearchRequest productSearchRequest = ProductSearchRequest.builder().build();

            //when
            Page<ProductSearchResponse> responses =
                productRepository.search(PageRequest.of(0, 10), productSearchRequest);


            //then
            assertThat(responses).isNotEmpty();
            List<ProductSearchResponse> content = responses.getContent();
            assertThat(content.size()).isEqualTo(10);

        }
    }
}
