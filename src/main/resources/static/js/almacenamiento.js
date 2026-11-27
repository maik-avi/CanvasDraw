function obtenerClaveDibujo() {
    const elemento = document.getElementById("canvasDibuix");

    return "drawing-" + JSON.stringify([
        elemento.dataset.propietari,
        elemento.dataset.id,
        elemento.dataset.creacio
    ]);
}

function guardarFigurasLocal(figuras) {
    try {
        localStorage.setItem(
            obtenerClaveDibujo(),
            JSON.stringify(figuras)
        );
    } catch (error) {
        console.error("No se ha podido guardar el borrador.", error);
    }
}

function recuperarFigurasLocal(figurasServidor) {
    try {
        const contenido = localStorage.getItem(obtenerClaveDibujo());
    

        if (contenido === null) {
            return figurasServidor;
        }

        const figurasGuardadas = JSON.parse(contenido);

        if (!Array.isArray(figurasGuardadas)) {
            return figurasServidor;
        }

        return figurasGuardadas;
    } catch(error) {
        console.error("No se ha podido recuperar el borrador.", error);
        return figurasServidor;
    }
}

function guardarConfiguracionLocal(configuracion) {
    try {
        localStorage.setItem(
            obtenerClaveDibujo() + "-configuracion",
            JSON.stringify(configuracion)
        );
    } catch (error) {
        console.error("No se ha podido guardar la configuración.", error);
    }
}

function recuperarConfiguracionLocal() {
    try {
        const contenido = localStorage.getItem(
            obtenerClaveDibujo() + "-configuracion"
        );

        return contenido === null ? null : JSON.parse(contenido);
    } catch (error) {
        console.error("No se ha podido recuperar la configuración.", error);
        return null;
    }
}

function guardarHistorialLocal(estado) {
    try {
        localStorage.setItem( 
            obtenerClaveDibujo() + "-historial",
            JSON.stringify(estado)
        );
    } catch (erro) {
        console.error("No se ha podido guardar el historial", error);
    }
}

function recuperarHistorialLocal() {
    try {
        const contenido = localStorage.getItem(
            obtenerClaveDibujo() + "-historial"
        );

        return contenido === null ? null : JSON.parse(contenido);
    } catch (error) {
    console.error("No se ha podido guardar el historial.", error);
    }
}