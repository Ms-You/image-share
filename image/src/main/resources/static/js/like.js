$(".area-desc").click(function() {
    var feedId = $("#feedId").val();
    var arrowImage = $(this).children("a").children("span").children("img");
    var likeCount = $("#likeCount").text();

    likeCount *= 1; // (string -> number)

    arrowImage.attr("src", function(index, attr){
      if (attr.match('full')) {
        $('#likeCount').text(likeCount-1);
        btnUnLike(feedId);
        return attr.replace("full", "empty");
      }
      else {
        $('#likeCount').text(likeCount+1);
        btnLike(feedId);
        return attr.replace("empty", "full");
      }
    });

  });


  function btnLike(feedId){
  	$.ajax({
  		type: "POST",
  		url: `/user/like/feed/${feedId}`,
  		dataType: "text"
  	}).done(res=>{
  	    console.log("OK");
  	}).fail(error=>{
  		console.log("오류", error);
  	});
  }

  function btnUnLike(feedId){
      $.ajax({
          type: "POST",
          url: `/user/unLike/feed/${feedId}`,
          dataType: "text"
      }).done(res=>{
          console.log("OK");
      }).fail(error=>{
          console.log("오류", error);
      });

  }