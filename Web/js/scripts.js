var cont = 2;

$(document).ready(function(){ 

    $("#btnImagen").click(function() {
        document.getElementById( 'containerImagen' ).style.display = 'block';
        document.getElementById( 'containerAudio' ).style.display = 'none';
        document.getElementById( 'containerVideo' ).style.display = 'none';
    });

    $("#btnAudio").click(function() {
        document.getElementById( 'containerImagen' ).style.display = 'none';
        document.getElementById( 'containerAudio' ).style.display = 'block';
        document.getElementById( 'containerVideo' ).style.display = 'none';
    });

    $("#btnVideo").click(function() {
        document.getElementById( 'containerImagen' ).style.display = 'none';
        document.getElementById( 'containerAudio' ).style.display = 'none';
        document.getElementById( 'containerVideo' ).style.display = 'block';

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

    document.getElementById("inputImagen").addEventListener("change", readFileImagen);
    document.getElementById("inputVideo").addEventListener("change", readFileVideo);
    document.getElementById("inputAudio").addEventListener("change", readFileAudio);


    $("#convertirImagen").click(function() {
        var obj = new Object();
        obj.img = document.getElementById("img").src;
        var jsonString= JSON.stringify(obj);
        console.log(jsonString);
        $.ajax({
            url: 'https://p2ejye0m46.execute-api.us-east-2.amazonaws.com/beta',
            method: 'POST',
            data: jsonString,
            contentType: false,
            processData: false,
            success: function(data){                                  
                console.log(data);
            }
        }).fail(function(e){
            console.log(data);
        });  
    });

    $("#convertirAudio").click(function() {
        alert("Convertir audio");
    });

    $("#convertirVideo").click(function() {
        var obj = new Object();
        obj.vid = document.getElementById("vid").src;
        var jsonString= JSON.stringify(obj);
        console.log(jsonString);
    });
})