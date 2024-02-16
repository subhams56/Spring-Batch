public class ZipCOdeMapper implements FieldSetMapper<ZIPCodes> {

    @Override
    public ZIPCodes mapFieldSet(FieldSet fieldSet) throws BindException {
        return new ZIPCodes(
                fieldSet.readString("Zip_Code"),
                fieldSet.readString("Official_USPS_city_name"),
                fieldSet.readString("Official_USPS_State_Code"));
    }
}
