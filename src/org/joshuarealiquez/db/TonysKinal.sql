/**
	Joshua Elí Isaac Realiquez Sosa
    2019342
    IN5AM
    Fecha de creación:
    28/03/2023
    Fecha de modificación:
    24/05/2023 - Se agregó una nueva consulta para reporteria.
*/

DROP DATABASE IF EXISTS DBTonysKinal2023;
CREATE DATABASE DBTonysKinal2023;
USE DBTonysKinal2023;

CREATE TABLE Empresas(
	codigoEmpresa INT NOT NULL AUTO_INCREMENT,
	nombreEmpresa VARCHAR(150) NOT NULL,
	direccion VARCHAR(150) NOT NULL,
	telefono VARCHAR(8) NOT NULL,
	PRIMARY KEY PK_codigoEmpresa (codigoEmpresa)
);

CREATE TABLE TipoEmpleado(
	codigoTipoEmpleado INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(50) NOT NULL,
    PRIMARY KEY PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

CREATE TABLE TipoPlato(
	codigoTipoPlato INT NOT NULL AUTO_INCREMENT,
    descripcionTipo VARCHAR(100) NOT NULL,
    PRIMARY KEY PK_codigoTipoPlato (codigoTipoPlato)
);

CREATE TABLE Productos(
	codigoProducto INT NOT NULL AUTO_INCREMENT,
    nombreProducto VARCHAR(150) NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY PK_codigoProducto (codigoProducto)
);

CREATE TABLE Empleados(
	codigoEmpleado INT NOT NULL AUTO_INCREMENT,
    numeroEmpleado INT NOT NULL,
    apellidosEmpleado VARCHAR(150) NOT NULL,
    nombresEmpleado VARCHAR(150) NOT NULL,
    direccionEmpleado VARCHAR(150) NOT NULL,
    telefonoContacto VARCHAR(8) NOT NULL,
    gradoCocinero VARCHAR(50),
    codigoTipoEmpleado INT NOT NULL,
    PRIMARY KEY PK_codigoEmpleado (codigoEmpleado),
    CONSTRAINT FK_Empleados_TipoEmpleado FOREIGN KEY (codigoTipoEmpleado)
		REFERENCES TipoEmpleado(codigoTipoEmpleado)
);

CREATE TABLE Servicios(
	codigoServicio INT NOT NULL AUTO_INCREMENT,
    fechaServicio DATE NOT NULL,
    tipoServicio VARCHAR(150) NOT NULL,
    horaServicio TIME NOT NULL,
    lugarServicio VARCHAR(150) NOT NULL,
    telefonoContacto VARCHAR(150) NOT NULL,
    codigoEmpresa INT NOT NULL,
    PRIMARY KEY PK_codigoServicio (codigoServicio),
    CONSTRAINT FK_Servicios_Empresas FOREIGN KEY (codigoEmpresa)
		REFERENCES Empresas(codigoEmpresa)
);

CREATE TABLE Presupuestos(
	codigoPresupuesto INT NOT NULL AUTO_INCREMENT,
    fechaSolicitud DATE NOT NULL,
    cantidadPresupuesto DECIMAL(10,2) NOT NULL,
    codigoEmpresa INT NOT NULL,
    PRIMARY KEY PK_codigopresupuesto (codigoPresupuesto),
    CONSTRAINT FK_Presupuestos_Empresas FOREIGN KEY (codigoEmpresa)
		REFERENCES Empresas(codigoEmpresa)
);

CREATE TABLE Platos(
	codigoPlato INT NOT NULL AUTO_INCREMENT,
    cantidad INT NOT NULL,
    nombrePlato VARCHAR(150) NOT NULL,
    descripcionPlato VARCHAR(150) NOT NULL,
    precioPlato decimal(10,2) NOT NULL,
    codigoTipoPlato INT NOT NULL,
    PRIMARY KEY PK_codigoPlatos (codigoPlato),
    CONSTRAINT FK_Platos_TipoPlato FOREIGN KEY (codigoTipoPlato)
		REFERENCES TipoPlato(codigoTipoPlato)
);

CREATE TABLE Productos_has_Platos(
	Productos_codigoProducto INT NOT NULL,
    codigoPlato INT NOT NULL,
    codigoProducto INT NOT NULL,
    PRIMARY KEY PK_Productos_codigoProducto(Productos_codigoProducto),
    CONSTRAINT FK_Productos_has_Platos_Productos FOREIGN KEY (codigoProducto)
		REFERENCES Productos(codigoProducto),
	CONSTRAINT FK_Productos_has_Platos_Platos FOREIGN KEY (codigoPlato)
		REFERENCES Platos(codigoPlato)
);

CREATE TABLE Servicios_has_Platos(
	Servicios_codigoServicio INT NOT NULL,
    codigoPlato INT NOT NULL,
    codigoServicio INT NOT NULL,
    PRIMARY KEY PK_Servicios_codigoServicio(Servicios_codigoServicio),
    CONSTRAINT FK_Servicios_has_Platos_Servicios FOREIGN KEY (codigoServicio)
		REFERENCES Servicios (codigoServicio),
	CONSTRAINT FK_Servicios_has_Platos_Platos FOREIGN KEY (codigoPlato)
		REFERENCES Platos (codigoPlato)
);

CREATE TABLE Servicios_has_Empleados(
	Servicios_codigoServicio INT NOT NULL,
    codigoServicio INT NOT NULL,
    codigoEmpleado INT NOT NULL,
    fechaEvento DATE NOT NULL,
    horaEvento TIME NOT NULL,
    lugarEvento VARCHAR(150) NOT NULL,
    PRIMARY KEY Pk_Servicios_codigoServicio(Servicios_codigoServicio),
	CONSTRAINT FK_Servicios_has_Empleados_Servicios FOREIGN KEY (codigoServicio)
		REFERENCES Servicios(codigoServicio),
	CONSTRAINT FK_Servicios_has_Empleados_Empleados FOREIGN KEY (codigoEmpleado)
		REFERENCES Empleados(codigoEmpleado)
);

CREATE TABLE Usuario(
	codigoUsuario INT NOT NULL AUTO_INCREMENT,
    nombreUsuario VARCHAR(100) NOT NULL,
    apellidoUsuario VARCHAR(100) NOT NULL,
    usuarioLogin VARCHAR(50) NOT NULL,
    contrasena VARCHAR(50) NOT NULL,
    PRIMARY KEY PK_codigoUsuario (codigoUsuario)
);

/*NO HACE FALTA HACER CRUD PARA EL LOGIN*/

CREATE TABLE Login(
	usuarioMaster VARCHAR(50) NOT NULL,
    passwordLogin VARCHAR(50) NOT NULL,
    PRIMARY KEY PK_usuarioMaster (usuarioMaster)
);

-- ---------------------------------- EMPRESAS ------------------------------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarEmpresa (IN nombreEmpresa VARCHAR(150), IN direccion VARCHAR(150), IN telefono VARCHAR(8))
    BEGIN
		INSERT INTO Empresas(nombreEmpresa, direccion, telefono)
        VALUES(nombreEmpresa, direccion, telefono)
        ;
    END $$
DELIMITER ;

CALL sp_AgregarEmpresa('Cinépolis Naranjo Mall','23 Calle 10-00 Condado Naranjo Zona 4 de Mixco, C.C. Naranjo Mall.','23782300');
CALL sp_AgregarEmpresa('Taco Bell 59','Diagonal 3, Calzada Atanasio Tzul, 16-67 Zona 12, C.C. Madero Express','22020059');
CALL sp_AgregarEmpresa('La Monumental','21 Avenida, 4-32, Zona 11, Miraflores, Tercer Nivel, Local RT-301','23365587');
CALL sp_AgregarEmpresa('Palletamericana Oakland','Diagonal 6 13-01 Zona 10, Oakland Mall, Kiosko K-59','57618297');
CALL sp_AgregarEmpresa('Pasty','Diagonal 3, Calz. Atanasio Tzul, 17-13 Zona 12, C.C. Plaza Madero Local 49','12345678');

CALL sp_AgregarEmpresa('Farmacias Galeno','11 Av. 22-68 Zona 12, La Reformita','1702');
CALL sp_AgregarEmpresa('Servi-Inox','Zona 12, Colonia Santa Elisa','22925534');
CALL sp_AgregarEmpresa('Suzuki','Av. La Castellana 39-48, z9','24202100');
CALL sp_AgregarEmpresa('Pollo Campero','Avenida Petapa 31-01, zona 12','23741777');
CALL sp_AgregarEmpresa('Olibodegas de Guatemala','18 calle 18-70 zona 12','24946400');

DELIMITER $$
	CREATE PROCEDURE sp_ReporteEmpresas()
    BEGIN
		SELECT
        E.codigoEmpresa,
        E.nombreEmpresa,
        E.direccion,
        E.telefono
        FROM Empresas E
        ;
    END $$
DELIMITER ;


CALL sp_ReporteEmpresas();


DELIMITER $$
	CREATE PROCEDURE sp_EditarEmpresa (IN codEmpresa INT, IN nomEmpresa VARCHAR(150), IN direcc VARCHAR(150), IN tele VARCHAR(8))
    BEGIN
		UPDATE Empresas E
		SET 
			E.nombreEmpresa = nomEmpresa,
            E.direccion = direcc,
            E.telefono = tele
        WHERE E.codigoEmpresa = codEmpresa
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ELiminarEmpresa (IN codEmpresa INT)
    BEGIN
		DELETE FROM Empresas
        WHERE codigoEmpresa = codEmpresa
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarEmpresas ()
    BEGIN
		SELECT
			E.codigoEmpresa,
            E.nombreEmpresa,
            E.direccion,
            E.telefono
            FROM Empresas E
		;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_BuscarEmpresa (IN idEmpresa INT)
    BEGIN
		SELECT
			E.codigoEmpresa,
            E.nombreEmpresa,
            E.direccion,
            E.telefono
		FROM Empresas E
        WHERE E.codigoEmpresa = idEmpresa;
    END $$
DELIMITER ;

-- ---------------------------- PRESUPUESTOS -----------------------------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarPresupuesto (IN fechaSolicitud DATE, IN cantidadPresupuesto DECIMAL(10,2), IN codigoEmpresa INT)
    BEGIN
		INSERT INTO Presupuestos(fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
        VALUES(fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
        ;
    END $$
DELIMITER ;

CALL sp_AgregarPresupuesto('2023-01-03', 60000.00,10);
CALL sp_AgregarPresupuesto('2023-01-04', 10000.00,9);
CALL sp_AgregarPresupuesto('2023-01-06', 5000.00,8);
CALL sp_AgregarPresupuesto('2023-01-08', 2300.00,7);
CALL sp_AgregarPresupuesto('2023-01-10', 500.00,6);

CALL sp_AgregarPresupuesto('2023-01-12', 20000.00,5);
CALL sp_AgregarPresupuesto('2023-01-15', 15000.00,4);
CALL sp_AgregarPresupuesto('2023-01-19', 10000.00,3);
CALL sp_AgregarPresupuesto('2023-01-20', 9000.00,2);
CALL sp_AgregarPresupuesto('2023-02-03', 7250.00,1);



DELIMITER $$
	CREATE PROCEDURE sp_ReportePresupuestos()
    BEGIN
		SELECT
		P.codigoPresupuesto,
        P.fechaSolicitud,
        P.cantidadPresupuesto,
        E.codigoEmpresa,
        E.nombreEmpresa,
        E.direccion,
        E.telefono
        FROM Presupuestos P
        INNER JOIN Empresas E
        ON E.codigoEmpresa = P.codigoEmpresa;
    END $$
DELIMITER ;

CALL sp_ReportePresupuestos();

DELIMITER $$
	CREATE PROCEDURE sp_EditarPresupuesto (IN codPresupuesto INT, IN fechaSoli DATE, IN cantidadPresu DECIMAL(10,2))
    BEGIN
		UPDATE Presupuestos P
        SET
            P.fechaSolicitud = fechaSoli,
            P.cantidadPresupuesto = cantidadPresu
		WHERE P.codigoPresupuesto = codPresupuesto
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_EliminarPresupuesto (IN codPresupuesto INT)
    BEGIN
		DELETE FROM Presupuestos P
        WHERE P.codigoPresupuesto = codPresupuesto
	;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarPresupuestos()
    BEGIN
		SELECT
			P.codigoPresupuesto,
            P.fechaSolicitud,
            P.cantidadPresupuesto,
            P.codigoEmpresa
		FROM Presupuestos P
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_BuscarPresupuesto(IN codPresupuesto INT)
    BEGIN
		SELECT
			P.codigoPresupuesto,
            P.fechaSolicitud,
            P.cantidadPresupuesto,
            P.codigoEmpresa
		FROM Presupuestos P
        WHERE P.codigoPresupuesto = codPresupuesto
        ;
    END $$
DELIMITER ;

-- ----------------------------------- TIPO EMPLEADO ---------------------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarTipoEmpleado(IN descripcion VARCHAR(50))
    BEGIN
		INSERT INTO TipoEmpleado(descripcion)
        VALUES(descripcion)
        ;
    END $$
DELIMITER ;

CALL sp_AgregarTipoEmpleado('Mesero');
CALL sp_AgregarTipoEmpleado('Chef Ejecutivo');
CALL sp_AgregarTipoEmpleado('Chef Segundo');
CALL sp_AgregarTipoEmpleado('Anfitrión');
CALL sp_AgregarTipoEmpleado('Gerente General');
CALL sp_AgregarTipoEmpleado('Administrador de Base de Datos');
CALL sp_AgregarTipoEmpleado('Bartender');
CALL sp_AgregarTipoEmpleado('Barback');
CALL sp_AgregarTipoEmpleado('Gerente de Alimentos y Bebidas');
CALL sp_AgregarTipoEmpleado('Cajero');
CALL sp_AgregarTipoEmpleado('Lavavajillas');


DELIMITER $$
	CREATE PROCEDURE sp_EditarTipoEmpleado (IN codTipoEmpleado INT, IN descri VARCHAR(50))
    BEGIN
		UPDATE TipoEmpleado T
        SET
			T.codigoTipoEmpleado = codTipoEmpleado,
            T.descripcion = descri
			;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_EliminarTipoEmpleado (IN codTipoEmpleado INT)
    BEGIN
		DELETE FROM TipoEmpleado T
        WHERE T.codigoTipoEmpleado = codTipoEmpleado
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarTiposEmpleados()
    BEGIN
		SELECT
			T.codigoTipoEmpleado,
            T.descripcion
		FROM TipoEmpleado T
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_BuscarTipoEmpleado(IN codTipoEmpleado INT)
    BEGIN
		SELECT
			T.codigoTipoEmpleado,
            T.descripcion
		FROM TipoEmpleado T
        WHERE T.codigoTipoEmpleado = codTipoEmpleado
        ;
    END $$
DELIMITER ;
	
-- -------------------------------------- TIPO PLATO ------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarTipoPlato(IN descripcionTipo VARCHAR(100))
    BEGIN
		INSERT INTO TipoPlato (descripcionTipo)
        VALUES(descripcionTipo)
        ;
    END $$
DELIMITER ;

CALL sp_AgregarTipoPlato('Americano');
CALL sp_AgregarTipoPlato('Guatemalteco');
CALL sp_AgregarTipoPlato('Argentino');
CALL sp_AgregarTipoPlato('Italiano');
CALL sp_AgregarTipoPlato('Peruano');
CALL sp_AgregarTipoPlato('Frances');
CALL sp_AgregarTipoPlato('Español');
CALL sp_AgregarTipoPlato('Brasileño');
CALL sp_AgregarTipoPlato('Escocesa');



DELIMITER $$
	CREATE PROCEDURE sp_EditarTipoPlato (IN codTipoPlato INT, IN descriTipo VARCHAR(100))
    BEGIN
		UPDATE TipoPlato T
		SET
			T.descripcionTipo = descriTipo
		WHERE T.codigoTipoPlato = codTipoPlato
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_EliminarTipoPlato (IN codTipoPlato INT)
    BEGIN
		DELETE FROM TipoPlato T
		WHERE T.codigoTipoPlato = codTipoPlato
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarTiposPlatos()
    BEGIN
		SELECT
			T.codigoTipoPlato,
            T.descripcionTipo
		FROM TipoPlato T
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_BuscarTipoPlato(IN codTipoPlato INT)
	BEGIN
		SELECT
			T.codigoTipoPlato,
            T.descripcionTipo
		FROM TipoPlato T
        WHERE T.codigoTipoPlato = codTipoPlato
        ;
    END $$
DELIMITER ;

-- --------------------------------------- PRODUCTOS ------------------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarProducto(IN nombreProducto VARCHAR(150), IN cantidad INT)
    BEGIN
		INSERT INTO Productos(nombreProducto, cantidad)
        VALUES(nombreProducto, cantidad)
        ;
    END $$
DELIMITER ;

-- para desayuno Desayuno Tipico ---------
CALL sp_AgregarProducto('Margarina Cremy Light Barra',50);
CALL sp_AgregarProducto('Huevos',500);
CALL sp_AgregarProducto('Salsa Naturas de Tomate Ranchera',100);
CALL sp_AgregarProducto('Frijoles La Chula Negros Volteados',100);
CALL sp_AgregarProducto('Crema Foremost Natural Bolsa',50);
CALL sp_AgregarProducto('Platano Importado', 50);
CALL sp_AgregarProducto('Media fila de Pan', 25);
CALL sp_AgregarProducto('Cebolla Blanca',100);

-- para el almuerzo Puyaso Importado -----
-- CALL sp_AgregarProducto('Cebolla Blanca',100);
CALL sp_AgregarProducto('Papa Super Granel',50);
-- CALL sp_AgregarProducto('Margarina Cremy Light Barra',50);
CALL sp_AgregarProducto('Puyazo Coulotte 2Lb',10);


-- para el almuerzo Tonys Burguer ----
-- CALL sp_AgregarProducto('Margarina Cremy Light Barra',50);
CALL sp_AgregarProducto('Carne Molida 1Lb',20);
CALL sp_AgregarProducto('Lechuga 1Lb',10);
CALL sp_AgregarProducto('Tomate Manzano Jumbo',50);
-- CALL sp_AgregarProducto('Cebolla Blanca',100);
CALL sp_AgregarProducto('Queso Mozzarella Rallado Great Value',20);
CALL sp_AgregarProducto('Tocino Ahumado De Cerdo Chimex',20);
CALL sp_AgregarProducto('Mayonesa McCormick Doypack -1000gr',10);
CALL sp_AgregarProducto('Salsa Heinz de Tomate Ketchup - 567gr',20);
-- CALL sp_AgregarProducto('Papa Super Granel',50);

-- para boda Burguer Kids ----
-- CALL sp_AgregarProducto('Carne Molida 1Lb',20);
-- CALL sp_AgregarProducto('Lechuga 1Lb',10);
-- CALL sp_AgregarProducto('Salsa Heinz de Tomate Ketchup - 567gr',20);
-- CALL sp_AgregarProducto('Mayonesa McCormick Doypack -1000gr',10);
-- CALL sp_AgregarProducto('Papa Super Granel',50);
/* incluye naranjada con soda el menu */

-- Naranjada con Soda -------
-- CALL sp_AgregarProducto('Naranja Importada',100);
CALL sp_AgregarProducto('Limon Persa',50);
CALL sp_AgregarProducto('La Madrileña Jarabe Granadina',4);
CALL sp_AgregarProducto('Bebida Gaseosa Shangrila 2L',20);

-- Para boda Chicken-Breast -------
-- CALL sp_AgregarProducto('Margarina Cremy Light Barra',50);
CALL sp_AgregarProducto('Filete Pechuga Pollo Rey 1.25Lb',20);
CALL sp_AgregarProducto('Pasta Larga Roma Lengua - 500gr',10);
/*salsa de champiñones*/
-- CALL sp_AgregarProducto('Margarina Cremy Light Barra',50);
CALL sp_AgregarProducto('Champiñones Killios Rebanados - 184gr',20);
CALL sp_AgregarProducto('Leche Australian Evaporada - 410gr',20);
CALL sp_AgregarProducto('Consome Sasson De Pollo 0% Grasas - 454gr',10);


-- Para postres Gelatina Kid ------------
CALL sp_AgregarProducto('Gelatina Castilla Sabor Fresa',20);
CALL sp_AgregarProducto('Gelatina Castilla Sabor Uva',10);
CALL sp_AgregarProducto('Gelatina Castilla Sabor Chicle',15);


-- Para hacer la Gelatina Supreme -----
-- CALL sp_AgregarProducto('Gelatina Castilla Sabor Fresa',20);
-- CALL sp_AgregarProducto('Gelatina Castilla Sabor Uva',10);
-- CALL sp_AgregarProducto('Gelatina Castilla Sabor Chicle',15);
-- CALL sp_AgregarProducto('Gelatina Castilla Sin Sabor Cajita',15);
CALL sp_AgregarProducto('Gelatina Castilla Sin Sabor Cajita',15);
CALL sp_AgregarProducto('Media Crema Lala Uht',15);

-- Para hacer Tartaleta Tonys -------
-- CALL sp_AgregarProducto('Margarina Cremy Light Barra',50);
CALL sp_AgregarProducto('Leche Condesada La Lechera -375gr',15);
-- CALL sp_AgregarProducto('Huevos',500);
CALL sp_AgregarProducto('Harina de Trigo Gold Medal - 2lb',10);
CALL sp_AgregarProducto('Polvo Para Hornear Sasson - 100gr',5);
CALL sp_AgregarProducto('F54 CHOCOLATE AMARGO (70% CACAO)',10);
CALL sp_AgregarProducto('Fecula Maizena De Maiz Original - 190gr',5);
-- CALL sp_AgregarProducto('Leche Australian Evaporada - 410gr',20);
CALL sp_AgregarProducto('Esencia Mansilla Vainilla Clara - 218ml',5);
CALL sp_AgregarProducto('Kiwi Importado - 1lb',6);
CALL sp_AgregarProducto('Frambuesa Clamshell - 170gr',5);
CALL sp_AgregarProducto('Mora Clamshell - 1Lb',5);
CALL sp_AgregarProducto('Durazno',50);
CALL sp_AgregarProducto('Fresa Clamshell - 1lb',10);

-- Bebidas Extras -------
CALL sp_AgregarProducto('Naranja Importada',100);
CALL sp_AgregarProducto('Whisky Johnnie Walker Black',50);
CALL sp_AgregarProducto('Cafe Leon 100% Puro Tostado Y Molido Rojo 350 Gr',20);



DELIMITER $$
	CREATE PROCEDURE sp_EditarProducto(IN codProducto INT, IN nomProducto VARCHAR(150), IN cant INT)
    BEGIN
		UPDATE Productos P
        SET
			P.nombreProducto = nomProducto,
            P.cantidad = cant
		WHERE P.codigoProducto = codProducto;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_EliminarProducto(IN codProducto INT)
    BEGIN
		DELETE FROM Productos P
        WHERE P.codigoProducto = codProducto
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarProductos()
    BEGIN
		SELECT
			P.codigoProducto,
            P.nombreProducto,
            P.cantidad
		FROM Productos P
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_BuscarProducto(IN codProducto INT)
    BEGIN
		SELECT
			P.codigoProducto,
            P.nombreProducto,
            P.cantidad
		FROM Productos P
        WHERE P.codigoProducto = codProducto
        ;
    END $$
DELIMITER ;

-- --------------------------------- EMPLEADOS -------------------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarEmpleado(IN numeroEmpleado INT, IN apellidosEmpleado VARCHAR(150), 
		IN nombresEmpleado VARCHAR(150), IN direccionEmpleado VARCHAR(150), IN telefonoContacto VARCHAR(8), 
        IN gradoCocinero VARCHAR(50), IN codigoTipoEmpleado INT)
    BEGIN
		INSERT INTO Empleados(numeroEmpleado, apellidosEmpleado, nombresEmpleado, 
			direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado)
        VALUES(numeroEmpleado, apellidosEmpleado, nombresEmpleado, 
			direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado)
        ;
    END $$
DELIMITER ;

CALL sp_Agregarempleado(2023001, 'Realiquez Sosa', 'Joshua Elí Isaac', '5ta Calle 5-41 Z13 Pamplona', '50524961', 'No aplica', 6);
CALL sp_Agregarempleado(2023002, 'De la Cruz Chu', 'Juan Arnoldo', '14 Calle 6-43 Z3 Barrio el Gallito', '12345678', 'Saucier', 3);
CALL sp_Agregarempleado(2023003, 'Mejicanos Roldán', 'Anthony Estuardo', '24 Calle A 9-02 Z5', '87654321', 'Ayudante', 1);
CALL sp_Agregarempleado(2023004, 'Cruz Fuentes', 'Ana Brenda', '12 Av 14-06 z7 de Mixco', '65432187', 'Rotisseur', 2);
CALL sp_Agregarempleado(2023005, 'Cruz Cruz', 'María de los Ángeles', '22 Av 12-69 Z12 Reformita', '18273645', 'Ayudante', 4);
CALL sp_Agregarempleado(2023006, 'Cardona López', 'Ana Belen', '14 calle 7-21 z1 Huehuetenango', '78456123', 'Ayudante', 5);
CALL sp_Agregarempleado(2023007, 'López Polanco', 'Daniela Dianne', '8va calle 6-21 z12 Reformita', '44167899', 'Ayudante', 8);
CALL sp_Agregarempleado(2023008, 'Martínez Duran', 'José Carlos', '8va calle 6-21 z12 Reformita', '44167899', 'Encargado', 7);
CALL sp_Agregarempleado(2023009, 'Saenz Letran', 'Fátima Paola', '1ra calle 5-02 z7 San Miguel Petapa', '12456378', 'Encargado', 9);
CALL sp_Agregarempleado(2023010, 'Kogler Sucht', 'Daniel Javier', '6ta calle 19-08 z2', '24156378', 'Ayudante', 10);
CALL sp_Agregarempleado(2023011, 'España Pineda', 'Marcos Daniel', '9na Av 1-06 z4', '95126348', 'Ayudante', 11);


DELIMITER $$
	CREATE PROCEDURE sp_EditarEmpleado(IN codEmpleado INT, IN numEmpleado INT, 
		IN apellidoEmpleado VARCHAR(150), IN nombreEmpleado VARCHAR(150), 
		IN direccEmpleado VARCHAR(150), IN telContacto VARCHAR(8), IN gradCocinero VARCHAR(50))
    BEGIN
		UPDATE Empleados E
        SET
			E.numeroEmpleado = numEmpleado, 
            E.apellidosEmpleado = apellidoEmpleado, 
            E.nombresEmpleado = nombreEmpleado, 
            E.direccionEmpleado = direccEmpleado, 
            E.telefonoContacto = telContacto, 
            E.gradoCocinero = gradCocinero
		WHERE E.codigoEmpleado = codEmpleado
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_EliminarEmpleado(IN codEmpleado INT)
    BEGIN
		DELETE FROM Empleados E
        WHERE E.codigoEmpleado = codEmpleado
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarEmpleados()
    BEGIN
		SELECT
			E.codigoEmpleado,
            E.numeroEmpleado,
            E.apellidosEmpleado,
            E.nombresEmpleado,
            E.direccionEmpleado,
            E.telefonoContacto,
            E.gradoCocinero,
            E.codigoTipoEmpleado
		FROM Empleados E
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_BuscarEmpleado(IN codEmpleado INT)
    BEGIN
		SELECT
			E.codigoEmpleado,
            E.numeroEmpleado,
            E.apellidosEmpleado,
            E.nombresEmpleado,
            E.direccionEmpleado,
            E.telefonoContacto,
            E.gradoCocinero,
            E.codigoTipoEmpleado
		FROM Empleados E
        WHERE E.codigoEmpleado = codEmpleado
        ;
    END $$
DELIMITER ;

-- -------------------------------------- SERVICIOS -------------------------------------------------------------------------
DELIMITER $$
	CREATE PROCEDURE sp_AgregarServicio(IN fechaServicio DATE, IN tipoServicio VARCHAR(150), IN horaServicio TIME, 
		IN lugarServicio VARCHAR(150), IN telefonoContacto VARCHAR(10), IN codigoEmpresa INT)
    BEGIN
		INSERT INTO Servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
        VALUES(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
        ;
    END $$
DELIMITER ;

call sp_AgregarServicio('2023-05-10', 'Servicio de almuerzos', '12:30:00', 'Casa Antigua', '23241297', 3);
call sp_AgregarServicio('2023-05-11', 'Servicio de postres', '15:15:00', 'Casa Antigua', '23241297', 2);
call sp_AgregarServicio('2023-04-08', 'Servicio de boda bronze', '19:30:00', 'Casa Antigua', '23241297', 9);
call sp_AgregarServicio('2023-04-22', 'Servicio de boda silver', '20:00:00', 'Casa Antigua', '23241297', 5);
call sp_AgregarServicio('2023-05-06', 'Servicio de boda gold', '20:00:00', 'Casa Antigua', '23241297', 10);
call sp_AgregarServicio('2023-05-08', 'Servicio de degustación', '13:30:00', 'Casa Antigua', '23241297', 8);
call sp_AgregarServicio('2023-05-07', 'Servicio de cenas', '20:30:00', 'Casa Antigua', '23241297', 4);
call sp_AgregarServicio('2023-05-09', 'Servicio de desayunos', '07:30:00', 'Casa Antigua', '23241297', 6);
call sp_AgregarServicio('2023-05-10', 'Servicio de almuerzos', '13:30:00', 'Casa Antigua', '23241297', 7);
call sp_AgregarServicio('2023-05-11', 'Servicio de almuerzos', '13:00:00', 'Casa Antigua', '23241297', 1);

DELIMITER $$
	CREATE PROCEDURE sp_ReporteServicios()
    BEGIN
		SELECT 
			S.codigoServicio,
            S.fechaServicio,
            S.horaServicio,
            S.tipoServicio,
            S.lugarServicio,
            S.telefonoContacto,
            E.codigoEmpresa,
            E.nombreEmpresa,
            E.direccion,
            E.telefono
		FROM Servicios S
        INNER JOIN Empresas E
        ON E.codigoEmpresa = S.codigoEmpresa
        ;
    END $$
DELIMITER ;

CALL sp_ReporteServicios();


DELIMITER $$
	CREATE PROCEDURE sp_EditarServicio(IN codServicio INT, IN fechaServ DATE, IN tipoServ VARCHAR(150), 
		IN horaServ TIME, IN lugarServ VARCHAR(150), IN telContacto VARCHAR(10))
    BEGIN
		UPDATE Servicios S
        SET
			S.fechaServicio = fechaServ, 
            S.tipoServicio = tipoServ,
            S.horaServicio = horaServ,
            S.lugarServicio = lugarServ,
            S.telefonocontacto = telContacto
		WHERE S.codigoServicio = codServicio
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_EliminarServicio(IN codServicio INT)
    BEGIN
		DELETE FROM Servicios S
        WHERE S.codigoServicio = codServicio
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarServicios()
    BEGIN
		SELECT
			S.codigoServicio,
            S.fechaServicio,
            S.tipoServicio,
            S.horaServicio,
            S.lugarServicio,
            S.telefonocontacto,
            S.codigoEmpresa
		FROM Servicios S
        ;
    END $$
DELIMITER ;

CALL sp_ListarServicios();

DELIMITER $$
	CREATE PROCEDURE sp_BuscarServicio(IN codServicio INT)
    BEGIN
		SELECT
			S.codigoServicio,
            S.fechaServicio,
            S.tipoServicio,
            S.horaServicio,
            S.lugarServicio,
            S.telefonoContacto,
            S.codigoEmpresa
		FROM Servicios S
        WHERE S.codigoServicio = codServicio
        ;
    END $$
DELIMITER ;

-- ----------------------------------- PLATOS ---------------------------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarPlato(IN cantidad INT, IN nombrePlato VARCHAR(150), 
		IN descripcionPlato VARCHAR(150), IN precioPlato DECIMAL(10,2), IN codigoTipoPlato INT)
    BEGIN
		INSERT INTO Platos(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
        VALUES(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
        ;
    END $$
DELIMITER ;

/*La cantidad es para saber cuantos platos se van a servir/preparar */
-- Desayunos
CALL sp_AgregarPlato(50, 'Desayuno Típico', 'huevos estrellados/revueltos, frijoles volteados, crema, platanos, pan, café/fresco.', 25.00, 2);
-- Almuerzos
CALL sp_AgregarPlato(50, 'Puyaso Importado', 'Puyaso (termino medio-3/4-bien cocido) acompañado con cebolla asada, papa con margarina.', 68.50, 7);
CALL sp_AgregarPlato(50, 'Tonys Burguer', 'Carne de res, lechuga, tomate, cebolla asada, queso mozzarella, tocino y mayonesa. Acompañado de papas fritas.', 55.50, 1);
-- Bodas
CALL sp_AgregarPlato(50, 'Burguer Kids', 'Una torta de carne, lechuga, salsa, mayonesa, papas fritas y jugo(naranjada con soda).', 65.50, 1);
CALL sp_AgregarPlato(50, 'Chicken-breast', 'Pechugas de pollo con crema de champiñones y fideos.', 85.50, 2);

-- Postres
CALL sp_AgregarPlato(50, 'Gelatina Kid', 'Vaso de gelatina con diversos sabores: fresa, chicle, uva.', 25.00, 2);
CALL sp_AgregarPlato(50, 'Rellenitos Tradicionales', 'Frijoles envueltos en una masa de platanos con azucar y crema.', 20.00, 2);
CALL sp_AgregarPlato(50, 'Gelatina Supreme', 'Vaso de gelatina con diversos sabores: fresa, chicle, uva + crema batida.', 35.00, 2);
CALL sp_AgregarPlato(50, 'Tartaleta Tonys', 'Tartaleta cubierta de frutas frescas.', 20.00, 2);

-- Bebidas
CALL sp_AgregarPlato(50, 'Naranjada con Soda', '20 onzas', 20.00, 2);
CALL sp_AgregarPlato(50, 'Whisky Shot', 'Black Label (shot)', 50.00, 9);
CALL sp_AgregarPlato(50, 'Whisky Botella', 'Black Label (Botella)', 750.00, 9);


DELIMITER $$
	CREATE PROCEDURE sp_ReportePlatos()
    BEGIN
		SELECT
			P.codigoPlato,
            P.cantidad,
            P.nombrePlato,
            P.descripcionPlato,
            P.precioPlato,
            T.codigoTipoPlato,
            T.descripcionTipo
		FROM Platos P
        INNER JOIN TipoPlato T
        ON T.codigoTipoPlato = P.codigoTipoPlato
        ;
    END $$
DELIMITER ;

CALL sp_ReportePlatos();

DELIMITER $$
	CREATE PROCEDURE sp_EditarPlato(IN codPlato INT, IN cant INT, IN nomPlato VARCHAR(150), 
		IN descPlato VARCHAR(150), IN prePlato DECIMAL(10,2))
    BEGIN
		UPDATE Platos P
        SET
			P.cantidad = cant, 
            P.nombrePlato = nomPlato,
            P.descripcionPlato = descPlato,
            P.precioPlato = prePlato
		WHERE P.codigoPlato = codPlato
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_EliminarPlato(IN codPlato INT)
    BEGIN
		DELETE FROM Platos P
        WHERE P.codigoPlato = codPlato
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarPlatos()
    BEGIN
		SELECT
			P.codigoPlato,
            P.cantidad,
            P.nombrePlato,
            P.descripcionPlato,
            P.precioPlato,
            P.codigoTipoPlato
		FROM Platos P
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_BuscarPlato(IN codPlato INT)
    BEGIN
		SELECT
			P.codigoPlato,
            P.cantidad,
            P.nombrePlato,
            P.descripcionPlato,
            P.precioPlato,
            P.codigoTipoPlato
		FROM Platos P
        WHERE P.codigoPlato = codPlato
        ;
    END $$
DELIMITER ;

-- -------------------------------- PRODUCTOS HAS PLATOS ------------------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarProductoHasPlato(IN Productos_codigoProducto INT,IN codigoPlato INT, IN codigoProducto INT)
    BEGIN
		INSERT INTO Productos_has_Platos(Productos_codigoProducto ,codigoPlato, codigoProducto)
        VALUES(Productos_codigoProducto, codigoPlato, codigoProducto)
        ;
    END $$
DELIMITER ;

CALL sp_AgregarProductoHasPlato(1,1,1);
CALL sp_AgregarProductoHasPlato(2,1,2);
CALL sp_AgregarProductoHasPlato(3,1,4);
CALL sp_AgregarProductoHasPlato(4,1,5);
CALL sp_AgregarProductoHasPlato(5,1,6);
CALL sp_AgregarProductoHasPlato(6,1,7);
CALL sp_AgregarProductoHasPlato(55,1,44);
CALL sp_AgregarProductoHasPlato(7,1,8);
CALL sp_AgregarProductoHasPlato(8,1,3);

CALL sp_AgregarProductoHasPlato(9,2,10);
CALL sp_AgregarProductoHasPlato(10,2,8);
CALL sp_AgregarProductoHasPlato(11,2,9);

CALL sp_AgregarProductoHasPlato(12,3,11);
CALL sp_AgregarProductoHasPlato(13,3,12);
CALL sp_AgregarProductoHasPlato(14,3,13);
CALL sp_AgregarProductoHasPlato(15,3,8);
CALL sp_AgregarProductoHasPlato(16,3,14);
CALL sp_AgregarProductoHasPlato(17,3,15);
CALL sp_AgregarProductoHasPlato(18,3,16);
CALL sp_AgregarProductoHasPlato(19,3,9);

CALL sp_AgregarProductoHasPlato(20,4,11);
CALL sp_AgregarProductoHasPlato(21,4,12);
CALL sp_AgregarProductoHasPlato(22,4,17);
CALL sp_AgregarProductoHasPlato(23,4,16);
CALL sp_AgregarProductoHasPlato(24,4,9);

CALL sp_AgregarProductoHasPlato(25,5,21);
CALL sp_AgregarProductoHasPlato(26,5,22);
CALL sp_AgregarProductoHasPlato(27,5,23);
CALL sp_AgregarProductoHasPlato(28,5,24);
CALL sp_AgregarProductoHasPlato(29,5,25);

CALL sp_AgregarProductoHasPlato(30,6,26);
CALL sp_AgregarProductoHasPlato(31,6,27);
CALL sp_AgregarProductoHasPlato(32,6,28);


CALL sp_AgregarProductoHasPlato(33,7,4);
CALL sp_AgregarProductoHasPlato(34,7,6);

CALL sp_AgregarProductoHasPlato(35,8,26);
CALL sp_AgregarProductoHasPlato(36,8,27);
CALL sp_AgregarProductoHasPlato(37,8,28);
CALL sp_AgregarProductoHasPlato(38,8,29);
CALL sp_AgregarProductoHasPlato(39,8,30);
CALL sp_AgregarProductoHasPlato(40,8,31);

CALL sp_AgregarProductoHasPlato(41,9,32);
CALL sp_AgregarProductoHasPlato(42,9,33);
CALL sp_AgregarProductoHasPlato(43,9,34);
CALL sp_AgregarProductoHasPlato(44,9,35);
CALL sp_AgregarProductoHasPlato(45,9,36);
CALL sp_AgregarProductoHasPlato(46,9,37);
CALL sp_AgregarProductoHasPlato(47,9,38);
CALL sp_AgregarProductoHasPlato(48,9,39);
CALL sp_AgregarProductoHasPlato(49,9,40);
CALL sp_AgregarProductoHasPlato(50,9,41);

CALL sp_AgregarProductoHasPlato(51,10,42);
CALL sp_AgregarProductoHasPlato(52,10,18);
CALL sp_AgregarProductoHasPlato(53,10,19);
CALL sp_AgregarProductoHasPlato(54,10,20);


DELIMITER $$
	CREATE PROCEDURE sp_ReporteProductoHasPlato()
    BEGIN
		SELECT
			Php.Productos_codigoProducto,
            Pl.codigoPlato,
            Pl.cantidad,
            Pl.nombrePlato,
            Pl.descripcionPlato,
            Pl.precioPlato,
            Tp.codigoTipoPlato,
            Tp.descripcionTipo,
            Pr.codigoProducto,
            Pr.nombreProducto,
            Pr.cantidad
		FROM Productos_has_Platos Php
        INNER JOIN Platos Pl
        ON Pl.codigoPlato = Php.codigoPlato
        INNER JOIN TipoPlato Tp
        ON Tp.codigoTipoPlato = Pl.codigoPlato
        INNER JOIN Productos pr
		ON pr.codigoProducto = Php.codigoProducto
        ;
    END $$
DELIMITER ;

CALL sp_ReporteProductoHasPlato();

/* No se puede modificar las llaves foraneas ni primarias.*/

DELIMITER $$
	CREATE PROCEDURE sp_EliminarProductoHasPlato(IN codProducto INT)
    BEGIN
		DELETE FROM Productos_has_Platos Php
        WHERE Php.Productos_codigoProducto = codProducto
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarProductosHasPlatos()
    BEGIN
		SELECT
			Php.Productos_codigoProducto,
            Php.codigoPlato,
            Php.codigoProducto
		FROM Productos_has_Platos Php
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_BuscarProductosHasPlatos(IN codProducto INT)
    BEGIN
		SELECT
			Php.Productos_codigoProducto,
            Php.codigoPlato,
            Php.codigoProducto
		FROM Productos_has_Platos Php
        WHERE Php.Productos_codigoProducto = codProducto
        ;
    END $$
DELIMITER ;

-- ------------------------------- SERVICIOS HAS PLATOS ---------------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarServicioHasPlato(IN Servicios_codigoServicio INT, IN codigoPlato INT, IN codigoServicio INT)
    BEGIN
		INSERT INTO Servicios_has_Platos(Servicios_codigoServicio,codigoPlato, codigoServicio)
        VALUES(Servicios_codigoServicio,codigoPlato, codigoServicio)
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ReporteServicioHasPlato()
    BEGIN
		SELECT
			Shp.Servicios_codigoServicio,
			Pl.codigoPlato,
            Pl.cantidad,
            Pl.nombrePlato,
            Pl.descripcionPlato,
            Pl.precioPlato,
            Tp.codigoTipoPlato,
            Tp.descripcionTipo,
            Se.codigoServicio,
            Se.fechaServicio,
            Se.horaServicio,
            Se.tipoServicio,
            Se.lugarServicio,
            Se.telefonoContacto,
            Em.codigoEmpresa,
            Em.nombreEmpresa,
            Em.direccion,
            Em.telefono
		FROM Servicios_has_Platos Shp
        INNER JOIN Platos Pl
        ON Pl.codigoPlato = Shp.codigoPlato
        INNER JOIN TipoPlato Tp
		ON Tp.codigoTipoPlato = Pl.codigoTipoPlato
        INNER JOIN Servicios Se
        ON Se.codigoServicio = Shp.codigoServicio
        INNER JOIN Empresas Em
        ON Em.codigoEmpresa = Se.codigoEmpresa
        ;
    END $$
DELIMITER ;

CALL sp_ReporteServicioHasPlato();

/* No se pueden modificar las llaves primarias ni las foraneas.*/

DELIMITER $$
	CREATE PROCEDURE sp_EliminarServicioHasPlato(IN codProducto INT)
    BEGIN
		DELETE FROM Servicios_has_Platos Shp
        WHERE Shp.Servicios_codigoProducto = codProducto
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarServiciosHasPlatos()
    BEGIN
		SELECT
			Shp.Servicios_codigoServicio,
            Shp.codigoPlato,
            Shp.codigoServicio
		FROM Servicios_has_Platos Shp
        ;
    END $$
DELIMITER ;

CALL sp_ListarServiciosHasPlatos();

DELIMITER $$
	CREATE PROCEDURE sp_BuscarServicioHasPlato(IN codServicio INT)
    BEGIN
		SELECT
			Shp.Servicios_codigoProducto,
            Shp.codigoPlato,
            Shp.codigoServicio
		FROM Servicios_has_Platos Shp
        WHERE Shp.Servicios_codigoProducto = codServicio
        ;
    END $$
DELIMITER ;

CALL sp_AgregarServicioHasPlato(1,2,1);

CALL sp_AgregarServicioHasPlato(2,6,2);
CALL sp_AgregarServicioHasPlato(3,7,2);
CALL sp_AgregarServicioHasPlato(4,8,2);
CALL sp_AgregarServicioHasPlato(5,9,2);
CALL sp_AgregarServicioHasPlato(6,10,2);

CALL sp_AgregarServicioHasPlato(7,4,3);
CALL sp_AgregarServicioHasPlato(8,5,3);
CALL sp_AgregarServicioHasPlato(9,10,3);
CALL sp_AgregarServicioHasPlato(10,12,3);

CALL sp_AgregarServicioHasPlato(12,4,4);
CALL sp_AgregarServicioHasPlato(13,5,4);
CALL sp_AgregarServicioHasPlato(14,10,4);
CALL sp_AgregarServicioHasPlato(15,11,4);

CALL sp_AgregarServicioHasPlato(16,4,5);
CALL sp_AgregarServicioHasPlato(17,5,5);
CALL sp_AgregarServicioHasPlato(18,10,5);
CALL sp_AgregarServicioHasPlato(19,11,5);
CALL sp_AgregarServicioHasPlato(20,12,5);
CALL sp_AgregarServicioHasPlato(21,6,5);
CALL sp_AgregarServicioHasPlato(22,8,5);
CALL sp_AgregarServicioHasPlato(23,2,5);

CALL sp_AgregarServicioHasPlato(24,6,6);
CALL sp_AgregarServicioHasPlato(25,7,6);
CALL sp_AgregarServicioHasPlato(26,8,6);
CALL sp_AgregarServicioHasPlato(27,9,6);

CALL sp_AgregarServicioHasPlato(28,1,7);
CALL sp_AgregarServicioHasPlato(29,2,7);

CALL sp_AgregarServicioHasPlato(30,1,8);

CALL sp_AgregarServicioHasPlato(31,2,9);
CALL sp_AgregarServicioHasPlato(32,10,9);
CALL sp_AgregarServicioHasPlato(33,7,9);

CALL sp_AgregarServicioHasPlato(34,2,10);
CALL sp_AgregarServicioHasPlato(35,10,10);
CALL sp_AgregarServicioHasPlato(36,8,10);

-- ------------------------------------------ SERVICIOS HAS EMPLEADOS -------------------------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarServicioHasEmpleado(IN Servicios_codigoServicio INT,IN codigoServicio INT, IN codigoEmpleado INT, IN fechaEvento DATE,
		IN horaEvento TIME, IN lugarEvento VARCHAR(150))
    BEGIN
		INSERT INTO Servicios_has_Empleados(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
        VALUES(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
        ;
    END $$
DELIMITER ;

CALL sp_AgregarServicioHasEmpleado(1,1,3,'2023-05-10','12:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(2,1,5,'2023-05-10','12:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(3,1,11,'2023-05-10','12:30:00','Casa Antigua');

CALL sp_AgregarServicioHasEmpleado(4,2,3,'2023-05-11','15:15:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(5,2,5,'2023-05-11','15:15:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(6,2,9,'2023-05-11','15:15:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(7,2,1,'2023-05-11','15:15:00','Casa Antigua');

CALL sp_AgregarServicioHasEmpleado(8,3,7,'2023-04-08','19:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(9,3,3,'2023-04-08','19:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(10,3,5,'2023-04-08','19:30:00','Casa Antigua');

CALL sp_AgregarServicioHasEmpleado(11,4,2,'2023-04-22','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(12,4,3,'2023-04-22','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(13,4,5,'2023-04-22','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(14,4,6,'2023-04-22','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(15,4,7,'2023-04-22','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(16,4,11,'2023-04-22','20:00:00','Casa Antigua');

CALL sp_AgregarServicioHasEmpleado(17,5,1,'2023-05-06','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(18,5,2,'2023-05-06','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(19,5,3,'2023-05-06','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(20,5,4,'2023-05-06','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(21,5,5,'2023-05-06','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(22,5,7,'2023-05-06','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(23,5,8,'2023-05-06','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(24,5,9,'2023-05-06','20:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(25,5,11,'2023-05-06','20:00:00','Casa Antigua');

CALL sp_AgregarServicioHasEmpleado(26,6,2,'2023-05-08','13:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(27,6,3,'2023-05-08','13:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(28,6,5,'2023-05-08','13:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(29,6,6,'2023-05-08','13:30:00','Casa Antigua');

CALL sp_AgregarServicioHasEmpleado(30,7,2,'2023-05-07','20:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(31,7,3,'2023-05-07','20:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(32,7,4,'2023-05-07','20:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(33,7,5,'2023-05-07','20:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(34,7,6,'2023-05-07','20:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(35,7,11,'2023-05-07','20:30:00','Casa Antigua');

CALL sp_AgregarServicioHasEmpleado(36,8,3,'2023-05-09','07:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(37,8,5,'2023-05-09','07:30:00','Casa Antigua');

CALL sp_AgregarServicioHasEmpleado(38,9,2,'2023-05-10','13:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(39,9,3,'2023-05-10','13:30:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(40,9,5,'2023-05-10','13:30:00','Casa Antigua');

CALL sp_AgregarServicioHasEmpleado(41,10,1,'2023-05-11','13:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(42,10,2,'2023-05-11','13:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(43,10,3,'2023-05-11','13:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(44,10,5,'2023-05-11','13:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(45,10,6,'2023-05-11','13:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(46,10,9,'2023-05-11','13:00:00','Casa Antigua');
CALL sp_AgregarServicioHasEmpleado(47,10,11,'2023-05-11','13:00:00','Casa Antigua');



DELIMITER $$
	CREATE PROCEDURE sp_ReporteServicioHasEmpleado()
    BEGIN
		SELECT
			She.Servicios_codigoServicio,
            Se.codigoServicio,
            Se.fechaServicio,
            Se.horaServicio,
            Se.tipoServicio,
            Se.lugarServicio,
            Se.telefonoContacto,
            Em.codigoEmpresa,
            Em.nombreEmpresa,
            Em.direccion,
            Em.telefono,
            Emp.codigoEmpleado,
            Emp.numeroEmpleado,
            Emp.nombresEmpleado,
            Emp.apellidosEmpleado,
            Emp.direccionEmpleado,
            Emp.telefonoContacto,
            Emp.gradoCocinero,
            Te.codigoTipoEmpleado,
            Te.descripcion,
            She.fechaEvento,
            She.horaEvento,
            She.lugarEvento
		FROM Servicios_has_Empleados She
        INNER JOIN Servicios Se
        ON Se.codigoServicio = She.codigoServicio
        INNER JOIN Empresas Em
        ON Em.codigoEmpresa = Se.codigoEmpresa
        INNER JOIN Empleados Emp
        ON Emp.codigoEmpleado = She.codigoEmpleado
        INNER JOIN TipoEmpleado Te
        ON Te.codigoTipoEmpleado = Emp.codigoTipoEmpleado
        ;
    END $$
DELIMITER ;

CALL sp_ReporteServicioHasEmpleado();
/* Segun la teoria no se actualizan las PK ni las FK*/

DELIMITER $$
	CREATE PROCEDURE sp_EditarServicioHasEmpleado(IN idCodigoServicio INT, IN fechaEv Date, IN horaEv TIME, IN lugarEv VARCHAR(150))
    BEGIN
		UPDATE servicios_has_empleados She
        SET 
			She.fechaEvento = fechaEv,
            She.horaEvento = horaEv,
            She.lugarEvento = lugarEv
		WHERE She.Servicios_codigoServicio = idCodigoServicio
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_EliminarServicioHasEmpleado(IN codServicio INT)
    BEGIN
		DELETE FROM Servicios_has_Empleados She
        WHERE She.Servicios_codigoServicio = codServicio
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarServiciosHasEmpleados()
    BEGIN
		SELECT
			She.Servicios_codigoServicio,
            She.codigoServicio,
            She.codigoEmpleado,
            She.fechaEvento,
            She.horaEvento,
            She.lugarEvento
		FROM Servicios_has_Empleados She
        ;
    END $$
DELIMITER ;

CALL sp_ListarServiciosHasEmpleados();

DELIMITER $$
	CREATE PROCEDURE sp_BuscarServicioHasEmpleado(IN codServicio INT)
    BEGIN
		SELECT
			She.Servicios_codigoServicio,
            She.codigoServicio,
            She.codigoEmpleado,
            She.fechaEvento,
            She.horaEvento,
            She.lugarEvento
		FROM Servicios_has_Empleados She
        WHERE She.Servicios_codigoServicio = codServicio
        ;
    END $$
DELIMITER ;



-- ---------------- USUARIO -------------------------------------------

DELIMITER $$
	CREATE PROCEDURE sp_AgregarUsuario(IN nombreUsuario VARCHAR(100), IN apellidoUsuario VARCHAR(100), IN usuarioLogin VARCHAR(50), IN contrasena VARCHAR(50))
    BEGIN
		INSERT INTO Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
        VALUES(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ListarUsuarios()
    BEGIN
		SELECT
			U.codigoUsuario, 
            U.nombreUsuario, 
            U.apellidoUsuario, 
            U.usuarioLogin, 
            U.contrasena
		FROM Usuario U;
    END $$
DELIMITER ;

-- CALL sp_AgregarUsuario('Joshua Elí Isaac', 'Realiquez Sosa','j8xuaaa','9ypZbjZx%');
CALL sp_AgregarUsuario('Joshua Elí Isaac', 'Realiquez Sosa','j8xuaaa','1ba2758b427f473b99e68a795968fa80');
-- CALL sp_AgregarUsuario('admin', 'admin','admin','admin');
CALL sp_AgregarUsuario('Pedro', 'Armas','admin','21232f297a57a5a743894a0e4a801fc3');
CALL sp_ListarUsuarios();
-- DELETE FROM Usuario U WHERE U.codigoUsuario = 1;

DELIMITER $$
	CREATE PROCEDURE sp_ReporteFinalServiciosEmpresa(IN codEmpresa INT)
    BEGIN
		SELECT
			E.codigoEmpresa,
            E.nombreEmpresa,
            E.direccion,
            E.telefono,
            P.codigoPresupuesto, 
            P.fechaSolicitud, 
            P.cantidadPresupuesto, 
            -- P.codigoEmpresa,
            -- S.codigoServicio, 
            S.fechaServicio, 
            S.tipoServicio, 
            S.horaServicio, 
            S.lugarServicio, 
            S.telefonoContacto
            -- S.codigoEmpresa
		FROM Servicios S
        INNER JOIN Empresas E
			ON S.codigoEmpresa = E.codigoEmpresa
        INNER JOIN Presupuestos P
			ON P.codigoEmpresa = E.codigoEmpresa
        INNER JOIN Servicios_has_Platos Sp
			ON S.codigoServicio = Sp.codigoServicio
        WHERE E.codigoEmpresa = codEmpresa
        GROUP BY E.codigoEmpresa
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ReporteGeneral(IN codEmpresa INT)
    BEGIN
		SELECT
			E.nombreEmpresa,
            E.direccion,
            E.telefono,
        -- solicitud de presupuesto
			P.fechaSolicitud,
            P.cantidadPresupuesto,
        -- datos del servicio ya presupuestado
			S.codigoServicio,
            S.fechaServicio,
            S.tipoServicio,
            S.horaServicio,
            S.lugarServicio,
            S.telefonoContacto
		FROM Servicios S
        INNER JOIN Empresas E
			ON S.codigoEmpresa = E.codigoEmpresa
		INNER JOIN Presupuestos P
			ON E.codigoEmpresa = P.codigoEmpresa
		WHERE E.codigoEmpresa = codEmpresa
        -- GROUP BY E.codigoEmpresa AND S.codigoServicio
        ;
		
        /*SELECT 
			E.nombresEmpleado,
            E.apellidosEmpleado,
            T.descripcion
        FROM Empleados E
		INNER JOIN Tipoempleado T
			ON E.codigoTipoEmpleado = T.codigoTipoEmpleado
		INNER JOIN Servicios_has_Empleados She
			ON E.codigoEmpleado = She.codigoEmpleado
		WHERE She.codigoServicio = (
				SELECT
					S.codigoServicio
				FROM Servicios S
                WHERE S.codigoEmpresa = codigoEmpresa
				)
        ;*/
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ReporteServicioEmpleados(IN codEmpresa INT)
    BEGIN
		SELECT
			Em.nombresEmpleado,
            Em.apellidosEmpleado,
            T.descripcion
        FROM Servicios S
        INNER JOIN Empresas E
			ON S.codigoEmpresa = E.codigoEmpresa
		INNER JOIN servicios_has_empleados She
			ON S.codigoServicio = She.codigoServicio
		INNER JOIN Empleados Em
			ON She.codigoEmpleado = Em.codigoEmpleado
		INNER JOIN TipoEmpleado T
			ON Em.codigoTipoEmpleado = T.codigoTipoEmpleado
		WHERE E.codigoEmpresa = codEmpresa
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ReporteServicioPlatos(IN codEmpresa INT)
    BEGIN
		SELECT
			Tp.descripcionTipo,
			P.nombrePlato,
            P.descripcionPlato,
            P.precioPlato
        FROM Servicios S
        INNER JOIN Empresas E
			ON E.codigoEmpresa = S.codigoEmpresa
        INNER JOIN Servicios_has_Platos Shp
			ON S.codigoServicio = Shp.codigoServicio
		INNER JOIN Platos P
			ON Shp.codigoPlato = P.codigoPlato
		INNER JOIN TipoPlato Tp
			ON P.codigoTipoPlato = Tp.codigoTipoPlato
		WHERE E.codigoEmpresa = codEmpresa
        ;
    END $$
DELIMITER ;

DELIMITER $$
	CREATE PROCEDURE sp_ReporteServicioProductos(IN codEmpresa INT)
    BEGIN
		SELECT
			Pr.nombreProducto,
            COUNT(Pr.nombreProducto)
        FROM Servicios S
        INNER JOIN Empresas E
			ON E.codigoEmpresa = S.codigoEmpresa
		INNER JOIN Servicios_has_Platos Shp
			ON S.codigoServicio = Shp.codigoServicio
		INNER JOIN Platos P
			ON Shp.codigoPlato = P.codigoPlato
		INNER JOIN Productos_has_Platos Php
			ON P.codigoPlato = Php.codigoPlato
		INNER JOIN Productos Pr
			ON Php.codigoProducto = Pr.codigoProducto
        WHERE E.codigoEmpresa = codEmpresa
        GROUP BY Pr.nombreProducto
        ;
    END $$
DELIMITER ;

CALL sp_ReporteGeneral(5);

CALL sp_ReporteFinalServiciosEmpresa(5);

CALL sp_ReporteServicioEmpleados(5);

CALL sp_ReporteServicioPlatos(5);

CALL sp_ReporteServicioProductos(5);

-- DROP PROCEDURE sp_ReporteFinalServiciosEmpresa;
-- DROP PROCEDURE sp_ReporteGeneral;

-- ALTER USER 'realiquez'@'localhost' IDENTIFIED WITH mysql_native_password BY 'admin';

-- SELECT * FROM EMPRESAS;
