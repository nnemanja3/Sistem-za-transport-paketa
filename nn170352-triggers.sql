
CREATE TRIGGER TR_TransportOffer_acceptAnOffer
   ON  Ponuda
   AFTER DELETE
AS 
BEGIN
	
	declare @cursor cursor
	declare @paket int

	set @cursor = cursor for
	select Paket
	from deleted

	open @cursor

	fetch next from @cursor
	into @paket

	while @@FETCH_STATUS = 0
	begin
		delete from Ponuda
		where Paket = @paket

		fetch next from @cursor
		into @paket
	end

	close @cursor
	deallocate @cursor

END
GO
