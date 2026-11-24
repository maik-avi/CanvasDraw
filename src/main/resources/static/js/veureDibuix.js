const canvas = document.getElementById("canvasDibuix");
const ctx = canvas.getContext("2d");

const figures = JSON.parse(
    document.getElementById("drawingData").textContent
);

function pintarDibuix() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    for (const figura of figures) {
        if (figura.tipus === "alzada") {
            dibujarManoAlzada(figura);
            continue;
        }

        ctx.beginPath();

        if (figura.tipus === "cercle") {
            ctx.arc(
                figura.x,
                figura.y,
                figura.mida,
                0,
                Math.PI * 2
            );
        } else if (figura.tipus === "cuadrado") {
            ctx.rect(
                figura.x - figura.mida / 2,
                figura.y - figura.mida / 2,
                figura.mida,
                figura.mida
            );
        } else if (figura.tipus === "triangulo") {
            for (let i = 0; i < 3; i++) {
                const angulo = -Math.PI / 2
                    + i * 2 * Math.PI / 3;

                const x = figura.x
                    + Math.cos(angulo) * figura.mida;

                const y = figura.y
                    + Math.sin(angulo) * figura.mida;

                if (i === 0) {
                    ctx.moveTo(x, y);
                } else {
                    ctx.lineTo(x, y);
                }
            }

            ctx.closePath();
        } else {
            continue;
        }

        if (figura.omplert ?? true) {
            ctx.fillStyle = figura.color;
            ctx.fill();
        } else {
            ctx.strokeStyle = figura.color;
            ctx.stroke();
        }
    }
}

function dibujarManoAlzada(figura) {
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

        ctx.stroke();
    }

    ctx.restore();
}

pintarDibuix();