const canvas = document.getElementById("canvasDibuix");
const ctx = canvas.getContext("2d");
const toolbar = document.getElementById("tool-bar");

const selectorModo = document.getElementById("modo-de-dibujo");
const selectorRellenado = document.getElementById("rellenado");

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

    if (tipus !== "cercle" && tipus !== "cuadrado") {
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

pintarDibuix();