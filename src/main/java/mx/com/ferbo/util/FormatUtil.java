package mx.com.ferbo.util;

public class FormatUtil {
	
	/**
	*Convierte un numero a su representacion en palabras
	*/
	public static String numeroPalabras(float numero)
	{
		StringBuffer sf=new StringBuffer(50);
		int res,tmp;
		boolean ponerCalif=false;
		//La maxima potencia de 10 que se tiene contemplada para la
		//conversion con el valor de 8 se tiene hasta cientos de millones
		int MAX_POT_10=8;
		
		String palabras[][]={
			 {"","UN","DOS","TRES","CUATRO","CINCO","SEIS","SIETE","OCHO","NUEVE","DIEZ"
			 ,"ONCE","DOCE","TRECE","CATORCE","QUINCE","DIECISEIS","DIECISIETE","DIECIOCHO"
			 ,"DIECINUEVE"}
			,{"","","","TREINTA","CUARENTA","CINCUENTA","SESENTA","SETENTA","OCHENTA","NOVENTA"}
			,{"","CIEN","DOSCIENTOS","TRESCIENTOS","CUATROCIENTOS","QUINIENTOS","SEISCIENTOS","SETECIENTOS"
			,"OCHOCIENTOS","NOVECIENTOS"}
			
			};
		String calificadores[]={"","MIL","MILLONES"};
			
		for(int i=MAX_POT_10,subc=2;i>=0;i--,subc--)
		{
		 //Obtenemos el valor del numero en la actual potencia
		   res =(int)(numero/Math.pow(10,i));
		  // System.out.println(res+":"+i+":"+subc);
		 
		   
		   
		   //En el caso de que estemos en la primera
		   //decena
		   if(res==1 && subc==1)
		   {
		   	//Necesitamos saber cual de los primeros 20 numeros es,
		   	//nos traemos el siguiente digito
		   	tmp=(int)(numero/Math.pow(10,i-1));
		   	sf.append(" "+palabras[0][tmp]);
		   	
		   	//Antes de regresar ponemos los contadores en orden
		   	//Nos saltamos el siguiente digito y la posicion en la terna
		   	//se regresa
		   	i--;
		   	subc=3;
		   	res=tmp;
		   	ponerCalif=true;
		   	
		   }
		   else
		   //En el caso en que estemos en la segunda decena
		   if(res==2&&subc==1)
		   {
		    
		    //El unico caso especial en esta decena es el VEINTE
		    tmp=(int)(numero/Math.pow(10,i-1))-20;
		    if(tmp==0)
		    	sf.append(" VEINTE");
		    else
		      sf.append(" VEINTI"+palabras[0][tmp]);
		    
		    //Antes de regresar ponemos los contadores en orden
		   	//Nos saltamos el siguiente digito y la posicion en la terna
		   	//se regresa
		   	numero-=res*Math.pow(10,i);
		   	i--;
		   	//subc=3;
		   	subc=3;
		   	res=tmp;
		   	ponerCalif=true;
		   }
		   else
		   //En el caso de que estemos en la primer centena
		   if(res==1&&subc==2)
		   {
		   	tmp=(int)(numero/Math.pow(10,i-2));
		   	if(tmp==100)
		   		sf.append(" CIEN");
		   	else
		   		sf.append(" CIENTO");
		   	ponerCalif=true;
		   }
		   else
		   {
		   	//El resto de los casos
		   	
		   	//Para que no imprima nada si es cero
		   	if(res!=0)
		   	{
		   		sf.append(" "+palabras[subc][res]);	
		   		if(subc==1)
		   		{
		  			//debemos verificar los casos enq ue es una decena sin
		   			//unidades
		   			tmp=(int)(numero/Math.pow(10,i-1));
		   			if(tmp%10!=0)
		   			sf.append(" Y");
		   		}
		   		ponerCalif=true;	
		   	}
		   	
		   }	   
		   
		   //En esta parte del algoritmo
		   //establece en que rango de potencias estamos: miles, millones,etc
		   //se va a agregar cuando la potencia de 10 sea divisible entre 3
		   if(i%3==0 && i/3<calificadores.length &&ponerCalif)
		   {
		   	 	sf.append(" "+calificadores[i/3]);
		   	 	ponerCalif=false;
		   }
		   
		   if(subc==0)
		   {
		   	subc=3;	
		   }
		   
		   //Antes de terminar la iteracion le restamos al numero el
		   //digito transformado
		   numero-=res*Math.pow(10,i);  
		}
		return sf.toString();
	}
	
	public static String numeroPalabras(double d)
    {
        StringBuffer stringbuffer = new StringBuffer(50);
        boolean flag = false;
        byte byte0 = 8;
        String as[][] = {
            {
                "", "UN", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE", 
                "DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISEIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"
            }, {
                "", "", "", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"
            }, {
                "", "CIEN", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"
            }
        };
        String as1[] = {
            "", "MIL", "MILLONES"
        };
        int i = byte0;
        for(int j = 2; i >= 0; j--)
        {
            int k = (int)(d / Math.pow(10D, i));
            if(k == 1 && j == 1)
            {
                int l = (int)(d / Math.pow(10D, i - 1));
                stringbuffer.append(" " + as[0][l]);
                i--;
                j = 3;
                k = l;
                flag = true;
            } else
            if(k == 2 && j == 1)
            {
                int i1 = (int)(d / Math.pow(10D, i - 1)) - 20;
                if(i1 == 0)
                    stringbuffer.append(" VEINTE");
                else
                    stringbuffer.append(" VEINTI" + as[0][i1]);
                d -= (double)k * Math.pow(10D, i);
                i--;
                j = 3;
                k = i1;
                flag = true;
            } else
            if(k == 1 && j == 2)
            {
                int j1 = (int)(d / Math.pow(10D, i - 2));
                if(j1 == 100)
                    stringbuffer.append(" CIEN");
                else
                    stringbuffer.append(" CIENTO");
                flag = true;
            } else
            if(k != 0)
            {
                stringbuffer.append(" " + as[j][k]);
                if(j == 1)
                {
                    int k1 = (int)(d / Math.pow(10D, i - 1));
                    if(k1 % 10 != 0)
                        stringbuffer.append(" Y");
                }
                flag = true;
            }
            if(i % 3 == 0 && i / 3 < as1.length && flag)
            {
                stringbuffer.append(" " + as1[i / 3]);
                flag = false;
            }
            if(j == 0)
                j = 3;
            d -= (double)k * Math.pow(10D, i);
            i--;
        }

        return stringbuffer.toString();
    }

}
