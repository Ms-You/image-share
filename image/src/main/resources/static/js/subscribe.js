$(".subscribe").click(function() {
    var feedId = $("#feedId").val();
    var arrowImage = $(this).children("span").children("img");

    arrowImage.attr("src", function(index, attr){
      if (attr.match('do')) {
        btnUnSubscribe(feedId);
        return attr.replace("do", "un");
      }
      else {
        btnSubscribe(feedId);
        return attr.replace("un", "do");
      }
    });

  });


  function btnSubscribe(feedId){
  	$.ajax({
  		type: "POST",
  		url: `/user/subscribe/feed/${feedId}`,
  		dataType: "text"
  	}).done(res=>{
  	    console.log("OK");
  	}).fail(error=>{
  		console.log("오류", error);
  	});
  }

  function btnUnSubscribe(feedId){
      $.ajax({
          type: "POST",
          url: `/user/unSubscribe/feed/${feedId}`,
          dataType: "text"
      }).done(res=>{
          console.log("OK");
      }).fail(error=>{
          console.log("오류", error);
      });

  }
