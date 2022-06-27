function replyLikeFunc(feedId, replyId){
    var arrowImage = $("#replyImage"+replyId);
    var replyLikeCount = $("#replyLikeCount"+replyId).text();

    replyLikeCount *= 1; // (string -> number)

    arrowImage.attr("src", function(index, attr){
      if (attr.match('full')) {
        $('#replyLikeCount'+replyId).text(replyLikeCount-1);
        btnReplyUnLike(feedId, replyId);
        return attr.replace("full", "empty");
      }
      else {
        $('#replyLikeCount'+replyId).text(replyLikeCount+1);
        btnReplyLike(feedId, replyId);
        return attr.replace("empty", "full");
      }
    });
}



  function btnReplyLike(feedId, replyId){
  	$.ajax({
  		type: "POST",
  		url: `/user/like/feed/${feedId}/reply/${replyId}`,
  		dataType: "text"
  	}).done(res=>{
  	    console.log("OK");
  	}).fail(error=>{
  		console.log("오류", error);
  	});
  }

  function btnReplyUnLike(feedId, replyId){
      $.ajax({
          type: "POST",
          url: `/user/unLike/feed/${feedId}/reply/${replyId}`,
          dataType: "text"
      }).done(res=>{
          console.log("OK");
      }).fail(error=>{
          console.log("오류", error);
      });

  }