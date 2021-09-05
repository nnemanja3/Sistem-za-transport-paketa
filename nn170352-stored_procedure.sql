
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
