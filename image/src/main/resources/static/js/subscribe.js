$(".subscribe").click(function() {
    var toUserId = $("#toUserId").val();
    var feedId = $("#feedId").val();
    var arrowImage = $(this).children("span").children("img");

    arrowImage.attr("src", function(index, attr){
      if (attr.match('do')) {
        btnUnSubscribe(toUserId, feedId);
        return attr.replace("do", "un");
      }
      else {
        btnSubscribe(toUserId, feedId);
        return attr.replace("un", "do");
      }
    });

  });


  function btnSubscribe(toUserId, feedId){
  	$.ajax({
  		type: "POST",
  		url: `/user/subscribe/${toUserId}/${feedId}`,
  		dataType: "text"
  	}).done(res=>{
  	    console.log("OK");
  	}).fail(error=>{
  		console.log("오류", error);
  	});
  }

  function btnUnSubscribe(toUserId, feedId){
      $.ajax({
          type: "POST",
          url: `/user/unSubscribe/${toUserId}/${feedId}`,
          dataType: "text"
      }).done(res=>{
          console.log("OK");
      }).fail(error=>{
          console.log("오류", error);
      });

  }
