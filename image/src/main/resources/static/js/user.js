let index = {
    init: function(){
        $("#btn-save").on("click", ()=>{
            this.save();
        });
    },

    save: function(){
        let data = {
            email: $("#email").val(),
            password: $("#password").val(),
            nickName: $("#nickName").val()
        };

        $.ajax({
            type: "POST",
            url: "/user/join",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function(resp){
            alert(resp);
            location.href = "/user/login";
        }).fail(function(error){
            alert(error.responseText);
        });

    },



}

index.init();
