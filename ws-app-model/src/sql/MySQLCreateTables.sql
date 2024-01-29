-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

DROP TABLE Compra;
DROP TABLE Partido;

------------------------------------ Partido ----------------------------------
CREATE TABLE Partido ( partidoId BIGINT NOT NULL AUTO_INCREMENT,
    nombreVisitante VARCHAR(255) NOT NULL,
    celebracion DATETIME NOT NULL,
    precio FLOAT NOT NULL,
    maxEntradas INT NOT NULL,
    vendidas INT NOT NULL ,
    alta DATETIME NOT NULL,
    CONSTRAINT PartidoPK PRIMARY KEY (partidoId)) ENGINE = InnoDB;


------------------------------------ Compra ------------------------------------
CREATE TABLE Compra (compraId BIGINT NOT NULL AUTO_INCREMENT,
    partidoId BIGINT NOT NULL,
    email VARCHAR(100) NOT NULL,
    tarjetaBancaria VARCHAR(16) NOT NULL,
    numEntradasCompradas INT NOT NULL,
    fechaHoraCompra DATETIME NOT NULL,
    recogidas BOOLEAN DEFAULT FALSE,
    CONSTRAINT CompraPK PRIMARY KEY (compraId),
    CONSTRAINT CompraPartidoIdFK FOREIGN KEY (partidoId)
        REFERENCES Partido(partidoId) ON DELETE CASCADE) ENGINE = InnoDB;