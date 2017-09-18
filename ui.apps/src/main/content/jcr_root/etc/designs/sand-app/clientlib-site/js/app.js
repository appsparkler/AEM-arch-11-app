
$(document).ready(function() {
	var tags = document.getElementById('news-tags').innerHTML;
 
    $('[data-toggle="tooltip"]').tooltip();
 
    if(typeof(Storage) !== "undefined") {
 
        // Store
        var news = localStorage.getItem("news");
        var time = localStorage.getItem("time");
        var lastTags = localStorage.getItem("tags");
        time = new Date(time);
        var currentDate = new Date();
 
        if(news != null && news.length > 0 && time!=null && currentDate-time<3600000 && lastTags == tags) {
            // Retrieve data from localstorage
            document.getElementById("news-data").innerHTML = localStorage.getItem("news");
        } else if(tags.length>5) {
             
            $.ajax({
                url: "/bin/getMorningNews?tags="+tags,
                success: function(result) {
                	console.log('results : ', result)
                    if(result.length==0) {
                         $("#news-data").html("<div class=\"alert alert-danger\"><strong>Oops!..</strong> Something went wrong, looks like no news today so please have ice-cream<span class=\"glyphicon glyphicon-ice-lolly-tasted\"></span></div>");
                    } else {
                        $("#news-data").html(result);
                        localStorage.setItem("news", result);
                        localStorage.setItem("time", new Date());
                        localStorage.setItem("tags", tags);
                    }
                },
                error: function(xhr, status, error) {
                	console.log('error arguments', arguments);
                    $("#news-data").html("<div class=\"alert alert-danger\"><strong>Oops!..</strong> Something went wrong, looks like no news today so please have ice-cream<span class=\"glyphicon glyphicon-ice-lolly-tasted\"></span></div>");
                }
 
            });
        }
 
    } else {
         $.ajax({
                url: "/bin/getMorningNews?tags="+tags,
                success: function(result) {
 
                    if(result.length==0) {
                         $("#news-data").html("<div class=\"alert alert-danger\"><strong>Oops!..</strong> Something went wrong, looks like no news today so please have ice-cream<span class=\"glyphicon glyphicon-ice-lolly-tasted\"></span></div>");
                    } else {
                        $("#news-data").html(result);
                        localStorage.setItem("news", result);
                    }
                },
                error: function(xhr, status, error) {
                    $("#news-data").html("<div class=\"alert alert-danger\"><strong>Oops!..</strong> Something went wrong, looks like no news today so please have ice-cream<span class=\"glyphicon glyphicon-ice-lolly-tasted\"></span></div>");
                }
 
            });
    }
 
 
});