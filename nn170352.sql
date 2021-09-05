
DROP TABLE [Admin]
go

DROP TABLE [ZahtevZaKurira]
go

DROP TABLE [Ponuda]
go

DROP TABLE [Paket]
go

DROP TABLE [Opstina]
go

DROP TABLE [Grad]
go

DROP TABLE [Kurir]
go

DROP TABLE [Vozilo]
go

DROP TABLE [Korisnik]
go

CREATE TABLE [Admin]
( 
	[IdK]                integer  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[IdG]                integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(100)  NOT NULL ,
	[PostanskiBr]        varchar(100)  NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[IdK]                integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NOT NULL ,
	[Prezime]            varchar(100)  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[Sifra]              varchar(100)  NOT NULL ,
	[BrojPoslatihPaketa] integer  NOT NULL 
	CONSTRAINT [VrednostNula_1496269520]
		 DEFAULT  0
)
go

CREATE TABLE [Kurir]
( 
	[IdK]                integer  NOT NULL ,
	[Vozilo]             integer  NOT NULL ,
	[BrojIsporucenihPaketa] integer  NOT NULL 
	CONSTRAINT [VrednostNula_1244526882]
		 DEFAULT  0,
	[OstvarenProfit]     decimal(10,3)  NOT NULL 
	CONSTRAINT [VrednostNula_966777480]
		 DEFAULT  0,
	[Status]             integer  NOT NULL 
	CONSTRAINT [VrednostNula_561413229]
		 DEFAULT  0
	CONSTRAINT [NulaIliJedan_931629709]
		CHECK  ( Status BETWEEN 0 AND 1 )
)
go

CREATE TABLE [Opstina]
( 
	[IdO]                integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(100)  NOT NULL ,
	[X]                  integer  NOT NULL ,
	[Y]                  integer  NOT NULL ,
	[Grad]               integer  NOT NULL 
)
go

CREATE TABLE [Paket]
( 
	[IdP]                integer  IDENTITY  NOT NULL ,
	[Korisnik]           integer  NOT NULL ,
	[OpstinaPreuzimanja] integer  NOT NULL ,
	[OpstinaDostavljanja] integer  NOT NULL ,
	[TipPaketa]          integer  NOT NULL 
	CONSTRAINT [NulaJedanDva_1197133980]
		CHECK  ( TipPaketa BETWEEN 0 AND 2 ),
	[TezinaPaketa]       decimal(10,3)  NOT NULL 
	CONSTRAINT [VeceJednakoNula_319240765]
		CHECK  ( TezinaPaketa >= 0 ),
	[Kurir]              integer  NULL ,
	[StatusIsporuke]     integer  NOT NULL 
	CONSTRAINT [VrednostNula_1770435743]
		 DEFAULT  0
	CONSTRAINT [NulaJedanDvaTri_1272886198]
		CHECK  ( StatusIsporuke BETWEEN 0 AND 3 ),
	[Cena]               decimal(10,3)  NULL ,
	[VremePrihvatanja]   datetime  NULL 
)
go

CREATE TABLE [Ponuda]
( 
	[Kurir]              integer  NOT NULL ,
	[Paket]              integer  NOT NULL ,
	[ProcenatCeneIsporuke] decimal(10,3)  NOT NULL 
	CONSTRAINT [VeceJednakoNula_77285224]
		CHECK  ( ProcenatCeneIsporuke >= 0 ),
	[IdPonuda]           integer  IDENTITY  NOT NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[IdV]                integer  IDENTITY  NOT NULL ,
	[RegBroj]            varchar(100)  NOT NULL ,
	[TipGoriva]          integer  NOT NULL 
	CONSTRAINT [NulaJedanDva_1287381955]
		CHECK  ( TipGoriva BETWEEN 0 AND 2 ),
	[Potrosnja]          decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [ZahtevZaKurira]
( 
	[Vozilo]             integer  NOT NULL ,
	[IdK]                integer  NOT NULL 
)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKAdmin] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdG] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XAK1Grad] UNIQUE ([PostanskiBr]  ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XAK2Grad] UNIQUE ([Naziv]  ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XAK1Korisnik] UNIQUE ([KorisnickoIme]  ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([IdO] ASC)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([IdP] ASC)
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKPonuda] PRIMARY KEY  CLUSTERED ([IdPonuda] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([IdV] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XAK1Vozilo] UNIQUE ([RegBroj]  ASC)
go

ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [XPKZahtevZaKurira] PRIMARY KEY  CLUSTERED ([IdK] ASC)
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([Vozilo]) REFERENCES [Vozilo]([IdV])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([Grad]) REFERENCES [Grad]([IdG])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([Korisnik]) REFERENCES [Korisnik]([IdK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([OpstinaPreuzimanja]) REFERENCES [Opstina]([IdO])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([OpstinaDostavljanja]) REFERENCES [Opstina]([IdO])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([Kurir]) REFERENCES [Kurir]([IdK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([Kurir]) REFERENCES [Kurir]([IdK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([Paket]) REFERENCES [Paket]([IdP])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([Vozilo]) REFERENCES [Vozilo]([IdV])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([IdK]) REFERENCES [Korisnik]([IdK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go




CREATE TRIGGER TR_TransportOffer_acceptAnOffer
   ON  Paket
   AFTER update
AS 
BEGIN
	
	declare @cursorI cursor
	declare @cursorD cursor
	declare @statusI int
	declare @statusD int
	declare @paketI int
	declare @paketD int

	set @cursorI = cursor for
	select StatusIsporuke, IdP 
	from inserted

	set @cursorD = cursor for
	select StatusIsporuke, IdP
	from deleted

	open @cursorI
	open @cursorD

	fetch next from @cursorI
	into @statusI, @paketI

	fetch next from @cursorD
	into @statusD, @paketD

	while @@FETCH_STATUS = 0
	begin
		if(@paketI = @paketD)
		begin
			if(@statusD = 0 and @statusI = 1)
			begin 
				delete from Ponuda 
				where Paket = @paketI
			end
		end

		fetch next from @cursorI
		into @statusI, @paketI

		fetch next from @cursorD
		into @statusD, @paketD
	end
	
	close @cursorI
	close @cursorD

	deallocate @cursorI
	deallocate @cursorD

END
GO


CREATE PROCEDURE SP_Odobri_Zahtev_Za_Kurira
@IdK int,
@broj int output
AS
BEGIN
	
	declare @IdV int

	select @IdV = Vozilo
	from ZahtevZaKurira
	where IdK = @IdK
	
	IF EXISTS (
		select *
		from Kurir
		where Vozilo = @IdV
	)
	begin 
		set @broj = 0
	end
	else 
	begin
		insert into Kurir(IdK, Vozilo) values(@IdK, @IdV)
		if (@@ROWCOUNT = 1)
		begin
			delete from ZahtevZaKurira where Vozilo = @IdV

			set @broj = 1
		end
		else 
		begin
			set @broj = 0
		end
	end


END
GO