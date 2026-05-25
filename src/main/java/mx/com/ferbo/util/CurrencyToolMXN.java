package mx.com.ferbo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CurrencyToolMXN {

    private static final String[] UNIDADES = {
        "", "UN", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"
    };
    
    private static final String[] DECENAS = {
        "DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISEIS", "DIECISIETE", 
        "DIECIOCHO", "DIECINUEVE", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", 
        "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"
    };
    
    private static final String[] CENTENAS = {
        "CIEN", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", 
        "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"
    };
    
    private static final String[] MILES = {
        "MIL", "MILLON", "MILLONES"
    };

    public static String convertirAMoneda(BigDecimal cantidad) {
        if (cantidad == null) {
            throw new IllegalArgumentException("La cantidad no puede ser nula");
        }
        
        // Redondear a 2 decimales
        BigDecimal cantidadRedondeada = cantidad.setScale(2, RoundingMode.HALF_UP);
        
        // Obtener parte entera y decimal
        long parteEntera = cantidadRedondeada.longValue();
        int parteDecimal = cantidadRedondeada.remainder(BigDecimal.ONE)
                .multiply(new BigDecimal(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
        
        // Convertir parte entera a texto
        String textoEntero = convertirNumeroATexto(parteEntera);
        
        // Construir el resultado final
        StringBuilder resultado = new StringBuilder();
        resultado.append("(");
        
        if (parteEntera == 0) {
            resultado.append("CERO");
        } else {
            resultado.append(textoEntero);
        }
        
        // Agregar "PESOS" o "PESO" según corresponda
        if (parteEntera == 1) {
            resultado.append(" PESO ");
        } else {
            resultado.append(" PESOS ");
        }
        
        // Agregar fracción decimal
        resultado.append(String.format("%02d/100 M. N.)", parteDecimal));
        
        return resultado.toString();
    }
    
    private static String convertirNumeroATexto(long numero) {
        if (numero == 0) {
            return "CERO";
        }
        
        List<String> partes = new ArrayList<>();
        
        // Convertir miles de millones
        if (numero >= 1_000_000_000) {
            long milesMillones = numero / 1_000_000_000;
            if (milesMillones == 1) {
                partes.add("MIL");
            } else if (milesMillones > 1) {
                partes.add(convertirCentenas((int)(milesMillones / 1000)));
                partes.add("MIL");
                partes.add(convertirCentenas((int)(milesMillones % 1000)));
            }
            partes.add("MILLONES");
            numero %= 1_000_000_000;
        }
        
        // Convertir millones
        if (numero >= 1_000_000) {
            long millones = numero / 1_000_000;
            if (millones == 1) {
                partes.add("UN MILLON");
            } else {
                partes.add(convertirCentenas((int)millones));
                partes.add("MILLONES");
            }
            numero %= 1_000_000;
        }
        
        // Convertir miles
        if (numero >= 1000) {
            int miles = (int)(numero / 1000);
            if (miles == 1) {
                partes.add("MIL");
            } else {
                partes.add(convertirCentenas(miles));
                partes.add("MIL");
            }
            numero %= 1000;
        }
        
        // Convertir centenas restantes
        if (numero > 0) {
            partes.add(convertirCentenas((int)numero));
        }
        
        // Unir todas las partes
        return String.join(" ", partes).trim();
    }
    
    private static String convertirCentenas(int numero) {
        if (numero == 0) {
            return "";
        }
        
        if (numero < 10) {
            return UNIDADES[numero];
        }
        
        if (numero < 20) {
            return DECENAS[numero - 10];
        }
        
        if (numero < 100) {
            int decena = (numero / 10) - 2 + 10; // Ajustar índice para DECENAS
            int unidad = numero % 10;
            
            if (unidad == 0) {
                return DECENAS[decena];
            } else if (numero >= 21 && numero <= 29) {
                return "VEINTI" + UNIDADES[unidad];
            } else {
                return DECENAS[decena] + " Y " + UNIDADES[unidad];
            }
        }
        
        // Números entre 100 y 999
        int centena = (numero / 100) - 1;
        int resto = numero % 100;
        
        String textoCentena = CENTENAS[centena];
        
        if (resto == 0) {
            return textoCentena;
        }
        
        if (centena == 0 && resto > 0) { // CIEN -> CIENTO
            textoCentena = "CIENTO";
        }
        
        return textoCentena + " " + convertirCentenas(resto);
    }

    // Método main para pruebas
    public static void main(String[] args) {
        // Ejemplos de prueba
        BigDecimal[] pruebas = {
        	new BigDecimal("24456.10"),
        	new BigDecimal("10426.25"),
            new BigDecimal("10456.25"),
            new BigDecimal("1234.56"),
            new BigDecimal("1.00"),
            new BigDecimal("0.50"),
            new BigDecimal("1000000.00"),
            new BigDecimal("1500.75"),
            new BigDecimal("999999.99")
        };
        
        for (BigDecimal prueba : pruebas) {
            System.out.println(prueba + " -> " + convertirAMoneda(prueba));
        }
    }
}