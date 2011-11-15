

/* Get the rows which are currently selected */
function fnGetSelected( oTableLocal )
{

	var aReturn = new Array();
	var aTrs = oTableLocal.fnGetNodes();
	
	for ( var i=0 ; i<aTrs.length ; i++ )
	{
		if ( $(aTrs[i]).hasClass('row_selected') )
		{
			aReturn.push( aTrs[i] );
		}
	}
	return aReturn;
}


function refreshTable(id,callback){
	var stocks=new Array();
	var fields=new Array();
	var hasCallback ;
	 if ( arguments.length == 2 ) {
			hasCallback = true;
		 }else{
			hasCallback = false;
		}
	
	 
	var oTable = $("#"+id).dataTable();
	


	for ( var i=0 ; i<oTable.fnGetData().length ; i++ ){
		var symbol = oTable.fnGetData(i).symbol;
		stocks[stocks.length] = "'" + symbol + "'";
	}

	
	$('#'+id+' thead tr th').each(function() {
		var field = $(this).attr('field');
		if(field != null){
			fields[fields.length] =  field;
		}
	 });

	 var quotesToLookUp = stocks.join(',');
	 var fieldsToLookUp =  fields.join(',');


	 if(stocks.length > 0 ){
		 $.yql("select Symbol,"+ fieldsToLookUp + " from yahoo.finance.quotes where symbol in (@quotes)",
				 {
		 			quotes: quotesToLookUp,
		 			fields: fieldsToLookUp
				 },
				 function (data) {
					 
					 var quotes = new Array();
					 
					 if(data.query.results != null){
						 quotes =  quotes.concat(data.query.results.quote);
						 updateTable(quotes,fields,id); 

						 if(hasCallback == true){
							 callback();
						 }
					 }

				 }	
		 ); 	 
	 }

}

function updateTable(data,fields,id) {


	
	var oTable = $("#"+id).dataTable();
	var trIndex = 0;
	
	for ( var i=0 ; i<oTable.fnGetData().length ; i++ ){
		var symbol = oTable.fnGetData(i).symbol;
		var index = -1;
		for ( var j = 0; j < data.length; j++) {
			if(symbol == data[j].Symbol){
				index = j;
			}
		}
		
		if(index == -1){
			//alert("Can't update finance information for stock : " + symbol);
		}else{
			
			
			var fieldIndex = 0;
			
			$('#'+id+' tbody tr:eq('+trIndex+')').children("td").each(function() {

				var field =  $('#'+id+' thead tr th:eq('+fieldIndex+')').attr('field');
				if(field != null && field != undefined){
					$(this).html(data[index][field]);
				}

				fieldIndex++;
			});
		}

		trIndex++;
		
	}
	

}