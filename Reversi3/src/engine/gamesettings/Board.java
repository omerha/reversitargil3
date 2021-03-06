
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
 *       &lt;attribute name="rows" use="required" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="columns" use="required" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "Board")
public class Board implements Serializable {

    @XmlAttribute(name = "rows", required = true)
    protected byte rows;
    @XmlAttribute(name = "columns", required = true)
    protected byte columns;

    /**
     * Gets the value of the rows property.
     * 
     */
    public byte getRows() {
        return rows;
    }

    /**
     * Sets the value of the rows property.
     * 
     */
    public void setRows(byte value) {
        this.rows = value;
    }

    /**
     * Gets the value of the columns property.
     * 
     */
    public byte getColumns() {
        return columns;
    }

    /**
     * Sets the value of the columns property.
     * 
     */
    public void setColumns(byte value) {
        this.columns = value;
    }

}
