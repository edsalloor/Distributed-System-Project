var cont = 2;

$(document).ready(function(){ 

    $("#btnImagen").click(function() {
        document.getElementById( 'containerImagen' ).style.display = 'block';
        document.getElementById( 'containerAudio' ).style.display = 'none';
        document.getElementById( 'containerVideo' ).style.display = 'none';
        document.getElementById( 'containerTexto' ).style.display = 'none';
    });

    $("#btnAudio").click(function() {
        document.getElementById( 'containerImagen' ).style.display = 'none';
        document.getElementById( 'containerAudio' ).style.display = 'block';
        document.getElementById( 'containerVideo' ).style.display = 'none';
        document.getElementById( 'containerTexto' ).style.display = 'none';
    });

    $("#btnVideo").click(function() {
        document.getElementById( 'containerImagen' ).style.display = 'none';
        document.getElementById( 'containerAudio' ).style.display = 'none';
        document.getElementById( 'containerVideo' ).style.display = 'block';
        document.getElementById( 'containerTexto' ).style.display = 'none';
    });

    $("#btnTexto").click(function() {
        document.getElementById( 'containerImagen' ).style.display = 'none';
        document.getElementById( 'containerAudio' ).style.display = 'none';
        document.getElementById( 'containerVideo' ).style.display = 'none';
        document.getElementById( 'containerTexto' ).style.display = 'block';

    });

    function readFileImagen() {
        if (this.files && this.files[0]) {
        
            var FR= new FileReader();
        
            FR.addEventListener("load", function(e) {
                document.getElementById("img").src = e.target.result;
                document.getElementById("img").style.display = 'block';
            }); 
            
            FR.readAsDataURL( this.files[0] );
        }
      
    }

    function readFileAudio() {
        if (this.files && this.files[0]) {
        
            var FR= new FileReader();
        
            FR.addEventListener("load", function(e) {
                document.getElementById("aud").src = e.target.result;
                document.getElementById("aud").style.display = 'block';
            }); 
            
            FR.readAsDataURL( this.files[0] );
        }
      
    }

    function readFileVideo() {
        if (this.files && this.files[0]) {
        
            var FR= new FileReader();
        
            FR.addEventListener("load", function(e) {
                document.getElementById("vid").src = e.target.result;
                document.getElementById("vid").style.display = 'block';
            }); 
            
            FR.readAsDataURL( this.files[0] );
        }
      
    }

    function readFileText() {
        if (this.files && this.files[0]) {
        
            var FR= new FileReader();
        
            FR.addEventListener("load", function(e) {
                document.getElementById("txt").src = e.target.result;
                document.getElementById("txt").style.display = 'block';
            }); 
            
            FR.readAsDataURL( this.files[0] );
        }
      
    }


    document.getElementById("inputImagen").addEventListener("change", readFileImagen);
    document.getElementById("inputVideo").addEventListener("change", readFileVideo);
    document.getElementById("inputAudio").addEventListener("change", readFileAudio);
    document.getElementById("inputText").addEventListener("change", readFileText);


    $("#convertirImagen").click(function() {
        var obj = new Object();
        obj.img = document.getElementById("img").src.split(",")[1];
        var formato = $("#selectImage :selected").val();
        obj.dst_format = formato;
        var jsonString= JSON.stringify(obj);
        //console.log(jsonString);
        $.ajax({
            url: 'https://ncmh9e63m6.execute-api.us-east-2.amazonaws.com/beta/convertimage',
            method: 'POST',
            dataType: "json",
            data: jsonString,
            contentType: false,
            processData: false,
            success: function(data){                             
                console.log(data["result"]);
                var a = document.createElement("a");
                a.href = "data:image/"+ formato +";base64," + data["result"];
                a.download = "Image."+ formato; 
                a.click();
            }
        }).fail(function(e){
            console.log("NO CORRECTO ?");  
            console.log(e.responseText);
            // var url = 'data:application/octet-stream,'+e.responseText;
            // window.open(url);
        });  
    });

    $("#convertirAudio").click(function() {
        var obj = new Object();
        obj.aud = document.getElementById("aud").src.split(",")[1];
        var formato = $("#selectAudio :selected").val();
        obj.dst_format = formato;
        var jsonString= JSON.stringify(obj);
        console.log(jsonString);
        $.ajax({
            url: 'https://ncmh9e63m6.execute-api.us-east-2.amazonaws.com/beta/convertaudio',
            method: 'POST',
            dataType: "json",
            data: jsonString,
            contentType: false,
            processData: false,
            success: function(data){                             
                console.log(data["result"]);
                var a = document.createElement("a");
                a.href = "data:audio/"+ formato +";base64," + data["result"];
                a.download = "Audio."+ formato; 
                a.click();
            }
        }).fail(function(e){
            console.log("NO CORRECTO ?");  
            console.log(e.responseText);
            // var url = 'data:application/octet-stream,'+e.responseText;
            // window.open(url);
        });
    });

    $("#convertirVideo").click(function() {
        var obj = new Object();
        obj.vid = document.getElementById("vid").src.split(",")[1];
        var formato = $("#selectVideo :selected").val();
        obj.dst_format = formato;
        var jsonString= JSON.stringify(obj);
        console.log(jsonString);
        $.ajax({
            url: 'https://ncmh9e63m6.execute-api.us-east-2.amazonaws.com/beta/convertvideo',
            method: 'POST',
            dataType: "json",
            data: jsonString,
            contentType: false,
            processData: false,
            success: function(data){                             
                console.log(data["result"]);
                var a = document.createElement("a");
                a.href = "data:video/"+ formato +";base64," + data["result"];
                a.download = "Video."+ formato; 
                a.click();
            }
        }).fail(function(e){
            console.log("NO CORRECTO ?");  
            console.log(e.responseText);
            // var url = 'data:application/octet-stream,'+e.responseText;
            // window.open(url);
        });
    });

    $("#convertirTexto").click(function() {
        var obj = new Object();
        obj.text_file = document.getElementById("txt").src.split(",")[1];
        var formato = $("#selectTexto :selected").val();
        obj.dst_format = formato;
        var jsonString= JSON.stringify(obj);
        console.log(jsonString);
        $.ajax({
            url: 'https://ncmh9e63m6.execute-api.us-east-2.amazonaws.com/beta/converttextfile',
            method: 'POST',
            dataType: "json",
            data: jsonString,
            contentType: false,
            processData: false,
            success: function(data){                             
                console.log(data["result"]);
                var a = document.createElement("a");
                a.href = "data:text/"+ formato +";base64," + data["result"];
                a.download = "Texto."+ formato; 
                a.click();
            }
        }).fail(function(e){
            console.log("NO CORRECTO ?");  
            console.log(e.responseText);
            // var url = 'data:application/octet-stream,'+e.responseText;
            // window.open(url);
        });
    });
})