import { useRef, useState } from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ResponsiveContainer,
} from "recharts";
import { Mic, Play, Square, Volume2 } from "lucide-react";
import { WebAudioCalibration } from "../audio/webAudioCalibration";
import type { FrequencyPoint } from "../types/Audio";

export function AudioMeasurementPage() {
  const calibrationRef = useRef<WebAudioCalibration | null>(null);

  const [status, setStatus] = useState("Sin inicializar");
  const [ambientDb, setAmbientDb] = useState<number | null>(null);
  const [points, setPoints] = useState<FrequencyPoint[]>([]);
  const [isWorking, setIsWorking] = useState(false);
  const [error, setError] = useState("");
  const [hasCalibration, setHasCalibration] = useState(false);

  const initialize = async () => {
    try {
      setError("");
      setIsWorking(true);
      setStatus("Solicitando permiso de micrófono...");

      const calibration = new WebAudioCalibration();
      await calibration.initializeMicrophone();

      calibrationRef.current = calibration;
      setHasCalibration(true);

      setStatus("Micrófono inicializado correctamente");
    } catch {
      setError("No fue posible acceder al micrófono. Revisa permisos del navegador.");
      setStatus("Error al inicializar micrófono");
      setHasCalibration(false);
    } finally {
      setIsWorking(false);
    }
  };

  const measureNoise = async () => {
    try {
      setError("");
      setIsWorking(true);
      setStatus("Midiendo ruido ambiente durante 3 segundos...");

      const result = await calibrationRef.current?.measureAmbientNoise(3000);

      if (result) {
        setAmbientDb(result.promedioDb);
        setStatus(`Ruido ambiente medido: ${result.promedioDb} dB aproximados`);
      }
    } catch {
      setError("No fue posible medir el ruido ambiente.");
    } finally {
      setIsWorking(false);
    }
  };

  const runSweep = async () => {
    try {
      setError("");
      setIsWorking(true);
      setStatus("Reproduciendo sweep y capturando respuesta...");

      const result = await calibrationRef.current?.runSweepAndMeasure(6000);

      if (result) {
        setPoints(result.puntos);
        setStatus("Medición finalizada. Respuesta aproximada capturada.");
      }
    } catch {
      setError("No fue posible ejecutar la medición.");
    } finally {
      setIsWorking(false);
    }
  };

  const stop = () => {
    calibrationRef.current?.stop();
    calibrationRef.current = null;
    setHasCalibration(false);
    setStatus("Medición detenida");
    setIsWorking(false);
  };

  return (
    <main className="measurement-page">
      <section className="measurement-header">
        <div>
          <span className="badge">Web Audio API</span>
          <h1>Medición acústica</h1>
          <p>
            Esta herramienta usa el micrófono del navegador para capturar una
            respuesta aproximada del parlante. Para mejores resultados usa un
            micrófono externo y un ambiente silencioso.
          </p>
        </div>
      </section>

      {error && <div className="alert">{error}</div>}

      <section className="measurement-panel">
        <div className="control-card">
          <h2>Controles</h2>

          <p>
            <strong>Estado:</strong> {status}
          </p>

          <p>
            <strong>Ruido ambiente:</strong>{" "}
            {ambientDb !== null ? `${ambientDb} dB aprox.` : "No medido"}
          </p>

          <div className="control-buttons">
            <button className="btn btn-primary" onClick={initialize} disabled={isWorking}>
              <Mic size={18} />
              Inicializar micrófono
            </button>

            <button className="btn btn-secondary" onClick={measureNoise} disabled={isWorking || !hasCalibration}>
              <Volume2 size={18} />
              Medir ruido
            </button>

            <button className="btn btn-primary" onClick={runSweep} disabled={isWorking || !hasCalibration}>
              <Play size={18} />
              Ejecutar sweep
            </button>

            <button className="btn btn-danger" onClick={stop}>
              <Square size={18} />
              Detener
            </button>
          </div>
        </div>

        <div className="chart-card">
          <h2>Respuesta en frecuencia aproximada</h2>

          {points.length === 0 ? (
            <div className="empty-chart">
              Ejecuta una medición para visualizar la curva.
            </div>
          ) : (
            <ResponsiveContainer width="100%" height={360}>
              <LineChart data={points}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis
                  dataKey="frecuenciaHz"
                  tickFormatter={(value) => `${value}`}
                  label={{
                    value: "Frecuencia Hz",
                    position: "insideBottom",
                    offset: -5,
                  }}
                />
                <YAxis
                  domain={[0, 100]}
                  label={{
                    value: "Nivel dB aprox.",
                    angle: -90,
                    position: "insideLeft",
                  }}
                />
                <Tooltip
                  formatter={(value) => [`${value} dB`, "Nivel"]}
                  labelFormatter={(label) => `${label} Hz`}
                />
                <Line
                  type="monotone"
                  dataKey="nivelDb"
                  strokeWidth={2}
                  dot
                />
              </LineChart>
            </ResponsiveContainer>
          )}
        </div>
      </section>

      <section className="info-card">
        <h2>Importante</h2>
        <p>
          Esta medición es una primera aproximación académica. Los navegadores,
          micrófonos no calibrados y sistemas de audio pueden aplicar
          procesamiento interno. Por eso, el algoritmo final debe ser
          conservador: reducir picos antes que aumentar valles profundos.
        </p>
      </section>
    </main>
  );
}