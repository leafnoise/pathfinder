================================================
PATHFINDER - Aplicativo Interceptor de Eventos
------------------------------------------------
Pathfinder es un aplicativo inicialmente pensado para recibir instancias de eventos que suceden, almacenarlos y procesarlos aplicando una lógica todavía no establecida.
Los eventos se reciben via una interfaz de servicios Web REST, y se almacenan en una base de datos NoSql para luego ser procesados. 
Pueden consultarse los eventos mediante la misma interfaz de servicios WEB y generarse archivos de ontologías en base a las búsquedas realizadas para aplicar posterior lógica de procesamiento. 

================================================
REQUISITOS PARA EJECUCION
A continuación se indican los componentes necesarios para poder ejecutar la aplicación:
------------------------------------------------
	• JDK 1.6.x
	• JBoss 7.1.0 
	• Maven 3.0.4
	• MongoDB

================================================
CONFIGURACION
A continuación se detallan las configuraciones básicas para la correcta ejecución del aplicativo
------------------------------------------------
Renombrar el archivo standalone.xml por standalone-bkp.xml y standalone-full-ha.xml por standalone.xml.
Ambos archivos se encuentran localizados en <JBOSS_DIRECTORY>/standalone/configuration

================================================
EJECUCION
A continuación se detalla el caso básico de prueba establecido para la actual entrega
------------------------------------------------
Para enviar un evento al aplicativo se debe invocar mediante método HTTP-POST al servicio REST:
	
	http://localhost:8080/pathfinder/rest/send

Teniendo en cuenta lo siguiente: (sirve para todas las invocaciones a los servicios WEB):
	a) http://localhost:8080 -> es el servidor y puerto en el que se encuentra levantado el aplicativo
	b) /pathfinder -> es el contexto del aplicativo
	c) /rest -> es la interfaz de servicios web
	d) /send -> es el servicio invocado
El servicio SEND consume un JSON con el siguiente formato:

{"type":<LITERAL_STRING>,"agent":<LITERAL_STRING>,"product":<LITERAL_JSON_VALIDO>[,"time":<LITERAL_LONG>,"location":<LITERAL_STRING>,"parent":<LITERAL_STRING>,]}

Teniendo en cuenta lo siguiente:

Los parámetros obligatorios son:
	• type: define el tipo de evento que se está disparando, necesario para posterior diferenciación
	• agent: el ente disparador o generador del evento
	• product: <VALID_JSON> producto del evento variable a cada tipo de evento y de acuerdo con las ontologías que se declaren posteriormente 
Los parámetros opcionales son:
	• time: <LONG_NUMBER> timestamp java que define el momento en el que fue disparado (independientemente del momento en el que fue enviado al sistema), por defecto es el momento en el que el sistema recibe la instancia del evento
	• location: <STRING> define una geolocaclización del evento -> (no implementado pero especificado por ontología de eventos utilizada)
	• parent: <STRING> define el evento padrel -> (no implementado pero especificado por ontología de eventos utilizada)
Los parámetros independientes agregados por el sistema
	• received: <DATETIME> se crea automáticamente cuando se recive la instancia del evento, define el momento de recepción de la instancia.	  


Ejemplo de mensaje mínimo para enviar un evento "SYSLOG" (falta definir ontología) pero se asume que tendrá un axioma que define un contenido de nombre "hasContent" de valor literal STRING y otro axioma de nombre "hasLevel" de valor literal STRING)

{"type":"syslog","agent":"testCase","product":{"hasContent":"LOG CONTENT","hasLevel":"INFO"}}

Para recuperar las instancias de los eventos se poseen 5 Servicios Web REST que deben ser invocados mediante método HTTP-GET:

1)/receiveall(HttpServletRequest)
	Busca todos los eventos recibidos de todos los tipos y todos los agentes
2)/receive/agent/{agent}
	Busca todos los eventos disparados por el agente
3)/receive/type/{type}
	Busca todos los eventos del tipo especificado
4)/receive/typeandagent/{type}/{agent}
	Busca todos los eventos del tipo especificado que hayan sido disparados por el agente especificado
5)/receive/between/{timestamp1}/and/{timestamp2}
	Busca todos los eventos de todos los tipos y agentes que se hayan sucedido entre las fechas especificadas

Todos los servicios suministran una URL de un archivo de ontología .rdf generado por la consulta para procesamiento semántico.
	


