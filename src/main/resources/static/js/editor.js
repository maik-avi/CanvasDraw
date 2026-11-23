const canvas = document.getElementById("canvasDibuix");
const ctx = canvas.getContext("2d");

const toolbar = document.getElementById("tool-bar");

const selectorModo = document.getElementById("modo-de-dibujo");
const selectorRellenado = document.getElementById("rellenado");

const llistaFigures = document.getElementById("llistaFigures");

let trazoActual = null;
let punteroActivo = null;

const figures = JSON.parse(
    document.getElementById("drawingData").textContent
);

const selectorColor = document.getElementById("color-set");
const selectorMida = document.getElementById("mida");

const nombreFigura = document.getElementById("nombre-figura");
const modificarColor = document.getElementById("modificar-color");
const modificarMida = document.getElementById("modificar-mida");
const modificarX = document.getElementById("posicion-editable-X");
const modificarY = document.getElementById("posicion-editable-Y");
const modificarRelleno = document.getElementById("modificar-relleno");

let figuraArrastrada = null;
let desplazamientoX = 0;
let desplazamientoY = 0;

let posicionInicialX = 0;
let posicionInicialY = 0;

const camposEdicion = [
    modificarColor,
    modificarMida,
    modificarX,
    modificarY,
    modificarRelleno
];

let idFiguraSeleccionada = null;

const listaHistorial = document.getElementById("historialCambios");
const historialCambios = [];

const cambiosDeshacer = [];
const cambiosRehacer = [];
const limiteCambios = 3;

let estadoAntesArrastre = null;

let seguentId = figures.reduce(
    (maxim, figura) => Math.max(maxim, figura.id ?? 0),
    0
) + 1;

function pintarDibuix() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    for (const figura of figures) {
        if (figura.tipus === "cercle") {
            dibujarCirculo(ctx, figura);
        } else if (figura.tipus === "cuadrado") {
            dibujarCuadrado(ctx, figura);
        } else if (figura.tipus === "triangulo") {
            dibujarTriangulo(ctx, figura);
        } else if (figura.tipus === "alzada") {
            dibujarManoAlzada(ctx, figura);
        }
    }

    actualizarLista();

    document.getElementById("deshacer").disabled =
        cambiosDeshacer.length === 0;

    document.getElementById("rehacer").disabled =
        cambiosRehacer.length === 0;
}

document.getElementById("guardarDibuix").addEventListener("submit", () => {
    document.getElementById("drawing").value = JSON.stringify(figures);
});

function dibujarCirculo(ctx, figura) {
    ctx.beginPath();
    ctx.arc(figura.x, figura.y, figura.mida, 0, 2 * Math.PI);
    aplicarAcabado(ctx, figura);
}

function dibujarCuadrado(ctx, figura) {
    const lado = figura.mida;

    ctx.beginPath();

    ctx.rect(
        figura.x - lado / 2,
        figura.y - lado / 2,
        lado,
        lado
    );

    aplicarAcabado(ctx, figura);
}

function dibujarTriangulo(ctx, figura) {
    ctx.beginPath();

    for (let i = 0; i < 3; i++) {
        const angulo = -Math.PI / 2 + i * 2 * Math.PI / 3;
        const x = figura.x + Math.cos(angulo) * figura.mida;
        const y = figura.y + Math.sin(angulo) * figura.mida;

        if (i === 0) {
            ctx.moveTo(x, y);
        } else {
            ctx.lineTo(x, y);
        }
    }

    ctx.closePath();
    aplicarAcabado(ctx, figura);
}

function aplicarAcabado(ctx, figura) {
    if (figura.omplert ?? true) {
        ctx.fillStyle = figura.color;
        ctx.fill();
    } else {
        ctx.strokeStyle = figura.color;
        ctx.stroke();
    }
}

function dibujarManoAlzada(ctx, figura) {
    if (figura.punts.length === 0) {
        return;
    }

    ctx.save();
    ctx.strokeStyle = figura.color;
    ctx.fillStyle = figura.color;
    ctx.lineWidth = 2;
    ctx.lineCap = "round";
    ctx.lineJoin = "round";
    ctx.beginPath();

    const primero = figura.punts[0];

    if (figura.punts.length === 1) {
        ctx.arc(
            figura.x + primero.x,
            figura.y + primero.y,
            1,
            0,
            Math.PI * 2
        );

        ctx.fill();
    } else {
        ctx.moveTo(figura.x + primero.x, figura.y + primero.y);

        for (let i = 1; i < figura.punts.length; i++) {
            const punto = figura.punts[i];
            ctx.lineTo(figura.x + punto.x, figura.y + punto.y);
        }

        ctx.stroke();
    }

    ctx.restore();
}

function posicionEnCanvas(event) {
    const rect = canvas.getBoundingClientRect();

    return {
        x: (event.clientX - rect.left) * canvas.width / rect.width,
        y: (event.clientY - rect.top) * canvas.height / rect.height
    };
}

function actualizarLista() {
    llistaFigures.replaceChildren();

    for (const figura of figures) {
        const item = document.createElement("li");

        const nombre = document.createElement("span");
        nombre.textContent = figura.nom + figura.id;

        const botonEditar = document.createElement("button");
        botonEditar.type = "button";
        botonEditar.textContent = "Editar";

        botonEditar.addEventListener("click", () => {
            ponerInformacionParaEditar(figura.id);
        });

        const botonEliminar = document.createElement("button");
        botonEliminar.type = "button";
        botonEliminar.textContent = "Eliminar";

        botonEliminar.addEventListener("click", () => {
            const indice = figures.findIndex(
                actual => actual.id === figura.id
            );

            const antes = JSON.stringify(figures);

            if (idFiguraSeleccionada === figura.id) {
                idFiguraSeleccionada = null;
                nombreFigura.textContent = "";

                for (const campo of camposEdicion) {
                    campo.disabled = true;
                }
            }

            if (indice !== -1) {
                figures.splice(indice, 1);

                registrarCambio(
                    antes,
                    `Se ha eliminado la figura: ${figura.nom} ${figura.id}`
                );

                pintarDibuix();
            }
        });

        item.append(nombre, botonEditar, botonEliminar);
        llistaFigures.append(item);
    }
}

for (const campo of camposEdicion) {
    campo.addEventListener("input", (event) => {
        if (campo.dataset.estadoAnterior === undefined) {
            campo.dataset.estadoAnterior = JSON.stringify(figures);
        }

        actualizarFigura(event);
    });

    campo.addEventListener("change", () => terminarEdicion(campo));
    campo.addEventListener("blur", () => terminarEdicion(campo));
}

function terminarEdicion(campo) {
    const antes = campo.dataset.estadoAnterior;
    const parametro = campo.dataset.parametroModificado;

    delete campo.dataset.estadoAnterior;
    delete campo.dataset.parametroModificado;

    const figura = figures.find(
        actual => actual.id === idFiguraSeleccionada
    );

    if (antes !== undefined && parametro && figura) {
        registrarCambio(
            antes,
            `Se ha modificado ${parametro} de ${figura.nom} ${figura.id}`
        );

        pintarDibuix();
    }
}

function ponerInformacionParaEditar(id) {
    for (const campo of camposEdicion) {
        terminarEdicion(campo);
    }

    const figura = figures.find(actual => actual.id === id);

    if (!figura) {
        return;
    }

    idFiguraSeleccionada = id;

    nombreFigura.textContent = `${figura.nom} ${figura.id}`;
    modificarColor.value = figura.color;
    modificarMida.value = figura.mida;
    modificarX.value = figura.x;
    modificarY.value = figura.y;
    modificarRelleno.value = String(figura.omplert ?? true);

    modificarColor.disabled = false;
    modificarX.disabled = false;
    modificarY.disabled = false;

    modificarRelleno.disabled = figura.tipus === "alzada";
    modificarMida.disabled = figura.tipus === "alzada";
}

function contienePunto(figura, x, y) {
    const dx = x - figura.x;
    const dy = y - figura.y;

    if (figura.tipus === "cercle") {
        return dx * dx + dy * dy <= figura.mida * figura.mida;
    }

    if (figura.tipus === "cuadrado") {
        const mitad = figura.mida / 2;

        return Math.abs(dx) <= mitad && Math.abs(dy) <= mitad;
    }

    if (figura.tipus === "triangulo") {
        ctx.beginPath();

        for (let i = 0; i < 3; i++) {
            const angulo = -Math.PI / 2 + i * 2 * Math.PI / 3;
            const verticeX = figura.x + Math.cos(angulo) * figura.mida;
            const verticeY = figura.y + Math.sin(angulo) * figura.mida;

            if (i === 0) {
                ctx.moveTo(verticeX, verticeY);
            } else {
                ctx.lineTo(verticeX, verticeY);
            }
        }

        ctx.closePath();

        return ctx.isPointInPath(x, y);
    }

    if (figura.tipus === "alzada") {
        if (figura.punts.length === 0) {
            return false;
        }

        const primero = figura.punts[0];
        const tolerancia = 6;

        if (figura.punts.length === 1) {
            const distancia = Math.hypot(
                x - (figura.x + primero.x),
                y - (figura.y + primero.y)
            );

            return distancia <= tolerancia;
        }

        ctx.save();

        ctx.lineWidth = tolerancia * 2;
        ctx.lineCap = "round";
        ctx.lineJoin = "round";

        ctx.beginPath();

        ctx.moveTo(
            figura.x + primero.x,
            figura.y + primero.y
        );

        for (let i = 1; i < figura.punts.length; i++) {
            const punto = figura.punts[i];

            ctx.lineTo(
                figura.x + punto.x,
                figura.y + punto.y
            );
        }

        const seleccionado = ctx.isPointInStroke(x, y);

        ctx.restore();

        return seleccionado;
    }
}

function actualizarFigura(event) {
    const figura = figures.find(
        actual => actual.id === idFiguraSeleccionada
    );

    if (!figura) {
        return;
    }

    const campo = event.currentTarget;

    let parametroModificado = "";

    if (campo === modificarColor) {
        figura.color = campo.value;
        parametroModificado = "color";
    } else if (campo === modificarRelleno) {
        figura.omplert = campo.value === "true";
        parametroModificado = "relleno";
    } else {
        if (campo.value.trim() === "" || !campo.checkValidity()) {
            return;
        }

        const valor = Number(campo.value);

        if (!Number.isFinite(valor)) {
            return;
        }

        if (campo === modificarMida) {
            figura.mida = valor;
            parametroModificado = "tamaño";
        } else if (campo === modificarX) {
            figura.x = valor;
            parametroModificado = "posición X";
        } else if (campo === modificarY) {
            figura.y = valor;
            parametroModificado = "posición Y";
        }
    }

    if (parametroModificado !== "") {
        campo.dataset.parametroModificado = parametroModificado;
    }

    pintarDibuix();
}

function registrarCambio(antes, descripcion) {
    const despues = JSON.stringify(figures);

    if (antes === despues) {
        return;
    }

    cambiosDeshacer.push({
        antes,
        despues,
        descripcion
    });

    if (cambiosDeshacer.length > limiteCambios) {
        cambiosDeshacer.shift();
    }

    cambiosRehacer.length = 0;

    registrarAccion(descripcion);
}

function restaurarEstado(estado) {
    figures.splice(0, figures.length, ...JSON.parse(estado));

    idFiguraSeleccionada = null;
    nombreFigura.textContent = "Ninguna seleccionada";

    for (const campo of camposEdicion) {
        campo.disabled = true;

        delete campo.dataset.estadoAnterior;
        delete campo.dataset.parametroModificado;
    }

    pintarDibuix();
}

function deshacer() {
    if (punteroActivo !== null) {
        return;
    }

    for (const campo of camposEdicion) {
        terminarEdicion(campo);
    }

    if (cambiosDeshacer.length === 0) {
        return;
    }

    const cambio = cambiosDeshacer.pop();

    cambiosRehacer.push(cambio);

    restaurarEstado(cambio.antes);

    registrarAccion(
        `Se ha deshecho: ${cambio.descripcion}`
    );
}

function rehacer() {
    if (punteroActivo !== null) {
        return;
    }

    for (const campo of camposEdicion) {
        terminarEdicion(campo);
    }

    if (cambiosRehacer.length === 0) {
        return;
    }

    const cambio = cambiosRehacer.pop();

    cambiosDeshacer.push(cambio);

    restaurarEstado(cambio.despues);

    registrarAccion(
        `Se ha rehecho: ${cambio.descripcion}`
    );
}

document.getElementById("deshacer").addEventListener("click", deshacer);
document.getElementById("rehacer").addEventListener("click", rehacer);

function registrarAccion(descripcion) {
    historialCambios.push(descripcion);

    const item = document.createElement("li");
    item.textContent = descripcion;

    listaHistorial.append(item);
}

document.getElementById("eliminarTodas").addEventListener("click", () => {
    const antes = JSON.stringify(figures);

    figures.splice(0, figures.length);

    registrarCambio(
        antes,
        "Se han eliminado todas las figuras"
    );

    idFiguraSeleccionada = null;
    nombreFigura.textContent = "";

    for (const campo of camposEdicion) {
        campo.disabled = true;
    }

    pintarDibuix();
});

canvas.addEventListener("pointerdown", (event) => {
    if (event.button !== 0) {
        return;
    }

    if (selectorModo.value === "seleccionar") {
        const posicion = posicionEnCanvas(event);

        for (let i = figures.length - 1; i >= 0; i--) {
            const figura = figures[i];

            if (contienePunto(figura, posicion.x, posicion.y)) {
                figuraArrastrada = figura;

                ponerInformacionParaEditar(figura.id);

                estadoAntesArrastre = JSON.stringify(figures);

                posicionInicialX = figura.x;
                posicionInicialY = figura.y;

                desplazamientoX = posicion.x - figura.x;
                desplazamientoY = posicion.y - figura.y;

                punteroActivo = event.pointerId;

                canvas.setPointerCapture(event.pointerId);
                break;
            }
        }

        return;
    }

    if (selectorModo.value !== "alzada"
            || event.button !== 0
            || trazoActual !== null) {
        return;
    }

    const posicion = posicionEnCanvas(event);

    trazoActual = {
        id: seguentId++,
        nom: "Trazo a mano alzada",
        tipus: "alzada",
        color: selectorColor.value,
        omplert: false,
        x: posicion.x,
        y: posicion.y,
        mida: 0,
        punts: [{x: 0, y: 0}]
    };

    punteroActivo = event.pointerId;

    canvas.setPointerCapture(event.pointerId);

    pintarDibuix();
    dibujarManoAlzada(ctx, trazoActual);
});

canvas.addEventListener("pointermove", (event) => {
    if (event.pointerId !== punteroActivo) {
        return;
    }

    if (figuraArrastrada !== null) {
        const posicion = posicionEnCanvas(event);

        figuraArrastrada.x = posicion.x - desplazamientoX;
        figuraArrastrada.y = posicion.y - desplazamientoY;

        modificarX.value = figuraArrastrada.x;
        modificarY.value = figuraArrastrada.y;

        pintarDibuix();
        return;
    }

    if (trazoActual === null || event.pointerId !== punteroActivo) {
        return;
    }

    const posicion = posicionEnCanvas(event);

    trazoActual.punts.push({
        x: posicion.x - trazoActual.x,
        y: posicion.y - trazoActual.y
    });

    pintarDibuix();
    dibujarManoAlzada(ctx, trazoActual);
});

canvas.addEventListener("pointerup", (event) => {
    if (event.pointerId !== punteroActivo) {
        return;
    }

    if (figuraArrastrada !== null) {
        const posicionFinalX = figuraArrastrada.x;
        const posicionFinalY = figuraArrastrada.y;

        if (posicionInicialX !== posicionFinalX
                || posicionInicialY !== posicionFinalY) {
            registrarCambio(
                estadoAntesArrastre,
                `Se ha movido ${figuraArrastrada.nom} ${figuraArrastrada.id}: `
                + `de (${posicionInicialX}, ${posicionInicialY}) `
                + `a (${posicionFinalX}, ${posicionFinalY})`
            );
        }

        figuraArrastrada = null;
        punteroActivo = null;

        pintarDibuix();
        return;
    }

    if (trazoActual === null || event.pointerId !== punteroActivo) {
        return;
    }

    const posicion = posicionEnCanvas(event);
    const ultimo = trazoActual.punts[trazoActual.punts.length - 1];

    const finalX = posicion.x - trazoActual.x;
    const finalY = posicion.y - trazoActual.y;

    if (ultimo.x !== finalX || ultimo.y !== finalY) {
        trazoActual.punts.push({
            x: finalX,
            y: finalY
        });
    }

    let minX = Infinity;
    let maxX = -Infinity;
    let minY = Infinity;
    let maxY = -Infinity;

    for (const punto of trazoActual.punts) {
        minX = Math.min(minX, punto.x);
        maxX = Math.max(maxX, punto.x);
        minY = Math.min(minY, punto.y);
        maxY = Math.max(maxY, punto.y);
    }

    const centroX = (minX + maxX) / 2;
    const centroY = (minY + maxY) / 2;

    trazoActual.x += centroX;
    trazoActual.y += centroY;

    for (const punto of trazoActual.punts) {
        punto.x -= centroX;
        punto.y -= centroY;
    }

    const antes = JSON.stringify(figures);

    figures.push(trazoActual);

    registrarCambio(
        antes,
        `Se ha creado la figura: ${trazoActual.nom} ${trazoActual.id}`
    );

    trazoActual = null;
    punteroActivo = null;

    pintarDibuix();
});

canvas.addEventListener("click", (event) => {
    const tipus = selectorModo.value;

    const nombresFiguras = {
        cercle: "Cercle",
        cuadrado: "Cuadrado",
        triangulo: "Triángulo",
        alzada: "Trazo a mano alzada",
        estrella7: "Estrella de 7 puntas"
    };

    if (!["cercle", "cuadrado", "triangulo"].includes(tipus)) {
        return;
    }

    const midaSeleccionada = Number(selectorMida.value);

    if (!Number.isFinite(midaSeleccionada)
            || midaSeleccionada < 1
            || midaSeleccionada > 100) {
        return;
    }

    const rect = canvas.getBoundingClientRect();

    const x = (event.clientX - rect.left)
        * canvas.width / rect.width;

    const y = (event.clientY - rect.top)
        * canvas.height / rect.height;

    const nuevaFigura = {
        id: seguentId++,
        nom: nombresFiguras[tipus],
        tipus,
        color: selectorColor.value,
        omplert: selectorRellenado.value === "si-rellenado",
        x,
        y,
        mida: midaSeleccionada,
        punts: []
    };

    const antes = JSON.stringify(figures);

    figures.push(nuevaFigura);

    registrarCambio(
        antes,
        `Se ha creado la figura: ${nuevaFigura.nom} ${nuevaFigura.id}`
    );

    pintarDibuix();
});

pintarDibuix();