let index = {
    init: function(){
        $("#btn-save").on("click", ()=>{
            this.save();
        });

        $("#nickname-duplicate").on("click", ()=>{
            this.check();
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
            url: "/auth/join",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function(resp){
            alert(resp);
            location.href = "/auth/login";
        }).fail(function(error){
            alert(error.responseText);
        });

    },

    check: function(){
        let data = {
            nickName: $("#nickName").val()
        };

        $.ajax({
            type: "POST",
            url: "/auth/nickname",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function(resp){
            alert(resp);
        }).fail(function(error){
            alert(error.responseText);
        });

    },


}

index.init();
