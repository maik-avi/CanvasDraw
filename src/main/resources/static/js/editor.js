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
        } else if (figura.tipus === "estrella7") {
            dibujoEstrella7(ctx, figura);
        }
    }

    actualizarLista();
}

canvas.addEventListener("click", (event) => {
    const tipus = selectorModo.value;

    const nombresFiguras = {
    cercle: "Cercle",
    cuadrado: "Cuadrado",
    triangulo: "Triángulo",
    alzada: "Trazo a mano alzada",
    estrella7: "Estrella de 7 puntas"
};

    if (!["cercle", "cuadrado", "triangulo", "estrella7"].includes(tipus)) {
    return;
}

    const midaSeleccionada = Number(selectorMida.value);

    if (!Number.isFinite(midaSeleccionada)
            || midaSeleccionada < 1
            || midaSeleccionada > 100) {
        return;
    }

    const rect = canvas.getBoundingClientRect();
    const x = (event.clientX - rect.left) * canvas.width / rect.width;
    const y = (event.clientY - rect.top) * canvas.height / rect.height;

    figures.push({
        id: seguentId++,
        nom: nombresFiguras[tipus],
        tipus,
        color: selectorColor.value,
        omplert: selectorRellenado.value === "si-rellenado",
        x,
        y,
        mida: midaSeleccionada,
        punts: []
    });

    pintarDibuix();
});

document.getElementById("guardarDibuix").addEventListener("submit", () => {
    document.getElementById("drawing").value = JSON.stringify(figures);
});


function dibujarCirculo(ctx, figura) {
    ctx.beginPath();
        ctx.arc(figura.x, figura.y, figura.mida, 0, 2 * Math.PI);
        if (figura.omplert ?? true) {
        ctx.fillStyle = figura.color;
            ctx.fill();
        } else {
            ctx.strokeStyle = figura.color;
            ctx.stroke();
        }
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

    if (figura.omplert ?? true) {
        ctx.fillStyle = figura.color;
        ctx.fill();
    } else {
        ctx.strokeStyle = figura.color;
        ctx.stroke();
    }
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
    ctxx.beginPath();

    const primero = figura.punts[0];

    if (figura.punts.length === 1) {
        ctx.arc(figura.x + primero.x,
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
        nombre.textContent = figura.nom;

        const botonEliminar = document.createElement("button");
        botonEliminar.type = "button";
        botonEliminar.textContent = "Eliminar";

        botonEliminar.addEventListener("click", () => {
            const indice = figures.findIndex(
                actual => actual.id === figura.id
            );

            if (indice !== -1) {
                figures.splice(indice, 1);
                pintarDibuix();
            }
        });

        item.append(nombre, botonEliminar);
        llistaFigures.append(item);
    }
}

pintarDibuix();