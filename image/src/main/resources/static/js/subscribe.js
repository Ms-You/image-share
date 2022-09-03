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

// 프로필-> 구독 관리 페이지 -> 특정 사용자 구독 작업
$(".user-subscribe").click(function() {
  var toUserId = $("#toUserId").val();
  var arrowImage = $(this).children("span").children("img");
  arrowImage.attr("src", function(index, attr){
    if (attr.match('do')) {
      userUnSubscribe(toUserId);
      return attr.replace("do", "un");
    }
    else {
      userSubscribe(toUserId);
      return attr.replace("un", "do");
    }
  });

});


  function userSubscribe(toUserId){
  	$.ajax({
  		type: "POST",
  		url: `/user/subscribe/${toUserId}`,
  		dataType: "text"
  	}).done(res=>{
  	    console.log("OK");
  	}).fail(error=>{
  		console.log("오류", error);
  	});
  }

  function userUnSubscribe(toUserId){
      $.ajax({
          type: "POST",
          url: `/user/unSubscribe/${toUserId}`,
          dataType: "text"
      }).done(res=>{
          console.log("OK");
      }).fail(error=>{
          console.log("오류", error);
      });

  }

// 피드 하단 특정 사용자 정보 페이지에서의 구독 작업
$(".specific-subscribe").click(function() {
  var toUserId = $("#toUserId").val();
  var arrowImage = $(this).children("span").children("img");
  arrowImage.attr("src", function(index, attr){
    if (attr.match('do')) {
      specificUnSubscribe(toUserId);
      return attr.replace("do", "un");
    }
    else {
      specificSubscribe(toUserId);
      return attr.replace("un", "do");
    }
  });

});


  function specificSubscribe(toUserId){
  	$.ajax({
  		type: "POST",
  		url: `/user/subscribe/toUser/${toUserId}`,
  		dataType: "text"
  	}).done(res=>{
  	    console.log("OK");
  	}).fail(error=>{
  		console.log("오류", error);
  	});
  }

  function specificUnSubscribe(toUserId){
      $.ajax({
          type: "POST",
          url: `/user/unSubscribe/toUser/${toUserId}`,
          dataType: "text"
      }).done(res=>{
          console.log("OK");
      }).fail(error=>{
          console.log("오류", error);
      });

  }