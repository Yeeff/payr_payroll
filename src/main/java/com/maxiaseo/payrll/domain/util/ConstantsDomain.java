package com.maxiaseo.payrll.domain.util;

import java.time.LocalTime;

public class ConstantsDomain {

    public static final LocalTime NIGHT_START = LocalTime.of(21, 0);
    public static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    public static final Long STEP_IN_MINUTES = 30L;

    public static final Integer MAXIMUM_HOURS_PER_DAY = 8;
    public static final Integer MAXIMUM_HOURS_PER_WEEK = 46;

    public static final String SIIGO_FORMAT_NAME = "Subir novedades desde Excel.xlsx";

    public enum TimeFormat {
        REGULAR, // 12-hour format with am/pm (e.g., "7:30am", "1:45pm")
        MILITARY_WITHOUT_COLON,
        MILITARY // 24-hour format (e.g., "07:30", "13:45")
    }

    public enum OvertimeSurchargeTypeEnum {
        HOLIDAY, NIGHT_HOLIDAY
    }

    public enum OvertimeTypeEnum {
        DAY, NIGHT, HOLIDAY, NIGHT_HOLIDAY
    }

    public enum SurchargeTypeEnum {
        NIGHT,
        HOLIDAY,
        NIGHT_HOLIDAY,
        DAY
    }

    public enum AbsenceReasonsEnum {
        INC_ARL,        //INCAPACIDAD_ARL
        INC,            //INCAPACIDAD_CON_SOPORTE,
        INC_SIN_SOPR,   //INCAPACIDAD_SIN_SOPORTE,
        PNR,            //PERMISO_NO_REMUNERADO,
        LR,             //LICENCIA_REMUNERADA,
        AUS,            //AUSENTISMO,
        EPS,            //COLABORADOR_EN_EPS,
        RET,            //RETIRO
        DESC,            //DESCANSO
        INC_FONDO,
        VAC,
        LM,
        X
    }

    public static final Integer FIRST_ROW_WITH_VALID_DATA_INDEX = 1;
    public static final Integer FIRST_COLUM_WITH_VALID_DATA_INDEX = 2;

    public static final Integer EMPLOYEE_DOCUMENT_ID_INDEX = 0;
    public static final Integer EMPLOYEE_NAME_INDEX = 1;

    public static final String FILE_NOT_FOUND_MESSAGE_ERROR = "File not found: '%s'";
    public static final String FILE_SERVICE_MESSAGE_ERROR = "File Service error:: '%s'";


}
