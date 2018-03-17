package Controller.ATBAPIController.HotelBookingsController;

import APIJSONs.ATBAPIJSONs.Hotel.*;
import APIJSONs.ATBAPIJSONs.Hotel.BookResult;
import APIJSONs.ATBAPIJSONs.Hotel.RoomsAndRoomTypes.CancelationPolicyResponse;
import Beans.ATBDBBeans.KalitaonHotel.MealBean;
import Beans.ATBDBBeans.KalitaonLog.PrebookLogBean;
import Beans.ATBDBBeans.KalitaonSystem.BookingTransactionBean;
import Beans.ATBDBBeans.KalitaonSystem.BookingsAllBean;
import Beans.ATBDBBeans.KalitaonSystem.SubAgencyBean;
import Beans.ATBDBBeans.KalitaonSystem.TravellerInfoBean;
import DAOs.ATBDBDAOs.KalitaonHotelDAOs.MealDAO;
import DAOs.ATBDBDAOs.KalitaonLogDAOs.PrebookLogDAO;
import DAOs.ATBDBDAOs.KalitaonSysDAOs.BookingTransactionDAO;
import DAOs.ATBDBDAOs.KalitaonSysDAOs.BookingsAllDAO;
import DAOs.ATBDBDAOs.KalitaonSysDAOs.SubAgencyDAO;
import DAOs.ATBDBDAOs.KalitaonSysDAOs.TravellerInfoDAO;
import DAOs.SunHotelsAPIDAOs.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.web.bind.annotation.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static Controller.Application.errLogger;
import static Helper.ProjectProperties.sanHotelsProviderId;
import static Helper.ProjectProperties.sunhotelsUsername;
import static Helper.ProjectProperties.sunhotelspass;

/**
 * Created by George on 03/02/2018.
 */
@RestController
public class HotelPostBookController {

    @RequestMapping("/hotel/booking/bookingInfo")
    public BookInfoJSON hotelBookingInfo(@RequestParam(value="bookingNumber", defaultValue="0") int bookingNumber,
                                         @RequestParam(value="dateFrom", defaultValue="") String dateFrom,
                                         @RequestParam(value="dateTo", defaultValue="") String dateTo,
                                         @RequestParam("apiKey") String apiKey) {

        BookInfoJSON bookInfoJSON=new BookInfoJSON();
        List<BookResult> bookings=new ArrayList<>();
        DateTime dateTime = new DateTime(DateTimeZone.UTC);
        bookInfoJSON.setDateStamp(Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d",
                dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
                dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute())).toString() + "+0000");
        SubAgencyBean subAgencyBean;
        String saltedPassword = Helper.ProjectProperties.SALTForKeyGeneration + apiKey;
        String hashedPassword = Helper.APIKeyGeneration.generateHash(saltedPassword);
        subAgencyBean = SubAgencyDAO.getSubAgentByStoreKey(hashedPassword);


        if (subAgencyBean != null) {
            BookResult bookResult;
            List<BookingsAllBean> bookingsBeans=new ArrayList<>();
            BookingsAllBean bookingsAllBean;


            if(bookingNumber!=0){
                bookingsAllBean=BookingsAllDAO.getBookingsAllBeanById(bookingNumber,subAgencyBean.getId());
                if(bookingsAllBean!=null)
                    bookingsBeans.add(bookingsAllBean);
            }else if(!dateFrom.equals("")  && !dateTo.equals("")){
                ZonedDateTime checkin = null;
                ZonedDateTime checkout = null;
                String checkIndate[] = dateFrom.split("-");
                String checkOutdate[] = dateTo.split("-");
                if (checkIndate.length == 3 && checkOutdate.length == 3) {
                    try {
                        checkin = ZonedDateTime.of(LocalDate.of(Integer.parseInt(checkIndate[0]), Integer.parseInt(checkIndate[1]), Integer.parseInt(checkIndate[2])),
                                LocalTime.of(9, 30), ZoneId.of("America/New_York"));
                        checkout = ZonedDateTime.of(LocalDate.of(Integer.parseInt(checkOutdate[0]), Integer.parseInt(checkOutdate[1]), Integer.parseInt(checkOutdate[2])),
                                LocalTime.of(9, 30), ZoneId.of("America/New_York"));
                    } catch (NumberFormatException e) {
                    }
                }
                bookingsBeans=BookingsAllDAO.getAllHotelBookingsByDate(checkin,checkout,subAgencyBean.getId());
            }else{
                bookInfoJSON.setSuccess(false);
                bookInfoJSON.setErrorType("Missing Parameters");
                bookInfoJSON.setErrorMessageText("Please Specify BookingNumber or Dates");
                return bookInfoJSON;
            }


            if(bookingsBeans!=null && bookingsBeans.size()>0){

                for(BookingsAllBean booking:bookingsBeans) {
                    PrebookLogBean prebookLogBean=null;
                    if(booking.getCommon9()!=null && !booking.getCommon9().equals(""))
                        prebookLogBean = PrebookLogDAO.getPrebookLogBeanByPrebookRef(booking.getCommon9());
                    List<TravellerInfoBean> travellersInfoBean;
                    travellersInfoBean = TravellerInfoDAO.getPrebookLogBeanByBookingId(booking.getBookingId());

                    if (prebookLogBean != null && travellersInfoBean != null) {

                        bookResult = new BookResult();
                        XMLGregorianCalendar xmlCheckin = null;
                        XMLGregorianCalendar xmlCheckout = null;
                        XMLGregorianCalendar currentDateTime = null;
                        try {
                            GregorianCalendar gregorianCheckin = new GregorianCalendar();
                            gregorianCheckin.setTime(booking.getCheckIn());
                            GregorianCalendar gregorianCheckout = new GregorianCalendar();
                            gregorianCheckout.setTime(booking.getCheckOut());
                            GregorianCalendar gregorianCurrentDateTime = new GregorianCalendar();
                            xmlCheckin = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCheckin);
                            xmlCheckout = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCheckout);
                            currentDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCurrentDateTime);
                        } catch (DatatypeConfigurationException e) {
                            bookInfoJSON.setSuccess(false);
                            bookInfoJSON.setErrorMessageText("Couldn't extract essential data from database.Please contact at:george.nafpaktitis@atbholidays.com");
                            bookInfoJSON.setErrorType("Internal Error");
                            return bookInfoJSON;
                        }
                        bookResult.setBookingdate(currentDateTime);
                        bookResult.setBookingdateTimezone("UTC");
                        bookResult.setBookingnumber(String.valueOf(booking.getId()));
                        bookResult.setInvoiceref("");
                        List<MealBean> meals = MealDAO.getMeals(sanHotelsProviderId);
                        if (meals != null) {
                            for (MealBean meal : MealDAO.getMeals(sanHotelsProviderId)) {
                                if (meal.getId() == Integer.parseInt(booking.getCommon15())) {
                                    bookResult.setMeal(meal.getName());
                                }
                            }
                        } else
                            bookResult.setMeal("");
                        bookResult.setMealId(Integer.parseInt(booking.getCommon15()));
                        bookResult.setNumberofrooms(booking.getPriceId());
                        bookResult.setPrice(new BigDecimal(Double.parseDouble(booking.getAgentSale())).setScale(2, BigDecimal.ROUND_HALF_UP));
                        bookResult.setRoomType("");//todo
                        bookResult.setCheckindate(xmlCheckin);
                        bookResult.setCheckoutdate(xmlCheckout);
                        bookResult.setCurrency(subAgencyBean.getCurrency());
                        bookResult.setPaymentmethod("Deposit");
                        bookResult.setErrorMessage(null);
                        bookResult.setErrorMessage(null);
                        bookResult.setErrorAttributes(null);
                        bookResult.setHotelPhone(booking.getCommon8());
                        bookResult.setHotelId(Integer.parseInt(booking.getProductId()));
                        bookResult.setHotelName(booking.getProductTitle());
                        bookResult.setHotelAddress(booking.getCommon7());
                        if (booking.getStatus().equals("4"))
                            bookResult.setBookingStatus("Active");
                        else if (booking.getStatus().equals("7"))
                            bookResult.setBookingStatus("Active(Test Booking)");
                        else if (booking.getStatus().equals("3"))
                            bookResult.setBookingStatus("Cancelled");

                        List<CancelationPolicyResponse> cancelationPolicies = new ArrayList();
                        CancelationPolicyResponse cancelationPolicy = new CancelationPolicyResponse();
                        if(prebookLogBean.getDeadline()!=null && !prebookLogBean.getDeadline().equals("null"))
                            cancelationPolicy.setDeadline(Integer.parseInt(prebookLogBean.getDeadline()));
                        else
                            cancelationPolicy.setDeadline(null);
                        cancelationPolicy.setText(prebookLogBean.getAtbCancelPolicy());
                        cancelationPolicy.setPercentage(new BigDecimal(Double.parseDouble(prebookLogBean.getPercentage())));
                        cancelationPolicies.add(cancelationPolicy);
                        bookResult.setCancellationpolicies(cancelationPolicies);
                        bookResult.setTravellerInfo(travellersInfoBean);
                        // bookResult.setVoucher();//todo
                        // bookResult.setEarliestNonFreeCancellationDateUTC("");


                        if(prebookLogBean.getNotes()!=null && !prebookLogBean.getNotes().equals("")) {
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                ArrayOfCalendarNote1 arrayOfCalendarNote1 = mapper.readValue(prebookLogBean.getNotes(), ArrayOfCalendarNote1.class);
                                List<Note> hotelNotes = new ArrayList<>();
                                Note hotelNote;
                                for (CalendarNote note : arrayOfCalendarNote1.getHotelNote()) {
                                    hotelNote = new Note();
                                    hotelNote.setEndDate(note.getEndDate());
                                    hotelNote.setStartDate(note.getStartDate());
                                    hotelNote.setText(note.getText());
                                    hotelNotes.add(hotelNote);
                                }
                                bookResult.setHotelNotes(hotelNotes);
                            } catch (IOException e) {
                            }
                        }

                        bookings.add(bookResult);
                    }
                }
            }
        }else{
            bookInfoJSON.setSuccess(false);
            bookInfoJSON.setErrorType("Internal Error");
            bookInfoJSON.setErrorMessageText("Couldn't extract essential data from database.Please contact at:george.nafpaktitis@atbholidays.com");
            return bookInfoJSON;
        }
        bookInfoJSON.setTotalCount(bookings.size());
        bookInfoJSON.setBookingInfo(bookings);
        bookInfoJSON.setSuccess(true);
        return bookInfoJSON;
    }


    @RequestMapping("/hotel/booking/cancelBooking")
    public CancelJSON cancelBooking(@RequestParam(value="bookingNumber", defaultValue="0") int bookingId,
                                    @RequestParam("apiKey") String apiKey) {

        CancelJSON cancelJSON=new CancelJSON();
        CancellResult cancellResult= new CancellResult();
        cancellResult.setCancellationSucceed(false);
        DateTime dateTime = new DateTime(DateTimeZone.UTC);
        cancelJSON.setDateStamp(Timestamp.valueOf(String.format("%04d-%02d-%02d %02d:%02d:%02d",
                dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
                dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute())).toString() + "+0000");
        SubAgencyBean subAgencyBean;
        String saltedPassword = Helper.ProjectProperties.SALTForKeyGeneration + apiKey;
        String hashedPassword = Helper.APIKeyGeneration.generateHash(saltedPassword);
        subAgencyBean = SubAgencyDAO.getSubAgentByStoreKey(hashedPassword);


        if (subAgencyBean != null) {

            BookingsAllBean booking = BookingsAllDAO.getBookingsAllBeanById(bookingId, subAgencyBean.getId());
            if (booking != null) {

                PrebookLogBean prebook = PrebookLogDAO.getPrebookLogBeanByPrebookRef(booking.getCommon9());
                if (prebook != null) {



                    DateTime deadlineDate;
                    if(prebook.getDeadline().equals("null"))
                        deadlineDate=null;
                    else {
                        long i=booking.getCheckIn().getTime()-(Integer.parseInt(prebook.getDeadline()+24)*60*60*1000);//todo
                        deadlineDate = new DateTime(i);
                    }

                    if(booking.getStatus().equals("4")) {

                        if (deadlineDate==null || dateTime.compareTo(deadlineDate) < 0) {

                            CancellationResult result = null;
                            try {
                                NonStaticXMLAPI nonStaticXMLAPI = new NonStaticXMLAPI();
                                NonStaticXMLAPISoap nonStaticXMLAPISoap = nonStaticXMLAPI.getNonStaticXMLAPISoap();
                                result = nonStaticXMLAPISoap.cancelBooking(sunhotelsUsername, sunhotelspass, booking.getCommon10(), "English");
                            } catch (NullPointerException e) {
                            }

                            if (result != null) {
                                if (result.getError() != null) {
                                    cancellResult.setCancellationSucceed(false);
                                    cancellResult.setCancellationNote("Cancelation couldn't completed.");
                                    cancelJSON.setCancellationResult(cancellResult);
                                    cancelJSON.setErrorMessageText(result.getError().getMessage());
                                    cancelJSON.setSuccess(false);
                                    return cancelJSON;
                                } else {
                                    if (result.getCode() == 1) {

                                        cancellResult.setCancellationSucceed(true);
                                        booking.setStatus("3");
                                        if (BookingsAllDAO.updateBooking(booking)) {
                                            errLogger.info("Booking with Id :"+booking.getId()+" cancelled at sunhotels side but failed to update bookingsall table.");
                                        }

                                        //todo refund also at gsa table
                                        subAgencyBean.setDeposit(String.valueOf(new BigDecimal(Double.parseDouble(subAgencyBean.getDeposit())).
                                                                 add( new BigDecimal((Double.parseDouble(booking.getAgentSale()) * Double.parseDouble(prebook.getPercentage()) / 100))).
                                                                 setScale(2, BigDecimal.ROUND_HALF_UP)));
                                        if (!SubAgencyDAO.updateSubAgentByName(subAgencyBean)) {
                                            BookingTransactionBean bookingTransactionBean = new BookingTransactionBean();
                                            bookingTransactionBean.setAgentId(String.valueOf(subAgencyBean.getId()));
                                            bookingTransactionBean.setAgentName(subAgencyBean.getAgentName());
                                            bookingTransactionBean.setBookingId(String.valueOf(booking.getId()));
                                            bookingTransactionBean.setGsaId(subAgencyBean.getGsaId());
                                            bookingTransactionBean.setTransactionType("Refund");
                                            bookingTransactionBean.setTransCur(subAgencyBean.getCurrency());
                                            bookingTransactionBean.setTransDate(dateTime.toString());
                                            bookingTransactionBean.setTransRate(String.valueOf(new BigDecimal((Double.parseDouble(booking.getAgentSale()) * Double.parseDouble(prebook.getPercentage()) / 100)).setScale(2, BigDecimal.ROUND_HALF_UP)));
                                            bookingTransactionBean.setTransType("Deposit");
                                            bookingTransactionBean.setTransNote("");

                                            if (BookingTransactionDAO.storeTransaction(bookingTransactionBean)) {
                                                errLogger.info("Booking with Id :"+booking.getId()+" cancelled at sunhotels side but failed to store transaction at BookingTransaction table.");
                                            }
                                        } else {
                                            errLogger.info("Booking with Id :"+booking.getId()+" cancelled at sunhotels side but failed to refund the price at subagency table.");
                                        }
                                    } else {
                                        cancellResult.setCancellationSucceed(false);
                                        cancellResult.setCancellationNote("Cancelation couldn't completed.Please contact at:george.nafpaktitis@atbholidays.com");
                                        cancelJSON.setCancellationResult(cancellResult);
                                        cancelJSON.setSuccess(true);
                                        return cancelJSON;
                                    }

                                }
                            }else{
                                cancellResult.setCancellationSucceed(false);
                                cancellResult.setCancellationNote("Cancelation couldn't completed.");
                                cancelJSON.setCancellationResult(cancellResult);
                                cancelJSON.setSuccess(false);
                                cancelJSON.setErrorMessageText("Communication error");
                                return cancelJSON;
                            }
                        } else {
                            cancellResult.setCancellationSucceed(false);
                            cancellResult.setCancellationNote("The booking could not be cancelled due to the cancellation deadline having expired.");
                        }
                    }else if(booking.getStatus().equals("7")){
                        booking.setStatus("3");
                        if (!BookingsAllDAO.updateBooking(booking)) {
                            cancellResult.setCancellationSucceed(true);
                        }
                    }else{
                        cancellResult.setCancellationNote("This booking isn't active");
                        cancellResult.setCancellationSucceed(false);
                        cancelJSON.setCancellationResult(cancellResult);
                        cancelJSON.setSuccess(true);
                        return cancelJSON;
                    }

                }else{
                    cancelJSON.setSuccess(false);
                    cancelJSON.setErrorMessageText("Internal error.Couldn't extract essential data.");
                    return cancelJSON;
                }
            }else{
                cancelJSON.setSuccess(false);
                cancelJSON.setErrorMessageText("Internal error.Couldn't extract essential data or bookingId doesn't exists.");
                return cancelJSON;
            }
        }else{
            cancelJSON.setSuccess(false);
            cancelJSON.setErrorMessageText("Internal error.Couldn't extract essential data.");
            return cancelJSON;
        }

        cancelJSON.setCancellationResult(cancellResult);
        cancelJSON.setSuccess(true);
        return cancelJSON;

    }


}