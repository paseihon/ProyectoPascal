--Entrega Pascal Proyecto  (Query utilizada)

-----------Sprint 1---------

-- Crear el schema "publicidad"
CREATE SCHEMA IF NOT EXISTS publicidad;
DROP TABLE IF EXISTS publicidad.tv;
DROP TABLE IF EXISTS publicidad.radio;
DROP TABLE IF EXISTS publicidad.social;
DROP TABLE IF EXISTS publicidad.kpi;
DROP TABLE IF EXISTS publicidad.publicitaria;
DROP TABLE IF EXISTS publicidad.paid;


-- Crear  Tabla TV
CREATE TABLE publicidad.tv (
    fecha_hora_minuto TIMESTAMP,
    campanya TEXT,
    cadena TEXT,
    franja TEXT,
    creatividad TEXT,
    duracion INT,
    posicion TEXT,
    grps_ad16 NUMERIC,
    inversion NUMERIC
);


-- Crear  Tabla  Radio
CREATE TABLE publicidad.radio (
    fecha_hora_minuto TIMESTAMP,
    franja TEXT,
    emisora TEXT,
    campanya TEXT,
    formato TEXT,
    posicion TEXT,
    duracion INT,
    grps_ad16 NUMERIC,
    inversion NUMERIC
);


-- Crear Tabla  Social
CREATE TABLE publicidad.social (
    fecha DATE,
    hora INT,
    ad_name TEXT,
    ad_set_name TEXT,
    campanya TEXT,
    impresiones INT,
    clics INT,
    leads INT,
    inversion NUMERIC
);

--Creación de la tabla kpi
CREATE TABLE publicidad.kpi(
    id SERIAL PRIMARY KEY,
    fecha_hora_minuto TIMESTAMP NOT NULL,
    dispositivo VARCHAR(20) NOT NULL,
    visitas INT NOT NULL,
    unidades_vendidas INT NOT NULL,
    importe DECIMAL(10,2) NOT NULL
);

--Creación de la tabla Proyecto
CREATE TABLE publicidad.paid (
    cuenta TEXT,
    dia DATE,
    hora INTEGER,
    semana INTEGER,
    mes INTEGER,
    campanya TEXT,
    dispositivo TEXT,
    impresiones INTEGER,
    clics INTEGER,
    inversion DECIMAL(10,2),
    posicion_media DECIMAL(5,2)
);
Select * from publicidad.tv limit 10;
Select * from publicidad.radio limit 10;
Select * from publicidad.social limit 10;
Select * from publicidad.kpi limit 10;
Select * from publicidad.paid limit 10;
Select * from publicidad.publicitaria limit 10;

----------- Sprint 2

--Creación de la tabla publicitaria
DROP TABLE IF EXISTS publicidad.publicitaria;
CREATE TABLE publicidad.publicitaria (
    id_publicidad SERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    visitas_web INT,
    unidades_vendidas INT,
    importe DECIMAL(10,2),
    inversion_total DECIMAL(10,2),
    inversion_tv DECIMAL(10,2),
    inversion_radio DECIMAL(10,2),
    inversion_social DECIMAL(10,2)
);

-- Aprovisionemos la tabla publicitaria a partir de las tablas creadas en el sprint 1 (radio, social , tv y kpi)
INSERT INTO publicidad.publicitaria (fecha, visitas_web, unidades_vendidas, importe,inversion_total, inversion_tv,inversion_radio,inversion_social)
SELECT
	kpi.Fecha as Fecha, 
	COALESCE( kpi.Visitas::NUMERIC, 0) as Visitas_web,
	COALESCE(kpi.Unidades_vendidas::NUMERIC,0) as Unidades_vendidas,
	COALESCE(kpi.Importe::NUMERIC,0) as Importe,
	(COALESCE(tv.Inversion_tv::NUMERIC,0) + COALESCE(radio.Inversion_radio::NUMERIC,0) + COALESCE(social.Inversion_social::NUMERIC,0) ) as Interversion_total,
	COALESCE(tv.Inversion_tv::NUMERIC,0) as Inversion_TV,
	COALESCE(radio.Inversion_radio::NUMERIC,0) as Inversion_radio,
	COALESCE(social.Inversion_social::NUMERIC,0) as Inversion_social
FROM
	(SELECT DATE("kpi"."fecha_hora_minuto") as Fecha, SUM("kpi"."visitas") as Visitas, SUM("kpi"."unidades_vendidas") as Unidades_vendidas,SUM("kpi"."importe") as Importe
	FROM "publicidad"."kpi"
	GROUP BY Fecha
	) kpi
	LEFT JOIN (SELECT
	DATE("tv"."fecha_hora_minuto") as Fecha,SUM("tv"."inversion") as Inversion_tv FROM "publicidad"."tv"
	GROUP BY fecha) tv ON kpi.fecha = tv.fecha
	LEFT JOIN (SELECT DATE("radio"."fecha_hora_minuto") as Fecha, SUM("radio"."inversion") as Inversion_radio
	FROM "publicidad"."radio"
	GROUP BY fecha) radio ON kpi.fecha = radio.fecha
	LEFT JOIN (SELECT DATE("social"."fecha") as Fecha, SUM("social"."inversion") as Inversion_social
	FROM "publicidad"."social"
	GROUP BY fecha) social ON kpi.fecha = social.fecha
ORDER BY
	kpi.fecha;

--listar los 10 primeros registros de la tabla publicidad
Select * from publicidad.publicitaria limit 10;


-- Medio con más gasto publicitario
SELECT 'TV' AS medio, SUM(inversion_tv) AS total
FROM publicidad.publicitaria
UNION ALL
SELECT 'Radio'AS medio, SUM(inversion_radio)
FROM publicidad.publicitaria
UNION ALL
SELECT 'Social'AS medio, SUM(inversion_social)
FROM publicidad.publicitaria
ORDER BY total DESC
LIMIT 1;


-- Día con mayor gasto publicitario
SELECT fecha, inversion_total 
FROM publicidad.publicitaria
ORDER BY inversion_total DESC
LIMIT 1;

--Otra alternativa
select fecha, inversion_total from publicidad.publicitaria
where inversion_total = (select max(inversion_total) from publicidad.publicitaria);


-- Crear Vista
DROP view IF EXISTS publicidad.vista_kpi;
CREATE VIEW publicidad.vista_kpi AS
SELECT * FROM publicidad.publicitaria;

 

select count (*) as conteo_publicidad from publicidad.publicitaria
Union all
select count (*) as conteo_Vista_publicidad from publicidad.vista_kpi;

 

--¿Qué ventajas tiene una vista sobre una tabla?


/*Las vistas en SQL brindan numerosas ventajas en comparación con las tablas, 
ya que contribuyen a una mejor seguridad, simplicidad y eficiencia en la gestión de la información. Permiten controlar 
el acceso a datos sensibles, agilizar consultas complejas y promover la reutilización del código.

Asimismo, facilitan el mantenimiento del sistema, ya que los cambios en la base de datos pueden realizarse 
sin afectar directamente a los usuarios. Aunque las vistas no almacenan datos por sí mismas (excepto las vistas materializadas), 
su implementación adecuada puede mejorar el rendimiento y la organización de la información.

En definitiva, las vistas son una herramienta valiosa para optimizar la estructura, accesibilidad y 
protección de los datos dentro de una base de datos.*/










---SQL Proyecto



---¿Qué día se registró el máximo número de impresiones servidas? 

SELECT FECHA, SUM(IMPRESIONES)AS MAX_IMPRESION FROM PUBLICIDAD.SOCIAL
GROUP BY FECHA  ORDER BY MAX_IMPRESION DESC LIMIT 1;
--Solucion ==> El dia con con el máximo número de impresion versidos es el 19 de marzo de 2019 con 278269 impresiones.


---¿Cuál es el coste por GRP de cada una de las emisoras de radio en la que se emiten anuncios? Ordenar de mayor a menor. 
SELECT EMISORA, SUM(INVERSION) AS INVERSION_EMISORA
    FROM PUBLICIDAD.RADIO
    GROUP BY EMISORA
    ORDER BY INVERSION_EMISORA DESC;

--Solucion ==> --Cadena ser 	= 48689.41
			   --40 princiapes  = 14685.11
			   --Onda cero      = 14351.06
			   --Cope 			= 14342.08
			   --Fiesta radio 	=10983.34
			   --Rock fm 		= 3458.51


--¿Qué día se registró el máximo presupuesto gastado en publicidad?
SELECT
	kpi.Fecha as Fecha, 
	COALESCE( kpi.Visitas::NUMERIC, 0) as Visitas_web,
	COALESCE(kpi.Unidades_vendidas::NUMERIC,0) as Unidades_vendidas,
	COALESCE(kpi.Importe::NUMERIC,0) as Importe,
	(COALESCE(tv.Inversion_tv::NUMERIC,0) + COALESCE(radio.Inversion_radio::NUMERIC,0) + COALESCE(social.Inversion_social::NUMERIC,0) ) as Interversion_total,
	COALESCE(tv.Inversion_tv::NUMERIC,0) as Inversion_TV,
	COALESCE(radio.Inversion_radio::NUMERIC,0) as Inversion_radio,
	COALESCE(social.Inversion_social::NUMERIC,0) as Inversion_social
FROM ((SELECT DATE("kpi"."fecha_hora_minuto") as Fecha, SUM("kpi"."visitas") as Visitas, SUM("kpi"."unidades_vendidas") as Unidades_vendidas,SUM("kpi"."importe") as Importe
	FROM "publicidad"."kpi"
	GROUP BY Fecha
	) kpi 
	LEFT JOIN (SELECT DATE("tv"."fecha_hora_minuto") as Fecha,SUM("tv"."inversion") as Inversion_tv FROM "publicidad"."tv"
	GROUP BY fecha) tv ON kpi.fecha = tv.fecha
	LEFT JOIN (SELECT DATE("radio"."fecha_hora_minuto") as Fecha, SUM("radio"."inversion") as Inversion_radio
	FROM "publicidad"."radio"
	GROUP BY fecha) radio ON kpi.fecha = radio.fecha
	LEFT JOIN (SELECT DATE("social"."fecha") as Fecha, SUM("social"."inversion") as Inversion_social
	FROM "publicidad"."social"
	GROUP BY fecha) social ON kpi.fecha = social.fecha)
ORDER BY Interversion_total desc limit 1;


--Fase 3: Creación de una consulta que permita analizar el impacto de los anuncios de TV en los minutos posteriores a su emisión. 
 ------- el impacto de los ultimos 30 minutos
 SELECT  tv.fecha_hora_minuto AS fecha_emision, kpi.fecha_hora_minuto AS fecha_visita,
        tv.campanya, tv.creatividad,tv.cadena,tv.franja, tv.grps_ad16,sum(visitas) as total_visitas_posteriores
    FROM publicidad.tv tv
    JOIN publicidad.kpi kpi
        ON kpi.fecha_hora_minuto = tv.fecha_hora_minuto 
		where kpi.fecha_hora_minuto >= tv.fecha_hora_minuto AND kpi.fecha_hora_minuto <= tv.fecha_hora_minuto + INTERVAL '30 minutes'
GROUP BY fecha_emision,fecha_visita, campanya, creatividad, cadena, franja, grps_ad16
ORDER BY tv.fecha_hora_minuto asc

--Fase 4: Creación de una tabla con el resultado de la consulta y obtener un gráfico que permita visualizar la evolución de las visitas y los GRPs de los anuncios emitidos en TV. 
DROP TABLE IF EXISTS publicidad.impacto_tv_posterior_emision;
CREATE TABLE publicidad.impacto_tv_posterior_emision (
    id SERIAL PRIMARY KEY,
    fecha_emision TIMESTAMP,
	fecha_visita TIMESTAMP,
    campanya TEXT,
    creatividad TEXT,
    cadena TEXT,
    franja TEXT,
    grps_ad16 NUMERIC,
	total_visitas_posteriores INT
);

INSERT INTO publicidad.impacto_tv_posterior_emision (fecha_emision, fecha_visita, campanya, creatividad, cadena, franja, grps_ad16,total_visitas_posteriores) (
    SELECT  tv.fecha_hora_minuto AS fecha_emision, kpi.fecha_hora_minuto AS fecha_visita,
        tv.campanya, tv.creatividad,tv.cadena,tv.franja, tv.grps_ad16,sum(visitas) as total_visitas_posteriores
    FROM publicidad.tv tv
    JOIN publicidad.kpi kpi
        ON kpi.fecha_hora_minuto = tv.fecha_hora_minuto 
		where kpi.fecha_hora_minuto >= tv.fecha_hora_minuto AND kpi.fecha_hora_minuto <= tv.fecha_hora_minuto + INTERVAL '30 minutes'
GROUP BY fecha_emision,fecha_visita, campanya, creatividad, cadena, franja, grps_ad16
ORDER BY tv.fecha_hora_minuto asc
);

SELECT * from  publicidad.impacto_tv_posterior_emision
ORDER BY total_visitas_posteriores DESC;



--Fase adicional: Identificar qué anuncio de TV ha generado más visitas en la página web tras su emisión.
----------
SELECT  campanya,  creatividad,   cadena,   franja,    grps_ad16,    MAX(total_visitas_posteriores) AS max_visitas
FROM publicidad.impacto_tv_posterior_emision
GROUP BY campanya, creatividad, cadena, franja, grps_ad16
ORDER BY max_visitas DESC
LIMIT 1;
