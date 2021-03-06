
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
 *         &lt;element name="ReadRQ" type="{http://www.juniper.es/webservice/2007/}JP_ReadRQ" minOccurs="0"/>
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
    "readRQ"
})
@XmlRootElement(name = "ReadBooking")
public class ReadBooking {

    @XmlElement(name = "ReadRQ")
    protected JPReadRQ readRQ;

    /**
     * Gets the value of the readRQ property.
     * 
     * @return
     *     possible object is
     *     {@link JPReadRQ }
     *     
     */
    public JPReadRQ getReadRQ() {
        return readRQ;
    }

    /**
     * Sets the value of the readRQ property.
     * 
     * @param value
     *     allowed object is
     *     {@link JPReadRQ }
     *     
     */
    public void setReadRQ(JPReadRQ value) {
        this.readRQ = value;
    }

}
