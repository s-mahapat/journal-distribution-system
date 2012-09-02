package IAS.Model.FileUpload;

import IAS.Class.InwardInfo;
import IAS.Class.JDSLogger;
import IAS.Class.Queries;
import IAS.Class.util;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class URNModel extends FileUploadBase {

    private String sql;
    private PreparedStatement pst;
    private ArrayList<InwardInfo> _failures = new ArrayList<>();
    private static Logger logger = JDSLogger.getJDSLogger(URNModel.class.getName());

    public URNModel() throws SQLException {
        sql = Queries.getQuery("update_urn_info");
        pst = this.conn.prepareStatement(sql);
    }

    @Override
    public void processFiles() throws IOException, SQLException {

        for (FileItem item : this.getFiles()) {

            try {
                InputStream is = item.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder docb = dbf.newDocumentBuilder();
                Document doc = docb.parse(is);
                NodeList nl = doc.getElementsByTagNameNS("*", "VOUCHER");

                for (int i = 0; i < nl.getLength(); i++) {
                    InwardInfo _inwardinfo = getInwardInfo((Element) nl.item(i));
                    try {
                        if (this.updateInward(_inwardinfo) == false) {
                            _failures.add(_inwardinfo);
                        }
                    } catch (ParseException e) {
                        logger.error(e.getMessage());
                        throw (new IOException(e.getMessage()));
                    }

                }
            } catch (ParserConfigurationException | IOException | SAXException e) {
                logger.error(e.getMessage());
                throw (new IOException(e.getMessage()));
            }

        }

    }

    private Boolean updateInward(InwardInfo _inwardinfo) throws SQLException, ParseException {

        int paramIndex = 0;
        pst.setInt(++paramIndex, _inwardinfo.getInstrumentNo());
        pst.setFloat(++paramIndex, _inwardinfo.getAmount());
        pst.setDate(++paramIndex, util.dateStringToSqlDate(_inwardinfo.getInstrumentDate()));
        pst.setString(++paramIndex, _inwardinfo.getInwardNo());
        int rc = pst.executeUpdate();
        if (rc == 1) {
            return true;
        }
        return false;
    }

    private InwardInfo getInwardInfo(Element el) {
        String _inwardno = this.getInwardNo(el);
        int _chqno = this.getChequeNo(el);
        int _subno = this.getSubscriberNo(el);
        String _chqdt = this.getChqDt(el);
        InwardInfo _inwardinfo = new InwardInfo(_inwardno,
                _chqno,
                _subno,
                _chqdt,
                Float.valueOf(0));

        return _inwardinfo;
    }

    private String getInwardNo(Element el) {
        NodeList inwardList = el.getElementsByTagNameNS("*", "INWARDNO.LIST");
        Element _inward_list_el = (Element) inwardList.item(0);
        String _inward = this.getTextValue(_inward_list_el, "INWARDNO");
        /*for (int i = 0; i < inwardList.getLength(); i++) {
         Element _inward_list_el = (Element) inwardList.item(i);
         _inward = this.getTextValue(_inward_list_el, "INWARDNO");
         }*/
        return _inward;
    }

    private int getChequeNo(Element el) {
        NodeList inwardList = el.getElementsByTagNameNS("*", "CHQNO.LIST");
        Element _inward_list_el = (Element) inwardList.item(0);
        return this.getIntValue(_inward_list_el, "CHQNO");
    }

    private int getSubscriberNo(Element el) {
        NodeList inwardList = el.getElementsByTagNameNS("*", "SUBNO.LIST");
        Element _inward_list_el = (Element) inwardList.item(0);
        return this.getIntValue(_inward_list_el, "SUBNO");
    }

    private String getChqDt(Element el) {
        NodeList inwardList = el.getElementsByTagNameNS("*", "CHQDATE.LIST");
        String _chqdt;
        String _tmpdt;
        Element _inward_list_el = (Element) inwardList.item(0);
        _tmpdt = this.getTextValue(_inward_list_el, "CHQDATE");
        _chqdt = _tmpdt.substring(6) + "/" + _tmpdt.substring(4, 6) + "/" + _tmpdt.substring(0, 4);
        return _chqdt;
    }

    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagNameNS("*", tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     */
    private int getIntValue(Element ele, String tagName) {
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    @Override
    public String getOutputAsXML() throws IOException {
        String xml;
        Iterator iter = _failures.iterator();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element results = doc.createElement("results");
            doc.appendChild(results);

            while (iter.hasNext()) {
                Element row = doc.createElement("row");
                results.appendChild(row);

                InwardInfo _inwardInfo = (InwardInfo) iter.next();
                Element _row = this.appendChildToElement(doc, row, "inwardno", _inwardInfo.getInwardNo());
                row.appendChild(_row);

                _row = this.appendChildToElement(doc, row, "subno", String.valueOf(_inwardInfo.getSubscriberNo()));
                row.appendChild(_row);

                _row = this.appendChildToElement(doc, row, "chqno", String.valueOf(_inwardInfo.getInstrumentNo()));
                row.appendChild(_row);

                _row = this.appendChildToElement(doc, row, "amount", String.valueOf(_inwardInfo.getAmount()));
                row.appendChild(_row);

                _row = this.appendChildToElement(doc, row, "chqdate", _inwardInfo.getInstrumentDate());
                row.appendChild(_row);

            }
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            xml = writer.toString();
            return xml;

        } catch (ParserConfigurationException | TransformerException e) {
            logger.error(e.getMessage());
            return null;
        }

    }

    private Element appendChildToElement(Document doc, Element _parentel, String childtag, String textValue) {
        Element row = doc.createElement(childtag);
        row.appendChild(doc.createTextNode(textValue));
        _parentel.appendChild(row);
        return row;
    }
}