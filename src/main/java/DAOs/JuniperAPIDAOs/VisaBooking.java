
package DAOs.JuniperAPIDAOs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VisaBookingRQ" type="{http://www.juniper.es/webservice/2007/}JP_VisaBooking" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "visaBookingRQ"
})
@XmlRootElement(name = "VisaBooking")
public class VisaBooking {

    @XmlElement(name = "VisaBookingRQ")
    protected JPVisaBooking visaBookingRQ;

    /**
     * Gets the value of the visaBookingRQ property.
     * 
     * @return
     *     possible object is
     *     {@link JPVisaBooking }
     *     
     */
    public JPVisaBooking getVisaBookingRQ() {
        return visaBookingRQ;
    }

    /**
     * Sets the value of the visaBookingRQ property.
     * 
     * @param value
     *     allowed object is
     *     {@link JPVisaBooking }
     *     
     */
    public void setVisaBookingRQ(JPVisaBooking value) {
        this.visaBookingRQ = value;
    }

}
