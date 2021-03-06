
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
 *         &lt;element name="CheckPaymentRequiredFieldsRQ" type="{http://www.juniper.es/webservice/2007/}JP_CheckPaymentRequiredFieldsRQ" minOccurs="0"/>
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
    "checkPaymentRequiredFieldsRQ"
})
@XmlRootElement(name = "CheckPaymentRequiredField")
public class CheckPaymentRequiredField {

    @XmlElement(name = "CheckPaymentRequiredFieldsRQ")
    protected JPCheckPaymentRequiredFieldsRQ checkPaymentRequiredFieldsRQ;

    /**
     * Gets the value of the checkPaymentRequiredFieldsRQ property.
     * 
     * @return
     *     possible object is
     *     {@link JPCheckPaymentRequiredFieldsRQ }
     *     
     */
    public JPCheckPaymentRequiredFieldsRQ getCheckPaymentRequiredFieldsRQ() {
        return checkPaymentRequiredFieldsRQ;
    }

    /**
     * Sets the value of the checkPaymentRequiredFieldsRQ property.
     * 
     * @param value
     *     allowed object is
     *     {@link JPCheckPaymentRequiredFieldsRQ }
     *     
     */
    public void setCheckPaymentRequiredFieldsRQ(JPCheckPaymentRequiredFieldsRQ value) {
        this.checkPaymentRequiredFieldsRQ = value;
    }

}
