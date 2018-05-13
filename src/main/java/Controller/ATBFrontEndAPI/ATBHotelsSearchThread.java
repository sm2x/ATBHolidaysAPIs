package Controller.ATBFrontEndAPI;

import APIJSONs.ATBAPIJSONs.HotelATBFrontEnd.RoomsAndRoomTypes.SunHotelsCancelationPolicy;
import APIJSONs.ATBAPIJSONs.HotelATBFrontEnd.RoomsAndRoomTypes.SunHotelsRoomMealResponse;
import APIJSONs.ATBAPIJSONs.HotelATBFrontEnd.RoomsAndRoomTypes.SunHotelsRoomResponse;
import APIJSONs.ATBAPIJSONs.HotelATBFrontEnd.RoomsAndRoomTypes.SunHotelsRoomTypeWithRoomsResponse;
import APIJSONs.ATBAPIJSONs.HotelATBFrontEnd.SunHotelsResponse;
import APIJSONs.ATBAPIJSONs.HotelATBFrontEnd.SunHotelsSearchPOST;
import Beans.ATBDBBeans.KalitaonHotel.*;
import DAOs.ATBDBDAOs.KalitaonHotelDAOs.*;
import Helper.CurrencyConverter;
import org.hibernate.StatelessSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static Controller.Application.errLogger;

/**
 * Created by George on 06/05/2018.
 */
public class ATBHotelsSearchThread implements Runnable {
    private DestinationBean destinationBean=null;
    private String[] childrenAgesSplit=null;
    private SunHotelsSearchPOST params;
    private StatelessSession session;
    private CountDownLatch latch;
    private ProvidersSearchRequestResponse aTBHotelsSearchRequestResponse = null;
    private ZonedDateTime checkin;
    private ZonedDateTime  checkout;

    public ProvidersSearchRequestResponse getaTBHotelsSearchRequestResponse() {
        return aTBHotelsSearchRequestResponse;
    }

    public void setaTBHotelsSearchRequestResponse(ProvidersSearchRequestResponse aTBHotelsSearchRequestResponse) {
        this.aTBHotelsSearchRequestResponse = aTBHotelsSearchRequestResponse;
    }

    public ATBHotelsSearchThread(DestinationBean destinationBean,
                                 String[] childrenAgesSplit,
                                 ZonedDateTime checkout,
                                 ZonedDateTime checkin,
                                 StatelessSession session,
                                 SunHotelsSearchPOST params,
                                 CountDownLatch latch){

        super();
        this.childrenAgesSplit=childrenAgesSplit;
        this.destinationBean=destinationBean;
        this.session=session;
        this.params=params;
        this.latch=latch;
        this.checkin=checkin;
        this.checkout=checkout;
        aTBHotelsSearchRequestResponse=new ProvidersSearchRequestResponse();
    }

    public void run() {
        try {
            aTBHotelsSearchRequestResponse=new ProvidersSearchRequestResponse();
            if (destinationBean != null && destinationBean.getDestinationId()!=0 ) {
                List<SunHotelsResponse> hotelsResponse = new ArrayList<>();
                List<HotelBean> hotels=HotelDAO.getATBHotelByDestId(String.valueOf(destinationBean.getDestinationId()));
                List<RoomAvailableBean> rooms=RoomAvailableDAO.getRoomAvailable(hotels,params.getCheckInDate(),params.getCheckOutDate(),0);//todo skip check out day from check.Check till the day before checkout
                for(HotelBean hotel:hotels) {

                   // if(hotel.isActive()) {//todo which provider room type we use and ,also fix meals
                        SunHotelsResponse hotelResponse = new SunHotelsResponse();
                        hotelResponse.setState(hotel.getState());
                        hotelResponse.setLongitude(hotel.getLongitude());
                        hotelResponse.setHeadline(null);
                        hotelResponse.setDistrict(hotel.getDistrict());
                        hotelResponse.setDescription(hotel.getDescription());
                        hotelResponse.setCity(hotel.getCity());
                        hotelResponse.setAddress(hotel.getAddress());
                        hotelResponse.setId(hotel.getHotelId());
                        hotelResponse.setStar_rating(hotel.getStarRating());
                        hotelResponse.setZip_code(hotel.getZipCode());
                        hotelResponse.setName(hotel.getName());
                        hotelResponse.setProviderId(hotel.getProviderId());
                        hotelResponse.setType(hotel.getAccommodationId());
                        hotelResponse.setCountry_code(hotel.getCountryCode());
                        hotelResponse.setCountry(hotel.getCountry());
                        hotelResponse.setLatitude(hotel.getLatitude());
                        hotelResponse.setTimezone(null);

                        List<SunHotelsRoomTypeWithRoomsResponse> roomsTypes= new ArrayList<>();
                        for(RoomAvailableBean roomAvailableBean:rooms) {
                            if(Integer.compare(roomAvailableBean.getHotelId(),hotel.getHotelId())==0) {
                                RoomBean room = RoomDAO.getRoomBeanByAtbRoomId(String.valueOf(roomAvailableBean.getRoomId()), roomAvailableBean.getHotelId(), roomAvailableBean.getProviderId(), null);
                                if (room != null) {
                                    RoomtypeBean roomtypeBean = RoomtypeDAO.getRoomsTypesbyId(room.getRoomTypeId(), room.getProviderId(), null);
                                    if (roomtypeBean != null) {

                                        int adultsPerRoom = (params.getNumberOfAdults() + params.getNumberOfRooms() - 1) / params.getNumberOfRooms();
                                        int childsPerRoom = (params.getNumberOfChildren() + params.getNumberOfRooms() - 1) / params.getNumberOfRooms() ;
                                        int infantPerRoom = (params.getInfant()+ params.getNumberOfRooms() -1) / params.getNumberOfRooms();
                                        if (adultsPerRoom <= roomtypeBean.getMaxAdultOccupation() && childsPerRoom <= roomtypeBean.getMaxChildOccupation() &&
                                                adultsPerRoom >= roomtypeBean.getMinAdultOccupation() && childsPerRoom >= roomtypeBean.getMinChildOccupation() &&
                                                infantPerRoom >= roomtypeBean.getMinInfantOccupation() && infantPerRoom <= roomtypeBean.getMaxInfantOccupation() &&
                                                adultsPerRoom + childsPerRoom <= roomtypeBean.getMaxAdultOccupation()) {


                                            ZonedDateTime checkin = null;
                                            ZonedDateTime checkout = null;

                                            String checkIndate[] = params.getCheckInDate().split("-");
                                            String checkOutdate[] = params.getCheckOutDate().split("-");
                                            if (checkIndate.length == 3 && checkOutdate.length == 3) {
                                                try {
                                                    checkin = ZonedDateTime.of(LocalDate.of(Integer.parseInt(checkIndate[0]), Integer.parseInt(checkIndate[1]), Integer.parseInt(checkIndate[2])),
                                                            LocalTime.of(9, 30), ZoneId.of("America/New_York"));
                                                    checkout = ZonedDateTime.of(LocalDate.of(Integer.parseInt(checkOutdate[0]), Integer.parseInt(checkOutdate[1]), Integer.parseInt(checkOutdate[2])),
                                                            LocalTime.of(9, 30), ZoneId.of("America/New_York"));

                                                    List<String> dates = new ArrayList<>();

                                                    for (ZonedDateTime date = checkin; date.isBefore(checkout); date = date.plusDays(1)) {
                                                        String day;
                                                        String month;
                                                        if (date.getMonthValue() < 10)
                                                            month = "0" + date.getMonthValue();
                                                        else
                                                            month = "" + date.getMonthValue();
                                                        if (date.getDayOfMonth() < 10)
                                                            day = "0" + date.getDayOfMonth();
                                                        else
                                                            day = "" + date.getDayOfMonth();
                                                        dates.add(date.getYear() + "-" + month + "-" + day);
                                                    }
                                                    List<RoomPriceBean> roomPrices = RoomPriceDAO.getRoomPricesbyRoomId(String.valueOf(room.getAtbRoomId()), room.getProviderId(), dates, roomAvailableBean.getId(), null);
                                                    Double sumPrice = new Double(0);
                                                    for (RoomPriceBean roomPrice : roomPrices) {
                                                        Double price = Double.parseDouble(CurrencyConverter.findExchangeRateAndConvert(roomPrice.getCurrency(), params.getCurrencies().get(0), Double.parseDouble(roomPrice.getRoomPrice())).toString());
                                                        if (price == null)
                                                            return;
                                                        sumPrice = sumPrice + price;
                                                    }
                                                    sumPrice = sumPrice * params.getNumberOfRooms();

                                                    List<SunHotelsRoomResponse> roomsresponse = new ArrayList<>();
                                                    SunHotelsRoomResponse roomResponse = new SunHotelsRoomResponse();

                                                    roomResponse.setRoomId(room.getAtbRoomId());
                                                    List<SunHotelsRoomMealResponse> meals = new ArrayList<>();
                                                    if(!sumPrice.equals(new Double(0))) {
                                                        SunHotelsRoomMealResponse meal = new SunHotelsRoomMealResponse();
                                                        meal.setMealName(roomAvailableBean.getMealType());
                                                        meal.setMealId(0);
                                                        meal.setPrice(new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
                                                        meals.add(meal);
                                                    }

                                                    RoombedBean roombed=RoombedDAO.getRoombed(room.getHotelId(),String.valueOf(room.getAtbRoomId()),room.getProviderId());
                                                    if(roombed!=null){
                                                        roomResponse.setExtrabeds(roombed.getExtraBedCount());
                                                        roomResponse.setBeds(roombed.getBedCount());
                                                    }


                                                    List<SunHotelsCancelationPolicy> policiesResponse=new ArrayList<>();
                                                    List<RoomPolicyBean> policices=RoomPolicyDAO.getRoomPolicies(room.getHotelId(),String.valueOf(room.getAtbRoomId()),room.getProviderId());
                                                    if(policices!=null) {
                                                        for (RoomPolicyBean policy : policices) {
                                                            SunHotelsCancelationPolicy policyResponse=new SunHotelsCancelationPolicy();
                                                            policyResponse.setDeadline(Integer.parseInt(policy.getPerDeadline()));
                                                            policyResponse.setPercentage(new BigDecimal(Double.parseDouble(policy.getPercent())));
                                                            policiesResponse.add(policyResponse);
                                                        }
                                                    }
                                                    roomResponse.setCancellationPolicies(policiesResponse);
                                                    roomResponse.setMeals(meals);
                                                    if(meals.size()>0)
                                                        roomsresponse.add(roomResponse);

                                                    SunHotelsRoomTypeWithRoomsResponse tempRoomtype=null;
                                                    for(SunHotelsRoomTypeWithRoomsResponse rt:roomsTypes){
                                                        if(Integer.compare(rt.getRoomTypeId(),roomtypeBean.getId())==0) {
                                                            tempRoomtype=rt;
                                                            break;
                                                        }
                                                    }
                                                    if(roomsresponse.size()>0) {
                                                        if (tempRoomtype == null) {
                                                            SunHotelsRoomTypeWithRoomsResponse roomtype = new SunHotelsRoomTypeWithRoomsResponse();
                                                            roomtype.setRoomType(roomtypeBean.getRoomType());
                                                            roomtype.setRoomTypeId(roomtypeBean.getId());
                                                            roomtype.setRooms(roomsresponse);
                                                            tempRoomtype = roomtype;
                                                        } else {
                                                            tempRoomtype.getRooms().addAll(roomsresponse);
                                                        }
                                                        roomsTypes.add(tempRoomtype);
                                                    }
                                                } catch (NumberFormatException e) {
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        hotelResponse.setRoom_types(roomsTypes);

                        if(hotelResponse.getRoom_types()!=null && hotelResponse.getRoom_types().size()>0)
                            hotelsResponse.add(hotelResponse);
                    //}
                }
                aTBHotelsSearchRequestResponse.setHotelsResponse(hotelsResponse);
            }else if(params.getHotelID() != null && !params.getHotelID().equals("")){

            }



        }catch(Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            errLogger.info(errors.toString());
        }finally {
            latch.countDown();
        }
    }
}
