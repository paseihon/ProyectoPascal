import express from "express";
import cors from "cors";

const app = express();
app.use(cors());
app.use(express.json());

let vehiculos = [];

app.get("/vehiculos", (req, res) => {
  res.json(vehiculos);
});

app.post("/vehiculos", (req, res) => {
  const { marca, matricula, precio, kms, descripcion } = req.body;

  if (!marca || !matricula) return res.status(400).json({ error: "Datos incompletos" });
  if (!/^\d{4}[A-Z]{3}$/.test(matricula)) return res.status(400).json({ error: "Matrícula no válida" });
  if (vehiculos.some(v => v.matricula === matricula))
    return res.status(409).json({ error: "Ya existe un vehículo con esa matrícula" });

  const nuevo = { marca, matricula, precio, kms, descripcion };
  vehiculos.push(nuevo);
  res.status(201).json(nuevo);
});

app.get("/vehiculos/:matricula", (req, res) => {
  const v = vehiculos.find(x => x.matricula === req.params.matricula);
  if (!v) return res.status(404).json({ error: "No existe ese vehículo" });
  res.json(v);
});

app.put("/vehiculos/:matricula", (req, res) => {
  const v = vehiculos.find(x => x.matricula === req.params.matricula);
  if (!v) return res.status(404).json({ error: "No existe ese vehículo" });
  if (typeof req.body.kms !== "number") return res.status(400).json({ error: "Kilómetros inválidos" });
  v.kms = req.body.kms;
  res.json(v);
});

app.listen(3000, () => console.log("✅ Servidor escuchando en http://localhost:3000"));
