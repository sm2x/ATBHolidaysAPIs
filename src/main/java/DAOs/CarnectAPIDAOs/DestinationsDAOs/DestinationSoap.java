
package DAOs.CarnectAPIDAOs.DestinationsDAOs;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "destinationSoap", targetNamespace = "http://www.opentravel.org/OTA/2003/05")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface DestinationSoap {


    /**
     * Diplays an array of Regions.
     * 
     * @param vehicleRegionRequest
     * @return
     *     returns org.opentravel.ota._2003._05.VehicleRegionResponse
     */
    @WebMethod(operationName = "GetRegions", action = "http://www.opentravel.org/OTA/2003/05/GetRegions")
    @WebResult(name = "VehicleRegionResponse", targetNamespace = "http://www.opentravel.org/OTA/2003/05", partName = "GetRegionsResult")
    public VehicleRegionResponse getRegions(
        @WebParam(name = "VehicleRegionRequest", targetNamespace = "http://www.opentravel.org/OTA/2003/05", partName = "VehicleRegionRequest")
        VehicleRegionRequest vehicleRegionRequest);

    /**
     * Diplays an array of Countries.
     * 
     * @param vehicleCountryRequest
     * @return
     *     returns org.opentravel.ota._2003._05.VehicleCountryResponse
     */
    @WebMethod(operationName = "GetCountries", action = "http://www.opentravel.org/OTA/2003/05/GetCountries")
    @WebResult(name = "VehicleCountryResponse", targetNamespace = "http://www.opentravel.org/OTA/2003/05", partName = "GetCountriesResult")
    public VehicleCountryResponse getCountries(
        @WebParam(name = "VehicleCountryRequest", targetNamespace = "http://www.opentravel.org/OTA/2003/05", partName = "VehicleCountryRequest")
        VehicleCountryRequest vehicleCountryRequest);

    /**
     * Diplays an array of Cities.
     * 
     * @param vehicleCityRequest
     * @return
     *     returns org.opentravel.ota._2003._05.VehicleCityResponse
     */
    @WebMethod(operationName = "GetCities", action = "http://www.opentravel.org/OTA/2003/05/GetCities")
    @WebResult(name = "VehicleCityResponse", targetNamespace = "http://www.opentravel.org/OTA/2003/05", partName = "GetCitiesResult")
    public VehicleCityResponse getCities(
        @WebParam(name = "VehicleCityRequest", targetNamespace = "http://www.opentravel.org/OTA/2003/05", partName = "VehicleCityRequest")
        VehicleCityRequest vehicleCityRequest);

    /**
     * Diplays an array of Airports.
     * 
     * @param vehicleAirportRequest
     * @return
     *     returns org.opentravel.ota._2003._05.VehicleAirportResponse
     */
    @WebMethod(operationName = "GetAirports", action = "http://www.opentravel.org/OTA/2003/05/GetAirports")
    @WebResult(name = "VehicleAirportResponse", targetNamespace = "http://www.opentravel.org/OTA/2003/05", partName = "GetAirportsResult")
    public VehicleAirportResponse getAirports(
        @WebParam(name = "VehicleAirportRequest", targetNamespace = "http://www.opentravel.org/OTA/2003/05", partName = "VehicleAirportRequest")
        VehicleAirportRequest vehicleAirportRequest);

}