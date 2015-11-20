
package com.dds.pathfinder.itaxiinterface.wslookup;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.dds.pathfinder.itaxiinterface.wslookup package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _PFAddressResponse_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "PFAddressResponse");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _PFAddressData_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "PFAddressData");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _ArrayOfPFAddressData_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "ArrayOfPFAddressData");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _PFDirectionsResponse_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "PFDirectionsResponse");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _PFAddressDataStreetNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "StreetNumber");
    private final static QName _PFAddressDataRegionAbbreviation_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "RegionAbbreviation");
    private final static QName _PFAddressDataAreaName_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "AreaName");
    private final static QName _PFAddressDataOrganizationName_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "OrganizationName");
    private final static QName _PFAddressDataLandmarkName_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "LandmarkName");
    private final static QName _PFAddressDataUnitNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "UnitNumber");
    private final static QName _PFAddressDataPostCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "PostCode");
    private final static QName _PFAddressDataStreetName_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "StreetName");
    private final static QName _GetAddressDataStreetsPartialStreetName_QNAME = new QName("http://DDSAddressConnectionManagerService", "PartialStreetName");
    private final static QName _GetAddressDataStreetsUnitNumber_QNAME = new QName("http://DDSAddressConnectionManagerService", "UnitNumber");
    private final static QName _GetAddressDataStreetsOrderBy_QNAME = new QName("http://DDSAddressConnectionManagerService", "OrderBy");
    private final static QName _GetAddressDataStreetsPostCode_QNAME = new QName("http://DDSAddressConnectionManagerService", "PostCode");
    private final static QName _GetAddressDataStreetsPartialAreaName_QNAME = new QName("http://DDSAddressConnectionManagerService", "PartialAreaName");
    private final static QName _GetAddressDataStreetsStreetNumber_QNAME = new QName("http://DDSAddressConnectionManagerService", "StreetNumber");
    private final static QName _GetAddressDataStreetsRegionAbbreviation_QNAME = new QName("http://DDSAddressConnectionManagerService", "RegionAbbreviation");
    private final static QName _GetAddressDataStreetsOrganizationName_QNAME = new QName("http://DDSAddressConnectionManagerService", "OrganizationName");
    private final static QName _GetAddressDataStreetsLandmarkName_QNAME = new QName("http://DDSAddressConnectionManagerService", "LandmarkName");
    private final static QName _GetAddressDataStreetsResponseGetAddressDataStreetsResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "GetAddressDataStreetsResult");
    private final static QName _GetDirectionsLocationTo_QNAME = new QName("http://DDSAddressConnectionManagerService", "LocationTo");
    private final static QName _GetDirectionsLocationFrom_QNAME = new QName("http://DDSAddressConnectionManagerService", "LocationFrom");
    private final static QName _GetRegionDataPartialRegionName_QNAME = new QName("http://DDSAddressConnectionManagerService", "PartialRegionName");
    private final static QName _GetRegionDataResponseGetRegionDataResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "GetRegionDataResult");
    private final static QName _GetLandmarkDataResponseGetLandmarkDataResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "GetLandmarkDataResult");
    private final static QName _GetDirectionsResponseGetDirectionsResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "GetDirectionsResult");
    private final static QName _GetAreaAddressResponseGetAreaAddressResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "GetAreaAddressResult");
    private final static QName _PFAddressResponseStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "Status");
    private final static QName _PFAddressResponseAddressData_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "AddressData");
    private final static QName _GetClosestAddressDataRelativeResponseGetClosestAddressDataRelativeResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "GetClosestAddressDataRelativeResult");
    private final static QName _PFDirectionsResponseDirectionsToDestination_QNAME = new QName("http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", "DirectionsToDestination");
    private final static QName _TestServiceResponseTestServiceResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "TestServiceResult");
    private final static QName _GetClosestAddressDataResponseGetClosestAddressDataResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "GetClosestAddressDataResult");
    private final static QName _GetAddressDataResponseGetAddressDataResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "GetAddressDataResult");
    private final static QName _DumpDataResponseDumpDataResult_QNAME = new QName("http://DDSAddressConnectionManagerService", "DumpDataResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.dds.pathfinder.itaxiinterface.wslookup
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TestService }
     * 
     */
    public TestService createTestService() {
        return new TestService();
    }

    /**
     * Create an instance of {@link ArrayOfPFAddressData }
     * 
     */
    public ArrayOfPFAddressData createArrayOfPFAddressData() {
        return new ArrayOfPFAddressData();
    }

    /**
     * Create an instance of {@link GetClosestAddressDataRelative }
     * 
     */
    public GetClosestAddressDataRelative createGetClosestAddressDataRelative() {
        return new GetClosestAddressDataRelative();
    }

    /**
     * Create an instance of {@link PFAddressData }
     * 
     */
    public PFAddressData createPFAddressData() {
        return new PFAddressData();
    }

    /**
     * Create an instance of {@link GetAddressDataStreets }
     * 
     */
    public GetAddressDataStreets createGetAddressDataStreets() {
        return new GetAddressDataStreets();
    }

    /**
     * Create an instance of {@link DumpData }
     * 
     */
    public DumpData createDumpData() {
        return new DumpData();
    }

    /**
     * Create an instance of {@link GetRegionDataResponse }
     * 
     */
    public GetRegionDataResponse createGetRegionDataResponse() {
        return new GetRegionDataResponse();
    }

    /**
     * Create an instance of {@link GetLandmarkData }
     * 
     */
    public GetLandmarkData createGetLandmarkData() {
        return new GetLandmarkData();
    }

    /**
     * Create an instance of {@link GetAreaAddress }
     * 
     */
    public GetAreaAddress createGetAreaAddress() {
        return new GetAreaAddress();
    }

    /**
     * Create an instance of {@link GetDirectionsResponse }
     * 
     */
    public GetDirectionsResponse createGetDirectionsResponse() {
        return new GetDirectionsResponse();
    }

    /**
     * Create an instance of {@link GetClosestAddressDataRelativeResponse }
     * 
     */
    public GetClosestAddressDataRelativeResponse createGetClosestAddressDataRelativeResponse() {
        return new GetClosestAddressDataRelativeResponse();
    }

    /**
     * Create an instance of {@link PFDirectionsResponse }
     * 
     */
    public PFDirectionsResponse createPFDirectionsResponse() {
        return new PFDirectionsResponse();
    }

    /**
     * Create an instance of {@link TestServiceResponse }
     * 
     */
    public TestServiceResponse createTestServiceResponse() {
        return new TestServiceResponse();
    }

    /**
     * Create an instance of {@link GetAddressDataResponse }
     * 
     */
    public GetAddressDataResponse createGetAddressDataResponse() {
        return new GetAddressDataResponse();
    }

    /**
     * Create an instance of {@link GetAreaIDForGPS }
     * 
     */
    public GetAreaIDForGPS createGetAreaIDForGPS() {
        return new GetAreaIDForGPS();
    }

    /**
     * Create an instance of {@link GetAddressDataStreetsResponse }
     * 
     */
    public GetAddressDataStreetsResponse createGetAddressDataStreetsResponse() {
        return new GetAddressDataStreetsResponse();
    }

    /**
     * Create an instance of {@link GetRegionData }
     * 
     */
    public GetRegionData createGetRegionData() {
        return new GetRegionData();
    }

    /**
     * Create an instance of {@link GetDirections }
     * 
     */
    public GetDirections createGetDirections() {
        return new GetDirections();
    }

    /**
     * Create an instance of {@link GetLandmarkDataResponse }
     * 
     */
    public GetLandmarkDataResponse createGetLandmarkDataResponse() {
        return new GetLandmarkDataResponse();
    }

    /**
     * Create an instance of {@link GetAreaIDForGPSResponse }
     * 
     */
    public GetAreaIDForGPSResponse createGetAreaIDForGPSResponse() {
        return new GetAreaIDForGPSResponse();
    }

    /**
     * Create an instance of {@link GetAreaAddressResponse }
     * 
     */
    public GetAreaAddressResponse createGetAreaAddressResponse() {
        return new GetAreaAddressResponse();
    }

    /**
     * Create an instance of {@link PFAddressResponse }
     * 
     */
    public PFAddressResponse createPFAddressResponse() {
        return new PFAddressResponse();
    }

    /**
     * Create an instance of {@link GetClosestAddressData }
     * 
     */
    public GetClosestAddressData createGetClosestAddressData() {
        return new GetClosestAddressData();
    }

    /**
     * Create an instance of {@link GetAddressData }
     * 
     */
    public GetAddressData createGetAddressData() {
        return new GetAddressData();
    }

    /**
     * Create an instance of {@link GetClosestAddressDataResponse }
     * 
     */
    public GetClosestAddressDataResponse createGetClosestAddressDataResponse() {
        return new GetClosestAddressDataResponse();
    }

    /**
     * Create an instance of {@link DumpDataResponse }
     * 
     */
    public DumpDataResponse createDumpDataResponse() {
        return new DumpDataResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "PFAddressResponse")
    public JAXBElement<PFAddressResponse> createPFAddressResponse(PFAddressResponse value) {
        return new JAXBElement<PFAddressResponse>(_PFAddressResponse_QNAME, PFAddressResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFAddressData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "PFAddressData")
    public JAXBElement<PFAddressData> createPFAddressData(PFAddressData value) {
        return new JAXBElement<PFAddressData>(_PFAddressData_QNAME, PFAddressData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPFAddressData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "ArrayOfPFAddressData")
    public JAXBElement<ArrayOfPFAddressData> createArrayOfPFAddressData(ArrayOfPFAddressData value) {
        return new JAXBElement<ArrayOfPFAddressData>(_ArrayOfPFAddressData_QNAME, ArrayOfPFAddressData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFDirectionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "PFDirectionsResponse")
    public JAXBElement<PFDirectionsResponse> createPFDirectionsResponse(PFDirectionsResponse value) {
        return new JAXBElement<PFDirectionsResponse>(_PFDirectionsResponse_QNAME, PFDirectionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "StreetNumber", scope = PFAddressData.class)
    public JAXBElement<String> createPFAddressDataStreetNumber(String value) {
        return new JAXBElement<String>(_PFAddressDataStreetNumber_QNAME, String.class, PFAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "RegionAbbreviation", scope = PFAddressData.class)
    public JAXBElement<String> createPFAddressDataRegionAbbreviation(String value) {
        return new JAXBElement<String>(_PFAddressDataRegionAbbreviation_QNAME, String.class, PFAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "AreaName", scope = PFAddressData.class)
    public JAXBElement<String> createPFAddressDataAreaName(String value) {
        return new JAXBElement<String>(_PFAddressDataAreaName_QNAME, String.class, PFAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "OrganizationName", scope = PFAddressData.class)
    public JAXBElement<String> createPFAddressDataOrganizationName(String value) {
        return new JAXBElement<String>(_PFAddressDataOrganizationName_QNAME, String.class, PFAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "LandmarkName", scope = PFAddressData.class)
    public JAXBElement<String> createPFAddressDataLandmarkName(String value) {
        return new JAXBElement<String>(_PFAddressDataLandmarkName_QNAME, String.class, PFAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "UnitNumber", scope = PFAddressData.class)
    public JAXBElement<String> createPFAddressDataUnitNumber(String value) {
        return new JAXBElement<String>(_PFAddressDataUnitNumber_QNAME, String.class, PFAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "PostCode", scope = PFAddressData.class)
    public JAXBElement<String> createPFAddressDataPostCode(String value) {
        return new JAXBElement<String>(_PFAddressDataPostCode_QNAME, String.class, PFAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "StreetName", scope = PFAddressData.class)
    public JAXBElement<String> createPFAddressDataStreetName(String value) {
        return new JAXBElement<String>(_PFAddressDataStreetName_QNAME, String.class, PFAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PartialStreetName", scope = GetAddressDataStreets.class)
    public JAXBElement<String> createGetAddressDataStreetsPartialStreetName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPartialStreetName_QNAME, String.class, GetAddressDataStreets.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "UnitNumber", scope = GetAddressDataStreets.class)
    public JAXBElement<String> createGetAddressDataStreetsUnitNumber(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsUnitNumber_QNAME, String.class, GetAddressDataStreets.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "OrderBy", scope = GetAddressDataStreets.class)
    public JAXBElement<String> createGetAddressDataStreetsOrderBy(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsOrderBy_QNAME, String.class, GetAddressDataStreets.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PostCode", scope = GetAddressDataStreets.class)
    public JAXBElement<String> createGetAddressDataStreetsPostCode(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPostCode_QNAME, String.class, GetAddressDataStreets.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PartialAreaName", scope = GetAddressDataStreets.class)
    public JAXBElement<String> createGetAddressDataStreetsPartialAreaName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPartialAreaName_QNAME, String.class, GetAddressDataStreets.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "StreetNumber", scope = GetAddressDataStreets.class)
    public JAXBElement<String> createGetAddressDataStreetsStreetNumber(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsStreetNumber_QNAME, String.class, GetAddressDataStreets.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "RegionAbbreviation", scope = GetAddressDataStreets.class)
    public JAXBElement<String> createGetAddressDataStreetsRegionAbbreviation(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsRegionAbbreviation_QNAME, String.class, GetAddressDataStreets.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "OrganizationName", scope = GetAddressDataStreets.class)
    public JAXBElement<String> createGetAddressDataStreetsOrganizationName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsOrganizationName_QNAME, String.class, GetAddressDataStreets.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "LandmarkName", scope = GetAddressDataStreets.class)
    public JAXBElement<String> createGetAddressDataStreetsLandmarkName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsLandmarkName_QNAME, String.class, GetAddressDataStreets.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "GetAddressDataStreetsResult", scope = GetAddressDataStreetsResponse.class)
    public JAXBElement<PFAddressResponse> createGetAddressDataStreetsResponseGetAddressDataStreetsResult(PFAddressResponse value) {
        return new JAXBElement<PFAddressResponse>(_GetAddressDataStreetsResponseGetAddressDataStreetsResult_QNAME, PFAddressResponse.class, GetAddressDataStreetsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "LocationTo", scope = GetDirections.class)
    public JAXBElement<String> createGetDirectionsLocationTo(String value) {
        return new JAXBElement<String>(_GetDirectionsLocationTo_QNAME, String.class, GetDirections.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "LocationFrom", scope = GetDirections.class)
    public JAXBElement<String> createGetDirectionsLocationFrom(String value) {
        return new JAXBElement<String>(_GetDirectionsLocationFrom_QNAME, String.class, GetDirections.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "OrderBy", scope = GetRegionData.class)
    public JAXBElement<String> createGetRegionDataOrderBy(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsOrderBy_QNAME, String.class, GetRegionData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PartialRegionName", scope = GetRegionData.class)
    public JAXBElement<String> createGetRegionDataPartialRegionName(String value) {
        return new JAXBElement<String>(_GetRegionDataPartialRegionName_QNAME, String.class, GetRegionData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "GetRegionDataResult", scope = GetRegionDataResponse.class)
    public JAXBElement<PFAddressResponse> createGetRegionDataResponseGetRegionDataResult(PFAddressResponse value) {
        return new JAXBElement<PFAddressResponse>(_GetRegionDataResponseGetRegionDataResult_QNAME, PFAddressResponse.class, GetRegionDataResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PartialStreetName", scope = GetLandmarkData.class)
    public JAXBElement<String> createGetLandmarkDataPartialStreetName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPartialStreetName_QNAME, String.class, GetLandmarkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "UnitNumber", scope = GetLandmarkData.class)
    public JAXBElement<String> createGetLandmarkDataUnitNumber(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsUnitNumber_QNAME, String.class, GetLandmarkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "OrderBy", scope = GetLandmarkData.class)
    public JAXBElement<String> createGetLandmarkDataOrderBy(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsOrderBy_QNAME, String.class, GetLandmarkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PostCode", scope = GetLandmarkData.class)
    public JAXBElement<String> createGetLandmarkDataPostCode(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPostCode_QNAME, String.class, GetLandmarkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "StreetNumber", scope = GetLandmarkData.class)
    public JAXBElement<String> createGetLandmarkDataStreetNumber(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsStreetNumber_QNAME, String.class, GetLandmarkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "RegionAbbreviation", scope = GetLandmarkData.class)
    public JAXBElement<String> createGetLandmarkDataRegionAbbreviation(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsRegionAbbreviation_QNAME, String.class, GetLandmarkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "OrganizationName", scope = GetLandmarkData.class)
    public JAXBElement<String> createGetLandmarkDataOrganizationName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsOrganizationName_QNAME, String.class, GetLandmarkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "LandmarkName", scope = GetLandmarkData.class)
    public JAXBElement<String> createGetLandmarkDataLandmarkName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsLandmarkName_QNAME, String.class, GetLandmarkData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "GetLandmarkDataResult", scope = GetLandmarkDataResponse.class)
    public JAXBElement<PFAddressResponse> createGetLandmarkDataResponseGetLandmarkDataResult(PFAddressResponse value) {
        return new JAXBElement<PFAddressResponse>(_GetLandmarkDataResponseGetLandmarkDataResult_QNAME, PFAddressResponse.class, GetLandmarkDataResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PartialStreetName", scope = GetAreaAddress.class)
    public JAXBElement<String> createGetAreaAddressPartialStreetName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPartialStreetName_QNAME, String.class, GetAreaAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "UnitNumber", scope = GetAreaAddress.class)
    public JAXBElement<String> createGetAreaAddressUnitNumber(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsUnitNumber_QNAME, String.class, GetAreaAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "OrderBy", scope = GetAreaAddress.class)
    public JAXBElement<String> createGetAreaAddressOrderBy(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsOrderBy_QNAME, String.class, GetAreaAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PostCode", scope = GetAreaAddress.class)
    public JAXBElement<String> createGetAreaAddressPostCode(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPostCode_QNAME, String.class, GetAreaAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PartialAreaName", scope = GetAreaAddress.class)
    public JAXBElement<String> createGetAreaAddressPartialAreaName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPartialAreaName_QNAME, String.class, GetAreaAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "StreetNumber", scope = GetAreaAddress.class)
    public JAXBElement<String> createGetAreaAddressStreetNumber(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsStreetNumber_QNAME, String.class, GetAreaAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "RegionAbbreviation", scope = GetAreaAddress.class)
    public JAXBElement<String> createGetAreaAddressRegionAbbreviation(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsRegionAbbreviation_QNAME, String.class, GetAreaAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "OrganizationName", scope = GetAreaAddress.class)
    public JAXBElement<String> createGetAreaAddressOrganizationName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsOrganizationName_QNAME, String.class, GetAreaAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "LandmarkName", scope = GetAreaAddress.class)
    public JAXBElement<String> createGetAreaAddressLandmarkName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsLandmarkName_QNAME, String.class, GetAreaAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFDirectionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "GetDirectionsResult", scope = GetDirectionsResponse.class)
    public JAXBElement<PFDirectionsResponse> createGetDirectionsResponseGetDirectionsResult(PFDirectionsResponse value) {
        return new JAXBElement<PFDirectionsResponse>(_GetDirectionsResponseGetDirectionsResult_QNAME, PFDirectionsResponse.class, GetDirectionsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "GetAreaAddressResult", scope = GetAreaAddressResponse.class)
    public JAXBElement<PFAddressResponse> createGetAreaAddressResponseGetAreaAddressResult(PFAddressResponse value) {
        return new JAXBElement<PFAddressResponse>(_GetAreaAddressResponseGetAreaAddressResult_QNAME, PFAddressResponse.class, GetAreaAddressResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "Status", scope = PFAddressResponse.class)
    public JAXBElement<String> createPFAddressResponseStatus(String value) {
        return new JAXBElement<String>(_PFAddressResponseStatus_QNAME, String.class, PFAddressResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPFAddressData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "AddressData", scope = PFAddressResponse.class)
    public JAXBElement<ArrayOfPFAddressData> createPFAddressResponseAddressData(ArrayOfPFAddressData value) {
        return new JAXBElement<ArrayOfPFAddressData>(_PFAddressResponseAddressData_QNAME, ArrayOfPFAddressData.class, PFAddressResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "GetClosestAddressDataRelativeResult", scope = GetClosestAddressDataRelativeResponse.class)
    public JAXBElement<PFAddressResponse> createGetClosestAddressDataRelativeResponseGetClosestAddressDataRelativeResult(PFAddressResponse value) {
        return new JAXBElement<PFAddressResponse>(_GetClosestAddressDataRelativeResponseGetClosestAddressDataRelativeResult_QNAME, PFAddressResponse.class, GetClosestAddressDataRelativeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PartialStreetName", scope = GetAddressData.class)
    public JAXBElement<String> createGetAddressDataPartialStreetName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPartialStreetName_QNAME, String.class, GetAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "UnitNumber", scope = GetAddressData.class)
    public JAXBElement<String> createGetAddressDataUnitNumber(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsUnitNumber_QNAME, String.class, GetAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "OrderBy", scope = GetAddressData.class)
    public JAXBElement<String> createGetAddressDataOrderBy(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsOrderBy_QNAME, String.class, GetAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "PostCode", scope = GetAddressData.class)
    public JAXBElement<String> createGetAddressDataPostCode(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsPostCode_QNAME, String.class, GetAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "StreetNumber", scope = GetAddressData.class)
    public JAXBElement<String> createGetAddressDataStreetNumber(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsStreetNumber_QNAME, String.class, GetAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "RegionAbbreviation", scope = GetAddressData.class)
    public JAXBElement<String> createGetAddressDataRegionAbbreviation(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsRegionAbbreviation_QNAME, String.class, GetAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "OrganizationName", scope = GetAddressData.class)
    public JAXBElement<String> createGetAddressDataOrganizationName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsOrganizationName_QNAME, String.class, GetAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "LandmarkName", scope = GetAddressData.class)
    public JAXBElement<String> createGetAddressDataLandmarkName(String value) {
        return new JAXBElement<String>(_GetAddressDataStreetsLandmarkName_QNAME, String.class, GetAddressData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "Status", scope = PFDirectionsResponse.class)
    public JAXBElement<String> createPFDirectionsResponseStatus(String value) {
        return new JAXBElement<String>(_PFAddressResponseStatus_QNAME, String.class, PFDirectionsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/DDSAddressConnectionManagerService", name = "DirectionsToDestination", scope = PFDirectionsResponse.class)
    public JAXBElement<String> createPFDirectionsResponseDirectionsToDestination(String value) {
        return new JAXBElement<String>(_PFDirectionsResponseDirectionsToDestination_QNAME, String.class, PFDirectionsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "TestServiceResult", scope = TestServiceResponse.class)
    public JAXBElement<String> createTestServiceResponseTestServiceResult(String value) {
        return new JAXBElement<String>(_TestServiceResponseTestServiceResult_QNAME, String.class, TestServiceResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "GetClosestAddressDataResult", scope = GetClosestAddressDataResponse.class)
    public JAXBElement<PFAddressResponse> createGetClosestAddressDataResponseGetClosestAddressDataResult(PFAddressResponse value) {
        return new JAXBElement<PFAddressResponse>(_GetClosestAddressDataResponseGetClosestAddressDataResult_QNAME, PFAddressResponse.class, GetClosestAddressDataResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PFAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "GetAddressDataResult", scope = GetAddressDataResponse.class)
    public JAXBElement<PFAddressResponse> createGetAddressDataResponseGetAddressDataResult(PFAddressResponse value) {
        return new JAXBElement<PFAddressResponse>(_GetAddressDataResponseGetAddressDataResult_QNAME, PFAddressResponse.class, GetAddressDataResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://DDSAddressConnectionManagerService", name = "DumpDataResult", scope = DumpDataResponse.class)
    public JAXBElement<String> createDumpDataResponseDumpDataResult(String value) {
        return new JAXBElement<String>(_DumpDataResponseDumpDataResult_QNAME, String.class, DumpDataResponse.class, value);
    }

}
