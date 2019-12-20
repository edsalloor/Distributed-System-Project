var cont = 2;

$(document).ready(function(){ 

    $("#btnImagen").click(function() {
        $("#containerImagen").css("visibility", "visible");
        $("#containerAudio").css("visibility", "hidden");
        $("#containerVideo").css("visibility", "hidden");

        $("#containerAudio").insertAfter("#containerImagen");
        $("#containerVideo").insertAfter("#containerAudio");
    });

    $("#btnAudio").click(function() {
        $("#containerImagen").css("visibility", "hidden");
        $("#containerAudio").css("visibility", "visible");
        $("#containerVideo").css("visibility", "hidden");

        $("#containerImagen").insertAfter("#containerAudio");
        $("#containerVideo").insertAfter("#containerImagen");
    });

    $("#btnVideo").click(function() {
        $("#containerImagen").css("visibility", "hidden");
        $("#containerAudio").css("visibility", "hidden");
        $("#containerVideo").css("visibility", "visible");

        $("#containerAudio").insertAfter("#containerVideo");
        $("#containerImagen").insertAfter("#containerAudio");
    });

    $("#convertirImagen").click(function() {
        alert("Convertir imagen");
    });

    $("#convertirAudio").click(function() {
        alert("Convertir audio");
    });

    $("#convertirVideo").click(function() {
        alert("Convertir video");
    });
})