declare namespace API {
	
	type doFavourRequest = {
	  generatorId?: String;
	};
	
	
	type queryFavourRequest = {
		favourUserId?: String;
		generatorQueryRequest?:API.GeneratorQueryRequest;
	};
}