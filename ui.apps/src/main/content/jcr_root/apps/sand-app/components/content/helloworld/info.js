use(["/libs/sightly/js/3rd-party/q.js"], function(q){
	var info = {};
	//
	info.title = granite.resource.properties["text"];
	//
	var deferredObjPromise = function(){
		var deferred = q.defer();
		
//		$.get("http://content.guardianapis.com/search",{
//				'api-key': 'test',
//				'page-size':4,
//				q:'bus'
//			}, function(results){},
//			'json'
//		);

		deferred.resolve("Done")
//		reqListener()
//		function reqListener(response) {
////		    deferred.resolve("Done");
//		}
		
//		var oReq = new XMLHttpRequest();
//		oReq.addEventListener("load", reqListener);
//		oReq.open("GET", "https://content.guardianapis.com/search?api-key=test&q=bus");
		// oReq.setRequestHeader('Access-Control-Allow-Headers', '*');
//		oReq.setRequestHeader('Access-Control-Allow-Origin', '*');
//		oReq.send();
		return deferred.promise;
	}
	//
	info.def = deferredObjPromise;
	//
	return info;
});