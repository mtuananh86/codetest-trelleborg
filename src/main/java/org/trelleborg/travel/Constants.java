package org.trelleborg.travel;

import java.util.regex.Pattern;

public class Constants {

    public static class Fields {
        public static final String IdField = "Id";
        public static final String DateTimeUTCField = "DateTimeUTC";
        public static final String TouchTypeField = "TouchType";
        public static final String StopIdField = "StopId";
        public static final String CompanyIdField = "CompanyId";
        public static final String BusIdField = "BusId";
        public static final String PanField = "PAN";
        public static final String DateField = "Date";
        public static final String CompleteTripCountField = "CompleteTripCount";
        public static final String IncompleteTripCountField = "IncompleteTripCount";
        public static final String CancelledTripCountField = "CancelledTripCount";
        public static final String TotalChargesField = "TotalCharges";

        public static final String StartedField = "Started";
        public static final String FinishedField = "Finished";
        public static final String DurationSecField = "DurationSec";
        public static final String FromStopIdField = "FromStopId";
        public static final String ToStopIdField = "ToStopId";
        public static final String ChargeAmountField = "ChargeAmount";
        public static final String HashedPanField = "HashedPan";
        public static final String StatusField = "Status";

        public static final String[] TouchFields = new String[] {
                IdField, DateTimeUTCField,
                TouchTypeField, StopIdField,
                CompanyIdField, BusIdField, PanField
        };

        public static final String[] TouchErrorFields = new String[] {
                StartedField, FinishedField, DurationSecField,
                FromStopIdField, ToStopIdField, ChargeAmountField,
                HashedPanField, StatusField
        };

        public static final String[] TripReportFields = new String[] {
                StartedField, FinishedField, DurationSecField,
                FromStopIdField, ToStopIdField, ChargeAmountField,
                HashedPanField, StatusField
        };

        public static final String[] CompanyTripReportFields = new String[] {
                DateField,CompanyIdField,BusIdField,
                CompleteTripCountField,IncompleteTripCountField,
                CancelledTripCountField,TotalChargesField
        };
    }

    public static class DateTime {
        public static final String PATTERN = "dd-MM-yyyy HH:mm:ss";
    }

    public static class Pan {
        public static final Pattern PATTERN = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    }
}
