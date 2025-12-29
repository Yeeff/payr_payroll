package com.horizonx.overtime_services.domain.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class ConstantsDomain {

    public static final LocalTime NIGHT_START = LocalTime.of(21, 0);
    public static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    public static final Long STEP_IN_MINUTES = 30L;

    public static final Integer MAXIMUM_HOURS_PER_DAY = 8;
    public static final Integer MAXIMUM_HOURS_PER_WEEK = 44;

    public static final String SIIGO_FORMAT_NAME = "Subir novedades desde Excel.xlsx";

    public enum TimeFormat {
        REGULAR, // 12-hour format with am/pm (e.g., "7:30am", "1:45pm")
        MILITARY_WITHOUT_COLON,
        MILITARY // 24-hour format (e.g., "07:30", "13:45")
    }

    public enum OvertimeTypeEnum {
        DAY, NIGHT, HOLIDAY, NIGHT_HOLIDAY
    }

    public enum SurchargeTypeEnum {
        NIGHT,
        OVERTIME_HOLIDAY,
        HOLIDAY,
        NIGHT_HOLIDAY,
        OVERTIME_NIGHT_HOLIDAY,
        DAY
    }
   // PNR,LR, INC
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
        SUS,             //SUSPENSION
        DLF,             //FAMILY DAY
        INC_FONDO,
        VAC,
        LM,
        X
    }

    public static final Set<LocalDate> holidays = Set.of(
            LocalDate.of(2025, 5 ,1 ), //Corpus Christi
            LocalDate.of(2025, 6 ,23 ), //Corpus Christi
            LocalDate.of(2025, 6 , 30), //Dia San Pedro y San Pablo
            LocalDate.of(2025, 7, 20),  // Día de la Independencia
            LocalDate.of(2025, 8, 7),  // Batalla de Boyaca
            LocalDate.of(2025, 8, 18),  //  La asusncion de la virgen
            LocalDate.of(2025, 10, 13),  //Dia de la raza
            LocalDate.of(2025, 11, 3),  // Todos los santos
            LocalDate.of(2025, 11, 17),  // Independencia de Cartagena
            LocalDate.of(2025, 12, 8),  // Dia de la inmaculada concepcion
            LocalDate.of(2025, 12, 25)  // Dia de navidad

    );

    public static final Integer FIRST_ROW_WITH_VALID_DATA_INDEX = 1;
    public static final Integer FIRST_COLUM_WITH_VALID_DATA_INDEX = 2;

    public static final Integer EMPLOYEE_DOCUMENT_ID_INDEX = 0;
    public static final Integer EMPLOYEE_NAME_INDEX = 1;

    public static final String FILE_NOT_FOUND_MESSAGE_ERROR = "File not found: '%s'";
    public static final String FILE_SERVICE_MESSAGE_ERROR = "File Service error:: '%s'";

    public static final Set<String> VALID_CODES = Set.of(
            AbsenceReasonsEnum.INC_ARL.toString(),
            AbsenceReasonsEnum.INC.toString(),
            AbsenceReasonsEnum.INC_SIN_SOPR.toString(),
            AbsenceReasonsEnum.PNR.toString(),
            AbsenceReasonsEnum.LR.toString(),
            AbsenceReasonsEnum.AUS.toString(),
            AbsenceReasonsEnum.EPS.toString(),
            AbsenceReasonsEnum.RET.toString(),
            AbsenceReasonsEnum.DESC.toString(),
            AbsenceReasonsEnum.INC_FONDO.toString(),
            AbsenceReasonsEnum.VAC.toString(),
            AbsenceReasonsEnum.LM.toString(),
            AbsenceReasonsEnum.X.toString()
    );

    public static final String INVALID_VALUE_MESSAGE_ERROR = "'%s' no, es un valor válido.";
    public static final Integer FIRST_DAY_OF_SECOND_FORTNIGHT = 16;
    public static final String EMPTY_NAME_VALUE_MESSAGE_ERROR = "El campo nombre no puede estar vacio.";
    public static final String DOCUMENT_ID_VALUE_MESSAGE_ERROR = "El valor '%s' no es válido como numero de identificacion del empleado.";
    public static final Integer FIRST_DAY_OF_FIRST_FORTNIGHT = 1;
    public static final String LAST_VALUE_FIRST_FORTNIGHT_MESSAGE_ERROR = "El ultimo dia la quincena debe ser 15 pero después de esa columana se encontro el valor: %s";

    public static final Integer FIRST_DAY_OF_MONTH = 1;
    public static final String LAST_VALUE_SECOND_FORTNIGHT_MESSAGE_ERROR = "El ultimo dia de %s es %s pero después de esa columana se encontro el valor: %s";
    public static final Integer LAST_DAY_OF_FIRST_FORTNIGHT = 15;
    public static final String NOT_CORRESPONDING_QUANTITY_OF_DAYS_FOR_FORTNIGHT_MESSAGE_ERROR = "La fila %s no contiene la candidad de dias correspondiente.";







}
