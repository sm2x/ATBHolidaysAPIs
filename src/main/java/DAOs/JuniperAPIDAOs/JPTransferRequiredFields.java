
package DAOs.JuniperAPIDAOs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for JP_TransferRequiredFields complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JP_TransferRequiredFields">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TransferBooking" type="{http://www.juniper.es/webservice/2007/}JP_TransferBooking" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JP_TransferRequiredFields", propOrder = {
    "transferBooking"
})
public class JPTransferRequiredFields {

    @XmlElement(name = "TransferBooking")
    protected JPTransferBooking transferBooking;

    /**
     * Gets the value of the transferBooking property.
     * 
     * @return
     *     possible object is
     *     {@link JPTransferBooking }
     *     
     */
    public JPTransferBooking getTransferBooking() {
        return transferBooking;
    }

    /**
     * Sets the value of the transferBooking property.
     * 
     * @param value
     *     allowed object is
     *     {@link JPTransferBooking }
     *     
     */
    public void setTransferBooking(JPTransferBooking value) {
        this.transferBooking = value;
    }

}
