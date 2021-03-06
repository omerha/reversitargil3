
package engine.gamesettings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="column" use="required" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="row" use="required" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "Position")
public class Position implements Serializable {

    @XmlAttribute(name = "column", required = true)
    protected byte column;
    @XmlAttribute(name = "row", required = true)
    protected byte row;

    /**
     * Gets the value of the column property.
     * 
     */
    public byte getColumn() {
        return column;
    }

    /**
     * Sets the value of the column property.
     * 
     */
    public void setColumn(byte value) {
        this.column = value;
    }

    /**
     * Gets the value of the row property.
     * 
     */
    public byte getRow() {
        return row;
    }

    /**
     * Sets the value of the row property.
     * 
     */
    public void setRow(byte value) {
        this.row = value;
    }

}
